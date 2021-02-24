package com.healthcare;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.healthcare.Main.ROOTPATH;

public class DataMapperMediator {

    List<Source> sourceList=new ArrayList<>();
    List<OutputPosition> outputPositions =new ArrayList<>();
    List <InputElement> inputElements =new ArrayList<>();
    List<OutputResolver> resolvers=new ArrayList<>();
    Map<OutputPosition,OutputResolver> map = new LinkedHashMap<>();
    Destination output;

    //temp
    String inputFileName ;
    String outputFileName;
    public void loadInput(String type, String inputPayload ,String inputFileName) {
        this.inputFileName=inputFileName;

        String srcId="src001"; //***temp
        Source src=new Source(inputPayload);
        src.setType(type);
        src.setId(srcId);

        setSourceList(src);
    }

    public void loadOutput(String type,String outputPayload,String outputFileName){
        this.outputFileName =outputFileName;

        String destId ="dest001";  //temp
        Destination dest =new Destination(outputPayload);
        dest.setId(destId);
        dest.setType(type);
        this.output=dest;
        File file= new File(ROOTPATH+outputFileName);
        createElementList(output.getId(),output.getType(),file,false);
       if(outputPositions.size()>0)System.out.println("---Output Element List created:"+outputPositions.size());
    }
    public void setSourceList(Source src) {
        //System.out.println(src.getId());
        sourceList.add(src);
        System.out.println("Source List : ");
        for (Object source:sourceList){
            System.out.println(source);
        }System.out.println("---Source List created----");
        File file =new File(ROOTPATH+inputFileName);
        createElementList(src.getId(),src.getType(),file,true);
        if(inputElements.size()>0)System.out.println("---Input Element List created:"+inputElements.size());

    }

    public void createElementList(String id,String type,File file,boolean isSource){
        switch (type){
            case "xml":
                FragmentContentHandler contentHandler=new FragmentContentHandler();
                try {
                    Map<String,String>xpathWithName=contentHandler.generateXPath(file);
                    for (Map.Entry<String, String> entry : xpathWithName.entrySet()) {
                       System.out.println(entry.getKey() + " -->  " + entry.getValue());

                        if(isSource) {
                        XmlElement input = new XmlElement();
                        input.setxPath(entry.getKey());
                        input.setSourceId(id);
                        input.setElement(entry.getValue());
                        input.setType("xml");
                        inputElements.add(input);
                       }
                     else{
                        XmlOutputPosition outputPosition =new XmlOutputPosition();
                        outputPosition.setPath(entry.getKey());
                        outputPosition.setElement(entry.getValue());
                        outputPosition.setType("xml");
                        outputPosition.setId(id);
                        outputPositions.add(outputPosition);

                        }
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "json":
            case "csv":
            default:
                System.out.println("Invalid Format");
       }
    }

    public void doMapping(){

        System.out.println("Map file:");
        Scanner scanner =new Scanner(System.in);
        String  filePath = ROOTPATH.concat(scanner.next());
        String line;
        try {
            FileInputStream inputStream=new FileInputStream(filePath);
            Scanner sc =new Scanner(inputStream);

            do{
                line =sc.nextLine();
                int i=line.indexOf('=');

                String input =line.substring(0,i).trim();// simple transition 1:1
                String output = line.substring(i+1).trim();

                for (OutputPosition position : outputPositions)
                 {    //extend for n:1 cases

                     if (position.getPath().equals(output)) {
                       //  System.out.println("position "+position.getPath()+")=="+output);
                         for(InputElement element:inputElements){
                             if (element.getPath().equals(input)) {
                                 OutputResolver resolver = new OutputResolver();
                                 resolver.elementList.add(element);
                                 resolvers.add(resolver);
                                 map.put(position, resolver);
                             }
                         }
                    }
                 }
            }while(sc.hasNextLine());
            System.out.println("---Map---");
            for (Map.Entry<OutputPosition,OutputResolver> entry : map.entrySet()) {
                System.out.println(entry.getKey() +" -->  " + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public  String getRequest(){
        return null;

    }
    public void evaluateRequest(File request) {
        List<String> values;
        String value;
        for (Map.Entry<OutputPosition, OutputResolver> entry : map.entrySet()) {
            value = "";
            values = entry.getValue().getFinalValue(request);
            for (String item : values) {
                value = value.concat(item).concat(",");
                System.out.println("Final Value:-"+value);
            }
            entry.getKey().setValue(value);
        }
        for (OutputPosition output : outputPositions) {
            System.out.println("Element:" + output.getElement() + "  value:" + output.getValue());
        }
    }
}
