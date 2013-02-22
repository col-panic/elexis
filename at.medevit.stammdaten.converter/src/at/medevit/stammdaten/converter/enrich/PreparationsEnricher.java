/*******************************************************************************
 * Copyright (c) 2013 MEDEVIT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 ******************************************************************************/
package at.medevit.stammdaten.converter.enrich;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.e_mediat.bag.Preparations;
import net.e_mediat.bag.parser.Pack;
import net.e_mediat.bag.parser.Preparation;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

public class PreparationsEnricher {
	
	private static List<Preparation> preparationsList = null;
	
	public static void enrichData(ARTIKELSTAMM artikelstamm, File preparationsFile)
		throws IOException{
		preparationsList = new Preparations(preparationsFile).getPreparations();
		addInformationToArtikelstamm(artikelstamm);
		
	}
	
	private static void addInformationToArtikelstamm(ARTIKELSTAMM artikelstamm){
		int counter = 0;
		int overallCounter = 0;
		for (Preparation preparation : preparationsList) {
			List<Pack> packs = preparation.getPacks();
			for (Pack pack : packs) {
				overallCounter++;
				ITEM item =
					ArtikelstammHelper.getItemInListByPharmacode(artikelstamm, pack.pharmacode);
				if (item == null) {
					// try to find by GTIN
					item = ArtikelstammHelper.getItemInListByGTIN(artikelstamm, pack.swissMedicNo8);
				}
				if (item != null) {
					// SL ENTRY
					item.setSLENTRY(true);
					// Selbstbehalt
					if (preparation.flagSb20)
						item.setDEDUCTIBLE(20);
					// swissmedic category
					if (preparation.swissmedicCategory != null
						&& preparation.swissmedicCategory.length() == 1)
						item.setIKSCAT(preparation.swissmedicCategory);
					// Prices
					try {
						if (pack.pexf != null)
							item.setPEXF(Double.parseDouble(pack.pexf));
						if (pack.ppub != null)
							item.setPPUB(Double.parseDouble(pack.ppub));
					} catch (NumberFormatException nfe) {
						System.out.println("[ERROR] on values " + nfe);
					}
					// Narcotics
					item.setNARCOTIC(pack.flagNarcosis);
					// original or generika
					if (preparation.orgGenCode != null && preparation.orgGenCode.length() == 1)
						item.setGENERICTYPE(preparation.orgGenCode);
					
					// TODO more
				} else {
					System.out.println("[INFO] No phar/GTIN entry " + pack.pharmacode + "/7680"
						+ pack.swissMedicNo8 + " found in artikelstamm");
					counter++;
				}
			}
		}
		System.out.println("[INFO] " + counter + " of " + overallCounter
			+ " pack entries could not be allocated to an artikelstamm ITEM");
	}
}
