package android.panos.ram.rammilkcollection.models;

import android.app.Application;

public class GlobalClass extends Application {

    private String callingForm;
    private String newcollection_aa;
    private String collection_aa;
    private String newtradecheck_aa;
    private String tradecheck_aa;
    private String tradeid;
    private String metaggisitradeid;


    public String getcallingForm() {

        return callingForm;
    }

    public void setcallingForm(String acallingForm) {

        this.callingForm = acallingForm;

    }

    public String getnewcollection_aa() {

        return newcollection_aa;
    }

    public void setnewcollection_aa(String newcollection_aa) {

        this.newcollection_aa = newcollection_aa;

    }

    public String getcollection_aa() {

        return collection_aa;
    }

    public void setcollection_aa(String collection_aa) {

        this.collection_aa = collection_aa;

    }

    public String getnewtradecheck_aa() {

        return newtradecheck_aa;
    }

    public void setnewtradecheck_aa(String newtradecheck_aa) {

        this.newtradecheck_aa = newtradecheck_aa;

    }
    public String gettradecheck_aa() {

        return tradecheck_aa;
    }

    public void settradecheck_aa(String tradecheck_aa) {

        this.tradecheck_aa = tradecheck_aa;

    }

    public String gettradeid() {

        return tradeid;
    }

    public void settradeid(String tradeid) {

        this.tradeid= tradeid;

    }
    public String getmetaggisitradeid() {

        return metaggisitradeid;
    }

    public void setmetaggisitradeid(String metaggisitradeid) {

        this.metaggisitradeid= metaggisitradeid;

    }

}
