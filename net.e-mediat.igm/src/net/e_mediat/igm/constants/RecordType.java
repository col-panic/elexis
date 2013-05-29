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
package net.e_mediat.igm.constants;

import java.text.DecimalFormat;

/**
 * Siehe
 * http://www.vitodata.ch/support/hilfe/vitomed2/vitoMed2_Popup/Die_verschiedenen_Recordarten.htm
 * 
 * @author marco
 */
public enum RecordType {

	//@formatter:off
	STAMMSATZ_OHNE_MWST(1),
	UPDATESATZ_OHNE_MWST(2),
	BESTELLSATZ_BASIS_AUSFUEHRLICH(3),
	UPDATESATZ_MIT_MWST(10),
	STAMMSATZ_MIT_MWST(11),
	UPDATESATZ_MIT_MWST_UND_APO_PREIS(20),
	STAMMSATZ_MIT_MWST_UND_APO_PREIS(21);
	//@formatter:on
	
	private int numericRecordart;
	DecimalFormat df = new DecimalFormat("00");
	
	private RecordType(int numericRecordart){
		this.numericRecordart = numericRecordart;
	}
	
	public int getNumericRecordart(){
		return numericRecordart;
	}
	
	public char[] getCharArrayRepresentation(){
		return df.format(numericRecordart).toCharArray();
	}
	
	/**
	 * 
	 * @param recordArt
	 * @return {@link RecordType} if recordArt is valid, else <code>null</code>
	 */
	public static RecordType getByRecordArt(String recordArt){
		try {
			int val = Integer.parseInt(recordArt);
			for (RecordType rt : RecordType.values()) {
				if (rt.getNumericRecordart() == val)
					return rt;
			}
		} catch (NumberFormatException nfe) {}
		return null;
	}
}
