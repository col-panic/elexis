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
package at.medevit.stammdaten.converter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

import net.swissindex.format.SwissindexHelper;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;
import at.medevit.stammdaten.converter.enrich.IGM11ArticleEnricher;
import at.medevit.stammdaten.converter.enrich.Oddb2XmlArticleEnricher;
import at.medevit.stammdaten.converter.enrich.PreparationsEnricher;
import at.medevit.stammdaten.converter.outbound.ElexisFormatConverter;

public class Convert {
	
	public static final String OPTION_INBOUND_FILE = "inboundFile";
	public static final String OPTION_PREPARATIONS_FILE = "preparationsFile";
	public static final String OPTION_ODDB2XML_ARTICLE_FILE = "oddb2xmlArticleFile";
	public static final String OPTION_ODDB2XML_LIMITATION_FILE = "oddb2xmlLimitationFile";
	public static final String OPTION_ODDB2XML_PRODUCT_FILE = "oddb2xmlProductFile";
	public static final String OPTION_IGM11_FILE = "igm11File";
	
	static Option inboundFileOption = OptionBuilder.withArgName(OPTION_INBOUND_FILE).hasArg()
		.withDescription("the file to read the source data from").create(OPTION_INBOUND_FILE);
	static Option preparationsFileOption = OptionBuilder.withArgName(OPTION_INBOUND_FILE).hasArg()
		.withDescription("enrich data with preparations file (PHARMA only)")
		.create(OPTION_PREPARATIONS_FILE);
	static Option oddbArticleFileOption = OptionBuilder
		.withArgName(OPTION_ODDB2XML_ARTICLE_FILE)
		.hasArg()
		.withDescription(
			"enrich with oddb2xml; requires oddb_product and oddb_limitation (PHARMA only)")
		.create(OPTION_ODDB2XML_ARTICLE_FILE);
	static Option oddbLimitationFileOption = OptionBuilder
		.withArgName(OPTION_ODDB2XML_LIMITATION_FILE).hasArg()
		.withDescription("oddb2xml oddb_limitation.xml file")
		.create(OPTION_ODDB2XML_LIMITATION_FILE);
	static Option oddbProductFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_PRODUCT_FILE)
		.hasArg().withDescription("oddb2xml oddb_product.xml file")
		.create(OPTION_ODDB2XML_PRODUCT_FILE);
	static Option igm11FileOption = OptionBuilder.withArgName(OPTION_IGM11_FILE).hasArg()
		.withDescription("IGM 11 full-set file (NON-PHARMA only").create(OPTION_IGM11_FILE);
	
	static DateFormat df = new SimpleDateFormat("ddMMyy");
	static String inboundFileName;
	static String preparationsFileName = null;
	static String oddb2xmlArticleFileName = null;
	static String oddb2xmlLimitationFileName = null;
	static String oddb2xmlProductFileName = null;
	static String igm11FileName = null;
	
	public static void main(String[] args){
		Options options = new Options();
		options.addOption(inboundFileOption);
		options.addOption(preparationsFileOption);
		options.addOption(oddbArticleFileOption);
		options.addOption(oddbLimitationFileOption);
		options.addOption(oddbProductFileOption);
		options.addOption(igm11FileOption);
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (!line.hasOption(OPTION_INBOUND_FILE)) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ConvertToArtikelstamm", options);
				return;
			}
			inboundFileName = line.getOptionValue(OPTION_INBOUND_FILE);
			preparationsFileName = line.getOptionValue(OPTION_PREPARATIONS_FILE);
			if (line.hasOption(OPTION_ODDB2XML_ARTICLE_FILE)
				&& (!line.hasOption(OPTION_ODDB2XML_LIMITATION_FILE) || !line
					.hasOption(OPTION_ODDB2XML_PRODUCT_FILE))) {
				System.out.println("[ERROR] Declaration " + OPTION_ODDB2XML_ARTICLE_FILE
					+ " Option requires parallel declaration of " + OPTION_ODDB2XML_LIMITATION_FILE
					+ " and " + OPTION_ODDB2XML_PRODUCT_FILE + " option");
				return;
			}
			oddb2xmlArticleFileName = line.getOptionValue(OPTION_ODDB2XML_ARTICLE_FILE);
			oddb2xmlLimitationFileName = line.getOptionValue(OPTION_ODDB2XML_LIMITATION_FILE);
			oddb2xmlProductFileName = line.getOptionValue(OPTION_ODDB2XML_PRODUCT_FILE);
			igm11FileName = line.getOptionValue(OPTION_IGM11_FILE);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
		
		try {
			File inboundFileObj = new File(inboundFileName);
			Object inbound = SwissindexHelper.unmarshallFile(inboundFileObj);
			
			ARTIKELSTAMM converted =
				(ARTIKELSTAMM) ElexisFormatConverter.convertFromSwissindex(inbound, "DE");
			converted.setDATAQUALITY(1);
			
			if (ArtikelstammConstants.TYPE.valueOf(converted.getTYPE()) == ArtikelstammConstants.TYPE.P) {
				// PHARMA enrich with Preparations -> v1b
				if (preparationsFileName != null) {
					File preparationsFileObj = new File(preparationsFileName);
					PreparationsEnricher.enrichData(converted, preparationsFileObj);
					converted.setDATAQUALITY(2);
					System.out.println("[OK] PHARMA enriched using " + preparationsFileName);
				}
				
				// PHARMA enrich with oddb2xml -> v2
				if (oddb2xmlArticleFileName != null && oddb2xmlLimitationFileName != null
					&& oddb2xmlProductFileName != null) {
					File oddb2xmlArticleFileObj = new File(oddb2xmlArticleFileName);
					File oddb2xmlLimitationFileObj = new File(oddb2xmlLimitationFileName);
					File oddb2xmlProductFileObj = new File(oddb2xmlProductFileName);
					Oddb2XmlArticleEnricher.enrichData(converted, oddb2xmlArticleFileObj,
						oddb2xmlLimitationFileObj, oddb2xmlProductFileObj);
					converted.setDATAQUALITY(3);
					System.out.println("[OK] PHARMA enriched using " + oddb2xmlArticleFileName);
				}
			}
			
			if (ArtikelstammConstants.TYPE.valueOf(converted.getTYPE()) == ArtikelstammConstants.TYPE.N) {
				if (igm11FileName != null) {
					File igm11FullSetFileObj = new File(igm11FileName);
					IGM11ArticleEnricher.enrichData(converted, igm11FullSetFileObj);
					converted.setDATAQUALITY(3);
					System.out.println("[OK] NON-PHARMA enriched using " + igm11FileName);
				}
			}
			
			String outputFilePrefix = null;
			switch (converted.getDATAQUALITY()) {
			case 1:
				outputFilePrefix = "v1"; // basic, swissindex only
				break;
			case 2:
				outputFilePrefix = "v1b"; // basic, enriched with BAG data
				break;
			case 3:
				outputFilePrefix = "v2"; // full set
				break;
			default:
				outputFilePrefix = "v0";
			}
			
			File outputFile =
				ArtikelstammHelper.determineOutputFileName(converted, inboundFileObj,
					outputFilePrefix);
			ArtikelstammHelper.marshallToFileSystem(converted, outputFile);
			System.out.println("[OK] " + outputFile.getAbsolutePath());
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
