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
import net.swissindex.ws.nonpharma.NONPHARMA;
import net.swissindex.ws.nonpharma.NONPHARMA.ITEM;
import net.swissindex.ws.nonpharma.WsNonPharmaV101;
import net.swissindex.ws.nonpharma.WsNonPharmaV101Soap;

import org.xml.sax.SAXException;

public class FetchNonPharma extends FetchCommon {
	public static void main(String[] args){
		timedSysout("Starting NON PHARMA download");
		NONPHARMA nonPharmaArticles = downloadArticles();
		if (REMOVE_INACTIVE)
			removeInactiveItems(nonPharmaArticles);
		try {
			File outputFile = new File("swissindex_NON_PHARMA_" + df.format(new Date()) + ".xml");
			SwissindexHelper.marshallToFileSystem(nonPharmaArticles, outputFile);
			timedSysout("[OK] " + outputFile.getAbsolutePath());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Filter out items marked as being inactive
	 * 
	 * @param data
	 */
	public static void removeInactiveItems(NONPHARMA data){
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
	
	private static NONPHARMA downloadArticles(){
		NONPHARMA ret = null;
		WsNonPharmaV101Soap ws = new WsNonPharmaV101().getWsNonPharmaV101Soap();
		ret = ws.downloadAll(LANGUAGE_TO_DOWNLOAD);
		return ret;
	}
	
}
