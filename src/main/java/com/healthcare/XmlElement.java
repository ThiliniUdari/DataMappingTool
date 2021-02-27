package com.healthcare;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlElement implements InputElement{
    private String sourceId;
    private String xPath;
    private String element;
    private String type;


    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

   public void setPath(String xPath) {
       this.xPath = xPath;
   }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element;}
    public String getPath() { return xPath;}

    public String getSourceId() { return sourceId;}
    public void setSourceId(String sourceId) { this.sourceId = sourceId;}


    public List evaluatePath(File request) {
        //Want to read all book names from XML
        ArrayList<String> values = new ArrayList<String>();

        try {
            //Parse XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(request));

            //Get XPath expression
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            xpath.setNamespaceContext(new NamespaceResolver(doc));
            XPathExpression expr = xpath.compile(this.xPath+"/text()");

            //Search XPath expression
            Object result = expr.evaluate(doc, XPathConstants.NODESET);

            //Iterate over results and fetch values
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return values;
    }
}
