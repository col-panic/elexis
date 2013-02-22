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
package at.medevit.swissindex.client;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBException;

import net.swissindex.format.SwissindexHelper;
import net.swissindex.ws.pharma.PHARMA;
import net.swissindex.ws.pharma.PHARMA.ITEM;
import net.swissindex.ws.pharma.WsPharmaV101;
import net.swissindex.ws.pharma.WsPharmaV101Soap;

import org.xml.sax.SAXException;

public class FetchPharma extends FetchCommon {
	
	public static void main(String[] args){
		timedSysout("Starting PHARMA download");
		
		PHARMA pharmaArticles = downloadArticles();
		if (REMOVE_INACTIVE)
			removeInactiveItems(pharmaArticles);
		try {
			File outputFile = new File("swissindex_PHARMA_" + df.format(new Date()) + ".xml");
			SwissindexHelper.marshallToFileSystem(pharmaArticles, outputFile);
			timedSysout("[OK] " + outputFile.getAbsolutePath());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		}
	}
	
	private static PHARMA downloadArticles(){
		PHARMA ret = null;
		WsPharmaV101Soap ws = new WsPharmaV101().getWsPharmaV101Soap();
		ret = ws.downloadAll(LANGUAGE_TO_DOWNLOAD);
		return ret;
	}
	
	public static void removeInactiveItems(PHARMA data){
		int counter = 0;
		ITEM[] itemList = data.getITEM().toArray(new ITEM[0]);
		for (ITEM item : itemList) {
			if (item.getSTATUS().equalsIgnoreCase("I")) {
				timedSysout("Removing inactive item " + item.getDSCR());
				data.getITEM().remove(item);
				counter++;
			}
		}
		timedSysout("Removed " + counter + " of " + data.getITEM().size() + " items.");
	}
	
}
