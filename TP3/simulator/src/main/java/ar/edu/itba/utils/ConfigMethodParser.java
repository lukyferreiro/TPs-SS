package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outFile;
    private final String outTimeFile;
    private final Double L;   //Alto del recinto


    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outFile = (String) json.get("outFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.L = (Double) json.get("L");
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
    public Double getL() {
        return L;
    }
}


