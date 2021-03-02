package com.healthcare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static com.healthcare.Main.ROOTPATH;
import static com.healthcare.XsdXpathGenerator.getAllXpaths;

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
        UUID uuid=UUID.randomUUID();
        String srcId=uuid.toString();
        Source src=new Source(inputPayload);
        src.setType(type);
        src.setId(srcId);
        setSourceList(src);
    }

    public void loadOutput(String type,String outputPayload,String outputFileName){
        this.outputFileName =outputFileName;
        UUID uuid=UUID.randomUUID();
        String destId =uuid.toString();
        Destination dest =new Destination(outputPayload);
        dest.setId(destId);
        dest.setType(type);
        this.output=dest;
        File file= new File(ROOTPATH+outputFileName);
        createElementList(output.getId(),output.getType(),file,false);
       if(outputPositions.size()>0)System.out.println("---Output Element List created:"+outputPositions.size());
    }
    public void setSourceList(Source src) {
        sourceList.add(src);
        System.out.println("Source List : ");
        for (Object source:sourceList){
            System.out.println(source);
        }
        if (sourceList.size()>0)    System.out.println("---Source List created----");
        File file =new File(ROOTPATH+inputFileName);
        createElementList(src.getId(),src.getType(),file,true);
        if(inputElements.size()>0)  System.out.println("---Input Element List created:"+inputElements.size());
    }

    public void createElementList(String id,String type,File file,boolean isSource){

       ElementFactory elementFactory =new ElementFactory();

       Map<String,String>pathResults=elementFactory.generatePath(type,file); // for xml files
       Map<String,String>  xpathfromXsd = getAllXpaths(file);// for xsd files

        for (Map.Entry<String, String> entry : pathResults.entrySet()) {
           System.out.println(entry.getKey() + " -->  " + entry.getValue());

            InputElement inputElement=elementFactory.getInputElement(type);
            OutputPosition outputPosition=elementFactory.getOutputElement(type);
         if(isSource) {
            inputElement.setPath(entry.getKey());
            inputElement.setSourceId(id);
            inputElement.setElement(entry.getValue());
            inputElement.setType(type);
            inputElements.add(inputElement);
           }
         else{
            outputPosition.setPath(entry.getKey());
            outputPosition.setElement(entry.getValue());
            outputPosition.setType(type);
            outputPosition.setId(id);
            outputPositions.add(outputPosition);
            }
        }
    }

    public void doMapping(File file){

       String line;
        try {
            FileInputStream inputStream=new FileInputStream(file);
            Scanner sc =new Scanner(inputStream);
            do{
                line =sc.nextLine();
                int i=line.indexOf('=');

                String input =line.substring(0,i).trim();
                String output = line.substring(i+1).trim();

                for (OutputPosition position : outputPositions)
                 {
                     if (position.getPath().equals(output)) {
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

            System.out.println("---Map : "+map.size());
            for (Map.Entry<OutputPosition,OutputResolver> entry : map.entrySet()) {
                System.out.println(entry.getKey() +" -->  " + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void evaluateRequest(File request) {
        List<String> values;
        String value;
        for (Map.Entry<OutputPosition, OutputResolver> entry : map.entrySet()) {
            value = "";
            values = entry.getValue().getFinalValue(request);
            for (String item : values) {
                value = value.concat(item).concat(",");
            }
            entry.getKey().setValue(value);
        }  System.out.println("---Result----");
        for (OutputPosition output : outputPositions) {
            System.out.println("Element:" + output.getElement() + "  value:" + output.getValue());
        }
    }
}

