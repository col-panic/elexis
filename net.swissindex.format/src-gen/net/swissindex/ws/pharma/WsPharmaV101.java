package net.swissindex.ws.pharma;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.4-b01 Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ws_Pharma_V101", targetNamespace = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101", wsdlLocation = "file:/Users/marco/Development/workspace-elexis-productive/net.swissindex.format/rsc/wsdl/ws_Pharma_V101.wsdl")
public class WsPharmaV101 extends Service {
	
	private final static URL WSPHARMAV101_WSDL_LOCATION;
	private final static WebServiceException WSPHARMAV101_EXCEPTION;
	private final static QName WSPHARMAV101_QNAME = new QName(
		"http://swissindex.e-mediat.net/SwissindexPharma_out_V101", "ws_Pharma_V101");
	
	static {
		URL url = null;
		WebServiceException e = null;
		url = WsPharmaV101.class.getResource("ws_Pharma_V101.wsdl");
		WSPHARMAV101_WSDL_LOCATION = url;
		WSPHARMAV101_EXCEPTION = e;
	}
	
	public WsPharmaV101(){
		super(__getWsdlLocation(), WSPHARMAV101_QNAME);
	}
	
	public WsPharmaV101(WebServiceFeature... features){
		super(__getWsdlLocation(), WSPHARMAV101_QNAME, features);
	}
	
	public WsPharmaV101(URL wsdlLocation){
		super(wsdlLocation, WSPHARMAV101_QNAME);
	}
	
	public WsPharmaV101(URL wsdlLocation, WebServiceFeature... features){
		super(wsdlLocation, WSPHARMAV101_QNAME, features);
	}
	
	public WsPharmaV101(URL wsdlLocation, QName serviceName){
		super(wsdlLocation, serviceName);
	}
	
	public WsPharmaV101(URL wsdlLocation, QName serviceName, WebServiceFeature... features){
		super(wsdlLocation, serviceName, features);
	}
	
	/**
	 * 
	 * @return returns WsPharmaV101Soap
	 */
	@WebEndpoint(name = "ws_Pharma_V101Soap")
	public WsPharmaV101Soap getWsPharmaV101Soap(){
		return super.getPort(new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101",
			"ws_Pharma_V101Soap"), WsPharmaV101Soap.class);
	}
	
	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
	 *            Supported features not in the <code>features</code> parameter will have their
	 *            default values.
	 * @return returns WsPharmaV101Soap
	 */
	@WebEndpoint(name = "ws_Pharma_V101Soap")
	public WsPharmaV101Soap getWsPharmaV101Soap(WebServiceFeature... features){
		return super.getPort(new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101",
			"ws_Pharma_V101Soap"), WsPharmaV101Soap.class, features);
	}
	
	/**
	 * 
	 * @return returns WsPharmaV101Soap
	 */
	@WebEndpoint(name = "ws_Pharma_V101Soap12")
	public WsPharmaV101Soap getWsPharmaV101Soap12(){
		return super.getPort(new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101",
			"ws_Pharma_V101Soap12"), WsPharmaV101Soap.class);
	}
	
	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
	 *            Supported features not in the <code>features</code> parameter will have their
	 *            default values.
	 * @return returns WsPharmaV101Soap
	 */
	@WebEndpoint(name = "ws_Pharma_V101Soap12")
	public WsPharmaV101Soap getWsPharmaV101Soap12(WebServiceFeature... features){
		return super.getPort(new QName("http://swissindex.e-mediat.net/SwissindexPharma_out_V101",
			"ws_Pharma_V101Soap12"), WsPharmaV101Soap.class, features);
	}
	
	private static URL __getWsdlLocation(){
		if (WSPHARMAV101_EXCEPTION != null) {
			throw WSPHARMAV101_EXCEPTION;
		}
		return WSPHARMAV101_WSDL_LOCATION;
	}
	
}