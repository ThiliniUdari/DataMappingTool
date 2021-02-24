package com.healthcare;

import org.xml.sax.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class FragmentContentHandler  extends DefaultHandler{
    String xPath = "";
    XMLReader xmlReader;
    FragmentContentHandler parent;
    StringBuilder characters = new StringBuilder();
    Map<String, Integer> elementNameCount = new HashMap<String, Integer>();
    static Map<String,String> result =new HashMap<String, String>();

    public FragmentContentHandler(XMLReader xmlReader) { this.xmlReader = xmlReader; }

    private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent) {
        this(xmlReader);
        this.xPath = xPath;
        this.parent = parent;
    }

    public FragmentContentHandler() {
        result.clear();
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Integer count = elementNameCount.get(qName);
        if (null == count) {
            count = 1;

        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        //String childXPath = xPath + "/" + qName + "[" + count + "]";
          String childXPath = xPath + "/" + qName ;

        int attsLength = atts.getLength();
        for (int x = 0; x < attsLength; x++) {
           System.out.println(childXPath + "[@" + atts.getQName(x) + "='"+ atts.getValue(x) + ']');
        }

        FragmentContentHandler child = new FragmentContentHandler(childXPath,xmlReader, this);
        xmlReader.setContentHandler(child);
    }

    boolean isAdded=false;
    public void endElement(String uri, String localName, String qName) throws SAXException {

        String value = characters.toString().trim();

        if (value.length() > 0) {
            //System.out.println(xPath + "='" + characters.toString() + "'");//with values
           // System.out.println(xPath +" Name :"+qName);//no value, xpath with element name

            for (Map.Entry<String,String> entry:result.entrySet()){
                if(entry.getKey() == xPath) {
                    isAdded=true;
                    continue;
                }
            }
            if(!isAdded){  result.put(xPath,qName); }
        }
        xmlReader.setContentHandler(parent);
     }

    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }
    public void clearResultMap(){
      //  result.clear();
    }
    public  Map generateXPath (File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();

        xr.setContentHandler(new FragmentContentHandler(xr));
        xr.parse(new InputSource(new FileInputStream(file)));
        return result;
    }

}
