package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outFile;
    private final String outTimeFile;
    private final Double deltaT;    // dt fijo e intrínseco de la simulación,
    private final Double deltaT2;   // dt2 para imprimir el estado del sistema (posiciones y velocidades de las partículas)
    private final Double maxTime;
    private final Double D;        // Ancho de apertura de salida
    private final Double omega;     // Frecuencia del forzado externo

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outFile = (String) json.get("outFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.deltaT = (Double) json.get("deltaT");
        this.deltaT2 = (Double) json.get("deltaT2");
        this.maxTime = (Double) json.get("maxTime");
        this.D = (Double) json.get("benchmark/D");
        this.omega = (Double) json.get("benchmark/omega");
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
    public Double getDeltaT() {
        return deltaT;
    }
    public Double getDeltaT2() {
        return deltaT2;
    }
    public Double getMaxTime() {
        return maxTime;
    }
    public Double getD() {
        return D;
    }
    public Double getOmega() {
        return omega;
    }
}


