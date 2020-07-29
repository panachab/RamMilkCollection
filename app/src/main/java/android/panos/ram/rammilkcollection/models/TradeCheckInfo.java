package android.panos.ram.rammilkcollection.models;

import java.util.ArrayList;

public class TradeCheckInfo {

    private String tradecode;

    private String transportation;

    private String quantity;

    private String fromtradecode;

    private String uptotradecode;

    private String isdiakinisi;

    private String ismetaggisi;

    private String ismetaggisicheck;


    private ArrayList<TradeCheckLinesInfo> tradelineslist = new ArrayList<TradeCheckLinesInfo>();

    public String getIsdiakinisi() {
        return isdiakinisi;
    }

    public void setIsdiakinisi(String isdiakinisi) {
        this.isdiakinisi = isdiakinisi;
    }

    public String getIsmetaggisi() {
        return ismetaggisi;
    }

    public void setIsmetaggisi(String ismetaggisi) {
        this.ismetaggisi = ismetaggisi;
    }

    public void setIsmetaggisicheck(String ismetaggisicheck) {
        this.ismetaggisicheck = ismetaggisicheck;
    }

    public String getIsmetaggisicheck() {
        return ismetaggisicheck;
    }

    public String getTradecode() {
        return tradecode;
    }

    public void setTradecode(String tradecode) {
        this.tradecode = tradecode;
    }

    public String getFromTradecode() {
        return fromtradecode;
    }

    public void setFromTradecode(String fromtradecode) {
        this.fromtradecode = fromtradecode;
    }

    public String getUptoTradecode() {
        return uptotradecode;
    }

    public void setUptoTradecode(String uptotradecode) {
        this.uptotradecode = uptotradecode;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ArrayList<TradeCheckLinesInfo> getTradeLinesList() {
        return tradelineslist;
    }

    public void setTradeLinesList(ArrayList<TradeCheckLinesInfo> tradelineslist) {
        this.tradelineslist = tradelineslist;
    }

}
