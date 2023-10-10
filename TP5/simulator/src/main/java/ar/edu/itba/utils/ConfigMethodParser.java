package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outFile;
    private final Double deltaT;    // dt fijo e intrínseco de la simulación,
    private final Double deltaT2;   // dt2 para imprimir el estado del sistema (posiciones y velocidades de las partículas)
    private final Double maxTime;

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outFile = (String) json.get("outFile");
        this.deltaT = (Double) json.get("deltaT");
        this.deltaT2 = (Double) json.get("deltaT2");
        this.maxTime = (Double) json.get("maxTime");
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
    public Double getDeltaT() {
        return deltaT;
    }
    public Double getDeltaT2() {
        return deltaT2;
    }
    public Double getMaxTime() {
        return maxTime;
    }
}


