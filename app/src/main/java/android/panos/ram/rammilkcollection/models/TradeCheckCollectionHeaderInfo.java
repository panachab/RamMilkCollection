package android.panos.ram.rammilkcollection.models;

import java.util.ArrayList;

public class TradeCheckCollectionHeaderInfo {

    private String id;
    private String sample;
    private String station;
    private String pagolekani;
    private String amount;



    private ArrayList<CollectionTradesInfo> tradeslist = new ArrayList<CollectionTradesInfo>();

    public String getId() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPagolekani() {
        return pagolekani;
    }

    public void setPagolekani(String pagolekani) {
        this.pagolekani = pagolekani;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ArrayList<CollectionTradesInfo> getTradesList() {
        return tradeslist;
    }

    public void setTradesList(ArrayList<CollectionTradesInfo> tradeslist) {
        this.tradeslist = tradeslist;
    }

}
