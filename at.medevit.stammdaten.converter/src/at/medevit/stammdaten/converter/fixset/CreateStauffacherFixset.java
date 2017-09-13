package at.medevit.stammdaten.converter.fixset;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.SAXException;

import at.medevit.stammdaten.converter.ArtikelstammHelper;
import info.artikelstamm.model.v4.ARTIKELSTAMM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.ITEMS;
import info.artikelstamm.model.v4.ARTIKELSTAMM.ITEMS.ITEM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.LIMITATIONS;
import info.artikelstamm.model.v4.ARTIKELSTAMM.PRODUCTS;
import info.artikelstamm.model.v4.ARTIKELSTAMM.PRODUCTS.PRODUCT;

public class CreateStauffacherFixset {
	public static void main(String[] args)
		throws DatatypeConfigurationException, SAXException, JAXBException, IOException{
		ARTIKELSTAMM astamm = new ARTIKELSTAMM();
		astamm.setPRODUCTS(new PRODUCTS());
		astamm.setITEMS(new ITEMS());
		astamm.setLIMITATIONS(new LIMITATIONS());
		astamm.setSETTYPE("D");
		
		GregorianCalendar gc = new GregorianCalendar();
		XMLGregorianCalendar creationDate =
			DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		astamm.setCREATIONDATETIME(creationDate);
		astamm.setVERSIONID(9000);
		
		Set<String> prodNoSet = new HashSet<String>();
		
		int pharma = 0;
		int nonPharma = 0;
		int alreadyInAS = 0;
		int addedViaNewerAS = 0;
		int addedProdViaNewerAS = 0;
		int addedViaNewerASPharma = 0;
		
		ARTIKELSTAMM _011116 = loadExistingArtikelstamm("artikelstamm_011116.xml");
		ARTIKELSTAMM _151116 = loadExistingArtikelstamm("artikelstamm_151116.xml");
		ARTIKELSTAMM _011216 = loadExistingArtikelstamm("artikelstamm_011216.xml");
		
		Set<String> missingToMatch;
		InputStream is = CreateStauffacherFixset.class.getResourceAsStream("deltaList.txt");
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
			missingToMatch = buffer.lines().collect(Collectors.toSet());
		}
		
		for (Iterator<String> iterator = missingToMatch.iterator(); iterator.hasNext();) {
			String missing = (String) iterator.next();
			List<ITEM> item = _151116.getITEMS().getITEM();
			for (ITEM i : item) {
				if (missing.equalsIgnoreCase(i.getGTIN())) {
					addedViaNewerAS++;
					if (missing.startsWith("7680")) {
						addedViaNewerASPharma++;
					}
					astamm.getITEMS().getITEM().add(i);
					iterator.remove();
					if (i.getPRODNO() != null) {
						String prodno = i.getPRODNO();
						if (!prodNoSet.contains(prodno)) {
							List<PRODUCT> product = _011216.getPRODUCTS().getPRODUCT();
							for (PRODUCT p : product) {
								if (prodno.equalsIgnoreCase(p.getPRODNO())) {
									astamm.getPRODUCTS().getPRODUCT().add(p);
									prodNoSet.add(prodno);
								}
							}
						}
					}
					break;
				}
			}
		}
		
		for (Iterator<String> iterator = missingToMatch.iterator(); iterator.hasNext();) {
			String missing = (String) iterator.next();
			List<ITEM> item = _011216.getITEMS().getITEM();
			for (ITEM i : item) {
				if (missing.equalsIgnoreCase(i.getGTIN())) {
					addedViaNewerAS++;
					if (missing.startsWith("7680")) {
						addedViaNewerASPharma++;
					}
					astamm.getITEMS().getITEM().add(i);
					iterator.remove();
					if (i.getPRODNO() != null) {
						String prodno = i.getPRODNO();
						if (!prodNoSet.contains(prodno)) {
							List<PRODUCT> product = _011216.getPRODUCTS().getPRODUCT();
							for (PRODUCT p : product) {
								if (prodno.equalsIgnoreCase(p.getPRODNO())) {
									astamm.getPRODUCTS().getPRODUCT().add(p);
									prodNoSet.add(prodno);
								}
							}
						}
					}
					break;
				}
			}
		}
		
		Set<String> existingGTINS = populateExistingGtins(_011116);
		System.out.println(
			"Indexed " + existingGTINS.size() + " from Artikelstamm v" + _011116.getVERSIONID());
		
		List<ImportItem> input = processInputFile();
		
		StringBuilder errorSB = new StringBuilder();
		
		Set<ImportItem> collect = input.stream().filter(p -> p.gtin.length() == 13)
			.filter(p -> !p.gtin.contains(".")).collect(Collectors.toSet());
		for (ImportItem importItem : collect) {
			System.out
				.println(importItem.itemNumber + ": " + importItem.name + " " + importItem.gtin);
			
			if (existingGTINS.contains(importItem.gtin)) {
				alreadyInAS++;
				errorSB.append("GTIN [" + importItem.gtin + "] already in AS, not adding\n");
				continue;
			}
			
			String setProdno = null;
			if (importItem.gtin.startsWith("7680")) {
				pharma++;
				PRODUCT prod = new PRODUCT();
				prod.setDSCR(importItem.name);
				prod.setDSCRF("~~missing~~");
				if (importItem.atc != null && importItem.atc.length() > 0) {
					prod.setATC(importItem.atc);
				}
				
				int counter = 0;
				String prodNo;
				do {
					counter++;
					// These artifical PRODNOS all start with 99
					prodNo = "99" + importItem.gtin.substring(4, 9) + counter;
					assert (prodNo.length() == 8);
				} while (prodNoSet.contains(prodNo));
				prodNoSet.add(prodNo);
				prod.setPRODNO(prodNo);
				
				setProdno = prodNo;
				
				List<PRODUCT> products = astamm.getPRODUCTS().getPRODUCT();
				products.add(prod);
			} else {
				nonPharma++;
			}
			ITEM item = new ITEM();
			item.setDSCR(importItem.name);
			item.setDSCRF(importItem.name);
			item.setGTIN(importItem.gtin);
			if (importItem.pharm != null && importItem.pharm.length() > 0) {
				item.setPHAR(BigInteger.valueOf(Long.parseLong(importItem.pharm)));
			}
			if (setProdno != null) {
				item.setPHARMATYPE("P");
				item.setPRODNO(setProdno);
			} else {
				item.setPHARMATYPE("N");
			}
			
			List<ITEM> items = astamm.getITEMS().getITEM();
			items.add(item);
			
		}
		
		Collections.sort((List<PRODUCT>) astamm.getPRODUCTS().getPRODUCT(),
			new Comparator<PRODUCT>() {
				@Override
				public int compare(PRODUCT o1, PRODUCT o2){
					return (o1.getDSCR().compareTo(o2.getDSCR()));
				}
			});
		
		assert(astamm.getPRODUCTS().getPRODUCT().size()>=pharma);
		
		ArtikelstammHelper.marshallToFileSystem(astamm,
			new File("/Users/marco/Desktop/stauffacherArtikelstammFix.xml"));
		
		System.out.println(collect.size() + " pharma:" + pharma + " nonpharma:" + nonPharma
			+ " alreadyInAS: " + alreadyInAS);
		System.out.println("PRODUCTS: " + astamm.getPRODUCTS().getPRODUCT().size() + " ITEMS:"
			+ astamm.getITEMS().getITEM().size());
		System.out.println("Added via newer AS: " + addedViaNewerAS + " prod via newer AS: "
			+ addedProdViaNewerAS + " pharma " + addedViaNewerASPharma);
		System.out.println("ERRORS:");
		System.out.println(errorSB.toString());
		
	}
	
	private static Set<String> populateExistingGtins(ARTIKELSTAMM existingArtikelstamm){
		return existingArtikelstamm.getITEMS().getITEM().stream().filter(i -> i.getGTIN() != null)
			.map(i -> i.getGTIN()).collect(Collectors.toSet());
	}
	
	private static ARTIKELSTAMM loadExistingArtikelstamm(String filename)
		throws JAXBException, SAXException{
		InputStream is = CreateStauffacherFixset.class.getResourceAsStream(filename);
		return ArtikelstammHelper.unmarshallInputStream(is);
	}
	
	private static List<ImportItem> processInputFile() throws IOException{
		List<ImportItem> inputList = new ArrayList<ImportItem>();
		InputStream is = CreateStauffacherFixset.class.getResourceAsStream("articles.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		// skip the header of the csv
		inputList = br.lines().map(mapToItem).collect(Collectors.toList());
		br.close();
		return inputList;
	}
	
	public static Function<String, ImportItem> mapToItem = (line) -> {
		
		String[] p = line.split("\\|");// a CSV has comma separated lines
		ImportItem item = new ImportItem();
		item.setItemNumber(p[0].trim());
		item.setName(p[1].trim());
		item.setGTIN(p[11].trim());
		item.setPHARM(p[12].trim());
		item.setATC(p[13].trim());
		
		return item;
		
	};
}
