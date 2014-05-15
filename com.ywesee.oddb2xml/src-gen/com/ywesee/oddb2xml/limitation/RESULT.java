//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.05.15 um 01:07:52 PM CEST 
//


package com.ywesee.oddb2xml.limitation;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}OK_ERROR"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}NBR_RECORD"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}ERROR_CODE"/>
 *         &lt;element ref="{http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration}MESSAGE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "okerror",
    "nbrrecord",
    "errorcode",
    "message"
})
@XmlRootElement(name = "RESULT")
public class RESULT {

    @XmlElement(name = "OK_ERROR", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String okerror;
    @XmlElement(name = "NBR_RECORD", required = true)
    protected BigInteger nbrrecord;
    @XmlElement(name = "ERROR_CODE", required = true)
    protected ERRORCODE errorcode;
    @XmlElement(name = "MESSAGE", required = true)
    protected MESSAGE message;

    /**
     * Ruft den Wert der okerror-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOKERROR() {
        return okerror;
    }

    /**
     * Legt den Wert der okerror-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOKERROR(String value) {
        this.okerror = value;
    }

    /**
     * Ruft den Wert der nbrrecord-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNBRRECORD() {
        return nbrrecord;
    }

    /**
     * Legt den Wert der nbrrecord-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNBRRECORD(BigInteger value) {
        this.nbrrecord = value;
    }

    /**
     * Ruft den Wert der errorcode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ERRORCODE }
     *     
     */
    public ERRORCODE getERRORCODE() {
        return errorcode;
    }

    /**
     * Legt den Wert der errorcode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ERRORCODE }
     *     
     */
    public void setERRORCODE(ERRORCODE value) {
        this.errorcode = value;
    }

    /**
     * Ruft den Wert der message-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MESSAGE }
     *     
     */
    public MESSAGE getMESSAGE() {
        return message;
    }

    /**
     * Legt den Wert der message-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MESSAGE }
     *     
     */
    public void setMESSAGE(MESSAGE value) {
        this.message = value;
    }

}
