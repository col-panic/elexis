/*******************************************************************************
 * Copyright (c) 2012 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 ******************************************************************************/
package at.medevit.elexis.geonames.at.buildDB;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class GemPlzStrContentHandler implements ContentHandler {

	private String currentValue;
	private Datensatz d;
	private int counter = 0;
	private int fullCounter = 0;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("element")) {
			switch (counter) {
			case 0:
				d.setGemnr(currentValue);
				break;
			case 1:
				d.setGemnam38(currentValue);
				break;
			case 2:
				d.setOkz(currentValue);
				break;
			case 3:
				d.setOrtnam(currentValue);
				break;
			case 4:
				d.setSkz(currentValue);
				break;
			case 5:
				d.setStroffi(currentValue);
				break;
			case 6:
				d.setPlznr(currentValue);
				break;
			case 7:
				d.setGemnr2(currentValue);
				break;
			default:
				break;
			}
			counter++;
		} else if (localName.equals("datensatz")) {
			System.out.println(d.gemnr + ": " + d.getStroffi());
			Main.addStrasse(d);
			fullCounter++;
		} else if (localName.equals("daten")) {
			System.out.println(fullCounter + " Strassen");
		}

	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (localName.equals("datensatz")) {
			d = new Datensatz();
			counter = 0;
		}

	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
	}

}
