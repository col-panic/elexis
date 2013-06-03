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
package net.e_mediat.bag.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PreparationsParser extends DefaultHandler {
	
	public static final String FLD_PREPARATION = "Preparation";
	public static final String FLD_FLAG_SB20 = "FlagSB20";
	public static final String FLD_ORIG_GEN = "OrgGenCode";
	public static final String FLD_PACK = "Pack";
	public static final String FLD_GTIN = "GTIN";
	public static final String ATTRIBUTE_PHARMACODE = "Pharmacode";
	public static final String FLD_SWISSMEDICNO8 = "SwissmedicNo8";
	public static final String FLD_SWISSMEDIC_CATEGORY = "SwissmedicCategory";
	public static final String FLD_FLAG_NARCOTIC = "FlagNarcosis";
	public static final String FLD_PRICE = "Price";
	public static final String FLD_EXF_PRICE = "ExFactoryPrice";
	public static final String FLD_PUB_PRICE = "PublicPrice";
	public static final String FLD_LIMITATION = "Limitation";
	
	private List<Preparation> preparations;
	private Preparation currentPreparation;
	private Pack currentPack;
	private String currentValue;
	
	private int currentPrice;
	
	public PreparationsParser(){
		preparations = new ArrayList<>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException{
		switch (localName) {
		case FLD_PREPARATION:
			currentPreparation = new Preparation();
			break;
		case FLD_PACK:
			currentPack = new Pack();
			currentPack.pharmacode = attributes.getValue(ATTRIBUTE_PHARMACODE);
			break;
		case FLD_EXF_PRICE:
			currentPrice = 1;
			break;
		case FLD_PUB_PRICE:
			currentPrice = 2;
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		currentValue = new String(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		super.endElement(uri, localName, qName);
		switch (localName) {
		case FLD_PREPARATION:
			preparations.add(currentPreparation);
			currentPreparation = null;
			break;
		case FLD_PACK:
			currentPreparation.getPacks().add(currentPack);
			currentPack = null;
			break;
		case FLD_FLAG_SB20:
			currentPreparation.flagSb20 = (currentValue.equalsIgnoreCase("Y")) ? true : false;
			break;
		case FLD_SWISSMEDIC_CATEGORY:
			currentPreparation.swissmedicCategory = currentValue;
			break;
		case FLD_PRICE:
			if (currentPrice == 1)
				currentPack.pexf = currentValue;
			if (currentPrice == 2)
				currentPack.ppub = currentValue;
			break;
		case FLD_FLAG_NARCOTIC:
			currentPack.flagNarcosis = (currentValue.equalsIgnoreCase("Y")) ? true : false;
			break;
		case FLD_ORIG_GEN:
			currentPreparation.orgGenCode =
				(currentValue.equalsIgnoreCase("G") || currentValue.equalsIgnoreCase("O")) ? currentValue
						: null;
			break;
		case FLD_SWISSMEDICNO8:
			currentPack.swissMedicNo8 = currentValue;
			break;
		case FLD_GTIN:
			currentPack.gtin = currentValue;
		default:
			break;
		}
	}
	
	public List<Preparation> getPreparations(){
		return preparations;
	}
	
}
