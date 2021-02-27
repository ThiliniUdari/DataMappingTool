package com.healthcare;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static String ROOTPATH ="//home//thilini//Desktop//OH_DataMapper//samples//";

    public static void main (String[] args){

        Scanner scanner=new Scanner(System.in);
        int option;
        String type,filePath;
        DataMapperMediator mediator =new DataMapperMediator();

        while (true) {
            System.out.println("*****************");
            System.out.println("DATA MAPPING TOOL");
            System.out.println("*****************");

            System.out.println("\t1.Input ");
            System.out.println("\t2.Output");
            System.out.println("\t3.Map");
            System.out.println("\t4.Request");

            System.out.print("Select the option :");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    //Input file
                    System.out.print("Type:");
                    type = scanner.next();

                    System.out.print("Input File: ");
                    String fileName=scanner.next();
                    filePath = ROOTPATH.concat(fileName);

                    String input = loadFile(filePath);
                    mediator.loadInput(type, input,fileName);
                    break;

                case 2:
                    //output file
                    System.out.print("Type:");
                    type = scanner.next();

                    System.out.print("Output File: ");
                    String outputfileName=scanner.next();

                    filePath = ROOTPATH.concat(outputfileName);

                    String output = loadFile(filePath);
                    mediator.loadOutput(type, output,outputfileName);
                    break;

                case 3:
                  //  mapping
                    System.out.print("Map file:");
                    filePath = ROOTPATH.concat(scanner.next());
                    mediator.doMapping(new File(filePath));
                    break;

                case 4:
                    //send request msg
                    System.out.print("Request: ");
                    filePath = ROOTPATH.concat(scanner.next());

                    mediator.evaluateRequest(new File(filePath));


            }
        }

    }

    public static String loadFile(String filePath){
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       // System.out.println("input:"+contentBuilder.toString());
        return contentBuilder.toString();
    }
}
