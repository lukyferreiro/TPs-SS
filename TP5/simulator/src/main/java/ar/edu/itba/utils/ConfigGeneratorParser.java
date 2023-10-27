package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigGeneratorParser {
    private final String staticFile;
    private final String dynamicFile;
    private final Long N;   //Cantidad particulas
    private final Double L;    //Alto del recinto
    private final Double W;    //Ancho del recinto
    private final Double minR;  //Radio minimo para las particulas
    private final Double maxR;  //Radio maximo para las particulas
    private final Double mass;  //Propiedad de las particulas
    private final Long times;

    public ConfigGeneratorParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.N = (Long) json.get("N");
        this.L = (Double) json.get("L");
        this.W = (Double) json.get("W");
        this.minR = (Double) json.get("max_radius");
        this.maxR = (Double) json.get("min_radius");
        this.mass = (Double) json.get("mass");
        this.times = (Long) json.get("times");
    }

    public String getStaticFile() {
        return staticFile;
    }
    public String getDynamicFile() {
        return dynamicFile;
    }
    public Long getN() {
        return N;
    }
    public Double getL() {
        return L;
    }
    public Double getW() {
        return W;
    }
    public Double getMinR() {
        return minR;
    }
    public Double getMaxR() {
        return maxR;
    }
    public Double getMass() {
        return mass;
    }
    public Long getTimes() {
        return times;
    }
}

