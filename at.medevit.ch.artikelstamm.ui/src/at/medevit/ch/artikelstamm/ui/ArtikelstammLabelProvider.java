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
package at.medevit.ch.artikelstamm.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

public class ArtikelstammLabelProvider extends LabelProvider {
	
	private Image emptyTransparent = ResourceManager.getPluginImage(
		"at.medevit.ch.artikelstamm.ui", "rsc/icons/emptyTransparent.png");
	private Image gGruen = ResourceManager.getPluginImage("at.medevit.ch.artikelstamm.ui",
		"/rsc/icons/ggruen.png");
	private Image oBlau = ResourceManager.getPluginImage("at.medevit.ch.artikelstamm.ui",
		"/rsc/icons/oblau.ico");
	private Image oRot = ResourceManager.getPluginImage("at.medevit.ch.artikelstamm.ui",
		"/rsc/icons/oblau.ico");
	
	@Override
	public String getText(Object element){
		IArtikelstammItem item = (IArtikelstammItem) element;
		StringBuilder sb = new StringBuilder();
		if (item.isInSLList()) {
			sb.append("*"); // * zeigt kassenpflicht eines medikaments an muss ggf. erweitert werden
		}
		if (item.getDeductible() > 0) {
			sb.append("[" + item.getDeductible() + "%] ");
		}
		
		sb.append(item.getLabel());
		return sb.toString();
	}
	
	@Override
	public Image getImage(Object element){
		IArtikelstammItem item = (IArtikelstammItem) element;
		String genericType = item.getGenericType();
		if (genericType == null || genericType.length() != 1)
			return emptyTransparent;
		if (genericType.startsWith("G")) {
			return gGruen;
		} else if (genericType.startsWith("O")) {
			return oBlau;
		} else {
			return oRot;
		}
	}
}
