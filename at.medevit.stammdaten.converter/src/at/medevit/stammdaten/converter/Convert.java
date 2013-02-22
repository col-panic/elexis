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
import at.medevit.ch.artikelstamm.ArtikelstammHelper;
import at.medevit.stammdaten.converter.enrich.PreparationsEnricher;
import at.medevit.stammdaten.converter.outbound.ElexisFormatConverter;

public class Convert {
	
	public static final String OPTION_INBOUND_FILE = "inboundFile";
	public static final String OPTION_PREPARATIONS_FILE = "preparationsFile";
	
	static Option inboundFileOption = OptionBuilder.withArgName(OPTION_INBOUND_FILE).hasArg()
		.withDescription("the file to read the source data from").create(OPTION_INBOUND_FILE);
	static Option preparationsFileOption = OptionBuilder.withArgName(OPTION_INBOUND_FILE).hasArg()
		.withDescription("enrich data with preparations file").create(OPTION_PREPARATIONS_FILE);
	
	static DateFormat df = new SimpleDateFormat("ddMMyy");
	static String inboundFileName;
	static String preparationsFileName = null;
	
	public static void main(String[] args){
		Options options = new Options();
		options.addOption(inboundFileOption);
		options.addOption(preparationsFileOption);
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
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
		
		try {
			File inboundFileObj = new File(inboundFileName);
			Object inbound = SwissindexHelper.unmarshallFile(inboundFileObj);
			
			String outputFilePrefix = "v1"; // basic, swissindex only
			
			ARTIKELSTAMM converted =
				(ARTIKELSTAMM) ElexisFormatConverter.convertFromSwissindex(inbound, "DE");
			
			if (preparationsFileName != null) {
				outputFilePrefix = "v1b"; // basic, enriched with BAG data
				File preparationsFileObj = new File(preparationsFileName);
				PreparationsEnricher.enrichData(converted, preparationsFileObj);
				System.out.println("[OK] data enriched using " + preparationsFileName);
			}
			
			File outputFile =
				ArtikelstammHelper.determineOutputFileName(converted, inboundFileObj,
					outputFilePrefix);
			ArtikelstammHelper.marshallToFileSystem(converted, outputFile);
			System.out.println("[OK] " + outputFile.getAbsolutePath());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException je) {
			je.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
