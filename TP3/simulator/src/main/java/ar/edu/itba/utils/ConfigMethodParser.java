package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outFile;
    private final String outTimeFile;

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outFile = (String) json.get("outFile");
        this.outTimeFile = (String) json.get("outTimeFile");
    }

    public String getStaticFile() {
        return staticFile;
    }
    public String getDynamicFile() {
        return dynamicFile;
    }
    public String getOutFile() {
        return outFile;
    }
    public String getOutTimeFile() {
        return outTimeFile;
    }
}

