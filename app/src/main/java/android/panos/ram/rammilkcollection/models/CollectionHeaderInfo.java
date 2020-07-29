package android.panos.ram.rammilkcollection.models;

import java.util.ArrayList;

public class CollectionHeaderInfo {

    private String aa;
    private String sample;
    private String fat;
    private String temperature;
    private String ph;

    private ArrayList<CollectionTradesInfo> tradeslist = new ArrayList<CollectionTradesInfo>();

    public String getAA() {
        return aa;
    }

    public void setAA(String aa) {
        this.aa = aa;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public ArrayList<CollectionTradesInfo> getTradesList() {
        return tradeslist;
    }

    public void setTradesList(ArrayList<CollectionTradesInfo> tradeslist) {
        this.tradeslist = tradeslist;
    }

}
