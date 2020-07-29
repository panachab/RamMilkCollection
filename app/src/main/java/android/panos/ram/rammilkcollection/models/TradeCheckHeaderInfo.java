package android.panos.ram.rammilkcollection.models;

import java.util.ArrayList;

public class TradeCheckHeaderInfo {

    private String aa;
    private String fromtradecode;
    private String uptptradecode;

    private ArrayList<TradeCheckInfo> tradeslist = new ArrayList<TradeCheckInfo>();

    public String getAA() {
        return aa;
    }

    public void setAA(String aa) {
        this.aa = aa;
    }

    public String getFromTradecode() {
        return fromtradecode;
    }

    public void setFromTradecode(String fromtradecode) {
        this.fromtradecode = fromtradecode;
    }

    public String getUpToTradecode() {
        return uptptradecode;
    }

    public void setUpToTradecode(String uptptradecode) {
        this.uptptradecode = uptptradecode;
    }

    public ArrayList<TradeCheckInfo> getTradesList() {
        return tradeslist;
    }

    public void setTradesList(ArrayList<TradeCheckInfo> tradeslist) {
        this.tradeslist = tradeslist;
    }

}
