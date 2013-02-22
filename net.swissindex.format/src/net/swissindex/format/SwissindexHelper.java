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
package net.swissindex.format;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.swissindex.ws.nonpharma.NONPHARMA;
import net.swissindex.ws.pharma.PHARMA;

import org.xml.sax.SAXException;

public class SwissindexHelper {
	static String PHARMA_XSD_LOCATION = "SwissindexPharma_out_V101_corrected.xsd";
	static String NONPHARMA_XSD_LOCATION = "SwissindexNonPharma_out_V101_corrected.xsd";
	
	/**
	 * 
	 * @param xmlFile
	 * @return either an object of type {@link PHARMA} or {@link NONPHARMA}
	 * @throws JAXBException
	 */
	public static Object unmarshallFile(File xmlFile) throws JAXBException{
		
		Unmarshaller u =
			JAXBContext.newInstance(PHARMA.class, NONPHARMA.class).createUnmarshaller();
		
		// Schema schema = sf.newSchema(validationSchema);
		// u.setSchema( schema );
		
		return u.unmarshal(xmlFile);
		
	}
	
	/**
	 * 
	 * @param newData
	 *            either {@link PHARMA} or {@link NONPHARMA} else throws
	 *            {@link IllegalArgumentException}
	 * @param outputFile
	 *            the {@link File} to write the output to
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static void marshallToFileSystem(Object newData, File outputFile) throws JAXBException,
		SAXException{
		
		URL schemaLocation = null;
		if (newData instanceof PHARMA) {
			schemaLocation = SwissindexHelper.class.getResource(PHARMA_XSD_LOCATION);
		} else if (newData instanceof NONPHARMA) {
			schemaLocation = SwissindexHelper.class.getResource(NONPHARMA_XSD_LOCATION);
		} else {
			throw new IllegalArgumentException();
		}
		
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		Schema validationSchema = factory.newSchema(schemaLocation);
		
		Marshaller m = JAXBContext.newInstance(PHARMA.class, NONPHARMA.class).createMarshaller();
		m.setSchema(validationSchema);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(newData, outputFile);
	}
}
