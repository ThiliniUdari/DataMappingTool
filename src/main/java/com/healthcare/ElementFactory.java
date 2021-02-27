package com.healthcare;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElementFactory {

    public InputElement getInputElement(String type){
        switch (type){
            case"xml":return new XmlElement();
            case "json":return new JsonElement();
            case "csv" :return new CsvElement();
            default:return null;
        }
    }

    public OutputPosition getOutputElement(String type){
        switch (type){
            case"xml":return new XmlOutputPosition();
//            case "json":return new JsonOutputPosition();
//            case "csv" :return new CsvOutputPosition();
            default:return null;
        }
    }

    public Map generatePath(String type, File file){
    Map <String,String> result=new HashMap<>();
        switch (type){
            case "xml":
                FragmentContentHandler contentHandler=new FragmentContentHandler();
                try {
                    result=contentHandler.generateXPath(file);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               break;
            case "json":break;
            case "csv":break;
        }
        return result;
    }
}
