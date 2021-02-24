package com.healthcare;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElement implements InputElement{
    private String sourceId;
    private String xPath;
    private String element;
    private String type;
    private String value; //request

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }


   public void setxPath(String xPath) {
       this.xPath = xPath;
   }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element;}
    public String getPath() { return xPath;}

    public String getSourceId() { return sourceId;}
    public void setSourceId(String sourceId) { this.sourceId = sourceId;}

    Map<String,String> result=new HashMap<>();


    public Map generatePath(File file)  {
//   set the xPath variable

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        XMLReader xr = null;

        try {
            sp = spf.newSAXParser();
            xr = sp.getXMLReader();
            FragmentContentHandler contentHandler=new FragmentContentHandler(xr);
            xr.setContentHandler(contentHandler);
            xr.parse(new InputSource(new FileInputStream(file)));
            result= contentHandler.result ;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        //contentHandler.clearResultMap();

        return result;
    }

    @Override
    public List evaluatePath(File request) {

    List<String> values=new ArrayList<>();
        //Get DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
                    "<soapenv:Envelope xmlns:urn=\"urn:enterprise.soap.sforce.com\" xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope/\">\n" +
                    "  <soapenv:Header>\n" +
                    "     <urn:SessionHeader>\n" +
                    "        <urn:sessionId>QwWsHJyTPW.1pd0_jXlNKOSU</urn:sessionId>\n" +
                    "     </urn:SessionHeader>\n" +
                    "     </soapenv:Header>\n" +
                    "     <soapenv:Body>\n" +
                    "     <urn:convertLead >\n" +
                    "        <urn:leadConverts> <!-- Zero or more repetitions -->\n" +
                    "           <urn:convertedStatus>Qualified</urn:convertedStatus>\n" +
                    "           <urn:doNotCreateOpportunity>false</urn:doNotCreateOpportunity>\n" +
                    "           <urn:leadId>00QD000000FP14JMAT</urn:leadId>\n" +
                    "           <urn:opportunityName>Partner Opportunity</urn:opportunityName>\n" +
                    "           <urn:overwriteLeadSource>true</urn:overwriteLeadSource>\n" +
                    "           <urn:ownerId>005D0000000nVYVIA2</urn:ownerId>\n" +
                    "           <urn:sendNotificationEmail>true</urn:sendNotificationEmail>\n" +
                    "        </urn:leadConverts>\n" +
                    "     </urn:convertLead>\n" +
                    "</soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            InputSource inputXML = new InputSource( new StringReader( xml ) );

            XPath xPath = XPathFactory.newInstance().newXPath();

            String value = xPath.evaluate("/soapenv:Envelope/soapenv:Header/urn:SessionHeader/urn:sessionId/text()", inputXML);

//                    String value =  xpath.evaluate("/soapenv:Envelope/soapenv:Header/urn:SessionHeader/urn:sessionId/text()", xml);
            System.out.println("Single value:"+value);
            ////////////////////////////////////
            db = dbf.newDocumentBuilder();
            Document xmlMsg = db.parse(request);
            //Get XPath
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            //Get all matches
            NodeList nodes = null;
                nodes = (NodeList) xpath.evaluate(this.xPath.concat("/text()"), xmlMsg, XPathConstants.NODESET);
                if (nodes.getLength()>0){
                    for (int i = 0; i < nodes.getLength(); i++) {
                        values.add(nodes.item(i).getNodeValue());
                        //System.out.println("Value:"+nodes.item(i).getNodeValue());
                    }
                }else{
//                    InputSource inputXML = new InputSource( new StringReader( xml ) );
//
//                    XPath xPath = XPathFactory.newInstance().newXPath();
//
//                    String value = xPath.evaluate(this.xPath+"/text()", inputXML);
//
//                   String value =  xpath.evaluate("/soapenv:Envelope/soapenv:Header/urn:SessionHeader/urn:sessionId/text()", xml);
//                    System.out.println("Single value:"+value);
//                    values.add(value);
                }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (XPathExpressionException e) {
            e.printStackTrace();
        }
       return values;
    }
}
