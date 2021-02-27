package com.healthcare;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface InputElement {
     void setType(String type);
     String getType();

     void setPath(String path) ;
     String getPath();

     String getSourceId();
     void setSourceId(String sourceId);

     String getElement();
     void setElement(String element);

     List evaluatePath(File msg) ;
}
