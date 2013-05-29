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

import net.e_mediat.igm.constants.RecordType;

// @formatter:off
/**
 * Nr Name	Feldbezeichnung						Position	Länge	Typ
 * 01 RECA	Recordart							001–002		2		alpha
 * 02 CMUT	Mutationscode						003-003		1		num
 * 03 PHAR	Pharmacode							004-010		7		num
 * 04 ABEZ	Artikelbezeichnung					011-060		50		alpha
 * 05 PRMO	Arztpreis (=Galexis-Basis-Preis)	061-066		6		num
 * 06 PRPU 	Publikumspreis (inkl. MWSt)			067-072		6		num
 * 07 CKZL	Kassenzulässigkeit					073-073		1		num
 * 08 CLAG	Lagerart							074-074		1		num
 * 09 CBGG	Betäubung-Gift						075-075		1		num
 * 10 CIKS	Swissmedic-Listencode				076-076		1		alpha
 * 11 ITHE	Index-Therapeutikus					077-083		7		num
 * 12 CEAN	EAN-Code							084-096		13		num
 */
//@formatter:on
public class IGMLineRecordType01 extends AbstractIGMLineRecord {
	
	char[] ABEZ;
	char[] PRMO;
	char[] PRPU;
	char[] CKZL;
	char[] CLAG;
	char[] CBGG;
	char[] CIKS;
	char[] ITHE;
	char[] CEAN;
	
	public IGMLineRecordType01(){
		super(RecordType.STAMMSATZ_OHNE_MWST);
	}
	
	@Override
	public void parseInputLine(String inputLine){
		parseHeader(inputLine);
		if (!inputLine.startsWith(new String(RECA)))
			throw new IllegalArgumentException("Invalid recordType for treatment in "
				+ IGMLineRecordType01.class.getSimpleName());
		
		ABEZ = parseValue(inputLine, 10, 60);
		PRMO = parseValue(inputLine, 60, 66);
		PRPU = parseValue(inputLine, 66, 72);
		CKZL = parseValue(inputLine, 72, 73);
		CLAG = parseValue(inputLine, 73, 74);
		CBGG = parseValue(inputLine, 74, 75);
		CIKS = parseValue(inputLine, 75, 76);
		ITHE = parseValue(inputLine, 76, 83);
		CEAN = parseValue(inputLine, 83, 96);
	}
	
	public String getCEAN(){
		return new String(CEAN);
	}
	
}
