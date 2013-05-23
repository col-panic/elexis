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
package at.medevit.ch.artikelstamm.elexisv21.common.importer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.statushandlers.StatusManager;
import org.xml.sax.SAXException;

import at.medevit.ch.artikelstamm.ARTIKELSTAMM;
import at.medevit.ch.artikelstamm.ARTIKELSTAMM.ITEM;
import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ArtikelstammConstants.TYPE;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;
import ch.artikelstamm.elexisv21.common.ArtikelstammItem;
import ch.artikelstamm.elexisv21.common.BlackBoxReason;
import ch.elexis.StringConstants;
import ch.elexis.actions.ElexisEventDispatcher;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Prescription;
import ch.elexis.data.Query;
import ch.elexis.data.Verrechnet;
import ch.elexis.util.ImporterPage;
import ch.elexis.util.Log;
import ch.elexis.util.SWTHelper;
import ch.rgw.tools.JdbcLink;

public class ArtikelstammImporter extends ImporterPage {
	
	private static String PLUGIN_ID = "at.medevit.ch.artikelstamm.elexisv21.common";
	
	private static Log logger = Log.get(ArtikelstammImporter.class.getName());
	
	@Override
	public IStatus doImport(IProgressMonitor monitor) throws Exception{
		File file = new File(results[0]);
		return performFileImport(file, monitor);
	}
	
	public IStatus performFileImport(File file, IProgressMonitor monitor){
		monitor.beginTask("Aktualisierung des Artikelstamms", 7);
		monitor.subTask("Einlesen der Aktualisierungsdaten");
		ARTIKELSTAMM importStamm = null;
		try {
			importStamm = ArtikelstammHelper.unmarshallFile(file);
		} catch (JAXBException je) {
			Status status =
				new Status(IStatus.ERROR, PLUGIN_ID, "Fehler beim Einlesen der Import-Datei", je);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
			return Status.CANCEL_STATUS;
		} catch (SAXException saxe) {
			Status status =
				new Status(IStatus.ERROR, PLUGIN_ID, "Fehler beim Einlesen der Import-Datei", saxe);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
			return Status.CANCEL_STATUS;
		}
		monitor.worked(1);
		
		int importStammVersion = importStamm.getCUMULVER();
		// the type of import articles in the file (PHARMA or NONPHARMA)
		TYPE importStammType = ArtikelstammConstants.TYPE.valueOf(importStamm.getTYPE());
		// the current version stored in the database for importStammType
		int currentStammVersion = ArtikelstammItem.getCumulatedVersion(importStammType);
		
		// only continue if the dataset to be imported for importStammType is newer than
		// the current
		if (currentStammVersion > importStammVersion) {
			Status status =
				new Status(IStatus.ERROR, PLUGIN_ID, "Import-Datei ist älter als vorhandener Stand");
			StatusManager.getManager().handle(status, StatusManager.SHOW);
			return Status.CANCEL_STATUS;
		}
		
		// clean all blackbox marks, as we will determine them newly
		monitor.subTask("Black-Box Markierung zurücksetzen");
		resetAllBlackboxMarks(importStammType);
		monitor.worked(1);
		// mark all items of type importStammType still referenced as blackbox
		setBlackboxOnAllReferencedItems(monitor, importStammType);
		// delete all items of type importStammType not blackboxed
		monitor.subTask("Lösche nicht Black-Box Artikel");
		removeAllNonBlackboxedWithVersion(importStammType, currentStammVersion, monitor);
		monitor.worked(1);
		// import the new dataset for type importStammType
		monitor.subTask("Importiere Datensatz " + importStamm.getTYPE() + " "
			+ importStamm.getMONTH() + "/" + importStamm.getYEAR());
		importNewItemsIntoDatabase(importStammType, importStamm, monitor);
		// update the version number for type importStammType
		monitor.subTask("Setze neue Versionsnummer");
		ArtikelstammItem.setCumulatedVersion(importStammType, importStammVersion);
		monitor.worked(1);
		monitor.done();
		
		ElexisEventDispatcher.reload(ArtikelstammItem.class);
		
		return Status.OK_STATUS;
	}
	
	/**
	 * reset all black-box marks for the item to zero, we have to determine them fresh, otherwise
	 * once blackboxed - always blackboxed
	 * 
	 * @param importStammType
	 */
	private void resetAllBlackboxMarks(TYPE importStammType){
		JdbcLink link = PersistentObject.getConnection();
		link.setAutoCommit(false);
		link.exec("UPDATE " + ArtikelstammItem.TABLENAME + " SET "
			+ ArtikelstammItem.FLD_BLACKBOXED + "=" + StringConstants.ZERO + " WHERE "
			+ ArtikelstammItem.FLD_ITEM_TYPE + " LIKE " + JdbcLink.wrap(importStammType.name()));
		try {
			link.commit();
		} catch (Throwable ex) {
			link.rollback();
			logger.log(ex, "Error on resetting blackboxes", Log.WARNINGS);
			link.setAutoCommit(true);
			
		}
		link.setAutoCommit(true);
	}
	
	/**
	 * Set {@link ArtikelstammItem#FLD_BLACKBOXED} = 1 to all items of type importStammType still
	 * being referenced by {@link Prescription}, ...
	 * 
	 * @param monitor
	 * 
	 * @param importStammType
	 */
	private void setBlackboxOnAllReferencedItems(IProgressMonitor monitor, TYPE importStammType){
		// black box all ArtikelStammItem referenced by a prescription
		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
		List<Prescription> resultPrescription =
			new Query<Prescription>(Prescription.class).execute();
		monitor.subTask("BlackBox Markierung für Medikationen");
		subMonitor.beginTask("", resultPrescription.size());
		for (Prescription p : resultPrescription) {
			if (p.getArtikel() instanceof ArtikelstammItem) {
				ArtikelstammItem ai = (ArtikelstammItem) p.getArtikel();
				if (ai.get(ArtikelstammItem.FLD_ITEM_TYPE).equalsIgnoreCase(importStammType.name()))
					ai.set(ArtikelstammItem.FLD_BLACKBOXED,
						BlackBoxReason.IS_REFERENCED_IN_FIXMEDICATION.getNumericalReasonString());
			}
			subMonitor.worked(1);
		}
		subMonitor.done();
		
		// black box all import ArtikelStammItem reference by a konsultations leistung
		SubProgressMonitor subMonitor2 = new SubProgressMonitor(monitor, 1);
		List<Verrechnet> resultVerrechnet = new Query<Verrechnet>(Verrechnet.class).execute();
		monitor.subTask("BlackBox Markierung für Artikel in Konsultationen");
		subMonitor2.beginTask("", resultVerrechnet.size());
		for (Verrechnet vr : resultVerrechnet) {
			if (vr.getVerrechenbar() != null
				&& vr.getVerrechenbar().getCodeSystemName()
					.equals(ArtikelstammConstants.CODESYSTEM_NAME)) {
				ArtikelstammItem ai = ArtikelstammItem.load(vr.getVerrechenbar().getId());
				if (ai.get(ArtikelstammItem.FLD_ITEM_TYPE).equalsIgnoreCase(importStammType.name()))
					ai.set(ArtikelstammItem.FLD_BLACKBOXED,
						BlackBoxReason.IS_REFERENCED_IN_CONSULTATION.getNumericalReasonString());
			}
			subMonitor2.worked(1);
		}
		subMonitor2.done();
		
		// Wenn ein Artikel auf Lager ist, darf er auch nicht gelöscht werden!
		SubProgressMonitor subMonitor3 = new SubProgressMonitor(monitor, 1);
		List<ArtikelstammItem> resultLagerartikel =
			new Query<ArtikelstammItem>(ArtikelstammItem.class).execute();
		monitor.subTask("BlackBox Markierung für Lagerartikel");
		subMonitor3.beginTask("", resultLagerartikel.size());
		for (ArtikelstammItem ai : resultLagerartikel) {
			if (ai.get(ArtikelstammItem.FLD_ITEM_TYPE).equalsIgnoreCase(importStammType.name()))
				if (ai.isLagerartikel())
					ai.set(ArtikelstammItem.FLD_BLACKBOXED,
						BlackBoxReason.IS_ON_STOCK.getNumericalReasonString());
			subMonitor3.worked(1);
		}
		subMonitor3.done();
	}
	
	/**
	 * remove all articles of importStammType with the cummulatedVersion smaller equal
	 * currentStammVersion not marked as black-boxed
	 * 
	 * @param importStammType
	 * @param currentStammVersion
	 * @param monitor
	 */
	private void removeAllNonBlackboxedWithVersion(TYPE importStammType, int currentStammVersion,
		IProgressMonitor monitor){
		Query<ArtikelstammItem> qbe = new Query<ArtikelstammItem>(ArtikelstammItem.class);
		
		qbe.add(ArtikelstammItem.FLD_BLACKBOXED, Query.EQUALS, StringConstants.ZERO);
		qbe.add(ArtikelstammItem.FLD_ITEM_TYPE, Query.EQUALS, importStammType.name());
		qbe.add(ArtikelstammItem.FLD_CUMMULATED_VERSION, Query.LESS_OR_EQUAL, currentStammVersion
			+ "");
		
		monitor.subTask("Suche nach zu entfernenden Artikeln ...");
		List<ArtikelstammItem> qre = qbe.execute();
		
		monitor.subTask("Entferne " + qre.size() + " nicht referenzierte Artikel ...");
		boolean success = ArtikelstammItem.purgeEntries(qre);
		if (!success)
			logger.log("Error purging items", Log.WARNINGS);
	}
	
	private void importNewItemsIntoDatabase(TYPE importStammType, ARTIKELSTAMM importStamm,
		IProgressMonitor monitor){
		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
		List<ITEM> importItemList = importStamm.getITEM();
		subMonitor.beginTask("", importItemList.size());
		
		ArtikelstammItem ai = null;
		for (ITEM item : importItemList) {
			String itemUuid =
				ArtikelstammHelper.createUUID(importStamm.getCUMULVER(), importStammType,
					item.getGTIN(), item.getPHAR());
			// Is the item to be imported already in the database? This should only happen
			// if one re-imports an already imported dataset and the item was marked as black-box
			int foundElements =
				PersistentObject.getConnection().queryInt(
					"SELECT COUNT(*) FROM " + ArtikelstammItem.TABLENAME + " WHERE "
						+ ArtikelstammItem.FLD_ID + " " + Query.LIKE + " "
						+ JdbcLink.wrap(itemUuid));
			
			if (foundElements == 0) {
				ai =
					new ArtikelstammItem(importStamm.getCUMULVER(), importStammType,
						item.getGTIN(), item.getPHAR(), item.getDSCR(), item.getADDSCR());
			} else {
				ai = ArtikelstammItem.load(itemUuid);
				logger.log("Item " + itemUuid + " will be overwritten.", Log.WARNINGS);
			}
			if (item.getATC() != null)
				ai.set(ArtikelstammItem.FLD_ATC, item.getATC());
			if (item.getCOMP() != null) {
				if (item.getCOMP().getNAME() != null)
					ai.set(ArtikelstammItem.FLD_COMP_NAME, item.getCOMP().getNAME());
				if (item.getCOMP().getGLN() != null)
					ai.set(ArtikelstammItem.FLD_COMP_GLN, item.getCOMP().getGLN());
			}
			if (item.getPEXF() != null)
				ai.set(ArtikelstammItem.FLD_PEXF, item.getPEXF().toString());
			if (item.getPPUB() != null)
				ai.set(ArtikelstammItem.FLD_PPUB, item.getPPUB().toString());
			if (item.isSLENTRY() != null && item.isSLENTRY())
				ai.set(ArtikelstammItem.FLD_SL_ENTRY, StringConstants.ONE);
			if (item.getDEDUCTIBLE() != null)
				ai.set(ArtikelstammItem.FLD_DEDUCTIBLE, item.getDEDUCTIBLE().toString());
			if (item.getGENERICTYPE() != null)
				ai.set(ArtikelstammItem.FLD_GENERIC_TYPE, item.getGENERICTYPE());
			if (item.getIKSCAT() != null)
				ai.set(ArtikelstammItem.FLD_IKSCAT, item.getIKSCAT());
			if (item.isNARCOTIC() != null && item.isNARCOTIC())
				ai.set(ArtikelstammItem.FLD_NARCOTIC, StringConstants.ONE);
			// TODO set all the values
			
			subMonitor.worked(1);
		}
		subMonitor.done();
	}
	
	@Override
	public String getTitle(){
		return "Artikelstamm CH Import";
	}
	
	@Override
	public String getDescription(){
		return "Importiere Artikelstamm";
	}
	
	@Override
	public Composite createPage(Composite parent){
		Composite versionInfo = new Composite(parent, SWT.None);
		versionInfo.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		versionInfo.setLayout(new GridLayout(2, false));
		Label lblVersion = new Label(versionInfo, SWT.None);
		lblVersion.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		lblVersion.setText("Aktuelle Versionen:");
		Label lblVERSION = new Label(versionInfo, SWT.None);
		lblVERSION.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		int pharmaCumulV = ArtikelstammItem.getCumulatedVersion(TYPE.P);
		int nonPharmaCumulV = ArtikelstammItem.getCumulatedVersion(TYPE.N);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		
		lblVERSION.setText("Pharma "
			+ sdf.format(ArtikelstammHelper.getDateFromCumulatedVersionNumber(pharmaCumulV))
			+ ", Non-Pharma "
			+ sdf.format(ArtikelstammHelper.getDateFromCumulatedVersionNumber(nonPharmaCumulV)));
		
		Composite ret = new ImporterPage.FileBasedImporter(parent, this);
		ret.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		ret.setLayout(new GridLayout(2, false));
		
		return ret;
	}
}
