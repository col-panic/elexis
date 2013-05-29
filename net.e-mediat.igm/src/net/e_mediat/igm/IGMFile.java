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
package net.e_mediat.igm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.e_mediat.igm.constants.RecordType;
import net.e_mediat.igm.lines.AbstractIGMLineRecord;
import net.e_mediat.igm.lines.IGMLineRecordType01;
import net.e_mediat.igm.lines.IGMLineRecordType11;

import org.apache.commons.io.FileUtils;

public class IGMFile {
	
	private List<AbstractIGMLineRecord> igmLines;
	
	public void readInputFile(String filename) throws IOException{
		File file = new File(filename);
		readInputFile(file);
	}
	
	public void readInputFile(File file) throws IOException{
		List<String> inputLines = FileUtils.readLines(file, "UTF-8");
		igmLines = new ArrayList<AbstractIGMLineRecord>(inputLines.size());
		for (String string : inputLines) {
			igmLines.add(parseInputLine(string));
		}
	}
	
	private AbstractIGMLineRecord parseInputLine(String inputLine){
		String recordType = inputLine.substring(0, 2);
		AbstractIGMLineRecord currentLineParser =
			getLineRecordTypeParser(RecordType.getByRecordArt(recordType));
		if (currentLineParser == null) {
			throw new UnsupportedOperationException("Parser for RecordType " + recordType
				+ " not yet implemented");
		}
		currentLineParser.parseInputLine(inputLine);
		return currentLineParser;
	}
	
	/**
	 * Fetch the concrete parser for a given input line of {@link RecordType}
	 * 
	 * @param recordType
	 * @return class that extends {@link AbstractIGMLineRecord}
	 */
	private AbstractIGMLineRecord getLineRecordTypeParser(RecordType recordType){
		switch (recordType) {
		case STAMMSATZ_MIT_MWST:
			return new IGMLineRecordType11();
		case STAMMSATZ_OHNE_MWST:
			return new IGMLineRecordType01();
		default:
			return null;
		}
	}
	
	/**
	 * Fetch lines of the given recordType parsed out of the input file
	 * 
	 * @param recordType
	 * @return
	 */
	public Object getLinesOfType(RecordType recordType){
		List<AbstractIGMLineRecord> ret = new ArrayList<AbstractIGMLineRecord>();
		for (AbstractIGMLineRecord igmLineRecord : igmLines) {
			if (igmLineRecord.getRecordType() == recordType)
				ret.add(igmLineRecord);
		}
		return ret;
	}
}
