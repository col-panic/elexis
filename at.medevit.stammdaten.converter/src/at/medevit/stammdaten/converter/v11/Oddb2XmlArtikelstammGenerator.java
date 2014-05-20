package at.medevit.stammdaten.converter.v11;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

import com.ywesee.oddb2xml.Oddb2XmlHelper;
import com.ywesee.oddb2xml.article.ART;
import com.ywesee.oddb2xml.article.ARTBAR;
import com.ywesee.oddb2xml.article.ARTICLE;
import com.ywesee.oddb2xml.article.ARTPRI;
import com.ywesee.oddb2xml.limitation.LIM;
import com.ywesee.oddb2xml.limitation.LIMITATION;
import com.ywesee.oddb2xml.product.PRD;
import com.ywesee.oddb2xml.product.PRODUCT;

public class Oddb2XmlArtikelstammGenerator {
	
	private static ARTICLE oddb2xmlArticle;
	private static LIMITATION oddb2xmlLimitations;
	private static PRODUCT oddb2xmlProducts;
	
	private static HashSet<String> articleIds = new HashSet<String>();
	
	private static int collisions = 0;
	
	public static void generate(ARTIKELSTAMM pharma, ARTIKELSTAMM nonPharma,
		File oddb2xmlArticleFileObj, File oddb2xmlProductFileObj, File oddb2xmlLimitationFileObj)
		throws JAXBException, DatatypeConfigurationException, ParseException{
		
		System.out.println("Unmarshalling oddb2xml files");
		unmarshallOddb2xmlFiles(oddb2xmlArticleFileObj, oddb2xmlProductFileObj,
			oddb2xmlLimitationFileObj);
		System.out.println("Setting artikelstamm headers");
		setArtikelstammHeaderInfo(pharma, nonPharma);
		System.out.println("Import oddb2xml_article; separate into pharma and non-pharma");
		populateFromOddb2Xml(pharma, nonPharma);
		
		System.out.println("STATS Total " + oddb2xmlArticle.getART().size() + " / collisions "
			+ collisions + " / pharma " + pharma.getITEM().size() + " / nonPharma "
			+ nonPharma.getITEM().size());
		
	}
	
	private static void populateFromOddb2Xml(ARTIKELSTAMM pharma, ARTIKELSTAMM nonPharma){
		List<ART> articles = oddb2xmlArticle.getART();
		for (ART a : articles) {
			ARTIKELSTAMM.ITEM item = new ARTIKELSTAMM.ITEM();
			
			String phar = a.getPHAR();
			String ean = "";
			
			if (a.getARTBAR() != null) {
				if (a.getARTBAR().getBC() != null) {
					ean = a.getARTBAR().getBC().toString();
				}
			}
			
			BigInteger uniqueId = new BigInteger(ean + "" + phar);
			if (articleIds.contains(uniqueId)) {
				System.out.println("Collision detected " + uniqueId + " is already in the set.");
				collisions++;
				continue;
			} else {
				item.setPHAR(new BigInteger(phar));
				item.setGTIN(ean);
				articleIds.add(uniqueId.toString());
			}
			
			// limit to max 50 chars
			int dscrdL = (a.getDSCRD().trim().length()>49) ? 50 : a.getDSCRD().trim().length();
			item.setDSCR(a.getDSCRD().trim().substring(0, dscrdL));
			
			item.setADDSCR(a.getQTY());
			
			if (a.getARTCOMP() != null) {
				if (a.getARTCOMP().getCOMPNO() != null) {
					ARTIKELSTAMM.ITEM.COMP comp = new ARTIKELSTAMM.ITEM.COMP();
					comp.setGLN(a.getARTCOMP().getCOMPNO().toString());
					item.setCOMP(comp);
				}
			}
			
			setPriceInformation(a, item);
			
			if (determineIfPharma(a)) {
				amendPharmaFromOddb2XmlArticle(a, item);
				pharma.getITEM().add(item);
			} else {
				nonPharma.getITEM().add(item);
			}
		}
		
	}
	
	private static void amendPharmaFromOddb2XmlArticle(ART a, ITEM item){
		// product dependen values
		if (item.getGTIN() != null) {
			PRD product =
				Oddb2XmlHelper
					.getItemInProductListByGTIN(oddb2xmlProducts.getPRD(), item.getGTIN());
			if (product != null) {
				// ATC
				item.setATC(product.getATC());
				// PKG_SIZE
				if (product.getPackGrSwissmedic() != null)
					try {
						int value = Integer.parseInt(product.getPackGrSwissmedic());
						item.setPKGSIZE(value);
					} catch (NumberFormatException nfe) {
						System.out.println(item.getGTIN() + ": Invalid number string "
							+ product.getPackGrSwissmedic() + " in Product/PackGrSwissmedic");
					}
				
				// GENCD
				item.setGENERICTYPE(product.getGENCD());
				
			} else {
				System.out.println("[WARNING] No product for " + a.getPHAR() + "/" + a.getDSCRD()
					+ " found.");
			}
		} else {
			System.out.println("[WARNING] No GTIN for " + a.getPHAR() + "/" + a.getDSCRD()
				+ " found, product can not be resolved;");
		}
		
		if (a.getARTINS() != null) {
			BigInteger nincd = a.getARTINS().getNINCD();
			if (nincd != null) {
				int nincdInt = nincd.intValue();
				// SL_ENTRY
				if (nincdInt == 10 || nincdInt == 12)
					item.setSLENTRY(true);
				// LPPV
				if (nincdInt == 20)
					item.setLPPV(true);
			}
		}
		
		// IKSCAT
		item.setIKSCAT(a.getSMCAT());
		
		// DEDUCTIBLE
		// Co-Payment information
		// 1: 20 %
		// 2: 10 %
		if (a.getSLOPLUS() != null) {
			int value = a.getSLOPLUS().intValue();
			if (value == 1) {
				item.setDEDUCTIBLE(20);
			} else if (value == 2) {
				item.setDEDUCTIBLE(10);
			}
		}
		
		// NARCOTIC
		if ("Y".equalsIgnoreCase(a.getCDBG()) && "Y".equalsIgnoreCase(a.getBG())) {
			item.setNARCOTIC(true);
		}
		
		// LIMITATION
		LIM limitation =
			Oddb2XmlHelper.getItemInLimitationListBySwissmedicNo(oddb2xmlLimitations.getLIM(),
				a.getSMNO());
		if (limitation != null) {
			item.setLIMITATION(true);
			// LIMITATION_PTS
			if (limitation.getLIMVAL() != null && limitation.getLIMVAL().length() > 0) {
				item.setLIMITATIONPTS(Integer.parseInt(limitation.getLIMVAL()));
			}
			// LIMITATION_TEXT
			item.setLIMITATIONTEXT(limitation.getDSCRD());
		}
	}
	
	/**
	 * set the price for the item; if we have official values go first. if e.g. we have PPUB and
	 * ZURROSEPPUB, PPUB goes first
	 * 
	 * @param a
	 * @param item
	 */
	private static void setPriceInformation(ART a, ITEM item){
		Double ppub = null;
		Double pexf = null;
		List<ARTPRI> prices = a.getARTPRI();
		for (ARTPRI artpri : prices) {
			if (artpri.getPTYP() == null || artpri.getPRICE() == null) {
				System.out.println(item.getGTIN() + ": Invalid or no price information.");
				continue;
			}
			
			switch (artpri.getPTYP()) {
			case "PPUB":
				ppub = artpri.getPRICE().doubleValue();
				break;
			case "PEXF":
				pexf = artpri.getPRICE().doubleValue();
			case "ZURROSEPUB":
				if (ppub == null)
					ppub = artpri.getPRICE().doubleValue();
				break;
			case "ZURROSE":
				if (pexf == null)
					pexf = artpri.getPRICE().doubleValue();
				break;
			default:
				break;
			}
		}
		if (ppub != null)
			item.setPPUB(ppub);
		if (pexf != null)
			item.setPEXF(pexf);
	}
	
	/**
	 * determines if an article is pharma or non pharma, according to following rule if article
	 * barcode ARTBAR/BC starts with 7680 its pharma, any other case non-pharma
	 * 
	 * @param a
	 * @return true if Pharma
	 */
	private static boolean determineIfPharma(ART a){
		ARTBAR artbar = a.getARTBAR();
		if (artbar == null)
			return false;
		BigInteger bc = artbar.getBC();
		if (bc == null)
			return false;
		return (bc.toString().startsWith("7680"));
	}
	
	final static String pattern = "yyyy-MM-dd'T'hh:mm:ssZ";
	final static SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	
	private static void setArtikelstammHeaderInfo(ARTIKELSTAMM pharma, ARTIKELSTAMM nonPharma)
		throws DatatypeConfigurationException, ParseException{
		
		Date parse = sdf.parse(oddb2xmlArticle.getPRODDATE());
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(parse);
		XMLGregorianCalendar creationDate =
			DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		pharma.setCREATIONDATETIME(creationDate);
		pharma.setMONTH(creationDate.getMonth());
		pharma.setYEAR(creationDate.getYear());
		pharma.setLANG("de");
		
		nonPharma.setCREATIONDATETIME(creationDate);
		nonPharma.setMONTH(creationDate.getMonth());
		nonPharma.setYEAR(creationDate.getYear());
		nonPharma.setLANG("de");
		
		pharma.setCUMULVER(ArtikelstammHelper.getCummulatedVersionNumber(creationDate.getYear(),
			creationDate.getMonth()));
		nonPharma.setCUMULVER(ArtikelstammHelper.getCummulatedVersionNumber(creationDate.getYear(),
			creationDate.getMonth()));
	}
	
	private static void unmarshallOddb2xmlFiles(File oddb2xmlArticleFileObj,
		File oddb2xmlProductFileObj, File oddb2xmlLimitationFileObj) throws JAXBException{
		oddb2xmlArticle = (ARTICLE) Oddb2XmlHelper.unmarshallFile(oddb2xmlArticleFileObj);
		oddb2xmlLimitations = (LIMITATION) Oddb2XmlHelper.unmarshallFile(oddb2xmlLimitationFileObj);
		oddb2xmlProducts = (PRODUCT) Oddb2XmlHelper.unmarshallFile(oddb2xmlProductFileObj);
	}
	
}
