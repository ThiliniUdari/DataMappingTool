package com.healthcare;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonElement implements InputElement{
    private String sourceId;
    private String jsonPath;
    private String element;
    private  String type;

    @Override
    public void setType(String type) {this.type=type; }
    @Override
    public String getType() {
        return type;
    }

    public String getElement() { return element; }
    public void setElement(String element) { this.element = element;}

    public String getPath() { return jsonPath;}
    public void setPath(String path) {
        this.jsonPath = path;
    }

    public String getSourceId() { return sourceId;}
    public void setSourceId(String sourceId) { this.sourceId = sourceId;}

    @Override
    public List evaluatePath(File msg){
        return null;
    }
}
