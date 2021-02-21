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
import java.util.List;

public class XmlElement implements InputElement{
    private String sourceId;
    private String xPath;
    private String element;
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

   // private String value=null;

   public void setxPath(String xPath) {
       this.xPath = xPath;
   }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element;}
    public String getPath() { return xPath;}

    public String getSourceId() { return sourceId;}
    public void setSourceId(String sourceId) { this.sourceId = sourceId;}

    @Override
    public void generatePath() {
//        //set the xPath variable

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        XMLReader xr = null;
        try {
            sp = spf.newSAXParser();
            xr = sp.getXMLReader();
            xr.setContentHandler(new FragmentContentHandler(xr));
            xr.parse(new InputSource(new FileInputStream("//home//thilini//Desktop//OH_DataMapper//samples//input.xml")));
                    //(new FileInputStream("input.xml")));


        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public List evaluatePath(File request) {

    List<String> values=new ArrayList<>();
        //Get DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document xmlMsg = db.parse(request);
            //Get XPath
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            String name = (String) xpath.evaluate(this.xPath.concat("/text()"), xmlMsg, XPathConstants.STRING);
            //Get all matches
            NodeList nodes = null;
                nodes = (NodeList) xpath.evaluate(this.xPath.concat("/text()"), xmlMsg, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
                //System.out.println("Value:"+nodes.item(i).getNodeValue());
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
