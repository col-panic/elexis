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
package at.medevit.swissindex.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FetchCommon {
	static DateFormat df = new SimpleDateFormat("ddMMyy");
	static DateFormat dfInfo = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	static String LANGUAGE_TO_DOWNLOAD = "DE";
	static final boolean REMOVE_INACTIVE = true;
	
	public static void timedSysout(String outputstring){
		System.out.println("[" + dfInfo.format(new Date()) + "] " + outputstring);
	}
	
}
