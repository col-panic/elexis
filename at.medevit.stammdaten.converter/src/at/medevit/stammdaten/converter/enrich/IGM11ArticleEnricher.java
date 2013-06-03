package at.medevit.stammdaten.converter.enrich;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.e_mediat.igm.IGMFile;
import net.e_mediat.igm.constants.RecordType;
import net.e_mediat.igm.lines.IGMLineRecordType11;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;

public class IGM11ArticleEnricher {
	
	static List<IGMLineRecordType11> entries;
	
	public static void enrichData(ARTIKELSTAMM artikelstamm, File igm11FullSetFileObj){
		IGMFile igm11 = new IGMFile();
		try {
			igm11.readInputFile(igm11FullSetFileObj);
			entries =
				(List<IGMLineRecordType11>) igm11.getLinesOfType(RecordType.STAMMSATZ_MIT_MWST);
			addInformationToArtikelstamm(artikelstamm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addInformationToArtikelstamm(ARTIKELSTAMM artikelstamm){
		int failCounter = 0;
		int overallCounter = 0;
		int successCounter = 0;
		for (IGMLineRecordType11 igm11Line : entries) {
			ITEM item = ArtikelstammHelper.getItemInListByGTIN(artikelstamm, igm11Line.getCEAN());
			if (item == null) {
				// try to find by Pharmacode
				item =
					ArtikelstammHelper.getItemInListByPharmacode(artikelstamm, igm11Line.getPHAR());
			}
			if (item != null) {
				if (igm11Line.getPRMO() > 0.0f)
					item.setPEXF(igm11Line.getPRMO());
				if (igm11Line.getPRPU() > 0.0f)
					item.setPPUB(igm11Line.getPRPU());
				
				successCounter++;
			} else {
				System.out.println("[INFO] No corresponding phar/GTIN entry " + igm11Line.getPHAR()
					+ "/" + igm11Line.getCEAN() + " found in Artikelstamm");
				failCounter++;
			}
			overallCounter++;
		}
		System.out.println("[INFO] fail: " + failCounter + " success: " + successCounter
			+ " total IGM: " + overallCounter + " total Artikelstamm-NonPharma: "
			+ artikelstamm.getITEM().size());
	}
	
}
