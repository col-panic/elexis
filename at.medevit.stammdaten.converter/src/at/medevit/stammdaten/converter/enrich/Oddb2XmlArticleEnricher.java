package at.medevit.stammdaten.converter.enrich;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

import com.ywesee.oddb2xml.Oddb2XmlHelper;
import com.ywesee.oddb2xml.article.ART;
import com.ywesee.oddb2xml.article.ARTICLE;
import com.ywesee.oddb2xml.limitation.LIM;
import com.ywesee.oddb2xml.limitation.LIMITATION;
import com.ywesee.oddb2xml.product.PRD;
import com.ywesee.oddb2xml.product.PRODUCT;

public class Oddb2XmlArticleEnricher {
	
	private static List<ART> oddb2xmlARTList = null;
	private static List<LIM> oddb2xmlLIMList = null;
	private static List<PRD> oddb2xmlPRDList = null;
	
	public static void enrichData(ARTIKELSTAMM artikelstamm, File oddb2xmlArticleFileObj,
		File oddb2xmlLimitationFileObj, File oddb2xmlProductFileObj) throws IOException,
		JAXBException{
		oddb2xmlARTList =
			((ARTICLE) Oddb2XmlHelper.unmarshallFile(oddb2xmlArticleFileObj)).getART();
		oddb2xmlLIMList =
			((LIMITATION) Oddb2XmlHelper.unmarshallFile(oddb2xmlLimitationFileObj)).getLIM();
		oddb2xmlPRDList =
			((PRODUCT) Oddb2XmlHelper.unmarshallFile(oddb2xmlProductFileObj)).getPRD();
		addInformationToArtikelstamm(artikelstamm);
	}
	
	private static void addInformationToArtikelstamm(ARTIKELSTAMM artikelstamm){
		int failCounter = 0;
		int overallCounter = 0;
		int successCounter = 0;
		for (ART article : oddb2xmlARTList) {
			String ean13 = article.getARTBAR().getBC().toString();
			ITEM item = ArtikelstammHelper.getItemInListByGTIN(artikelstamm, ean13);
			if (item == null) {
				// try to find by Pharmacode
				item =
					ArtikelstammHelper.getItemInListByPharmacode(artikelstamm, article.getPHAR());
			}
			if (item != null) {
				// Limitation information
				LIM limitation =
					Oddb2XmlHelper.getItemInLimitationListBySwissmedicNo(oddb2xmlLIMList,
						article.getSMNO());
				if (limitation != null) {
					item.setLIMITATION(true);
					item.setLIMITATIONTEXT(limitation.getDSCRD());
				}
				if (article.getLIMPTS() != null) {
					item.setLIMITATION(true);
					item.setLIMITATIONPTS(article.getLIMPTS().intValue());
				}
				// Is in LPPV List
				if (article.getARTINS() != null && article.getARTINS().getNINCD() != null) {
					if (article.getARTINS().getNINCD().intValue() == 20)
						item.setLPPV(true);
				}
				// Co-Payment information
				// 1: 20 %
				// 2: 10 %
				if (article.getSLOPLUS() != null) {
					int value = article.getSLOPLUS().intValue();
					if (value == 1) {
						item.setDEDUCTIBLE(20);
					} else if (value == 2) {
						item.setDEDUCTIBLE(10);
					}
				}
				
				// Package size information
				PRD product =
					Oddb2XmlHelper.getItemInProductListByGTIN(oddb2xmlPRDList, item.getGTIN());
				if (product != null) {
					if (product.getPackGrSwissmedic() != null) {
						try {
							int pkgSize = Integer.parseInt(product.getPackGrSwissmedic());
							item.setPKGSIZE(pkgSize);
						} catch (NumberFormatException nfe) {
							System.out.println("[WARN] Invalid PackGrSwissmedic for "
								+ item.getGTIN() + ": " + product.getPackGrSwissmedic());
						}
						
					}
				} else {
					System.out.println("[INFO] No product entry for GTIN " + item.getGTIN()
						+ " found in oddb_product.xml");
				}
				successCounter++;
			} else {
// System.out.println("[INFO] No corresponding phar/GTIN entry " + article.getPHAR()
// + "/" + article.getARTBAR().getBC().toString() + " found in Artikelstamm");
				failCounter++;
			}
			overallCounter++;
		}
		System.out.println("[INFO] fail: " + failCounter + " success: " + successCounter
			+ " total IGM: " + overallCounter + " total Artikelstamm-Pharma: "
			+ artikelstamm.getITEM().size());
	}
	
}
