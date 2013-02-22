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
package net.e_mediat.bag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.e_mediat.bag.parser.Preparation;
import net.e_mediat.bag.parser.PreparationsParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Preparations {
	
	private PreparationsParser pParser;
	
	public Preparations(File preparationsFile) throws IOException{
		pParser = new PreparationsParser();
		readXMLFile(preparationsFile, pParser);
	}
	
	protected void readXMLFile(File inFile, DefaultHandler parser) throws IOException{
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(parser);
			xr.setErrorHandler(parser);
			
			FileInputStream fis = new FileInputStream(inFile);
			UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(fis);
			InputSource is = new InputSource(ubis);
			xr.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Preparation> getPreparations(){
		return pParser.getPreparations();
	}
}
