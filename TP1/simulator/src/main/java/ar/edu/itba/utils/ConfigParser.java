package ar.edu.itba.utils;

import org.json.simple.JSONObject;

public class ConfigParser {

    private final String staticFile;
    private final String dynamicFile;
    private final String outNeighborsFile;
    private final String outTimeFile;
    private final Boolean isPeriodic;
    private final Double R;
    private final Long M;
    private final Long N;
    private final Long L;
    private final Double minR;
    private final Double maxR;
    private final Long times;

    public ConfigParser(JSONObject json){
        this.staticFile = (String) json.get("staticFile");
        this.dynamicFile = (String) json.get("dynamicFile");
        this.outNeighborsFile = (String) json.get("outNeighborsFile");
        this.outTimeFile = (String) json.get("outTimeFile");
        this.isPeriodic = (Boolean) json.get("isPeriodic");
        this.R = (Double) json.get("R");
        this.M = (Long) json.get("M");
        this.N = (Long) json.get("N");
        this.L = (Long) json.get("L");
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
    public Boolean getPeriodic() {
        return isPeriodic;
    }
    public Double getR() {
        return R;
    }
    public Long getM() {
        return M;
    }
    public Long getN() {
        return N;
    }
    public Long getL() {
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


