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
package ch.artikelstamm.elexisv21.common;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.medevit.ch.artikelstamm.ArtikelstammConstants;
import at.medevit.ch.artikelstamm.ArtikelstammConstants.TYPE;
import at.medevit.ch.artikelstamm.ArtikelstammHelper;
import at.medevit.ch.artikelstamm.ui.IArtikelstammItem;
import ch.elexis.Hub;
import ch.elexis.StringConstants;
import ch.elexis.data.Artikel;
import ch.elexis.data.Fall;
import ch.elexis.data.Query;
import ch.elexis.util.Log;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.Money;
import ch.rgw.tools.TimeTool;
import ch.rgw.tools.VersionInfo;

/**
 * {@link ArtikelstammItem} persistent object implementation. This class conforms both to the
 * requirements of an {@link Artikel} as required by Elexis v2.1 and to the requirements given by
 * the common base in form of {@link IArtikelstammItem}
 */
public class ArtikelstammItem extends Artikel implements IArtikelstammItem {
	private static Log logger = Log.get(ArtikelstammItem.class.getName());
	private static DateFormat df = new SimpleDateFormat("ddMMyy HH:mm");
	
	public static final String TABLENAME = "ARTIKELSTAMM_CH";
	static final String VERSION = "1.0.0";
	
	//@formatter:off
	/** Eintrag zugeh. zu  */ public static final String FLD_CUMMULATED_VERSION = "CUMM_VERSION";
	/** Blackboxed item */ public static final String FLD_BLACKBOXED = "BB";
	/** (P)harma / (N)on Pharma */ public static final String FLD_ITEM_TYPE = "TYPE";
	
	/** Global Trade Index Number **/ public static final String FLD_GTIN = "GTIN";
	/** Pharmacode, unique record identifier */ public static final String FLD_PHAR = "PHAR";
	/** Artikelbeschreibung */ 		public static final String FLD_DSCR = "DSCR";
	/** Additional descr. of the dataset */ public static final String FLD_ADDDSCR = "ADDDSCR";
	/** ATC Code */ 				public static final String FLD_ATC = "ATC";
	/** Manufacturer GLN Number */ 	public static final String FLD_COMP_GLN = "COMP_GLN";
	/** Manufacturer Name */		public static final String FLD_COMP_NAME = "COMP_NAME";
	/** Ex-factory price */ 		public static final String FLD_PEXF = "PEXF";
	/** Public price */ 			public static final String FLD_PPUB = "PPUB";
	/** package size */ 			public static final String FLD_PKG_SIZE = "PKG_SIZE";
	/** Spezialitätenliste Entry */ public static final String FLD_SL_ENTRY = "SL_ENTRY";
	/** Abgabekategorie */ 		public static final String FLD_IKSCAT = "IKSCAT";
	/** Limitation ja/nein */ 	public static final String FLD_LIMITATION = "LIMITATION";
	/** Limitationspunkte */ 	public static final String FLD_LIMITATION_PTS = "LIMITIATION_PTS";
	/** Limitationstext */ 		public static final String FLD_LIMITATION_TEXT = "LIMITATION_TXT";
	/** Generiktyp O/G */ 		public static final String FLD_GENERIC_TYPE = "GENERIC_TYPE";
	/** Generikum verfügbar */ 	public static final String FLD_HAS_GENERIC = "HAS_GENERIC";
	/** LPPV Eintrag ja/nein */	public static final String FLD_LPPV = "LPPV";
	/** Selbstbehalt in % */	public static final String FLD_DEDUCTIBLE = "DEDUCTIBLE";
	/** Ist Betäubungsmittel */	public static final String FLD_NARCOTIC = "NARCOTIC";
	/** CAS Nr wenn Betäub */	public static final String FLD_NARCOTIC_CAS = "NARCOTIC_CAS";
	/** Ist Impfstoff */		public static final String FLD_VACCINE = "VACCINE";
	
	/** Definition of the database table */
	static final String createDB =
		"CREATE TABLE " + TABLENAME
			+ "("
			+ "ID VARCHAR(25) primary key," // has to be of type varchar else version.exists fails
			+ "lastupdate BIGINT,"
			+ "deleted CHAR(1) default '0'," // will never be set to 1
			+ FLD_ITEM_TYPE + " CHAR(1)," // id(VERSION) DataQuality of last import P
			+ FLD_BLACKBOXED + " CHAR(1),"// id(VERSION) DataQuality of last import N
			+ FLD_CUMMULATED_VERSION + " CHAR(4),"
			+ FLD_GTIN + " VARCHAR(14)," // id(VERSION) contains table version, has to be varchar else vi.isolder() fails
			+ FLD_PHAR 	+" CHAR(7),"
			+ FLD_DSCR	+" VARCHAR(50)," // id(VERSION) filename of last P import
			+ FLD_ADDDSCR	+" VARCHAR(50)," // id(VERSION) filename of last N import
			+ FLD_ATC +" CHAR(10),"
			+ FLD_COMP_GLN + " CHAR(13),"
			+ FLD_COMP_NAME + " VARCHAR(255),"
			+ FLD_PEXF + " CHAR(10)," // id(VERSION) contains cummulated P dataset vers
			+ FLD_PPUB + " CHAR(10)," // id(VERSION) contains cummulated N dataset vers
			+ FLD_PKG_SIZE + " CHAR(6),"
			+ FLD_SL_ENTRY + " CHAR(1),"
			+ FLD_IKSCAT + " CHAR(1),"
			+ FLD_LIMITATION + " CHAR(1),"
			+ FLD_LIMITATION_PTS+ " CHAR(4),"
			+ FLD_LIMITATION_TEXT + " TEXT,"
			+ FLD_GENERIC_TYPE + " CHAR(1),"
			+ FLD_HAS_GENERIC + " CHAR(1),"
			+ FLD_LPPV + " CHAR(1),"
			+ FLD_DEDUCTIBLE + " CHAR(6),"
			+ FLD_NARCOTIC + " CHAR(1),"
			+ FLD_NARCOTIC_CAS + " VARCHAR(20)," // id(VERSION) contains table creation date
			+ FLD_VACCINE + " CHAR(1),"
			+ FLD_LIEFERANT_ID +" VARCHAR(25),"
			+ MAXBESTAND + " VARCHAR(4),"
			+ MINBESTAND + " VARCHAR(4),"
			+ ISTBESTAND + " VARCHAR(4),"
			+ VERKAUFSEINHEIT + " VARCHAR(4),"
			+ ANBRUCH + " VARCHAR(4)"
			+ "); "
			+ "CREATE INDEX idxPHAR ON " + TABLENAME + " ("+FLD_PHAR+"); "
			+ "CREATE INDEX idxITEMTYPE ON " + TABLENAME + " ("+FLD_ITEM_TYPE+"); "
			+ "CREATE INDEX idxGTIN ON "+ TABLENAME + " ("+FLD_GTIN+"); "
			+ "CREATE INDEX idxMONTH ON "+ TABLENAME + " ("+FLD_CUMMULATED_VERSION+"); "
			+ "CREATE INDEX idxBB ON "+ TABLENAME + " ("+FLD_BLACKBOXED+"); "
			+ "INSERT INTO " + TABLENAME + " (ID,"+FLD_GTIN+","+FLD_NARCOTIC_CAS+","+FLD_PEXF+","+FLD_PPUB+") VALUES ('VERSION',"
			+ JdbcLink.wrap(VERSION) +","+JdbcLink.wrap(df.format(new Date()))+",0,0);";
		//@formatter:on
	
	static {
		addMapping(TABLENAME, FLD_ITEM_TYPE, FLD_CUMMULATED_VERSION, FLD_BLACKBOXED, FLD_GTIN,
			FLD_PHAR, FLD_DSCR, FLD_ADDDSCR, FLD_ATC, FLD_COMP_GLN, FLD_COMP_NAME, FLD_PEXF,
			FLD_PPUB, FLD_PKG_SIZE, FLD_SL_ENTRY, FLD_IKSCAT, FLD_LIMITATION, FLD_LIMITATION_PTS,
			FLD_LIMITATION_TEXT, FLD_GENERIC_TYPE, FLD_HAS_GENERIC, FLD_LPPV, FLD_DEDUCTIBLE,
			FLD_NARCOTIC, FLD_NARCOTIC_CAS, FLD_VACCINE, FLD_LIEFERANT_ID, MAXBESTAND, MINBESTAND,
			ISTBESTAND, VERKAUFSEINHEIT, ANBRUCH);
		ArtikelstammItem version = load("VERSION"); //$NON-NLS-1$
		if (!version.exists()) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_GTIN));
			
			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_PHAR, VERSION);
			}
		}
	}
	
	@Override
	public String getLabel(){
		return get(FLD_DSCR) + " (" + get(FLD_ADDDSCR) + ")";
	}
	
	@Override
	protected String getTableName(){
		return TABLENAME;
	}
	
	ArtikelstammItem(){}
	
	public static ArtikelstammItem load(String id){
		return new ArtikelstammItem(id);
	}
	
	protected ArtikelstammItem(String id){
		super(id);
	}
	
	/**
	 * Main import constructor containing the required properties to create the deterministic id
	 * 
	 * @param cummulatedVersion
	 *            dataset version for type
	 * @param type
	 *            the data set type
	 * @param gtin
	 *            global trade index number
	 * @param phar
	 *            pharmacode
	 * @param dscr
	 *            description
	 * @param addscr
	 *            additional description
	 */
	public ArtikelstammItem(int cummulatedVersion, TYPE type, String gtin, BigInteger phar,
		String dscr, String addscr){
		create(ArtikelstammHelper.createUUID(cummulatedVersion, type, gtin, phar));
		set(new String[] {
			FLD_ITEM_TYPE, FLD_GTIN, FLD_PHAR, FLD_DSCR, FLD_ADDDSCR, FLD_CUMMULATED_VERSION,
			FLD_BLACKBOXED
		}, type.name(), gtin, (phar != null) ? phar.toString() : "", dscr, addscr,
			cummulatedVersion + "", StringConstants.ZERO);
	}
	
	// -------------------------------------------------
	/**
	 * Load an article according to its Pharmacode (unique identifier)
	 * 
	 * @param pharNo
	 *            Pharmacode, unique record identifier
	 * @return element if exists, else null
	 */
	public static ArtikelstammItem loadByPHARNo(String pharNo){
		Query<ArtikelstammItem> qbe = new Query<ArtikelstammItem>(ArtikelstammItem.class);
		String res = qbe.findSingle(FLD_PHAR, Query.EQUALS, pharNo);
		if (res == null)
			return null;
		return ArtikelstammItem.load(res);
	}
	
	@Override
	public String getName(){
		return checkNull(get(FLD_DSCR));
	}
	
	// --- ARTIKEL OVERRIDE
	
	@Override
	public int getTotalCount(){
		// TODO
		return 0;
	}
	
	@Override
	public Money getKosten(final TimeTool dat){
		double vkt = checkZeroDouble(getTP(dat, null) + "");
		double vpe = checkZeroDouble((String) get(FLD_PKG_SIZE));
		double vke = checkZeroDouble((String) get(VERKAUFSEINHEIT));
		if (vpe != vke) {
			return new Money((int) Math.round(vke * (vkt / vpe)));
		} else {
			return new Money((int) Math.round(vkt));
		}
	}
	
	@Override
	public void einzelAbgabe(final int n){
		// TODO invalid code
	}
	
	@Override
	public void einzelRuecknahme(final int n){
		int anbruch = checkZero(get(ANBRUCH));
		int ve = checkZero(get(VERKAUFSEINHEIT));
		int vk = getVerpackungsEinheit();
		int num = n * ve;
		if (vk == ve) {
			setIstbestand(getIstbestand() + n);
		} else {
			int rest = anbruch + num;
			while (rest > vk) {
				rest = rest - vk;
				setIstbestand(getIstbestand() + 1);
			}
			set(ANBRUCH, Integer.toString(rest));
		}
	}
	
	@Override
	public int getVerpackungsEinheit(){
		// TODO
		return 0;
	}
	
	@Override
	public Money getVKPreis(){
		try {
			return new Money(checkZero(get(FLD_PPUB)));
		} catch (Throwable ex) {
			Hub.log.log("Fehler beim Einlesen von VK für " + getLabel(), Log.ERRORS);
		}
		return new Money();
	}
	
	// -- VERRECHENBAR ADAPTER ---
	@Override
	public VatInfo getVatInfo(){
		switch (getType()) {
		case P:
			return VatInfo.VAT_CH_ISMEDICAMENT;
		case N:
			return VatInfo.VAT_CH_NOTMEDICAMENT;
		}
		return VatInfo.VAT_NONE;
	}
	
	@Override
	public int getPreis(TimeTool dat, Fall fall){
		try {
			Money m = new Money(get(FLD_PPUB));
			return m.getCents();
		} catch (ParseException e) {
			logger.log(e, "Error parsing price " + get(FLD_PPUB), Log.WARNINGS);
		}
		return 0;
	}
	
	@Override
	public int getTP(TimeTool date, Fall fall){
		return getPreis(date, fall);
	}
	
	@Override
	public double getFactor(TimeTool date, Fall fall){
		// TODO
		return 1;
	}
	
	@Override
	public boolean isDragOK(){
		return true;
	}
	
	@Override
	public String getXidDomain(){
		return ArtikelstammConstants.CODESYSTEM_NAME;
	}
	
	@Override
	public String getCodeSystemName(){
		return ArtikelstammConstants.CODESYSTEM_NAME;
	}
	
	@Override
	public String getCode(){
		return get(FLD_PHAR);
	}
	
	@Override
	public String getPharmaCode(){
		return get(FLD_PHAR);
	};
	
	@Override
	public String getText(){
		return getLabel();
	}
	
	@Override
	public String getEAN(){
		return get(FLD_GTIN);
	}
	
	/**
	 * @param stammType
	 * @return The version of the resp {@link TYPE}, or 99999 if not found
	 */
	public static int getImportSetCumulatedVersion(TYPE stammType){
		ArtikelstammItem version = load("VERSION");
		switch (stammType) {
		case N:
			return version.getInt(FLD_PEXF);
		case P:
			return version.getInt(FLD_PPUB);
		}
		return 99999;
	}
	
	public static void setImportSetCumulatedVersion(TYPE stammType, int importStammVersion){
		ArtikelstammItem version = load("VERSION");
		switch (stammType) {
		case N:
			version.setInt(FLD_PEXF, importStammVersion);
			return;
		case P:
			version.setInt(FLD_PPUB, importStammVersion);
			return;
		}
	}
	
	public static void setImportSetDataQuality(TYPE p, int dq){
		ArtikelstammItem version = load("VERSION");
		switch (p) {
		case N:
			version.setInt(FLD_BLACKBOXED, dq);
			return;
		case P:
			version.setInt(FLD_ITEM_TYPE, dq);
			return;
		}
	}
	
	public static int getImportSetDataQuality(TYPE p){
		ArtikelstammItem version = load("VERSION");
		switch (p) {
		case N:
			return version.getInt(FLD_BLACKBOXED);
		case P:
			return version.getInt(FLD_ITEM_TYPE);
		default:
			return 0;
		}
	}
	
	/**
	 * Stores the filename of the last import source file
	 * 
	 * @param stammType
	 * @param importFilename
	 */
	public static void setImportSetLastImportFileName(TYPE stammType, String importFilename){
		ArtikelstammItem version = load("VERSION");
		switch (stammType) {
		case N:
			version.set(FLD_ADDDSCR, importFilename);
			return;
		case P:
			version.set(FLD_DSCR, importFilename);
			return;
		}
	}
	
	/**
	 * Retrieves the filename of the last import source file
	 * 
	 * @param stammType
	 */
	public static String getImportSetLastImportFileName(TYPE stammType){
		ArtikelstammItem version = load("VERSION");
		switch (stammType) {
		case N:
			return version.get(FLD_ADDDSCR);
		case P:
			return version.get(FLD_DSCR);
		default:
			return "";
		}
	}
	
	public static boolean purgeEntries(List<ArtikelstammItem> qre){
		JdbcLink link = getConnection();
		link.setAutoCommit(false);
		for (ArtikelstammItem artikelstammItem : qre) {
			link.exec("DELETE FROM " + TABLENAME + " WHERE ID=" + artikelstammItem.getWrappedId());
		}
		try {
			link.commit();
		} catch (Throwable ex) {
			link.rollback();
			logger.log(ex, "Error on remove commit!", Log.WARNINGS);
			link.setAutoCommit(true);
			return false;
		}
		link.setAutoCommit(true);
		return true;
	}
	
	/**
	 * The article is marked as black-boxed if the value within {@link #FLD_BLACKBOXED} does not
	 * equal 0
	 * 
	 * @return
	 */
	public boolean isBlackBoxed(){
		String val = get(FLD_BLACKBOXED);
		if (!val.equals(StringConstants.ZERO))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return {@link BlackBoxReason} or <code>null</code> if invalid value
	 */
	public BlackBoxReason getBlackBoxReason(){
		int value = Integer.parseInt(get(FLD_BLACKBOXED));
		return BlackBoxReason.getByInteger(value);
	}
	
	// requirements for IArtikelstammItem
	@Override
	public String getDSCR(){
		return get(FLD_DSCR);
	}
	
	@Override
	public String getGTIN(){
		return get(FLD_GTIN);
	}
	
	@Override
	public String getPHAR(){
		return get(FLD_PHAR);
	}
	
	@Override
	public String getATCCode(){
		return get(FLD_ATC);
	}
	
	@Override
	public TYPE getType(){
		return ArtikelstammConstants.TYPE.valueOf(get(FLD_ITEM_TYPE));
	}
	
	@Override
	public String getManufacturerLabel(){
		StringBuilder sb = new StringBuilder();
		if (get(FLD_COMP_NAME) != null && get(FLD_COMP_NAME).length() > 1)
			sb.append(get(FLD_COMP_NAME));
		if (get(FLD_COMP_GLN) != null && get(FLD_COMP_GLN).length() > 1)
			sb.append(" (GLN " + get(FLD_COMP_GLN) + ")");
		return sb.toString();
	}
	
	@Override
	public Double getExFactoryPrice(){
		if (get(FLD_PEXF) == null)
			return null;
		try {
			return Double.parseDouble(get(FLD_PEXF));
		} catch (NumberFormatException ex) {
			return null;
		}
		
	}
	
	@Override
	public Double getPublicPrice(){
		try {
			return Double.parseDouble(get(FLD_PPUB));
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	@Override
	public boolean isInSLList(){
		return (get(FLD_SL_ENTRY) != null && get(FLD_SL_ENTRY).equals(StringConstants.ONE)) ? true
				: false;
	}
	
	@Override
	public String getSwissmedicCategory(){
		return get(FLD_IKSCAT);
	}
	
	@Override
	public String getGenericType(){
		return get(FLD_GENERIC_TYPE);
	}
	
	@Override
	public Integer getDeductible(){
		// Medikament wenn SL nicht 20 % dann 10 %
		String val = get(FLD_DEDUCTIBLE).trim();
		if (val == null || val.length() < 1)
			return 0;
		try {
			return new Integer(val);
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}
	
	@Override
	public boolean isNarcotic(){
		return (get(FLD_NARCOTIC).equals(StringConstants.ONE)) ? true : false;
	}
	
	@Override
	public boolean isLimited(){
		return (get(FLD_LIMITATION).equals(StringConstants.ONE)) ? true : false;
	}
	
	@Override
	public String getLimitationPoints(){
		return get(FLD_LIMITATION_PTS);
	}
	
	@Override
	public String getLimitationText(){
		return get(FLD_LIMITATION_TEXT);
	}
	
	@Override
	public boolean isInLPPV(){
		return (get(FLD_LPPV).equals(StringConstants.ONE)) ? true : false;
	}
	
}
