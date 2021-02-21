package com.healthcare;

import java.io.File;

public class Destination {
    private String id;
    private String type;
    private String payload;

    Destination( String output) {
        this.payload = output;
    }
    public void setId(String id){  this.id=id; }
    public void setType(String type){ this.type=type;}
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
