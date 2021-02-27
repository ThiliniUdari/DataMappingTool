package com.healthcare;

public class XmlOutputPosition implements OutputPosition{
    private String type;
    private String element;
    private String path;
    private String value;
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id;  }
    public String getType() {  return type; }
    public void setType(String type) { this.type = type;}

    public String getPath() {return path;}
    public void setPath(String path) { this.path=path; }

    public String getValue() { return value; }
    public void setValue(String value) {this.value = value; }
    public String getElement() { return element;}
    public void setElement(String element) { this.element = element; }

}
