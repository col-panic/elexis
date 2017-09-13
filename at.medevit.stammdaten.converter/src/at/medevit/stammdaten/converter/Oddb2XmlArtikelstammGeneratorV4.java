package at.medevit.stammdaten.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ywesee.oddb2xml.Oddb2XmlHelper;
import com.ywesee.oddb2xml.article.ART;
import com.ywesee.oddb2xml.article.ARTBAR;
import com.ywesee.oddb2xml.article.ARTICLE;
import com.ywesee.oddb2xml.article.ARTPRI;
import com.ywesee.oddb2xml.limitation.LIM;
import com.ywesee.oddb2xml.product.PRD;
import com.ywesee.oddb2xml.sequences.Sequence;
import com.ywesee.oddb2xml.sequences.Sequence.SequenceItem;
import com.ywesee.oddb2xml.sequences.SequencesHelper;

import at.medevit.atc_codes.ATCCode;
import at.medevit.atc_codes.internal.ATCCodes;
import info.artikelstamm.model.v4.ARTIKELSTAMM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.ITEMS.ITEM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.ITEMS.ITEM.COMP;
import info.artikelstamm.model.v4.ARTIKELSTAMM.LIMITATIONS.LIMITATION;
import info.artikelstamm.model.v4.ARTIKELSTAMM.PRODUCTS.PRODUCT;

public class Oddb2XmlArtikelstammGeneratorV4 {
	
	//	Tradonal one 150 mg, Retard-Tabletten [7680551750284]: 		(S)TRADONAL one 150 mg Tabl [5517501]		(P)TRADONAL ONE Ret Tabl 150 mg 50 Stk [null]
	//			Tradonal one 150 mg, Retard-Tabletten [7680551750260]: 		(S)TRADONAL one 150 mg Tabl [5517501]		(P)TRADONAL ONE Ret Tabl 150 mg 20 Stk [null]
	//			Tradonal one 200 mg, Retard-Tabletten [7680551750321]: 		(S)TRADONAL one 200 mg Tabl [5517502]		(P)TRADONAL ONE Ret Tabl 200 mg 20 Stk [null]
	//			Tradonal one 200 mg, Retard-Tabletten [7680551750345]: 		(S)TRADONAL one 200 mg Tabl [5517502]		(P)TRADONAL ONE Ret Tabl 200 mg 50 Stk [null]
	//			Tradonal one 300 mg, Retard-Tabletten [7680551750406]: 		(S)TRADONAL one 300 mg Tabl [5517503]		(P)TRADONAL ONE Ret Tabl 300 mg 50 Stk [null]
	//			Tradonal one 400 mg, Retard-Tabletten [7680551750444]: 		(S)TRADONAL one 400 mg Tabl [5517504]		(P)TRADONAL ONE Ret Tabl 400 mg 50 Stk [null]
	//			Tramadol Streuli [7680577730079]: 		(S)TRAMADOL streuli 50 mg Kaps [5777301]		(P)TRAMADOL Streuli Kaps 50 mg (alt) 10 Stk [null]
	
	/**
	 * Exporting this tool requires to comment these lines in
	 * {@link ArtikelstammHelper#marshallToFileSystem(Object, File)} Schema validationSchema =
	 * schemaFactory.newSchema(schemaLocationUrl); m.setSchema(validationSchema);
	 */
	
	private static ARTICLE oddb2xmlArticle;
	private static com.ywesee.oddb2xml.limitation.LIMITATION oddb2xmlLimitations;
	private static com.ywesee.oddb2xml.product.PRODUCT oddb2xmlProducts;
	
	private static Map<String, ART> oddbArticlesMap = new HashMap<String, ART>();
	private static Map<String, LIMITATION> limitations = new HashMap<String, LIMITATION>();
	private static Map<String, Sequence> sequences = new HashMap<String, Sequence>();
	
	private static Set<String> inactivePharmaGtin = new HashSet<String>();
	
	private static final String SALECD_INACTIVE = "I";
	
	private static int pharma = 0;
	private static int pharma_inactive = 0;
	private static int nonpharma = 0;
	private static int nonpharma_inactive = 0;
	private static int nonPharmaNoGtin = 0;
	
	private static Set<String> GTINS = new HashSet<String>();
	
	public static void generate(ARTIKELSTAMM astamm, File oddb2xmlArticleFileObj,
		File oddb2xmlProductFileObj, File oddb2xmlLimitationFileObj, File oddb2xmlSequencesFileObj)
		throws JAXBException, DatatypeConfigurationException, ParseException, IOException{
		
		System.out.println("Unmarshalling oddb2xml files");
		unmarshallOddb2xmlFiles(oddb2xmlArticleFileObj, oddb2xmlProductFileObj,
			oddb2xmlLimitationFileObj, oddb2xmlSequencesFileObj);
		System.out.println("Setting artikelstamm headers");
		setArtikelstammHeaderInfo(astamm);
		System.out.println("Import pharma articles from oddb_sequences");
		populatePharmaWoDataAndProducts(astamm);
		removeProductsWithNoItemsAvailable(astamm);
		System.out.println("Import non-pharma articles from oddb_article");
		populateNonPharmaWoData(astamm);
		System.out.println("Enrich ITEM information");
		enrichItemData(astamm);
		System.out.println("Ammend LIMITATION information");
		amendLimitationInformation(astamm);
		generateOverviewFile(astamm);
		
		System.out.println("PHARMA: " + pharma + " NON-PHARMA: " + nonpharma);
		System.out.println("PHARMA I: " + pharma_inactive + " NON-PHARMA I: " + nonpharma_inactive);
		System.out.println("NONPHARMA NO GTIN: " + nonPharmaNoGtin);
	}
	
	private static void generateOverviewFile(ARTIKELSTAMM astamm){
		File outFile = new File("overview.txt");
		try (PrintWriter out = new PrintWriter(outFile)) {
			List<ITEM> items = astamm.getITEMS().getITEM();
			Collections.sort(items, new Comparator<ITEM>() {
				
				@Override
				public int compare(ITEM o1, ITEM o2){
					return o1.getDSCR().compareTo(o2.getDSCR());
				}
				
			});
			for (ITEM item : items) {
				out.write(item.getDSCR() + " [" + item.getGTIN() + "]: ");
				if (item.getPRODNO() != null) {
					Sequence sequence = sequences.get(item.getPRODNO());
					if (sequence != null) {
						out.write(
							"\t\t(S)" + sequence.getDscr() + " [" + sequence.getProdno() + "]");
					}
					
					PRD prod = Oddb2XmlHelper.getItemInProductListByGTIN(oddb2xmlProducts.getPRD(),
						item.getGTIN());
					if (prod != null) {
						out.write("\t\t(P)" + prod.getDSCRD() + " [" + prod.getPRODNO() + "]");
					}
				}
				out.write("\n");
			}
			
			out.flush();
			System.out.println("Overview file written to " + outFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Remove PRODUCT entries for which no ITEM is in the list (might be removed as not available in
	 * trade)
	 */
	private static void removeProductsWithNoItemsAvailable(ARTIKELSTAMM astamm){
		Set<PRODUCT> prodNos =
			astamm.getPRODUCTS().getPRODUCT().stream().collect(Collectors.toSet());
		for (PRODUCT prodNo : prodNos) {
			long count = 0;
			count = astamm.getITEMS().getITEM().stream()
				.filter(i -> prodNo.getPRODNO().equalsIgnoreCase(i.getPRODNO())).count();
			if (count == 0) {
				System.out.println("Removing product " + prodNo.getPRODNO() + " " + prodNo.getDSCR()
					+ " as no items referenced.");
				astamm.getPRODUCTS().getPRODUCT().remove(prodNo);
			}
		}
	}
	
	private static void amendLimitationInformation(ARTIKELSTAMM astamm){
		List<ITEM> items = astamm.getITEMS().getITEM();
		for (ITEM item : items) {
			ART art = oddbArticlesMap.get(item.getGTIN());
			if (art == null || art.getSMNO() == null) {
				continue;
			}
			
			LIM limitation = Oddb2XmlHelper
				.getItemInLimitationListBySwissmedicNo(oddb2xmlLimitations.getLIM(), art.getSMNO());
			if (limitation != null) {
				LIMITATION astammLimitation = limitations.get(limitation.getLIMNAMEBAG());
				if (astammLimitation == null) {
					astammLimitation = new LIMITATION();
					astammLimitation.setDSCR(limitation.getDSCRD().trim());
					astammLimitation.setDSCRF(limitation.getDSCRF().trim());
					astammLimitation.setLIMNAMEBAG(limitation.getLIMNAMEBAG());
					// LIMITATION_PTS
					if (limitation.getLIMVAL() != null && limitation.getLIMVAL().length() > 0) {
						astammLimitation.setLIMITATIONPTS(Integer.parseInt(limitation.getLIMVAL()));
					}
					limitations.put(limitation.getLIMNAMEBAG(), astammLimitation);
				}
				
				List<PRODUCT> products = astamm.getPRODUCTS().getPRODUCT();
				for (PRODUCT product : products) {
					if (product.getPRODNO().equals(item.getPRODNO())) {
						product.setLIMNAMEBAG(limitation.getLIMNAMEBAG());
					}
				}
			}
		}
		
		Collection<LIMITATION> values = limitations.values();
		for (LIMITATION limitation : values) {
			astamm.getLIMITATIONS().getLIMITATION().add(limitation);
		}
	}
	
	private static void populatePharmaWoDataAndProducts(ARTIKELSTAMM astamm){
		Map<String, PRD> oddbProductsMap = new HashMap<String, PRD>();
		List<PRD> products = oddb2xmlProducts.getPRD();
		for (PRD product : products) {
			oddbProductsMap.put(product.getPRODNO(), product);
		}
		
		Set<Entry<String, Sequence>> entrySet = sequences.entrySet();
		for (Entry<String, Sequence> entry : entrySet) {
			Sequence product = entry.getValue();
			
			PRODUCT p = new PRODUCT();
			p.setDSCR(product.getDscr());
			p.setDSCRF("___~~MISSING~~__");
			p.setPRODNO(product.getProdno());
			
			PRD prd = oddbProductsMap.get(p.getPRODNO());
			if (prd != null) {
				p.setATC(prd.getATC());
				if (prd.getATC() != null) {
					ATCCode atcCode = ATCCodes.getInstance().getATCCode(prd.getATC());
					if (atcCode != null && atcCode.level == 5) {
						p.setSUBSTANCE(atcCode.name_german);
					}
				}
			} else {
				System.out.println("Missing product [" + p.getPRODNO() + "] in oddbProductsMap");
			}
			
			astamm.getPRODUCTS().getPRODUCT().add(p);
			
			Map<String, SequenceItem> articles = product.getSequenceItems();
			for (SequenceItem article : articles.values()) {
				if (GTINS.contains(article.getGtin())) {
					System.out.println("GTIN [" + article.getGtin() + "] already imported for ["
						+ article.getDesc1() + "] prodNo [" + product.getProdno() + "], exiting.");
					continue;
				}
				
				GTINS.add(article.getGtin());
				
				pharma++;
				ITEM item = new ITEM();
				item.setPRODNO(product.getProdno());
				item.setPHARMATYPE("P");
				item.setDSCR(article.getDesc1());
				item.setGTIN(article.getGtin());
				try {
					int amount = Integer.parseInt(article.getAmount());
					item.setPKGSIZE(amount);
				} catch (NumberFormatException nfe) {
					System.out.println(item.getGTIN() + ": Invalid number string "
						+ article.getAmount() + " in sequences");
				}
				
				item.setPKGSIZESTRING(article.getAmount() + " " + article.getMunit());
				item.setMEASURE(article.getMunit());
				
				astamm.getITEMS().getITEM().add(item);
			}
		}
	}
	
	private static void populateNonPharmaWoData(ARTIKELSTAMM astamm){
		List<ART> articles = oddb2xmlArticle.getART();
		for (ART a : articles) {
			String salecd = a.getSALECD();
			if (SALECD_INACTIVE.equalsIgnoreCase(salecd)) {
				nonpharma_inactive++;
				//				continue;
			}
			
			if (determineIfPharma(a)) {
				continue;
			}
			
			nonpharma++;
			
			String ean = null;
			
			if (a.getARTBAR() != null) {
				if (a.getARTBAR().getBC() != null) {
					BigInteger eanBi = a.getARTBAR().getBC();
					if (eanBi.longValue() == 0) {
						System.out.println("[WARNING] GTIN is 0 for [" + a.getDSCRD() + "]");
						nonPharmaNoGtin++;
						continue;
						
					}
					ean = String.format("%013d", eanBi);
				}
			} else {
				System.out.println("[WARNING] GTIN is null for [" + a.getDSCRD() + "]");
				nonPharmaNoGtin++;
				continue;
			}
			
			if (GTINS.contains(ean)) {
				System.out.println("GTIN already imported for [" + a.getDSCRD() + "]");
				continue;
			}
			
			GTINS.add(ean);
			
			ITEM item = new ITEM();
			item.setPHARMATYPE("N");
			item.setDSCR(a.getDSCRD().trim());
			item.setDSCRF(a.getDSCRF().trim());
			
			item.setGTIN(ean);
			if (a.getPHAR() != null) {
				item.setPHAR(new BigInteger(a.getPHAR()));
			}
			
			if (ean != null) {
				astamm.getITEMS().getITEM().add(item);
			} else {
				System.out.println("WARN NO GTIN: " + item.getDSCR());
			}
		}
	}
	
	private static void enrichItemData(ARTIKELSTAMM astamm){
		List<ITEM> items = astamm.getITEMS().getITEM();
		for (ITEM item : items) {
			ART art = oddbArticlesMap.get(item.getGTIN());
			if (art != null) {
				item.setDSCRF(art.getDSCRF());
				
				if (art.getARTCOMP() != null) {
					if (art.getARTCOMP().getCOMPNO() != null) {
						COMP comp = new COMP();
						comp.setGLN(art.getARTCOMP().getCOMPNO().toString());
						item.setCOMP(comp);
					}
				}
				
				amendPharmaFromOddb2XmlArticle(art, item);
				setPriceInformation(art, item);
			} else {
				item.setDSCRF("___~~MISSING~~__");
				
				System.out.println("Error: could not find art for GTIN " + item.getGTIN());
			}
		}
	}
	
	private static void amendPharmaFromOddb2XmlArticle(ART a, ITEM item){
		// product dependent values
		if (item.getGTIN() != null) {
			PRD product = Oddb2XmlHelper.getItemInProductListByGTIN(oddb2xmlProducts.getPRD(),
				item.getGTIN());
			if (product != null) {
				String measure = item.getMEASURE();
				if (measure == null && product.getEinheitSwissmedic() != null) {
					measure = product.getEinheitSwissmedic();
				}
				item.setMEASURE(measure);
				
				// PKG_SIZE
				if (product.getPackGrSwissmedic() != null && item.getPKGSIZE() == null) {
					if (measure != null) {
						item.setPKGSIZESTRING(product.getPackGrSwissmedic() + " " + measure);
					} else {
						try {
							int value = Integer.parseInt(product.getPackGrSwissmedic());
							item.setPKGSIZE(value);
						} catch (NumberFormatException nfe) {
							System.out.println(item.getGTIN() + ": Invalid number string "
								+ product.getPackGrSwissmedic() + " in Product/PackGrSwissmedic");
						}
						
						if (product.getEinheitSwissmedic() != null) {
							item.setPKGSIZESTRING(product.getPackGrSwissmedic() + " "
								+ product.getEinheitSwissmedic());
						}
					}
				}
				
				// GENCD
				String gencd = product.getGENCD();
				// co-marketing article, requires artikelstamm update, will
				// remove for the time being - 4194
				gencd = ("C".equalsIgnoreCase(gencd)) ? null : gencd;
				item.setGENERICTYPE(gencd);
			} else {
				System.out.println("[WARNING] No product for " + a.getPHAR() + "/" + a.getDSCRD()
					+ " found, not enriching data.");
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
		
		if (a.getPHAR() != null) {
			item.setPHAR(new BigInteger(a.getPHAR()));
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
		
	}
	
	/**
	 * set the price for the item; if we have official values go first. if e.g. we have PPUB and
	 * ZURROSEPPUB, PPUB goes first
	 * 
	 * https://redmine.medelexis.ch/issues/3404 we do prefer zurRose prices now
	 * 
	 * @param a
	 * @param item
	 */
	private static void setPriceInformation(ART a, ITEM item){
		Double ppub = null;
		Double pexf = null;
		
		HashMap<String, ARTPRI> hmPrices = new HashMap<>();
		for (ARTPRI artpri : a.getARTPRI()) {
			if (artpri.getPTYP() == null || artpri.getPRICE() == null) {
				System.out
					.println("ERROR " + item.getGTIN() + ": Invalid or no price information.");
				continue;
			}
			hmPrices.put(artpri.getPTYP(), artpri);
		}
		
		// #3645
		if (item.isSLENTRY() != null && item.isSLENTRY()) {
			if (hmPrices.containsKey("PPUB")) {
				ppub = hmPrices.get("PPUB").getPRICE().doubleValue();
			} else {
				System.out
					.println("ERROR no PPUB for " + item.getDSCR() + " (" + item.getGTIN() + ")");
			}
			
			if (hmPrices.containsKey("PEXF")) {
				pexf = hmPrices.get("PEXF").getPRICE().doubleValue();
			} else {
				System.out
					.println("ERROR no PEXF for " + item.getDSCR() + " (" + item.getGTIN() + ")");
			}
		} else {
			// fetch public prices
			if (hmPrices.containsKey("ZURROSEPUB")) {
				ppub = hmPrices.get("ZURROSEPUB").getPRICE().doubleValue();
			} else if (hmPrices.containsKey("PPUB")) {
				ppub = hmPrices.get("PPUB").getPRICE().doubleValue();
			}
			
			// fetch ex-factory prices
			if (hmPrices.containsKey("ZURROSE")) {
				pexf = hmPrices.get("ZURROSE").getPRICE().doubleValue();
			} else if (hmPrices.containsKey("PEXF")) {
				pexf = hmPrices.get("PEXF").getPRICE().doubleValue();
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
	
	private static void setArtikelstammHeaderInfo(ARTIKELSTAMM astamm)
		throws DatatypeConfigurationException, ParseException{
		
		Date parse = sdf.parse(oddb2xmlArticle.getPRODDATE());
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(parse);
		XMLGregorianCalendar creationDate =
			DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		astamm.setCREATIONDATETIME(creationDate);
		astamm.setBUILDDATETIME(
			DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		astamm.setMONTH(creationDate.getMonth());
		astamm.setYEAR(creationDate.getYear());
		astamm.setLANG("DE");
	}
	
	private static void unmarshallOddb2xmlFiles(File oddb2xmlArticleFileObj,
		File oddb2xmlProductFileObj, File oddb2xmlLimitationFileObj, File oddb2xmlSequencesFileObj)
		throws JAXBException, IOException{
		oddb2xmlArticle = (ARTICLE) Oddb2XmlHelper.unmarshallFile(oddb2xmlArticleFileObj);
		oddb2xmlLimitations = (com.ywesee.oddb2xml.limitation.LIMITATION) Oddb2XmlHelper
			.unmarshallFile(oddb2xmlLimitationFileObj);
		oddb2xmlProducts = (com.ywesee.oddb2xml.product.PRODUCT) Oddb2XmlHelper
			.unmarshallFile(oddb2xmlProductFileObj);
		sequences = SequencesHelper.unmarshallSequences(oddb2xmlSequencesFileObj);
		
		List<ART> articles = oddb2xmlArticle.getART();
		for (ART article : articles) {
			if (article.getARTBAR() != null) {
				String ean;
				if (article.getARTBAR().getBC() != null) {
					BigInteger eanBi = article.getARTBAR().getBC();
					ean = String.format("%013d", eanBi);
					oddbArticlesMap.put(ean, article);
					if (article.getSALECD() != null && "I".equalsIgnoreCase(article.getSALECD())) {
						inactivePharmaGtin.add(ean);
					}
				}
			}
		}
	}
	
}
