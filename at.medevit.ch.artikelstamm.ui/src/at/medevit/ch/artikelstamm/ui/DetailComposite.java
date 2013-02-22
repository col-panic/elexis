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

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import at.medevit.atc_codes.ATCCode;
import at.medevit.ch.artikelstamm.ui.internal.ATCCodeServiceConsumer;

public class DetailComposite extends Composite {
	private DataBindingContext m_bindingContext;
	
	private WritableValue item = new WritableValue(null, IArtikelstammItem.class);
	
	private Label lblDSCR;
	private Label lblPHZNR;
	private Label lblGTIN;
	private Label lblHERSTELLER;
	private Label lblEXFACTORYPRICE;
	private Label lblPUBLICPRICE;
	private Tree treeATC;
	private Label lblAbgabekategorie;
	private Label lblABGABEKATEGORIE;
	private Label lblSelbstbehalt;
	private Label lblSELBSTBEHALT;
	private Button btnCheckIsNarcotic;
	
	private UpdateValueStrategy doubleToString = new UpdateValueStrategy()
		.setConverter(NumberToStringConverter.fromDouble(false));
	private UpdateValueStrategy integerToString = new UpdateValueStrategy()
		.setConverter(NumberToStringConverter.fromInteger(true));
	
	public DetailComposite(Composite parent, int style){
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite headerComposite = new Composite(this, SWT.NONE);
		headerComposite.setLayout(new GridLayout(4, false));
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblDSCR = new Label(headerComposite, SWT.NONE);
		lblDSCR.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDSCR.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		lblDSCR.setFont(SWTResourceManager.getFont("Lucida Grande", 16, SWT.BOLD));
		GridData gd_lblDSCR = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_lblDSCR.widthHint = 435;
		lblDSCR.setLayoutData(gd_lblDSCR);
		
		Label lblGtin = new Label(headerComposite, SWT.NONE);
		lblGtin.setToolTipText("European Article Number / Global Trade Index Number");
		lblGtin.setText("EAN/GTIN");
		
		lblGTIN = new Label(headerComposite, SWT.NONE);
		lblGTIN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPhznr = new Label(headerComposite, SWT.NONE);
		lblPhznr.setToolTipText("Pharmacode");
		lblPhznr.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPhznr.setText("Pharmacode");
		
		lblPHZNR = new Label(headerComposite, SWT.NONE);
		lblPHZNR.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblAbgabekategorie = new Label(headerComposite, SWT.NONE);
		lblAbgabekategorie.setText("Abgabekategorie");
		
		lblABGABEKATEGORIE = new Label(headerComposite, SWT.NONE);
		lblABGABEKATEGORIE.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(headerComposite, SWT.NONE);
		new Label(headerComposite, SWT.NONE);
		
		Group grpPackungsgroessenPreise = new Group(this, SWT.NONE);
		grpPackungsgroessenPreise.setText("Preis");
		grpPackungsgroessenPreise.setLayout(new GridLayout(6, false));
		grpPackungsgroessenPreise.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
			1));
		Label lblExFactoryPreis = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblExFactoryPreis.setText("Ex-Factory");
		
		lblEXFACTORYPRICE = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblEXFACTORYPRICE.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPublicPrice = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblPublicPrice.setText("Publikumspreis");
		
		lblPUBLICPRICE = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblPUBLICPRICE.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblSelbstbehalt = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblSelbstbehalt.setText("Selbstbehalt (%)");
		
		lblSELBSTBEHALT = new Label(grpPackungsgroessenPreise, SWT.NONE);
		lblSELBSTBEHALT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grepATCCode = new Group(this, SWT.NONE);
		grepATCCode.setLayout(new GridLayout(1, false));
		grepATCCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grepATCCode.setText("ATC-Code");
		
		treeATC = new Tree(grepATCCode, SWT.BORDER);
		GridData gd_treeATC = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_treeATC.heightHint = 80;
		treeATC.setLayoutData(gd_treeATC);
		treeATC.setBackground(parent.getBackground());
		
		Group grpMarker = new Group(this, SWT.None);
		grpMarker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpMarker.setText("Marker");
		grpMarker.setLayout(new GridLayout(1, false));
		
		btnCheckIsNarcotic = new Button(grpMarker, SWT.CHECK);
		btnCheckIsNarcotic.setText("Betäubungsmittel");
		
		Group grpHersteller = new Group(this, SWT.NONE);
		grpHersteller.setLayout(new GridLayout(1, false));
		grpHersteller.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHersteller.setText("Hersteller");
		
		lblHERSTELLER = new Label(grpHersteller, SWT.NONE);
		lblHERSTELLER.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_bindingContext = initDataBindings();
		
	}
	
	@Override
	protected void checkSubclass(){
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setItem(IArtikelstammItem obj){
		item.setValue(obj);
		
		treeATC.removeAll();
		if (ATCCodeServiceConsumer.getATCCodeService() != null) {
			List<ATCCode> atcHierarchy =
				ATCCodeServiceConsumer.getATCCodeService().getHierarchyForATCCode(obj.getATCCode());
			if (atcHierarchy != null && atcHierarchy.size() > 0) {
				ATCCode rootCode = atcHierarchy.get(atcHierarchy.size() - 1);
				TreeItem root = new TreeItem(treeATC, SWT.None);
				root.setText(rootCode.atcCode + " " + rootCode.name);
				TreeItem parent = root;
				for (int i = atcHierarchy.size() - 2; i >= 0; i--) {
					ATCCode code = atcHierarchy.get(i);
					TreeItem newItem = new TreeItem(parent, SWT.None);
					newItem.setText(code.atcCode + " " + code.name);
					parent = newItem;
					if (i == 0)
						treeATC.setSelection(newItem);
				}
			}
		} else {
			TreeItem root = new TreeItem(treeATC, SWT.None);
			root.setText(obj.getATCCode());
		}
	}
	
	protected DataBindingContext initDataBindings(){
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextLblDSCRObserveWidget = WidgetProperties.text().observe(lblDSCR);
		IObservableValue itemDSCRObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "DSCR", String.class).observeDetail(item);
		bindingContext.bindValue(observeTextLblDSCRObserveWidget, itemDSCRObserveDetailValue,
			new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		//
		IObservableValue observeTextLblGTINObserveWidget = WidgetProperties.text().observe(lblGTIN);
		IObservableValue itemGTINObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "GTIN", String.class).observeDetail(item);
		bindingContext.bindValue(observeTextLblGTINObserveWidget, itemGTINObserveDetailValue,
			new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		//
		IObservableValue observeTextLblPHZNRObserveWidget =
			WidgetProperties.text().observe(lblPHZNR);
		IObservableValue itemPHARObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "PHAR", String.class).observeDetail(item);
		bindingContext.bindValue(observeTextLblPHZNRObserveWidget, itemPHARObserveDetailValue,
			new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		//
		
		IObservableValue observeTextLblHERSTELLERObserveWidget =
			WidgetProperties.text().observe(lblHERSTELLER);
		IObservableValue itemManufacturerLabelObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "manufacturerLabel", String.class)
				.observeDetail(item);
		bindingContext.bindValue(observeTextLblHERSTELLERObserveWidget,
			itemManufacturerLabelObserveDetailValue, null, null);
		//
		
		IObservableValue observeTextLblEXFACTORYPRICEObserveWidget =
			WidgetProperties.text().observe(lblEXFACTORYPRICE);
		IObservableValue itemExFactoryPriceObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "exFactoryPrice", double.class)
				.observeDetail(item);
		bindingContext.bindValue(observeTextLblEXFACTORYPRICEObserveWidget,
			itemExFactoryPriceObserveDetailValue, null, doubleToString);
		//
		IObservableValue observeTextLblPUBLICPRICEObserveWidget =
			WidgetProperties.text().observe(lblPUBLICPRICE);
		IObservableValue itemPublicPriceObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "publicPrice", double.class)
				.observeDetail(item);
		bindingContext.bindValue(observeTextLblPUBLICPRICEObserveWidget,
			itemPublicPriceObserveDetailValue, null, doubleToString);
		//
		IObservableValue observeTextLblABGABEKATEGORIEObserveWidget =
			WidgetProperties.text().observe(lblABGABEKATEGORIE);
		IObservableValue itemSwissmedicCategoryObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "swissmedicCategory", String.class)
				.observeDetail(item);
		bindingContext.bindValue(observeTextLblABGABEKATEGORIEObserveWidget,
			itemSwissmedicCategoryObserveDetailValue, null, null);
		//
		IObservableValue observeTextLblSELBSTBEHALTObserveWidget =
			WidgetProperties.text().observe(lblSELBSTBEHALT);
		IObservableValue itemDeductibleObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "deductible", int.class).observeDetail(
				item);
		bindingContext.bindValue(observeTextLblSELBSTBEHALTObserveWidget,
			itemDeductibleObserveDetailValue, null, integerToString);
		//
		IObservableValue observeSelectionBtnCheckIsNarcoticObserveWidget =
			WidgetProperties.selection().observe(btnCheckIsNarcotic);
		IObservableValue itemNarcoticObserveDetailValue =
			PojoProperties.value(IArtikelstammItem.class, "narcotic", boolean.class).observeDetail(
				item);
		bindingContext.bindValue(observeSelectionBtnCheckIsNarcoticObserveWidget,
			itemNarcoticObserveDetailValue, null, null);
		//
		return bindingContext;
	}
}
