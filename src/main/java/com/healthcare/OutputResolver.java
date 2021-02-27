package com.healthcare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OutputResolver {

    protected List<InputElement> elementList =new ArrayList<>();

    public void setInputElements(InputElement input){
        elementList.add(input);
    }

    public List getFinalValue(File request){
    List<String> values = null;
        for(InputElement input:elementList){
            values= input.evaluatePath(request);
        }
       return values;
      }
}
/*
Functions
    -pre built functions
    -custom functions
 */


