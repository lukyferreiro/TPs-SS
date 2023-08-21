package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigMethodParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outOffLatticeFile;
    private final String outOrderParametersVaFile;
    private final String outTimeFile;
    private final Boolean isPeriodic;
    private final Double Rc;    //Radio de interaccion entre las particulas
    private final Double dt;    //Paso temporal
    private final Double eta;   //Amplitud de ruido
    private final Long maxIterations;

    public ConfigMethodParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outOffLatticeFile = (String) json.get("outOffLatticeFile");
        this.outOrderParametersVaFile = (String) json.get("outOrderParametersVaFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.isPeriodic = (Boolean) json.get("isPeriodic");
        this.Rc = (Double) json.get("Rc");
        this.dt = (Double) json.get("dt");
        this.eta = (Double) json.get("eta");
        this.maxIterations = (Long) json.get("maxIterations");
    }

    public String getStaticFile() {
        return staticFile;
    }
    public String getDynamicFile() {
        return dynamicFile;
    }
    public String getOutOffLatticeFile() {
        return outOffLatticeFile;
    }
    public String getOutOrderParametersVaFile() {
        return outOrderParametersVaFile;
    }
    public String getOutTimeFile() {
        return outTimeFile;
    }
    public Boolean getPeriodic() {
        return isPeriodic;
    }
    public Double getRc() {
        return Rc;
    }
    public Double getDt() {
        return dt;
    }
    public Double getEta() {
        return eta;
    }
    public Long getMaxIterations() {
        return maxIterations;
    }
}


