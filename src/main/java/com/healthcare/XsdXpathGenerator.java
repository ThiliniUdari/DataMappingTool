package com.healthcare;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XsdXpathGenerator {

    protected static File XmlToXsd(){
        return null;
    }


    /**
     *
     * @param file Path of XSD file
     * @return Map of xpath as key and type as value
     */
    protected static Map<String,String> getAllXpaths(File file) {
        Map<String,String> result = new HashMap<String, String>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse (file);
            NodeList elementList = document.getElementsByTagName("xs:element");
            NodeList attributeList = document.getElementsByTagName("xs:attribute");
            for (int i = 0 ; i < attributeList.getLength(); i++) {
                Element element = (Element)attributeList.item(i);
                if(element.hasAttributes() && element.getAttribute("type") != "") {
                    result.put(element.getAttribute("name"),extractName(element.getAttribute("name")));
                }
            }
            for(int i = 0 ; i < elementList.getLength(); i++) {
                Element element = (Element)elementList.item(i);
                if(element.hasAttributes() && element.getAttribute("type") != "") {
                    result.put("/"+xpathString(element,new StringBuilder(element.getAttribute("name"))).toString() ,extractName(element.getAttribute("name")));
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException ed) {
            ed.printStackTrace();
        }
        return result;
    }




    /**
     *
     * @param element Element whose parent node is to be found
     * @param xpath Current xpath
     * @return Complete xpath
     */
    private static StringBuilder xpathString(Element element,StringBuilder xpath) {
        Element parent = getParentElement(element);
        if(parent != null){
            if(parent.hasAttributes() && parent.getAttribute("name") != ""){
                xpath = new StringBuilder(parent.getAttribute("name")).append("/").append(xpath);
                return xpathString(parent,xpath);
            }else {
                return xpath;
            }
        }else {
            return xpath;
        }
    }

    /**
     *
     * @param element Element of type xs:element
     * @return Parent element
     */
    private static Element getParentElement(Element element) {
        Node parentNode = element.getParentNode();
        if (parentNode != null && parentNode instanceof Element) {
            String nodeName = parentNode.getNodeName();
            if (nodeName.equalsIgnoreCase("xs:element")) {
                return (Element) element.getParentNode();
            } else {
                return getParentElement((Element) parentNode);
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @param name String type in xsd
     * @return scrapped type
     */
    private static String extractName(String name) {
        if(name.startsWith("xs:")){
            return name.replace("xs:","");
        }else {
            return name;
        }
    }

}
