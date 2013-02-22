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
package at.medevit.stammdaten.converter.outbound;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import net.swissindex.ws.nonpharma.NONPHARMA;
import net.swissindex.ws.pharma.PHARMA;
import net.swissindex.ws.pharma.PHARMA.ITEM;
import net.swissindex.ws.pharma.PHARMA.ITEM.COMP;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

public class ElexisFormatConverter {
	
	/**
	 * 
	 * @param object
	 *            either {@link PHARMA} or {@link NONPHARMA}
	 * @param language
	 *            the language used in the original dataset
	 * @return {@link ARTIKELSTAMM} or <code>null</code> if error
	 */
	public static ARTIKELSTAMM convertFromSwissindex(Object object, String language){
		if (object == null || (!(object instanceof PHARMA) && !(object instanceof NONPHARMA)))
			throw new IllegalArgumentException("Only PHARMA or NONPHARMA objects allowed.");
		
		ARTIKELSTAMM artikelstamm = new ARTIKELSTAMM();
		
		if (object instanceof PHARMA) {
			PHARMA swissindexPharma = (PHARMA) object;
			
			// Header
			XMLGregorianCalendar creationDate = swissindexPharma.getCREATIONDATETIME();
			artikelstamm.setCREATIONDATETIME(creationDate);
			artikelstamm.setMONTH(creationDate.getMonth());
			artikelstamm.setYEAR(creationDate.getYear());
			artikelstamm.setLANG(language);
			artikelstamm.setCUMULVER(ArtikelstammHelper.getCummulatedVersionNumber(
				creationDate.getYear(), creationDate.getMonth()));
			artikelstamm.setTYPE(ArtikelstammConstants.TYPE.P.name());
			
			// Items
			List<ITEM> swissindexItemList = swissindexPharma.getITEM();
			for (ITEM siItem : swissindexItemList) {
				ARTIKELSTAMM.ITEM eItem = new ARTIKELSTAMM.ITEM();
				// GTIN
				eItem.setGTIN(siItem.getGTIN());
				// Pharmacode
				if (siItem.getPHAR() != null)
					eItem.setPHAR(siItem.getPHAR());
				// Description
				eItem.setDSCR(siItem.getDSCR());
				// Additional Description
				eItem.setADDSCR(siItem.getADDSCR());
				if (siItem.getATC() != null)
					eItem.setATC(siItem.getATC());
				// Manufacturer
				if (siItem.getCOMP() != null) {
					ARTIKELSTAMM.ITEM.COMP eItemComp = new ARTIKELSTAMM.ITEM.COMP();
					COMP siComp = siItem.getCOMP();
					if (siComp.getGLN() != null)
						eItemComp.setGLN(siComp.getGLN());
					eItemComp.setNAME(siComp.getNAME());
					eItem.setCOMP(eItemComp);
				}
				artikelstamm.getITEM().add(eItem);
			}
			
			if (swissindexPharma.getITEM().size() != artikelstamm.getITEM().size()) {
				throw new IllegalStateException("The number of items in the lists is different!");
			}
		} else if (object instanceof NONPHARMA) {
			NONPHARMA swissindexNonPharma = (NONPHARMA) object;
			
			// Header
			XMLGregorianCalendar creationDate = swissindexNonPharma.getCREATIONDATETIME();
			artikelstamm.setCREATIONDATETIME(creationDate);
			artikelstamm.setMONTH(creationDate.getMonth());
			artikelstamm.setYEAR(creationDate.getYear());
			artikelstamm.setLANG(language);
			artikelstamm.setCUMULVER(ArtikelstammHelper.getCummulatedVersionNumber(
				creationDate.getYear(), creationDate.getMonth()));
			artikelstamm.setTYPE(ArtikelstammConstants.TYPE.N.name());
			
			// Items
			List<net.swissindex.ws.nonpharma.NONPHARMA.ITEM> swissindexItemList =
				swissindexNonPharma.getITEM();
			for (net.swissindex.ws.nonpharma.NONPHARMA.ITEM siItem : swissindexItemList) {
				ARTIKELSTAMM.ITEM eItem = new ARTIKELSTAMM.ITEM();
				// GTIN
				eItem.setGTIN(siItem.getGTIN());
				// Pharmacode
				if (siItem.getPHAR() != null)
					eItem.setPHAR(siItem.getPHAR());
				// Description
				eItem.setDSCR(siItem.getDSCR());
				// Additional Description
				eItem.setADDSCR(siItem.getADDSCR());
				// Manufacturer
				if (siItem.getCOMP() != null) {
					ARTIKELSTAMM.ITEM.COMP eItemComp = new ARTIKELSTAMM.ITEM.COMP();
					net.swissindex.ws.nonpharma.NONPHARMA.ITEM.COMP siComp = siItem.getCOMP();
					if (siComp.getGLN() != null)
						eItemComp.setGLN(siComp.getGLN());
					eItemComp.setNAME(siComp.getNAME());
					eItem.setCOMP(eItemComp);
				}
				
				// OPEN
				
				artikelstamm.getITEM().add(eItem);
			}
			
			if (swissindexNonPharma.getITEM().size() != artikelstamm.getITEM().size()) {
				throw new IllegalStateException("The number of items in the lists is different!");
			}
		}
		
		return artikelstamm;
	}
}
