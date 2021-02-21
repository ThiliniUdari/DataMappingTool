package com.healthcare;

import org.xml.sax.*;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class FragmentContentHandler  extends DefaultHandler{
    String xPath = "/";
    XMLReader xmlReader;
    FragmentContentHandler parent;
    StringBuilder characters = new StringBuilder();
    Map<String, Integer> elementNameCount = new HashMap<String, Integer>();

    public FragmentContentHandler(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent) {
        this(xmlReader);
        this.xPath = xPath;
        this.parent = parent;
    }

      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Integer count = elementNameCount.get(qName);
        if (null == count) {
            count = 1;

        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        String childXPath = xPath + "/" + qName + "[" + count + "]";

        int attsLength = atts.getLength();
        for (int x = 0; x < attsLength; x++) {
            System.out.println(childXPath + "[@" + atts.getQName(x) + "='"+ atts.getValue(x) + ']');
        }

        FragmentContentHandler child = new FragmentContentHandler(childXPath,xmlReader, this);
        xmlReader.setContentHandler(child);
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
        String value = characters.toString().trim();
        if (value.length() > 0) {
            System.out.println(xPath + "='" + characters.toString() + "'");
        }
        xmlReader.setContentHandler(parent);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }

}
