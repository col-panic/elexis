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
package at.medevit.ch.artikelstamm.elexisv21.common.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;
import org.eclipse.core.databinding.conversion.StringToNumberConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;

import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ui.DetailComposite;
import ch.artikelstamm.elexisv21.common.ArtikelstammItem;
import ch.elexis.Desk;
import ch.elexis.actions.ElexisEventDispatcher;
import ch.elexis.data.Kontakt;
import ch.elexis.data.PersistentObject;
import ch.elexis.dialogs.KontaktSelektor;
import ch.elexis.views.IDetailDisplay;
import ch.elexis.views.artikel.Messages;

public class DetailDisplay implements IDetailDisplay {
	
	private UpdateValueStrategy integerToString = new UpdateValueStrategy()
		.setConverter(NumberToStringConverter.fromInteger(true));
	private UpdateValueStrategy stringToInteger = new UpdateValueStrategy()
		.setConverter(StringToNumberConverter.toInteger(true));
	
	private WritableValue item = new WritableValue(null, ArtikelstammItem.class);
	
	private DetailComposite dc = null;
	private Text txtLIEFERANT;
	
	@Override
	public Class<? extends PersistentObject> getElementClass(){
		return ArtikelstammItem.class;
	}
	
	@Override
	public void display(Object obj){
		ArtikelstammItem ai = (ArtikelstammItem) obj;
		if (dc != null)
			dc.setItem(ai);
		item.setValue(ai);
		txtLIEFERANT.setText((ai.getLieferant().exists()) ? ai.getLieferant().getLabel() : "");
	}
	
	@Override
	public String getTitle(){
		return ArtikelstammConstants.CODESYSTEM_NAME;
	}
	
	@Override
	public Composite createDisplay(Composite parent, IViewSite site){
		if (dc == null) {
			dc = new DetailComposite(parent, SWT.None);
			
			addLagerhaltungGroupToComposite(dc);
		}
		return dc;
	}
	
	/**
	 * This method adds the Elexis v2.1 aspect of stock to the detail display. As this functionality
	 * is specific to Elexis v2.1 it is not included in the base {@link DetailComposite}
	 * 
	 * @param dc
	 *            the {@link DetailComposite} of {@link ArtikelstammItem}
	 */
	private void addLagerhaltungGroupToComposite(final DetailComposite dc){
		DataBindingContext bindingContext = new DataBindingContext();
		
		Group grpLagerhaltung = new Group(dc, SWT.NONE);
		grpLagerhaltung.setText("Lagerhaltung");
		grpLagerhaltung.setLayout(new GridLayout(4, false));
		grpLagerhaltung.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblMaxbestand = new Label(grpLagerhaltung, SWT.NONE);
		lblMaxbestand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxbestand.setText("Max. Pckg. an Lager");
		
		Text txtMAXBESTAND = new Text(grpLagerhaltung, SWT.BORDER);
		txtMAXBESTAND.setTextLimit(4);
		GridData gd_txtMAXBESTAND = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtMAXBESTAND.widthHint = 40;
		txtMAXBESTAND.setLayoutData(gd_txtMAXBESTAND);
		
		IObservableValue propertyMax =
			PojoProperties.value(ArtikelstammItem.class, "maxbestand", Integer.class)
				.observeDetail(item);
		IObservableValue targetMax = WidgetProperties.text(SWT.Modify).observe(txtMAXBESTAND);
		bindingContext.bindValue(targetMax, propertyMax, stringToInteger, integerToString);
		
		Label lblMinbestand = new Label(grpLagerhaltung, SWT.NONE);
		lblMinbestand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinbestand.setText("Min. Pckg. an Lager");
		
		Text txtMINBESTAND = new Text(grpLagerhaltung, SWT.BORDER);
		txtMINBESTAND.setTextLimit(4);
		GridData gd_txtMINBESTAND = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtMINBESTAND.widthHint = 40;
		txtMINBESTAND.setLayoutData(gd_txtMINBESTAND);
		IObservableValue propertyMin =
			PojoProperties.value(ArtikelstammItem.class, "minbestand", Integer.class)
				.observeDetail(item);
		IObservableValue targetMin = WidgetProperties.text(SWT.Modify).observe(txtMINBESTAND);
		bindingContext.bindValue(targetMin, propertyMin, stringToInteger, integerToString);
		
		Label lblAktuellPckgAn = new Label(grpLagerhaltung, SWT.NONE);
		lblAktuellPckgAn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAktuellPckgAn.setText("Aktuell Pckg. an Lager");
		
		Text txtISTBESTAND = new Text(grpLagerhaltung, SWT.BORDER);
		txtISTBESTAND.setTextLimit(4);
		GridData gd_txtISTBESTAND = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtISTBESTAND.widthHint = 40;
		txtISTBESTAND.setLayoutData(gd_txtISTBESTAND);
		IObservableValue propertyIst =
			PojoProperties.value(ArtikelstammItem.class, "istbestand", Integer.class)
				.observeDetail(item);
		IObservableValue targetIst = WidgetProperties.text(SWT.Modify).observe(txtISTBESTAND);
		bindingContext.bindValue(targetIst, propertyIst, stringToInteger, integerToString);
		
		Label lblLieferant = new Label(grpLagerhaltung, SWT.NONE);
		lblLieferant.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLieferant.setText("Lieferant");
		lblLieferant.setForeground(Desk.getColorRegistry().get(Desk.COL_BLUE));
		
		txtLIEFERANT = new Text(grpLagerhaltung, SWT.BORDER | SWT.READ_ONLY);
		txtLIEFERANT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblLieferant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e){
				if (item.getValue() == null)
					return;
				KontaktSelektor ksl =
					new KontaktSelektor(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						Kontakt.class, Messages.Artikeldetail_lieferant,
						Messages.Artikeldetail_LieferantWaehlen, Kontakt.DEFAULT_SORT);
				if (ksl.open() == IDialogConstants.OK_ID) {
					ArtikelstammItem ai = (ArtikelstammItem) item.getValue();
					Kontakt k = (Kontakt) ksl.getSelection();
					ai.setLieferant(k);
					String lbl = ai.getLieferant().getLabel();
					if (lbl.length() > 15) {
						lbl = lbl.substring(0, 12) + "..."; //$NON-NLS-1$
					}
					txtLIEFERANT.setText(lbl);
					ElexisEventDispatcher.reload(ArtikelstammItem.class);
				}
			}
		});
		
	}
}