//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.03.17 um 12:27:08 PM CET 
//


package com.ywesee.oddb2xml.product;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ywesee.oddb2xml.product package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OKERROR_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "OK_ERROR");
    private final static QName _QTYU_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "QTYU");
    private final static QName _GENCD_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "GENCD");
    private final static QName _SUBNO_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "SUBNO");
    private final static QName _ORPH_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "ORPH");
    private final static QName _NBRRECORD_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "NBR_RECORD");
    private final static QName _GTIN_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "GTIN");
    private final static QName _EinheitSwissmedic_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "EinheitSwissmedic");
    private final static QName _ATC_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "ATC");
    private final static QName _LINE_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "LINE");
    private final static QName _QTY_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "QTY");
    private final static QName _PRODNO_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "PRODNO");
    private final static QName _PackGrSwissmedic_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "PackGrSwissmedic");
    private final static QName _IT_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "IT");
    private final static QName _SubstanceSwissmedic_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "SubstanceSwissmedic");
    private final static QName _ADINFD_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "ADINFD");
    private final static QName _DSCRF_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "DSCRF");
    private final static QName _ADINFF_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "ADINFF");
    private final static QName _DSCRD_QNAME = new QName("http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", "DSCRD");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ywesee.oddb2xml.product
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PRD }
     * 
     */
    public PRD createPRD() {
        return new PRD();
    }

    /**
     * Create an instance of {@link CPT }
     * 
     */
    public CPT createCPT() {
        return new CPT();
    }

    /**
     * Create an instance of {@link CPTCMP }
     * 
     */
    public CPTCMP createCPTCMP() {
        return new CPTCMP();
    }

    /**
     * Create an instance of {@link ERRORCODE }
     * 
     */
    public ERRORCODE createERRORCODE() {
        return new ERRORCODE();
    }

    /**
     * Create an instance of {@link MESSAGE }
     * 
     */
    public MESSAGE createMESSAGE() {
        return new MESSAGE();
    }

    /**
     * Create an instance of {@link PRODUCT }
     * 
     */
    public PRODUCT createPRODUCT() {
        return new PRODUCT();
    }

    /**
     * Create an instance of {@link RESULT }
     * 
     */
    public RESULT createRESULT() {
        return new RESULT();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "OK_ERROR")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createOKERROR(String value) {
        return new JAXBElement<String>(_OKERROR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "QTYU")
    public JAXBElement<String> createQTYU(String value) {
        return new JAXBElement<String>(_QTYU_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "GENCD")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGENCD(String value) {
        return new JAXBElement<String>(_GENCD_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "SUBNO")
    public JAXBElement<BigInteger> createSUBNO(BigInteger value) {
        return new JAXBElement<BigInteger>(_SUBNO_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "ORPH")
    public JAXBElement<Boolean> createORPH(Boolean value) {
        return new JAXBElement<Boolean>(_ORPH_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "NBR_RECORD")
    public JAXBElement<BigInteger> createNBRRECORD(BigInteger value) {
        return new JAXBElement<BigInteger>(_NBRRECORD_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "GTIN")
    public JAXBElement<BigInteger> createGTIN(BigInteger value) {
        return new JAXBElement<BigInteger>(_GTIN_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "EinheitSwissmedic")
    public JAXBElement<String> createEinheitSwissmedic(String value) {
        return new JAXBElement<String>(_EinheitSwissmedic_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "ATC")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createATC(String value) {
        return new JAXBElement<String>(_ATC_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "LINE")
    public JAXBElement<BigInteger> createLINE(BigInteger value) {
        return new JAXBElement<BigInteger>(_LINE_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "QTY")
    public JAXBElement<String> createQTY(String value) {
        return new JAXBElement<String>(_QTY_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "PRODNO")
    public JAXBElement<String> createPRODNO(String value) {
        return new JAXBElement<String>(_PRODNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "PackGrSwissmedic")
    public JAXBElement<String> createPackGrSwissmedic(String value) {
        return new JAXBElement<String>(_PackGrSwissmedic_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "IT")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIT(String value) {
        return new JAXBElement<String>(_IT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "SubstanceSwissmedic")
    public JAXBElement<String> createSubstanceSwissmedic(String value) {
        return new JAXBElement<String>(_SubstanceSwissmedic_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "ADINFD")
    public JAXBElement<String> createADINFD(String value) {
        return new JAXBElement<String>(_ADINFD_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "DSCRF")
    public JAXBElement<String> createDSCRF(String value) {
        return new JAXBElement<String>(_DSCRF_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "ADINFF")
    public JAXBElement<String> createADINFF(String value) {
        return new JAXBElement<String>(_ADINFF_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wiki.oddb.org/wiki.php?pagename=Swissmedic.Datendeklaration", name = "DSCRD")
    public JAXBElement<String> createDSCRD(String value) {
        return new JAXBElement<String>(_DSCRD_QNAME, String.class, null, value);
    }

}
