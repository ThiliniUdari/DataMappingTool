package com.healthcare;


public class Source {

    private String id;
    private String type;
    private String payload;

    Source(String payload){
        this.payload=payload;
    }
    public void setId(String id){  this.id=id; }
    public void setType(String type){ this.type=type;}
    public String getId() { return id;}
    public String getType() { return type; }



}
