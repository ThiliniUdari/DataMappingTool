package com.healthcare;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CsvElement implements InputElement{
    private String sourceId;
    private String csvPath;
    private String element;
    private String type;

    public void setType(String type){this.type=type ;}
    public String getType(){
        return this.type;
    }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element;}
    public String getPath() { return csvPath;}
    public void setPath(String path){this.csvPath=path;}


    public String getSourceId() { return sourceId;}
    public void setSourceId(String sourceId) { this.sourceId = sourceId;}

    @Override
    public List evaluatePath(File msg){
        return null;
    }
}
