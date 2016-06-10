package at.medevit.stammdaten.converter.v2;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ywesee.oddb2xml.Oddb2XmlHelper;
import com.ywesee.oddb2xml.Sequence;
import com.ywesee.oddb2xml.Sequence.SequenceItem;
import com.ywesee.oddb2xml.article.ART;
import com.ywesee.oddb2xml.article.ARTBAR;
import com.ywesee.oddb2xml.article.ARTICLE;
import com.ywesee.oddb2xml.article.ARTPRI;
import com.ywesee.oddb2xml.limitation.LIM;
import com.ywesee.oddb2xml.product.PRD;

import at.medevit.atc_codes.ATCCode;
import at.medevit.atc_codes.internal.ATCCodes;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEMS.ITEM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEMS.ITEM.COMP;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.LIMITATIONS.LIMITATION;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.PRODUCTS.PRODUCT;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

public class Oddb2XmlArtikelstammGenerator {
	
	/**
	 * Exporting this tool requires to comment these lines in
	 * {@link ArtikelstammHelper#marshallToFileSystem(Object, File)} Schema validationSchema =
	 * schemaFactory.newSchema(schemaLocationUrl); m.setSchema(validationSchema);
	 */
	
	private static ARTICLE oddb2xmlArticle;
	private static com.ywesee.oddb2xml.limitation.LIMITATION oddb2xmlLimitations;
	private static com.ywesee.oddb2xml.product.PRODUCT oddb2xmlProducts;
	
	private static HashSet<String> articleIds = new HashSet<String>();
	
	private static Map<String, PRODUCT> products = new HashMap<String, PRODUCT>();
	private static Map<String, LIMITATION> limitations = new HashMap<String, LIMITATION>();
	private static Map<String, Sequence> sequences = new HashMap<String, Sequence>();
	
	private static final String SALECD_INACTIVE = "I";
	
	private static int collisions = 0;
	private static int inactive = 0;
	
	public static void generate(ARTIKELSTAMM astamm, File oddb2xmlArticleFileObj,
		File oddb2xmlProductFileObj, File oddb2xmlLimitationFileObj, File oddb2xmlSequencesFileObj)
		throws JAXBException, DatatypeConfigurationException, ParseException, IOException{
		
		System.out.println("Unmarshalling oddb2xml files");
		unmarshallOddb2xmlFiles(oddb2xmlArticleFileObj, oddb2xmlProductFileObj,
			oddb2xmlLimitationFileObj, oddb2xmlSequencesFileObj);
		System.out.println("Setting artikelstamm headers");
		setArtikelstammHeaderInfo(astamm);
		System.out.println("Import oddb2xml_article");
		populateFromOddb2Xml(astamm);
		System.out.println("Adding products...");
		populateProductNames(astamm);
		populateProducts(astamm);
		System.out.println("Adding limitations...");
		populateLimitations(astamm);
		
		System.out.println("STATS: collisions: " + collisions + "/ inactive " + inactive);
	}
	
	private static void populateProductNames(ARTIKELSTAMM astamm){
		Collection<PRODUCT> values = products.values();
		for (PRODUCT product : values) {
			String prodno = product.getPRODNO();
			if (prodno.length() == 7) {
				if (!sequences.containsKey(prodno)) {
					continue;
				} else {
					Sequence seq = sequences.get(prodno);
					product.setDSCR(seq.getDscr());
					product.setDSCRF(seq.getDcsrf());
				}
			} else {
				System.out.println("PRODNO length is NOT 7 chars: " + product.getPRODNO());
			}
		}
	}
	
	private static void populateProducts(ARTIKELSTAMM astamm){
		Collection<PRODUCT> values = products.values();
		for (PRODUCT product : values) {
			astamm.getPRODUCTS().getPRODUCT().add(product);
		}
	}
	
	private static void populateLimitations(ARTIKELSTAMM astamm){
		Collection<LIMITATION> values = limitations.values();
		for (LIMITATION limitation : values) {
			astamm.getLIMITATIONS().getLIMITATION().add(limitation);
		}
	}
	
	private static void populateFromOddb2Xml(ARTIKELSTAMM astamm){
		List<ART> articles = oddb2xmlArticle.getART();
		for (ART a : articles) {
			String salecd = a.getSALECD();
			if (SALECD_INACTIVE.equalsIgnoreCase(salecd)) {
				inactive++;
				System.out.println("I: " + a.getDSCRD() + " (" + a.getPHAR() + ")");
				continue;
			}
			
			ITEM item = new ITEM();
			
			String phar = a.getPHAR();
			String ean = "";
			
			if (a.getARTBAR() != null) {
				if (a.getARTBAR().getBC() != null) {
					BigInteger eanBi = a.getARTBAR().getBC();
					ean = String.format("%013d", eanBi);
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
			//			int dscrdL = (a.getDSCRD().trim().length() > 49) ? 50 : a.getDSCRD().trim().length();
			//			item.setDSCR(a.getDSCRD().trim().substring(0, dscrdL));
			item.setDSCR(a.getDSCRD().trim());
			item.setDSCRF(a.getDSCRF().trim());
			
			if (a.getARTCOMP() != null) {
				if (a.getARTCOMP().getCOMPNO() != null) {
					COMP comp = new COMP();
					comp.setGLN(a.getARTCOMP().getCOMPNO().toString());
					item.setCOMP(comp);
				}
			}
			
			if (determineIfPharma(a)) {
				amendPharmaFromOddb2XmlArticle(a, item);
				item.setPHARMATYPE("P");
			} else {
				item.setPHARMATYPE("N");
			}
			
			astamm.getITEMS().getITEM().add(item);
			
			setPriceInformation(a, item);
		}
		
	}
	
	private static void amendPharmaFromOddb2XmlArticle(ART a, ITEM item){
		// product dependen values
		if (item.getGTIN() != null) {
			PRD product = Oddb2XmlHelper.getItemInProductListByGTIN(oddb2xmlProducts.getPRD(),
				item.getGTIN());
			if (product != null) {
				String prodno = product.getPRODNO();
				
				assert (prodno != null);
				
				PRODUCT astammProduct = products.get(prodno.toString());
				if (astammProduct == null) {
					astammProduct = new PRODUCT();
					astammProduct.setPRODNO(prodno.toString());
					astammProduct.setATC(product.getATC());
					astammProduct.setDSCR((product.getDSCRD()!=null) ? product.getDSCRD() : "___~~MISSING~~__");
					astammProduct.setDSCRF((product.getDSCRF()!=null) ? product.getDSCRF() : "___~~MISSING~~__");
					
					if (product.getATC() != null) {
						ATCCode atcCode = ATCCodes.getInstance().getATCCode(product.getATC());
						if (atcCode != null && atcCode.level == 5) {
							astammProduct.setSUBSTANCE(atcCode.name_german);
						}
					}
					
					// LIMITATION
					LIM limitation = Oddb2XmlHelper.getItemInLimitationListBySwissmedicNo(
						oddb2xmlLimitations.getLIM(), a.getSMNO());
					if (limitation != null) {
						LIMITATION astammLimitation = limitations.get(limitation.getLIMNAMEBAG());
						if (astammLimitation == null) {
							astammLimitation = new LIMITATION();
							astammLimitation.setDSCR(limitation.getDSCRD().trim());
							astammLimitation.setDSCRF(limitation.getDSCRF().trim());
							astammLimitation.setLIMNAMEBAG(limitation.getLIMNAMEBAG());
							// LIMITATION_PTS
							if (limitation.getLIMVAL() != null
								&& limitation.getLIMVAL().length() > 0) {
								astammLimitation
									.setLIMITATIONPTS(Integer.parseInt(limitation.getLIMVAL()));
							}
							limitations.put(limitation.getLIMNAMEBAG(), astammLimitation);
						}
						astammProduct.setLIMNAMEBAG(limitation.getLIMNAMEBAG());
					}
					
					// TODO set text
					products.put(prodno.toString(), astammProduct);
				}
				
				item.setPRODNO(prodno.toString());
				
				String measure = null;
				Sequence sequence = sequences.get(prodno.toString());
				if (sequence != null) {
					SequenceItem sequenceItem = sequence.getSequenceItems().get(item.getGTIN());
					if (sequenceItem != null) {
						measure = sequenceItem.getMunit();
					}
				}
				
				if (measure == null && product.getEinheitSwissmedic() != null) {
					measure = product.getEinheitSwissmedic();
				}
				
				item.setMEASURE(measure);
				
				// PKG_SIZE
				if (product.getPackGrSwissmedic() != null) {
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
				System.out.println(
					"[WARNING] No product for " + a.getPHAR() + "/" + a.getDSCRD() + " found.");
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
		sequences = Oddb2XmlHelper.unmarshallSequences(oddb2xmlSequencesFileObj);
	}
	
}
