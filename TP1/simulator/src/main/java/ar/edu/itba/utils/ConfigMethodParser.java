package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outNeighborsFile;
    private final String outTimeFile;
    private final String method;
    private final Boolean isPeriodic;
    private final Double Rc;    //Radio de interaccion entre las particulas
    private final Long M;   //Tendremos MxM celdas

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outNeighborsFile = (String) json.get("outNeighborsFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.method = (String) json.get("method");
        this.isPeriodic = (Boolean) json.get("isPeriodic");
        this.Rc = (Double) json.get("Rc");
        this.M = (Long) json.get("M_variation");
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
}


