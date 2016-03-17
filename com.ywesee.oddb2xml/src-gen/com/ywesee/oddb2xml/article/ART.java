//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.03.17 um 12:27:07 PM CET 
//


package com.ywesee.oddb2xml.article;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}PHAR"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SMCAT" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SMNO" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SALECD"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}COOL"/>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}LIMPTS"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}CDBG"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}BG"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}QTY" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRD"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRF"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SORTD"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SORTF"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SYN1D"/>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SYN1F"/>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SLOPLUS"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ARTCOMP"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ARTBAR"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ARTPRI" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ARTINS" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="DT" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "phar",
    "smcat",
    "smno",
    "salecd",
    "cool",
    "limpts",
    "cdbg",
    "bg",
    "qty",
    "dscrd",
    "dscrf",
    "sortd",
    "sortf",
    "syn1D",
    "syn1F",
    "sloplus",
    "artcomp",
    "artbar",
    "artpri",
    "artins"
})
@XmlRootElement(name = "ART")
public class ART {

    @XmlElement(name = "PHAR", required = true)
    protected String phar;
    @XmlElement(name = "SMCAT")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String smcat;
    @XmlElement(name = "SMNO")
    protected BigInteger smno;
    @XmlElement(name = "SALECD", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String salecd;
    @XmlElement(name = "COOL")
    protected BigInteger cool;
    @XmlElement(name = "LIMPTS")
    protected BigInteger limpts;
    @XmlElement(name = "CDBG", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String cdbg;
    @XmlElement(name = "BG", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String bg;
    @XmlElement(name = "QTY")
    protected String qty;
    @XmlElement(name = "DSCRD", required = true)
    protected String dscrd;
    @XmlElement(name = "DSCRF", required = true)
    protected String dscrf;
    @XmlElement(name = "SORTD", required = true)
    protected String sortd;
    @XmlElement(name = "SORTF", required = true)
    protected String sortf;
    @XmlElement(name = "SYN1D")
    protected String syn1D;
    @XmlElement(name = "SYN1F")
    protected String syn1F;
    @XmlElement(name = "SLOPLUS")
    protected BigInteger sloplus;
    @XmlElement(name = "ARTCOMP", required = true)
    protected ARTCOMP artcomp;
    @XmlElement(name = "ARTBAR", required = true)
    protected ARTBAR artbar;
    @XmlElement(name = "ARTPRI")
    protected List<ARTPRI> artpri;
    @XmlElement(name = "ARTINS")
    protected ARTINS artins;
    @XmlAttribute(name = "DT", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String dt;

    /**
     * Ruft den Wert der phar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHAR() {
        return phar;
    }

    /**
     * Legt den Wert der phar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHAR(String value) {
        this.phar = value;
    }

    /**
     * Ruft den Wert der smcat-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMCAT() {
        return smcat;
    }

    /**
     * Legt den Wert der smcat-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMCAT(String value) {
        this.smcat = value;
    }

    /**
     * Ruft den Wert der smno-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSMNO() {
        return smno;
    }

    /**
     * Legt den Wert der smno-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSMNO(BigInteger value) {
        this.smno = value;
    }

    /**
     * Ruft den Wert der salecd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSALECD() {
        return salecd;
    }

    /**
     * Legt den Wert der salecd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSALECD(String value) {
        this.salecd = value;
    }

    /**
     * Ruft den Wert der cool-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCOOL() {
        return cool;
    }

    /**
     * Legt den Wert der cool-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCOOL(BigInteger value) {
        this.cool = value;
    }

    /**
     * Ruft den Wert der limpts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLIMPTS() {
        return limpts;
    }

    /**
     * Legt den Wert der limpts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLIMPTS(BigInteger value) {
        this.limpts = value;
    }

    /**
     * Ruft den Wert der cdbg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCDBG() {
        return cdbg;
    }

    /**
     * Legt den Wert der cdbg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCDBG(String value) {
        this.cdbg = value;
    }

    /**
     * Ruft den Wert der bg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBG() {
        return bg;
    }

    /**
     * Legt den Wert der bg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBG(String value) {
        this.bg = value;
    }

    /**
     * Ruft den Wert der qty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTY() {
        return qty;
    }

    /**
     * Legt den Wert der qty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTY(String value) {
        this.qty = value;
    }

    /**
     * Ruft den Wert der dscrd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSCRD() {
        return dscrd;
    }

    /**
     * Legt den Wert der dscrd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSCRD(String value) {
        this.dscrd = value;
    }

    /**
     * Ruft den Wert der dscrf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSCRF() {
        return dscrf;
    }

    /**
     * Legt den Wert der dscrf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSCRF(String value) {
        this.dscrf = value;
    }

    /**
     * Ruft den Wert der sortd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSORTD() {
        return sortd;
    }

    /**
     * Legt den Wert der sortd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSORTD(String value) {
        this.sortd = value;
    }

    /**
     * Ruft den Wert der sortf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSORTF() {
        return sortf;
    }

    /**
     * Legt den Wert der sortf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSORTF(String value) {
        this.sortf = value;
    }

    /**
     * Ruft den Wert der syn1D-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSYN1D() {
        return syn1D;
    }

    /**
     * Legt den Wert der syn1D-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSYN1D(String value) {
        this.syn1D = value;
    }

    /**
     * Ruft den Wert der syn1F-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSYN1F() {
        return syn1F;
    }

    /**
     * Legt den Wert der syn1F-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSYN1F(String value) {
        this.syn1F = value;
    }

    /**
     * Ruft den Wert der sloplus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSLOPLUS() {
        return sloplus;
    }

    /**
     * Legt den Wert der sloplus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSLOPLUS(BigInteger value) {
        this.sloplus = value;
    }

    /**
     * Ruft den Wert der artcomp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ARTCOMP }
     *     
     */
    public ARTCOMP getARTCOMP() {
        return artcomp;
    }

    /**
     * Legt den Wert der artcomp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ARTCOMP }
     *     
     */
    public void setARTCOMP(ARTCOMP value) {
        this.artcomp = value;
    }

    /**
     * Ruft den Wert der artbar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ARTBAR }
     *     
     */
    public ARTBAR getARTBAR() {
        return artbar;
    }

    /**
     * Legt den Wert der artbar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ARTBAR }
     *     
     */
    public void setARTBAR(ARTBAR value) {
        this.artbar = value;
    }

    /**
     * Gets the value of the artpri property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the artpri property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getARTPRI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ARTPRI }
     * 
     * 
     */
    public List<ARTPRI> getARTPRI() {
        if (artpri == null) {
            artpri = new ArrayList<ARTPRI>();
        }
        return this.artpri;
    }

    /**
     * Ruft den Wert der artins-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ARTINS }
     *     
     */
    public ARTINS getARTINS() {
        return artins;
    }

    /**
     * Legt den Wert der artins-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ARTINS }
     *     
     */
    public void setARTINS(ARTINS value) {
        this.artins = value;
    }

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDT() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDT(String value) {
        this.dt = value;
    }

}
