package at.medevit.stammdaten.converter.v11;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

public class ConvertV11 {
	public static final String OPTION_ODDB2XML_ARTICLE_FILE = "oddb2xmlArticleFile";
	public static final String OPTION_ODDB2XML_LIMITATION_FILE = "oddb2xmlLimitationFile";
	public static final String OPTION_ODDB2XML_PRODUCT_FILE = "oddb2xmlProductFile";
	
	static Option oddbArticleFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_ARTICLE_FILE)
		.hasArg().withDescription("oddb2xml_article.xml file").create(OPTION_ODDB2XML_ARTICLE_FILE);
	static Option oddbLimitationFileOption = OptionBuilder
		.withArgName(OPTION_ODDB2XML_LIMITATION_FILE).hasArg()
		.withDescription("oddb_limitation.xml file").create(OPTION_ODDB2XML_LIMITATION_FILE);
	static Option oddbProductFileOption = OptionBuilder.withArgName(OPTION_ODDB2XML_PRODUCT_FILE)
		.hasArg().withDescription("oddb_product.xml file").create(OPTION_ODDB2XML_PRODUCT_FILE);
	
	static DateFormat df = new SimpleDateFormat("ddMMyy");
	
	static String oddb2xmlArticleFileName = null;
	static String oddb2xmlLimitationFileName = null;
	static String oddb2xmlProductFileName = null;
	
	public static void main(String[] args){
		
		System.out.println("---------------------------------------------");
		System.out.println(ConvertV11.class.getName());
		System.out.println("| parameters:");
		for (int i = 0; i < args.length; i++) {
			System.out.println("|- "+args[i]);
		}
		System.out.println("---------------------------------------------");
		
		Options options = new Options();
		
		options.addOption(oddbArticleFileOption);
		options.addOption(oddbLimitationFileOption);
		options.addOption(oddbProductFileOption);
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
				formatter.printHelp("ConvertToArtikelstamm v1.1", options);
				return;
			}
			
			oddb2xmlArticleFileName = line.getOptionValue(OPTION_ODDB2XML_ARTICLE_FILE);
			oddb2xmlLimitationFileName = line.getOptionValue(OPTION_ODDB2XML_LIMITATION_FILE);
			oddb2xmlProductFileName = line.getOptionValue(OPTION_ODDB2XML_PRODUCT_FILE);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
		
		try {
			
			File oddb2xmlArticleFileObj = new File(oddb2xmlArticleFileName);
			File oddb2xmlLimitationFileObj = new File(oddb2xmlLimitationFileName);
			File oddb2xmlProductFileObj = new File(oddb2xmlProductFileName);
			
			ARTIKELSTAMM pharma = new ARTIKELSTAMM();
			ARTIKELSTAMM nonPharma = new ARTIKELSTAMM();
			
			pharma.setTYPE(ArtikelstammConstants.TYPE.P.name());
			pharma.setDATAQUALITY(3);
			nonPharma.setTYPE(ArtikelstammConstants.TYPE.N.name());
			nonPharma.setDATAQUALITY(3);
			
			Oddb2XmlArtikelstammGenerator.generate(pharma, nonPharma, oddb2xmlArticleFileObj,
				oddb2xmlProductFileObj, oddb2xmlLimitationFileObj);
			
			filterOutVetMedArticles(pharma);
			
			File outputPharmaFile =
				ArtikelstammHelper.determineOutputFileName(pharma, oddb2xmlArticleFileObj, null);
			ArtikelstammHelper.marshallToFileSystem(pharma, outputPharmaFile);
			System.out.println("[OK] " + outputPharmaFile.getAbsolutePath());
			
			File outputNonPharmaFile =
				ArtikelstammHelper.determineOutputFileName(nonPharma, oddb2xmlArticleFileObj, null);
			ArtikelstammHelper.marshallToFileSystem(nonPharma, outputNonPharmaFile);
			System.out.println("[OK] " + outputNonPharmaFile.getAbsolutePath());
			
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

	private static void filterOutVetMedArticles(ARTIKELSTAMM pharma){
		List<ITEM> item = pharma.getITEM();
		for (ITEM item2 : item.toArray(new ITEM[0])) {
			if(item2.getATC()==null) continue;
			if(item2.getATC().startsWith("Q")) {
				System.out.println(item2.getGTIN() + ":Removing VetMed Article with ATC "+item2.getATC());
				item.remove(item2);
			}
		}
	}
}
