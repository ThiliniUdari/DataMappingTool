package com.healthcare;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface InputElement {

    public String getElement();
    public String getPath();
    public String getSourceId();
    public Map generatePath(File file);
    public String getType();
    public List evaluatePath(File msg) ;
}
