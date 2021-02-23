package com.healthcare;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class XmlOutputPosition implements OutputPosition{
    private String type;
    private String element;
    private String path;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id;  }

    private String id;


    public String getType() {  return type; }
    public void setType(String type) { this.type = type;}

    public String getPath() {return path;}
    public void setPath(String path) {
        this.path=path;
    }

    public Map generatePath(File file) {
        this.path = path;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        XMLReader xr = null;
        Map<String,String> result=null;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getValue() { return value; }
    public void setValue(String value) {this.value = value; }
    public String getElement() { return element;}
    public void setElement(String element) { this.element = element; }


}
