package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outFile;
    private final Double deltaT;    // dt fijo e intrínseco de la simulación,
    private final Double deltaT2;   // dt2 para imprimir el estado del sistema (posiciones y velocidades de las partículas)
    private final Double maxTime;
    private final Integer D;        // Ancho de apertura de salida
    private final Double A;         // Amplitud
    private final Double omega;     // Frecuencia del forzado externo

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outFile = (String) json.get("outFile");
        this.deltaT = (Double) json.get("deltaT");
        this.deltaT2 = (Double) json.get("deltaT2");
        this.maxTime = (Double) json.get("maxTime");
        this.D = (Integer) json.get("D");
        this.A = (Double) json.get("A");
        this.omega = (Double) json.get("omega");
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
    public Integer getD() {
        return D;
    }
    public Double getA() {
        return A;
    }
    public Double getOmega() {
        return omega;
    }
}


