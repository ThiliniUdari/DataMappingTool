package com.healthcare;

import java.io.File;
import java.util.List;

public interface InputElement {

    public String getElement();
    public String getPath();
    public String getSourceId();
    public void generatePath();
    public String getType();
    public List evaluatePath(File msg) ;
}
