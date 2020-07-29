package android.panos.ram.rammilkcollection.models;

import java.util.ArrayList;

public class CollectionTradesInfo {

    private String tradecode;

    private String supplier;

    private String milktype;

    private String quantity;

    private ArrayList<CollectionTradeLinesInfo> tradelineslist = new ArrayList<CollectionTradeLinesInfo>();


    public String getTradecode() {
        return tradecode;
    }

    public void setTradecode(String tradecode) {
        this.tradecode = tradecode;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getMilktype() {
        return milktype;
    }

    public void setMilktype(String milktype) {
        this.milktype = milktype;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ArrayList<CollectionTradeLinesInfo> getTradeLinesList() {
        return tradelineslist;
    }

    public void setTradeLinesList(ArrayList<CollectionTradeLinesInfo> tradelineslist) {
        this.tradelineslist = tradelineslist;
    }

}
