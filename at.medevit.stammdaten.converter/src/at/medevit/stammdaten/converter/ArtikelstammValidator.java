package at.medevit.stammdaten.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import info.artikelstamm.model.v4.ARTIKELSTAMM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.ITEMS.ITEM;
import info.artikelstamm.model.v4.ARTIKELSTAMM.LIMITATIONS.LIMITATION;
import info.artikelstamm.model.v4.ARTIKELSTAMM.PRODUCTS.PRODUCT;


public class ArtikelstammValidator {
	
	private ARTIKELSTAMM artikelstamm;
	
	Set<String> gtinSet = new HashSet<>();
	Set<String> productSet = new HashSet<>();
	Map<String, List<ITEM>> itemMap = new HashMap<>();
	Set<String> referencedLimitations = new HashSet<>();
	
	public ArtikelstammValidator(ARTIKELSTAMM artikelstamm){
		this.artikelstamm = artikelstamm;
	}
	
	public boolean everyProductNumberIsUnique(){
		boolean error = false;
		List<PRODUCT> product = artikelstamm.getPRODUCTS().getPRODUCT();
		for (PRODUCT prd : product) {
			if (productSet.contains(prd.getPRODNO())) {
				System.out.println("Unique PRODNO violated [" + prd.getPRODNO() + "]");
				error = true;
			} else {
				productSet.add(prd.getPRODNO());
				
				// preparation for #evereyReferencedLimitationIsIncluded
				if (prd.getLIMNAMEBAG() != null) {
					referencedLimitations.add(prd.getLIMNAMEBAG());
				}
			}
		}
		return error;
	}
	
	public boolean everyGTINIsUnique(){
		boolean error = false;
		List<ITEM> items = artikelstamm.getITEMS().getITEM();
		for (ITEM item : items) {
			if (gtinSet.contains(item.getGTIN())) {
				System.out.println("Unique GTIN violated [" + item.getGTIN() + "]");
				error = true;
			} else {
				gtinSet.add(item.getGTIN());
				
				// preparation for #everyProductHasAtLeastOneArticle
				List<ITEM> list = itemMap.get(item.getPRODNO());
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(item);
				itemMap.put(item.getPRODNO(), list);
			}
		}
		return error;
	}
	
	public boolean everyPharmaArticleHasAProductItem(){
		boolean error = false;
		List<ITEM> item = artikelstamm.getITEMS().getITEM();
		for (ITEM item2 : item) {
			if ("P".equals(item2.getPHARMATYPE())) {
				boolean contains = productSet.contains(item2.getPRODNO());
				if (!contains) {
					error = true;
					System.out
						.println("Pharma article has Product violation [" + item2.getDSCR() + "]");
				}
			}
		}
		return error;
	}
	
	public boolean everyProductHasAtLeastOneArticle(){
		boolean error = false;
		List<PRODUCT> product = artikelstamm.getPRODUCTS().getPRODUCT();
		for (PRODUCT product2 : product) {
			List<ITEM> list = itemMap.get(product2.getPRODNO());
			if (list == null || list.size() == 0) {
				error = true;
				System.out
					.println("Product has no article violation [" + product2.getPRODNO() + "]");
			}
		}
		return error;
	}
	
	public boolean everyReferencedLimitationIsIncluded(){
		boolean error = false;
		List<LIMITATION> limitations = artikelstamm.getLIMITATIONS().getLIMITATION();
		Set<String> existingLimitations = new HashSet<>();
		for (LIMITATION limitation : limitations) {
			existingLimitations.add(limitation.getLIMNAMEBAG());
		}
		
		for (String refLim : referencedLimitations) {
			boolean contains = existingLimitations.contains(refLim);
			if (!contains) {
				error = true;
				System.out.println("Referenced limitation is included violation [" + refLim + "]");
			}
		}
		return error;
	}
	
	public boolean semanticChecks(){
		boolean error = false;
		List<ITEM> item = artikelstamm.getITEMS().getITEM();
		for (ITEM item2 : item) {
			if (item2.getPKGSIZE() != null) {
				String string = item2.getPKGSIZE().toString();
				if (string.length() > 6) {
					System.out.println("{" + item2.getGTIN()
						+ "} WARNING possibly invalid package size [" + string + "] string is ["+item2.getPKGSIZE()+"]");
				}
			}
		}
		return error;
	}
	
	public boolean everyGTINIsNumericOnly() {
		boolean error = false;
		List<ITEM> items = artikelstamm.getITEMS().getITEM();
		for (ITEM item : items) {
			String gtin = item.getGTIN();
			if(!StringUtils.isNumeric(gtin)) {
				error = true;
				System.out.println("{"+item.getGTIN()+"} is non numeric for item {"+item.getDSCR()+"}");
			}

		}
		return error;
	}
	
	public boolean validate(){
		boolean error = everyProductNumberIsUnique();
		error |= everyGTINIsUnique();
		error |= everyGTINIsNumericOnly();
		error |= everyPharmaArticleHasAProductItem();
		error |= everyProductHasAtLeastOneArticle();
		error |= everyReferencedLimitationIsIncluded();
		error |= semanticChecks();
		return error;
	}
}
