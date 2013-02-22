
package net.swissindex.ws.pharma;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.swissindex.ws.pharma package. 
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

    private final static QName _Pharmacode_QNAME = new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "pharmacode");
    private final static QName _GTIN_QNAME = new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "GTIN");
    private final static QName _MutationDate_QNAME = new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "mutationDate");
    private final static QName _Lang_QNAME = new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "lang");
    private final static QName _Descr_QNAME = new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "descr");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.swissindex.ws.pharma
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PHARMA }
     * 
     */
    public PHARMA createPHARMA() {
        return new PHARMA();
    }

    /**
     * Create an instance of {@link PHARMA.ITEM }
     * 
     */
    public PHARMA.ITEM createPHARMAITEM() {
        return new PHARMA.ITEM();
    }

    /**
     * Create an instance of {@link PHARMA.RESULT }
     * 
     */
    public PHARMA.RESULT createPHARMARESULT() {
        return new PHARMA.RESULT();
    }

    /**
     * Create an instance of {@link PHARMA.ITEM.COMP }
     * 
     */
    public PHARMA.ITEM.COMP createPHARMAITEMCOMP() {
        return new PHARMA.ITEM.COMP();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", name = "pharmacode")
    public JAXBElement<String> createPharmacode(String value) {
        return new JAXBElement<String>(_Pharmacode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", name = "GTIN")
    public JAXBElement<String> createGTIN(String value) {
        return new JAXBElement<String>(_GTIN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", name = "mutationDate")
    public JAXBElement<XMLGregorianCalendar> createMutationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_MutationDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", name = "lang")
    public JAXBElement<String> createLang(String value) {
        return new JAXBElement<String>(_Lang_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", name = "descr")
    public JAXBElement<String> createDescr(String value) {
        return new JAXBElement<String>(_Descr_QNAME, String.class, null, value);
    }

}
