//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.03.17 um 12:27:07 PM CET 
//


package com.ywesee.oddb2xml.limitation;

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
 *         &lt;choice>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SwissmedicNo5"/>
 *           &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}SwissmedicNo8"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}IT"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}LIMTYP"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}LIMVAL"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}LIMNAMEBAG"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}LIMNIV"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRD"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}DSCRF"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}VDAT"/>
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
    "swissmedicNo5",
    "swissmedicNo8",
    "it",
    "limtyp",
    "limval",
    "limnamebag",
    "limniv",
    "dscrd",
    "dscrf",
    "vdat"
})
@XmlRootElement(name = "LIM")
public class LIM {

    @XmlElement(name = "SwissmedicNo5")
    protected BigInteger swissmedicNo5;
    @XmlElement(name = "SwissmedicNo8")
    protected BigInteger swissmedicNo8;
    @XmlElement(name = "IT", required = true)
    protected String it;
    @XmlElement(name = "LIMTYP", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String limtyp;
    @XmlElement(name = "LIMVAL", required = true)
    protected String limval;
    @XmlElement(name = "LIMNAMEBAG", required = true)
    protected String limnamebag;
    @XmlElement(name = "LIMNIV", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String limniv;
    @XmlElement(name = "DSCRD", required = true)
    protected String dscrd;
    @XmlElement(name = "DSCRF", required = true)
    protected String dscrf;
    @XmlElement(name = "VDAT", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String vdat;
    @XmlAttribute(name = "DT", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String dt;

    /**
     * Ruft den Wert der swissmedicNo5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSwissmedicNo5() {
        return swissmedicNo5;
    }

    /**
     * Legt den Wert der swissmedicNo5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSwissmedicNo5(BigInteger value) {
        this.swissmedicNo5 = value;
    }

    /**
     * Ruft den Wert der swissmedicNo8-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSwissmedicNo8() {
        return swissmedicNo8;
    }

    /**
     * Legt den Wert der swissmedicNo8-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSwissmedicNo8(BigInteger value) {
        this.swissmedicNo8 = value;
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
     * Ruft den Wert der limtyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMTYP() {
        return limtyp;
    }

    /**
     * Legt den Wert der limtyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMTYP(String value) {
        this.limtyp = value;
    }

    /**
     * Ruft den Wert der limval-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMVAL() {
        return limval;
    }

    /**
     * Legt den Wert der limval-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMVAL(String value) {
        this.limval = value;
    }

    /**
     * Ruft den Wert der limnamebag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMNAMEBAG() {
        return limnamebag;
    }

    /**
     * Legt den Wert der limnamebag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMNAMEBAG(String value) {
        this.limnamebag = value;
    }

    /**
     * Ruft den Wert der limniv-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIMNIV() {
        return limniv;
    }

    /**
     * Legt den Wert der limniv-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIMNIV(String value) {
        this.limniv = value;
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
     * Ruft den Wert der vdat-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVDAT() {
        return vdat;
    }

    /**
     * Legt den Wert der vdat-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVDAT(String value) {
        this.vdat = value;
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
