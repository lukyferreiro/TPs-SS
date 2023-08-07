package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outNeighborsFile;
    private final String outTimeFile;
    private final String method;
    private final Boolean isPeriodic;
    private final Double Rc;    //Radio de interaccion entre las particulas
    private final Long M;   //Tendremos MxM celdas
    private final Long N;   //Cantidad particulas
    private final Double L;   //Longitud del tablero
    private final Double minR;  //Radio minimo para las particulas
    private final Double maxR;  //Radio maximo para las particulas
    private final Long times;   //Cantidad de tiempos

    public ConfigParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outNeighborsFile = (String) json.get("outNeighborsFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.method = (String) json.get("method");
        this.isPeriodic = (Boolean) json.get("isPeriodic");
        this.Rc = (Double) json.get("Rc");
        this.M = (Long) json.get("M");
        this.N = (Long) json.get("N");
        this.L = (Double) json.get("L");
        this.minR = (Double) json.get("max-radius");
        this.maxR = (Double) json.get("min-radius");
        this.times = (Long) json.get("times");
    }

    public String getStaticFile() {
        return staticFile;
    }
    public String getDynamicFile() {
        return dynamicFile;
    }
    public String getOutNeighborsFile() {
        return outNeighborsFile;
    }
    public String getOutTimeFile() {
        return outTimeFile;
    }
    public String getMethod() {
        return method;
    }
    public Boolean getPeriodic() {
        return isPeriodic;
    }
    public Double getRc() {
        return Rc;
    }
    public Long getM() {
        return M;
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
}


