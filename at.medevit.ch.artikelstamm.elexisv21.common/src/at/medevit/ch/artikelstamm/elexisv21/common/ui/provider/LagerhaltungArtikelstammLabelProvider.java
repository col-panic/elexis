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
package at.medevit.ch.artikelstamm.elexisv21.common.ui.provider;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import at.medevit.ch.artikelstamm.ui.ArtikelstammLabelProvider;
import ch.artikelstamm.elexisv21.common.ArtikelstammItem;
import ch.elexis.Desk;
import ch.elexis.Hub;
import ch.elexis.preferences.PreferenceConstants;

/**
 * {@link LabelProvider} that extends the basic {@link ArtikelstammLabelProvider} to consider the
 * stock status of articles. Applicable to Elexis v2.1 only.
 */
public class LagerhaltungArtikelstammLabelProvider extends ArtikelstammLabelProvider implements
		ITableColorProvider {
	
	private Image blackBoxedImage = ResourceManager.getPluginImage("at.medevit.ch.artikelstamm.ui",
		"/rsc/icons/flag-black.png");
	
	/**
	 * Lagerartikel are shown in blue, articles that should be ordered are shown in red
	 */
	@Override
	public Color getForeground(Object element, int columnIndex){
		ArtikelstammItem ai = (ArtikelstammItem) element;
		if (ai.isLagerartikel()) {
			int trigger =
				Hub.globalCfg.get(PreferenceConstants.INVENTORY_ORDER_TRIGGER,
					PreferenceConstants.INVENTORY_ORDER_TRIGGER_DEFAULT);
			
			int ist = ai.getIstbestand();
			int min = ai.getMinbestand();
			
			boolean order = false;
			switch (trigger) {
			case PreferenceConstants.INVENTORY_ORDER_TRIGGER_BELOW:
				order = (ist < min);
				break;
			case PreferenceConstants.INVENTORY_ORDER_TRIGGER_EQUAL:
				order = (ist <= min);
				break;
			default:
				order = (ist < min);
			}
			
			if (order) {
				return Desk.getColor(Desk.COL_RED);
			} else {
				return Desk.getColor(Desk.COL_BLUE);
			}
		}
		return null;
	}
	
	@Override
	public Color getBackground(Object element, int columnIndex){
		ArtikelstammItem ai = (ArtikelstammItem) element;
		if (ai.isBlackBoxed())
			return Desk.getColor(Desk.COL_GREY60);
		return null;
	}
	
	@Override
	public Image getImage(Object element){
		ArtikelstammItem ai = (ArtikelstammItem) element;
		if (ai.isBlackBoxed())
			return blackBoxedImage;
		return super.getImage(element);
	}
	
}
