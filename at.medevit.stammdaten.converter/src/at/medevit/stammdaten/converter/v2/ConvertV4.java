package at.medevit.stammdaten.converter.v2;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

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
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEMS;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.LIMITATIONS;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.PRODUCTS;
import at.medevit.stammdaten.converter.Oddb2XmlArtikelstammGeneratorV4;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

/**
 * Support for Elexis_Artikelstamm_v4.xsd
 *
 */
public class ConvertV4 {
	public static final String OPTION_ODDB2XML_ARTICLE_FILE = "oddb2xmlArticleFile";
	public static final String OPTION_ODDB2XML_LIMITATION_FILE = "oddb2xmlLimitationFile";
	public static final String OPTION_ODDB2XML_PRODUCT_FILE = "oddb2xmlProductFile";
	public static final String OPTION_ODDB2XML_SEQUENCES_FILE = "oddb2xmlSequencesFile";
	
	static Option oddbArticleFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_ARTICLE_FILE)
		.hasArg().withDescription("oddb2xml_article.xml file").create(OPTION_ODDB2XML_ARTICLE_FILE);
	static Option oddbLimitationFileOption = OptionBuilder
		.withArgName(OPTION_ODDB2XML_LIMITATION_FILE).hasArg()
		.withDescription("oddb_limitation.xml file").create(OPTION_ODDB2XML_LIMITATION_FILE);
	static Option oddbProductFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_PRODUCT_FILE)
		.hasArg().withDescription("oddb_product.xml file").create(OPTION_ODDB2XML_PRODUCT_FILE);
	static Option oddbSwissmedicSequencesFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_SEQUENCES_FILE)
			.hasArg().withDescription("oddb2xml_swissmedic_sequences.csv file").create(OPTION_ODDB2XML_SEQUENCES_FILE);
	
	static DateFormat df = new SimpleDateFormat("ddMMyy");
	
	static String oddb2xmlArticleFileName = null;
	static String oddb2xmlLimitationFileName = null;
	static String oddb2xmlProductFileName = null;
	static String oddb2xmlSequencesFileName = null;
	
	public static void main(String[] args) throws IOException{
		
		System.out.println("---------------------------------------------");
		System.out.println(ConvertV4.class.getName());
		System.out.println("| parameters:");
		for (int i = 0; i < args.length; i++) {
			System.out.println("|- "+args[i]);
		}
		System.out.println("---------------------------------------------");
		
		Options options = new Options();
		
		options.addOption(oddbArticleFileOption);
		options.addOption(oddbLimitationFileOption);
		options.addOption(oddbProductFileOption);
		options.addOption(oddbSwissmedicSequencesFileOption);
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (!line.hasOption(OPTION_ODDB2XML_ARTICLE_FILE)
				|| !line.hasOption(OPTION_ODDB2XML_LIMITATION_FILE)
				|| !line.hasOption(OPTION_ODDB2XML_PRODUCT_FILE)) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ConvertToArtikelstamm v4", options);
				return;
			}
			
			oddb2xmlArticleFileName = line.getOptionValue(OPTION_ODDB2XML_ARTICLE_FILE);
			oddb2xmlLimitationFileName = line.getOptionValue(OPTION_ODDB2XML_LIMITATION_FILE);
			oddb2xmlProductFileName = line.getOptionValue(OPTION_ODDB2XML_PRODUCT_FILE);
			oddb2xmlSequencesFileName = line.getOptionValue(OPTION_ODDB2XML_SEQUENCES_FILE);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
		
		try {
			
			File oddb2xmlArticleFileObj = new File(oddb2xmlArticleFileName);
			File oddb2xmlLimitationFileObj = new File(oddb2xmlLimitationFileName);
			File oddb2xmlProductFileObj = new File(oddb2xmlProductFileName);
			File oddb2xmlSequencesFileObj = new File(oddb2xmlSequencesFileName);
			
			ARTIKELSTAMM astamm = new ARTIKELSTAMM();
			astamm.setSETTYPE("F"); // full data-set
			
			astamm.setITEMS(new ITEMS());
			astamm.setPRODUCTS(new PRODUCTS());
			astamm.setLIMITATIONS(new LIMITATIONS());
			
			// old format #4836
//			Oddb2XmlArtikelstammGenerator.generate(astamm, oddb2xmlArticleFileObj,
//				oddb2xmlProductFileObj, oddb2xmlLimitationFileObj, oddb2xmlSequencesFileObj);
			Oddb2XmlArtikelstammGeneratorV4.generate(astamm, oddb2xmlArticleFileObj,
				oddb2xmlProductFileObj, oddb2xmlLimitationFileObj, oddb2xmlSequencesFileObj);
			
			File outputFile =
				ArtikelstammHelper.determineOutputFileName(astamm, oddb2xmlArticleFileObj, null);
			ArtikelstammHelper.marshallToFileSystem(astamm, outputFile);
			System.out.println("[OK] " + outputFile.getAbsolutePath());
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
