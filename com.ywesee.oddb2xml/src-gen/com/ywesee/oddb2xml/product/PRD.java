//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.03.17 um 12:27:08 PM CET 
//


package com.ywesee.oddb2xml.product;

import java.math.BigInteger;
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
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}GTIN"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}PRODNO" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRD"/>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRF"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ADINFD" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ADINFF" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}GENCD" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ATC" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}IT" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ORPH" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}CPT"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}PackGrSwissmedic" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}EinheitSwissmedic" minOccurs="0"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SubstanceSwissmedic" minOccurs="0"/>
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
    "gtin",
    "prodno",
    "dscrd",
    "dscrf",
    "adinfd",
    "adinff",
    "gencd",
    "atc",
    "it",
    "orph",
    "cpt",
    "packGrSwissmedic",
    "einheitSwissmedic",
    "substanceSwissmedic"
})
@XmlRootElement(name = "PRD")
public class PRD {

    @XmlElement(name = "GTIN", required = true)
    protected BigInteger gtin;
    @XmlElement(name = "PRODNO")
    protected String prodno;
    @XmlElement(name = "DSCRD")
    protected String dscrd;
    @XmlElement(name = "DSCRF")
    protected String dscrf;
    @XmlElement(name = "ADINFD")
    protected String adinfd;
    @XmlElement(name = "ADINFF")
    protected String adinff;
    @XmlElement(name = "GENCD")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String gencd;
    @XmlElement(name = "ATC")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String atc;
    @XmlElement(name = "IT")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String it;
    @XmlElement(name = "ORPH")
    protected Boolean orph;
    @XmlElement(name = "CPT", required = true)
    protected CPT cpt;
    @XmlElement(name = "PackGrSwissmedic")
    protected String packGrSwissmedic;
    @XmlElement(name = "EinheitSwissmedic")
    protected String einheitSwissmedic;
    @XmlElement(name = "SubstanceSwissmedic")
    protected String substanceSwissmedic;
    @XmlAttribute(name = "DT", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String dt;

    /**
     * Ruft den Wert der gtin-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGTIN() {
        return gtin;
    }

    /**
     * Legt den Wert der gtin-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGTIN(BigInteger value) {
        this.gtin = value;
    }

    /**
     * Ruft den Wert der prodno-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODNO() {
        return prodno;
    }

    /**
     * Legt den Wert der prodno-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODNO(String value) {
        this.prodno = value;
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
     * Ruft den Wert der adinfd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADINFD() {
        return adinfd;
    }

    /**
     * Legt den Wert der adinfd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADINFD(String value) {
        this.adinfd = value;
    }

    /**
     * Ruft den Wert der adinff-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADINFF() {
        return adinff;
    }

    /**
     * Legt den Wert der adinff-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADINFF(String value) {
        this.adinff = value;
    }

    /**
     * Ruft den Wert der gencd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGENCD() {
        return gencd;
    }

    /**
     * Legt den Wert der gencd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGENCD(String value) {
        this.gencd = value;
    }

    /**
     * Ruft den Wert der atc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getATC() {
        return atc;
    }

    /**
     * Legt den Wert der atc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setATC(String value) {
        this.atc = value;
    }

    /**
     * Ruft den Wert der it-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIT() {
        return it;
    }

    /**
     * Legt den Wert der it-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIT(String value) {
        this.it = value;
    }

    /**
     * Ruft den Wert der orph-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isORPH() {
        return orph;
    }

    /**
     * Legt den Wert der orph-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setORPH(Boolean value) {
        this.orph = value;
    }

    /**
     * Ruft den Wert der cpt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CPT }
     *     
     */
    public CPT getCPT() {
        return cpt;
    }

    /**
     * Legt den Wert der cpt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CPT }
     *     
     */
    public void setCPT(CPT value) {
        this.cpt = value;
    }

    /**
     * Ruft den Wert der packGrSwissmedic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackGrSwissmedic() {
        return packGrSwissmedic;
    }

    /**
     * Legt den Wert der packGrSwissmedic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackGrSwissmedic(String value) {
        this.packGrSwissmedic = value;
    }

    /**
     * Ruft den Wert der einheitSwissmedic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEinheitSwissmedic() {
        return einheitSwissmedic;
    }

    /**
     * Legt den Wert der einheitSwissmedic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEinheitSwissmedic(String value) {
        this.einheitSwissmedic = value;
    }

    /**
     * Ruft den Wert der substanceSwissmedic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstanceSwissmedic() {
        return substanceSwissmedic;
    }

    /**
     * Legt den Wert der substanceSwissmedic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstanceSwissmedic(String value) {
        this.substanceSwissmedic = value;
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
