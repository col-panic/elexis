//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.27 at 09:14:10 AM MESZ 
//


package com.ywesee.oddb2xml.article;

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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the okerror property.
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
     * Sets the value of the okerror property.
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
     * Gets the value of the nbrrecord property.
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
     * Sets the value of the nbrrecord property.
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
     * Gets the value of the errorcode property.
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
     * Sets the value of the errorcode property.
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
     * Gets the value of the message property.
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
     * Sets the value of the message property.
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
