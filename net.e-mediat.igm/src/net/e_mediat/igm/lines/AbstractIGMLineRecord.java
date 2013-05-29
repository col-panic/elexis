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
package net.e_mediat.igm.lines;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import net.e_mediat.igm.constants.RecordType;

//@formatter:off
/**
* Nr Name	Feldbezeichnung						Position	Länge	Typ
* 01 RECA	Recordart							001–002		2		alpha
* 02 CMUT	Mutationscode						003-003		1		num
* 03 PHAR	Pharmacode							004-010		7		num
*/
//@formatter:on
public abstract class AbstractIGMLineRecord {
	char[] RECA;
	char[] CMUT;
	char[] PHAR;
	
	DecimalFormat twoDForm = new DecimalFormat("#.##");
	BigDecimal hundred = new BigDecimal(100);
	
	public AbstractIGMLineRecord(RecordType recordType){
		this.RECA = recordType.getCharArrayRepresentation();
	}
	
	public abstract void parseInputLine(String inputLine);
	
	protected void parseHeader(String inputLine){
		// TODO Check -> not valid for all types!!
		if (inputLine.length() < 96)
			throw new IllegalArgumentException("Invalid length " + inputLine.length());
		
		CMUT = parseValue(inputLine, 2, 3);
		PHAR = parseValue(inputLine, 3, 10);
	}
	
	public RecordType getRecordType(){
		return RecordType.getByRecordArt(new String(RECA));
	}
	
	/**
	 * 
	 * @return Mutationscode
	 */
	public String getCMUT(){
		return new String(CMUT);
	}
	
	/**
	 * @return Pharmacode
	 */
	public String getPHAR(){
		return new String(PHAR);
	}
	
	/**
	 * 
	 * @param outputArray
	 *            the char array to store the string into
	 * @param inputString
	 * @param concision
	 *            the concision of the array, either 'r'ight or 'l'eft, where a right concision
	 *            fills the array with '0' and a left concision with ' '
	 */
	protected void insertIntoCharArray(char[] outputArray, String inputString, char concision){
		char[] incoming = inputString.trim().toCharArray();
		switch (concision) {
		case 'r':
			Arrays.fill(outputArray, '0');
			System.arraycopy(incoming, 0, outputArray, outputArray.length - incoming.length,
				incoming.length);
			break;
		case 'l':
			Arrays.fill(outputArray, ' ');
			for (int i = 0; i < outputArray.length; i++) {
				if (incoming.length > i) {
					outputArray[i] = incoming[i];
				}
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Parse the char values of an input line into a char array
	 * 
	 * @param inputLine
	 * @param i
	 * @param j
	 * @return
	 */
	protected char[] parseValue(String inputLine, int i, int j){
		return inputLine.substring(i, j).toCharArray();
	}
}
