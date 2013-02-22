<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://swissindex.e-mediat.net/SwissindexPharma_out_V101" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" targetNamespace="http://swissindex.e-mediat.net/SwissindexPharma_out_V101" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://swissindex.e-mediat.net/SwissindexPharma_out_V101">
      <s:element name="lang" type="s:string" />
      <s:element name="PHARMA">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="unbounded" name="ITEM">
              <s:complexType>
                <s:sequence>
                  <s:element minOccurs="0" maxOccurs="1" name="GTIN" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="PHAR" type="s:integer" />
                  <s:element minOccurs="1" maxOccurs="1" name="STATUS">
                    <s:simpleType>
                      <s:restriction base="s:string">
                        <s:enumeration value="A" />
                        <s:enumeration value="I" />
                      </s:restriction>
                    </s:simpleType>
                  </s:element>
                  <s:element minOccurs="1" maxOccurs="1" name="STDATE" type="s:dateTime" />
                  <s:element minOccurs="0" maxOccurs="1" name="LANG" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="DSCR" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="ADDSCR" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="ATC" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="COMP">
                    <s:complexType>
                      <s:sequence>
                        <s:element minOccurs="0" maxOccurs="1" name="NAME" type="s:string" />
                        <s:element minOccurs="0" maxOccurs="1" name="GLN" type="s:string" />
                      </s:sequence>
                    </s:complexType>
                  </s:element>
                </s:sequence>
                <s:attribute name="DT" type="s:dateTime" use="required" />
              </s:complexType>
            </s:element>
            <s:element minOccurs="0" maxOccurs="1" name="RESULT">
              <s:complexType>
                <s:sequence>
                  <s:element minOccurs="1" maxOccurs="1" name="OK_ERROR">
                    <s:simpleType>
                      <s:restriction base="s:string">
                        <s:enumeration value="OK" />
                        <s:enumeration value="ERROR" />
                      </s:restriction>
                    </s:simpleType>
                  </s:element>
                  <s:element minOccurs="0" maxOccurs="1" name="NBR_RECORD" type="s:int" />
                  <s:element minOccurs="0" maxOccurs="1" name="ERROR_CODE" type="s:string" />
                  <s:element minOccurs="0" maxOccurs="1" name="MESSAGE" type="s:string" />
                </s:sequence>
              </s:complexType>
            </s:element>
          </s:sequence>
          <s:attribute name="CREATION_DATETIME" type="s:dateTime" use="required" />
        </s:complexType>
      </s:element>
      <s:element name="mutationDate" type="s:dateTime" />
      <s:element name="GTIN" type="s:string" />
      <s:element name="pharmacode" type="s:string" />
      <s:element name="descr" type="s:string" />
    </s:schema>
  </wsdl:types>
  <wsdl:message name="DownloadAllSoapIn">
    <wsdl:part name="lang" element="tns:lang" />
  </wsdl:message>
  <wsdl:message name="DownloadAllSoapOut">
    <wsdl:part name="DownloadAllResult" element="tns:PHARMA" />
  </wsdl:message>
  <wsdl:message name="DownloadByDateSoapIn">
    <wsdl:part name="mutationDate" element="tns:mutationDate" />
    <wsdl:part name="lang" element="tns:lang" />
  </wsdl:message>
  <wsdl:message name="DownloadByDateSoapOut">
    <wsdl:part name="DownloadByDateResult" element="tns:PHARMA" />
  </wsdl:message>
  <wsdl:message name="GetByGTINSoapIn">
    <wsdl:part name="GTIN" element="tns:GTIN" />
    <wsdl:part name="lang" element="tns:lang" />
  </wsdl:message>
  <wsdl:message name="GetByGTINSoapOut">
    <wsdl:part name="GetByGTINResult" element="tns:PHARMA" />
  </wsdl:message>
  <wsdl:message name="GetByPharmacodeSoapIn">
    <wsdl:part name="pharmacode" element="tns:pharmacode" />
    <wsdl:part name="lang" element="tns:lang" />
  </wsdl:message>
  <wsdl:message name="GetByPharmacodeSoapOut">
    <wsdl:part name="GetByPharmacodeResult" element="tns:PHARMA" />
  </wsdl:message>
  <wsdl:message name="GetByDescrSoapIn">
    <wsdl:part name="descr" element="tns:descr" />
    <wsdl:part name="lang" element="tns:lang" />
  </wsdl:message>
  <wsdl:message name="GetByDescrSoapOut">
    <wsdl:part name="GetByDescrResult" element="tns:PHARMA" />
  </wsdl:message>
  <wsdl:portType name="ws_Pharma_V101Soap">
    <wsdl:operation name="DownloadAll">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;p&gt;&lt;/p&gt;&lt;p&gt;&lt;b&gt;Schema Output:&lt;/b&gt; Click &lt;a href='../schema/SwissindexPharma_out_V101.xsd'&gt;here&lt;/a&gt;</wsdl:documentation>
      <wsdl:input message="tns:DownloadAllSoapIn" />
      <wsdl:output message="tns:DownloadAllSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="DownloadByDate">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;p&gt;&lt;/p&gt;&lt;p&gt;&lt;b&gt;Schema Output:&lt;/b&gt; Click &lt;a href='../schema/SwissindexPharma_out_V101.xsd'&gt;here&lt;/a&gt;</wsdl:documentation>
      <wsdl:input message="tns:DownloadByDateSoapIn" />
      <wsdl:output message="tns:DownloadByDateSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetByGTIN">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;p&gt;&lt;/p&gt;&lt;p&gt;&lt;b&gt;Schema Output:&lt;/b&gt; Click &lt;a href='../schema/SwissindexPharma_out_V101.xsd'&gt;here&lt;/a&gt;</wsdl:documentation>
      <wsdl:input message="tns:GetByGTINSoapIn" />
      <wsdl:output message="tns:GetByGTINSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetByPharmacode">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;p&gt;&lt;/p&gt;&lt;p&gt;&lt;b&gt;Schema Output:&lt;/b&gt; Click &lt;a href='../schema/SwissindexPharma_out_V101.xsd'&gt;here&lt;/a&gt;</wsdl:documentation>
      <wsdl:input message="tns:GetByPharmacodeSoapIn" />
      <wsdl:output message="tns:GetByPharmacodeSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetByDescr">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;p&gt;&lt;/p&gt;&lt;p&gt;&lt;b&gt;Schema Output:&lt;/b&gt; Click &lt;a href='../schema/SwissindexPharma_out_V101.xsd'&gt;here&lt;/a&gt;</wsdl:documentation>
      <wsdl:input message="tns:GetByDescrSoapIn" />
      <wsdl:output message="tns:GetByDescrSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="ws_Pharma_V101Soap" type="tns:ws_Pharma_V101Soap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="DownloadAll">
      <soap:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/DownloadAll" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DownloadByDate">
      <soap:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/DownloadByDate" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByGTIN">
      <soap:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByGTIN" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByPharmacode">
      <soap:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByPharmacode" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByDescr">
      <soap:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByDescr" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="ws_Pharma_V101Soap12" type="tns:ws_Pharma_V101Soap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="DownloadAll">
      <soap12:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/DownloadAll" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DownloadByDate">
      <soap12:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/DownloadByDate" style="document" soapActionRequired="true" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByGTIN">
      <soap12:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByGTIN" style="document" soapActionRequired="true" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByPharmacode">
      <soap12:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByPharmacode" style="document" soapActionRequired="true" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetByDescr">
      <soap12:operation soapAction="http://swissindex.e-mediat.net/SwissindexPharma_out_V101/GetByDescr" style="document" soapActionRequired="true" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="ws_Pharma_V101">
    <wsdl:port name="ws_Pharma_V101Soap" binding="tns:ws_Pharma_V101Soap">
      <soap:address location="https://swissindex.refdata.ch/Swissindex/Pharma/ws_Pharma_V101.asmx" />
    </wsdl:port>
    <wsdl:port name="ws_Pharma_V101Soap12" binding="tns:ws_Pharma_V101Soap12">
      <soap12:address location="https://swissindex.refdata.ch/Swissindex/Pharma/ws_Pharma_V101.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>