package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigGeneratorParser {
    private final String staticFile;
    private final String dynamicFile;
    private final Long N;   //Cantidad particulas
    private final Double L;   //Alto del recinto
    private final Double minR;  //Radio minimo para las particulas
    private final Double maxR;  //Radio maximo para las particulas
    private final Long times;   //Cantidad de tiempos
    private final Double property;  //Propiedad de las particulas
    private final Double speed;     //Velocidad de las particulas

    public ConfigGeneratorParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.N = (Long) json.get("N");
        this.L = (Double) json.get("L");
        this.minR = (Double) json.get("max_radius");
        this.maxR = (Double) json.get("min_radius");
        this.times = (Long) json.get("times");
        this.property = (Double) json.get("property");
        this.speed = (Double) json.get("speed");
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
    public Double getMinR() {
        return minR;
    }
    public Double getMaxR() {
        return maxR;
    }
    public Long getTimes() {
        return times;
    }
    public Double getProperty() {
        return property;
    }
    public Double getSpeed() {
        return speed;
    }
}
