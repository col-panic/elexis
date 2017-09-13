package at.medevit.stammdaten.converter.fixset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import at.medevit.stammdaten.converter.ArtikelstammHelper;
import info.artikelstamm.model.v4.ARTIKELSTAMM;


public class FindGTINReferencedInStauffacherInArtikelstamm {
	
	public static void main(String[] args) throws JAXBException, SAXException, IOException{
		int notFound = 0;
		int notFoundPharma = 0;
		int foundInArticle = 0;
		ARTIKELSTAMM existingArtikelstamm = loadExistingArtikelstamm();
		Set<String> existingGTINS = populateExistingGtins(existingArtikelstamm);
		System.out.println("Indexed " + existingGTINS.size() + " from Artikelstamm v"
			+ existingArtikelstamm.getVERSIONID());
		
		List<String> referencedGTINs;
		InputStream is =
			CreateStauffacherFixset.class.getResourceAsStream("deltaList.txt");
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
			referencedGTINs = buffer.lines().collect(Collectors.toList());
		}
		
		List<ImportItem> input = processInputFile();
		
		for (String string : referencedGTINs) {
			if (existingGTINS.contains(string)) {
				continue;
			}
			if(string.startsWith("7680")) {
				notFoundPharma++;
			}
			ImportItem foundItem = null;
			for (ImportItem importItem : input) {
				if(importItem.gtin.equalsIgnoreCase(string)) {
					foundItem = importItem;
					break;
				}
			}
			
			if(foundItem!= null) {
				foundInArticle++;
				System.out.println("[" + string + "] not found in Artikelstamm BUT articles ["+foundItem.name+"]");
			} else {
				notFound++;
				System.out.println("[" + string + "] not found in Artikelstamm AND articles");
			}

		}
		
		System.out.println(existingGTINS.size()+" of which not found "+notFound +" of total "+referencedGTINs.size());
		System.out.println("NotFoundPharma "+notFoundPharma);
		System.out.println("Found in Article "+foundInArticle);
	}
	
	private static Set<String> populateExistingGtins(ARTIKELSTAMM existingArtikelstamm){
		return existingArtikelstamm.getITEMS().getITEM().stream().filter(i -> i.getGTIN() != null)
			.map(i -> i.getGTIN()).collect(Collectors.toSet());
	}
	
	private static ARTIKELSTAMM loadExistingArtikelstamm() throws JAXBException, SAXException{
		InputStream is =
			CreateStauffacherFixset.class.getResourceAsStream("artikelstamm_011216.xml");
		return ArtikelstammHelper.unmarshallInputStream(is);
	}
	
	private static List<ImportItem> processInputFile() throws IOException{
		List<ImportItem> inputList = new ArrayList<ImportItem>();
		InputStream is = CreateStauffacherFixset.class.getResourceAsStream("articles.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		// skip the header of the csv
		inputList = br.lines().map(CreateStauffacherFixset.mapToItem).collect(Collectors.toList());
		br.close();
		return inputList;
	}
	
}
