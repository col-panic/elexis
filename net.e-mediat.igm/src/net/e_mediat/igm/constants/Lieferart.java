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

public enum Lieferart {
	NORMAL(1),
	POST_EXPRESS(2),
	BAHN(3),
	AUTO(4),
	AUSSENDIENST(5),
	WIRD_ABGEHOLT(6);
	
	int lieferart;
	
	/**
	 * Delivery type for the {@link IGMLineRecordType03}
	 * @param lieferart
	 */
	private Lieferart(int lieferart) {
		this.lieferart = lieferart;
	}
	
	public int getLieferart() {
		return lieferart;
	}
}
