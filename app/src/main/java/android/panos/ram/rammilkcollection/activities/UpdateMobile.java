package android.panos.ram.rammilkcollection.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateMobile extends AppCompatActivity implements View.OnClickListener {

    private DBHelper mydb;

    CheckBox farmerscheck;
    CheckBox materiascheck;
    CheckBox parameterscheck;

    ProgressBar pg;

    String webserviceurl;
    String getfarmersurl;
    String getmaterialsurl;
    String getdocseriessurl;
    String getsuptosupurl;
    String getsuptosuplinesurl;
    String getsuptosuplines2url;
    String getsuptobarcodeurl;
    String getsalesmanurl;
    String getshipviaurl;
    String gettransportationurl;
    String getetailcustomerurl;

    String last_sup_rv,last_meterial_rv,last_docseries_rv,last_suptosup_rv;
    String last_suptosuplines_rv,last_suptosuplines2_rv,last_suptobarcode_rv ;
    String last_salesman_rv,last_shipvia_rv,last_transportation_rv,last_retailcustomer_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Ενημέρωση του Mobile</small>"));

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("UpdateMobile");

        farmerscheck = (CheckBox) findViewById(R.id.farmerscheckBox);
        materiascheck = (CheckBox) findViewById(R.id.materialscheck);
        parameterscheck = (CheckBox) findViewById(R.id.parameterscheck);

        farmerscheck.setOnClickListener(this);
        materiascheck.setOnClickListener(this);
        parameterscheck.setOnClickListener(this);

        pg = (ProgressBar) findViewById(R.id.progressBar1);

    }

    @Override
    public void onClick(View view) {
        if (view == farmerscheck) {
            if (farmerscheck.isChecked()) {
                materiascheck.setChecked(false);
                parameterscheck.setChecked(false);
            }
            return;
        }
        if (view == materiascheck) {
            if (materiascheck.isChecked()) {
                farmerscheck.setChecked(false);
                parameterscheck.setChecked(false);
            }
            return;
        }

        if (view == parameterscheck) {
            if (parameterscheck.isChecked()) {
                farmerscheck.setChecked(false);
                materiascheck.setChecked(false);
            }
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return false;
        }
        else {
            return true;
        }
    }

    public void updatemobile(View view) {

        mydb = new DBHelper(this);

/*
        int dos=mydb.numberOfTableRows("docseries");
        int supl=mydb.numberOfTableRows("supplier");
        int supltobar=mydb.numberOfTableRows("milksuptobarcode");
        int supltosup=mydb.numberOfTableRows("milksuptosup");
        int suptos=mydb.numberOfTableRows("milksuptosuplines");
        int supltosu2=mydb.numberOfTableRows("milksuptosuplines2");
        int rcus=mydb.numberOfTableRows("retailcustomer");
        int rm=mydb.numberOfTableRows("retailmaterial");
        int rt=mydb.numberOfTableRows("route");
        int sm=mydb.numberOfTableRows("salesman");
        int sv=mydb.numberOfTableRows("shipvia");
        int tr=mydb.numberOfTableRows("transportation");
*/

        Cursor rs = mydb.getConfigData(1);

        rs.moveToFirst();

        if (rs.isAfterLast()) {

            if (!rs.isClosed()) {
                rs.close();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.confignotexists);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);

        } else {


            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnectionExist = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();


            if (!isConnectionExist) {

                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this);
                alertDialog1.setTitle(R.string.app_name);
                alertDialog1.setIcon(R.mipmap.ic_launcher);
                alertDialog1.setMessage("Η Συσκευή σας δεν έχει πρόσβαση στο Internet!");
                alertDialog1.setNegativeButton("OK", null);
                AlertDialog dialog1 = alertDialog1.show();
                TextView messageText1 = (TextView) dialog1.findViewById(android.R.id.message);
                messageText1.setGravity(Gravity.CENTER);

            } else {


                webserviceurl=rs.getString(rs.getColumnIndex("webserviceurl"));

                if (farmerscheck.isChecked()) {

                    last_sup_rv= "0";

                    if (rs.getString(rs.getColumnIndex("last_sup_rv")) != null) {
                        last_sup_rv=rs.getString(rs.getColumnIndex("last_sup_rv"));
                    }


                    getfarmersurl=webserviceurl + "?TABLE=SUPPLIER&RV=" + last_sup_rv;

                    AsyncCallWSGetFarmers task = new AsyncCallWSGetFarmers();
                    task.execute();

                }


                if (materiascheck.isChecked()) {

                    last_meterial_rv= "0";

                    if (rs.getString(rs.getColumnIndex("last_meterial_rv")) != null) {
                        last_meterial_rv=rs.getString(rs.getColumnIndex("last_meterial_rv"));
                    }

                    getmaterialsurl=webserviceurl + "?TABLE=RETAILMATERIAL&RV=" + last_meterial_rv;

                    AsyncCallWSGetMaterials task1 = new AsyncCallWSGetMaterials();

                    task1.execute();

                }

                if (parameterscheck.isChecked()) {

                    last_docseries_rv= "0";
                    last_suptosup_rv = "0";
                    last_suptosuplines_rv= "0";
                    last_suptosuplines2_rv= "0";
                    last_suptobarcode_rv = "0";
                    last_salesman_rv= "0";
                    last_shipvia_rv= "0";
                    last_transportation_rv = "0";
                    last_retailcustomer_rv = "0";

                    if (rs.getString(rs.getColumnIndex("last_docseries_rv")) != null) {
                        last_docseries_rv=rs.getString(rs.getColumnIndex("last_docseries_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_suptosup_rv")) != null) {
                        last_suptosup_rv=rs.getString(rs.getColumnIndex("last_suptosup_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_suptosuplines_rv")) != null) {
                        last_suptosuplines_rv=rs.getString(rs.getColumnIndex("last_suptosuplines_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_suptosuplines2_rv")) != null) {
                        last_suptosuplines2_rv=rs.getString(rs.getColumnIndex("last_suptosuplines2_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_suptobarcode_rv")) != null) {
                        last_suptobarcode_rv=rs.getString(rs.getColumnIndex("last_suptobarcode_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_salesman_rv")) != null) {
                        last_salesman_rv=rs.getString(rs.getColumnIndex("last_salesman_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_shipvia_rv")) != null) {
                        last_shipvia_rv=rs.getString(rs.getColumnIndex("last_shipvia_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_transportation_rv")) != null) {
                        last_transportation_rv=rs.getString(rs.getColumnIndex("last_transportation_rv"));
                    }
                    if (rs.getString(rs.getColumnIndex("last_retailcustomer_rv")) != null) {
                        last_retailcustomer_rv=rs.getString(rs.getColumnIndex("last_retailcustomer_rv"));
                    }


                    getdocseriessurl=webserviceurl + "?TABLE=DOCSERIES&RV=" + last_docseries_rv;
                    getetailcustomerurl=webserviceurl + "?TABLE=RETAILCUSTOMER&RV=" + last_retailcustomer_rv;
                    getsuptosupurl=webserviceurl + "?TABLE=MLK_SUPTOSUP&RV=" + last_suptosup_rv;
                    getsuptosuplinesurl=webserviceurl + "?TABLE=MLK_SUPTOSUPLINES&RV=" + last_suptosuplines_rv;
                    getsuptosuplines2url=webserviceurl + "?TABLE=MLK_SUPTOSUPLINES2&RV=" + last_suptosuplines2_rv;
                    getsuptobarcodeurl=webserviceurl + "?TABLE=MLK_SUPTOBARCODE&RV=" + last_suptobarcode_rv;
                    getsalesmanurl=webserviceurl + "?TABLE=SALESMAN&RV=" + last_salesman_rv;
                    getshipviaurl=webserviceurl + "?TABLE=SHIPVIA&RV=" + last_shipvia_rv;
                    gettransportationurl=webserviceurl + "?TABLE=TRANSPORTATION&RV=" + last_transportation_rv;


                    AsyncCallWSGetDocSeries task2 = new AsyncCallWSGetDocSeries();
                    task2.execute();

                }


            }
        }

    }

    private class AsyncCallWSGetFarmers extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                pg.setVisibility(View.VISIBLE);
            }


            @Override
            protected void onPostExecute(String json) {

                super.onPostExecute(json);

                if (json.equals("no connection")) {
                   pg.setVisibility(View.INVISIBLE);
                   Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                   return;
                }

                try {
                        JSONArray jsonArray = new JSONArray(json);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            String supid = obj.getString("ID");
                            String code=obj.getString("CODE");
                            String name=obj.getString("NAME");
                            String occupation=obj.getString("OCCUPATION");
                            String street1=obj.getString("STREET1");
                            String city1 = obj.getString("CITY1");
                            String phone11=obj.getString("PHONE11");
                            String identitynum=obj.getString("IDENTITYNUM");
                            String afm=obj.getString("AFM");
                            String doyid=obj.getString("DOYID");
                            String fpastatus=obj.getString("FPASTATUS");
                            String fldid1 = obj.getString("FLDID1");
                            String isvalid=obj.getString("ISVALID");
                            String rv=obj.getString("RV");
                            String z_seira=obj.getString("Z_SEIRA");
                            String vendorwerks=obj.getString("VENDORWERKS");
                            String remarks=obj.getString("REMARKS");
                            String shvid=obj.getString("SHVID");
                            String z_pagolekaniid2=obj.getString("ZPAGOLEKANIID2");

                             if (mydb.existsSupplier(supid)) {

                                  if (mydb.updateSuppliers(supid, code, name, occupation, street1, city1, phone11,
                                          identitynum, afm, doyid, fpastatus, fldid1, isvalid, rv, z_seira, vendorwerks, remarks, shvid, z_pagolekaniid2)) {

                                      last_sup_rv = rv;

                                  } else {

                                      pg.setVisibility(View.INVISIBLE);
                                      Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών!!", Toast.LENGTH_SHORT).show();
                                      return;

                                  }

                             }
                             else
                             {
                                  if (mydb.insertSuppliers(supid, code, name, occupation, street1, city1, phone11,
                                          identitynum, afm, doyid, fpastatus, fldid1, isvalid, rv, z_seira, vendorwerks, remarks, shvid, z_pagolekaniid2)) {

                                      last_sup_rv = rv;

                                  } else {

                                      pg.setVisibility(View.INVISIBLE);
                                      Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών!!", Toast.LENGTH_SHORT).show();
                                      return;

                                  }

                             }
                        }

                    pg.setVisibility(View.INVISIBLE);

                    if (mydb.updateLastSupplierRv(1,last_sup_rv)) {
                        Toast.makeText(getApplicationContext(), "Επιτυχής Ενημέρωση Παραγωγών", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    pg.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών!!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(getfarmersurl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(300000);
                    con.setReadTimeout(300000);
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"),40950);
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    //pg.setVisibility(View.INVISIBLE);
                    return "no connection";
                }
            }
        }

    private class AsyncCallWSGetMaterials extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String iteid = obj.getString("ID");
                    String code=obj.getString("CODE");
                    String descr=obj.getString("DESCR");
                    String descr2=obj.getString("DESCR2");
                    String price=obj.getString("PRICE");
                    String fprice = obj.getString("FPRICE");
                    String whsprice=obj.getString("WHSPRICE");
                    String fwhsprice=obj.getString("FWHSPRICE");
                    String vtcid=obj.getString("VTCID");
                    String mu=obj.getString("MU");
                    String su=obj.getString("SU");
                    String mu2_1 = obj.getString("MU2_1");
                    String mu1_2mode=obj.getString("MU1_2MODE");
                    String ictid=obj.getString("ICTID");
                    String igpid=obj.getString("IGPID");
                    String igsid=obj.getString("IGSID");
                    String igtid=obj.getString("IGTID");
                    String mpcid=obj.getString("MPCID");
                    String mainszlid="";
                    String recomqty=obj.getString("RECOMQTY");
                    String isoffer=obj.getString("ISOFFER");
                    String glmid=obj.getString("GLMID");
                    String efkax=obj.getString("EFKTAX");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");

                    if (mydb.existsMaterial(iteid)) {

                        if (mydb.updateMaterials(iteid, code, descr, descr2, price, fprice, whsprice,
                                fwhsprice, vtcid, mu, su, mu2_1, mu1_2mode, ictid, igpid, igsid, igtid,
                                mpcid, mainszlid,recomqty, isoffer,glmid, efkax,isvalid, rv)) {

                            last_meterial_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Ειδών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertMaterials(iteid, code, descr, descr2, price, fprice, whsprice,
                                fwhsprice, vtcid, mu, su, mu2_1, mu1_2mode, ictid, igpid, igsid, igtid,
                                mpcid, mainszlid,recomqty, isoffer,glmid, efkax,isvalid, rv)) {

                            last_meterial_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Ειδών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastMaterialRv(1,last_meterial_rv)) {
                    Toast.makeText(getApplicationContext(), "Επιτυχής Ενημέρωση Ειδών", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Ειδών!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getmaterialsurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetDocSeries extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);


                    String domaintype = obj.getString("DOMAINTYPE");
                    String codeid=obj.getString("CODEID");
                    String docseries=obj.getString("DOCSERIES");
                    String dockind=obj.getString("DOCKIND");
                    String docseriesdescr=obj.getString("DOCSERIESDESCR");
                    String braid = obj.getString("BRAID");
                    String ord=obj.getString("ORD");
                    String mode=obj.getString("MODE");
                    String qmode=obj.getString("QMODE");
                    String nextdocnum=obj.getString("NEXTDOCNUM");
                    String cancelseries=obj.getString("CANCELSERIES");
                    String defaultcustomercode = obj.getString("DEFAULTCUSTOMERCODE");
                    String paymentdsrid=obj.getString("PAYMENTDSRID");
                    String interfacetype=obj.getString("INTERFACETYPE");
                    String printername=obj.getString("PRINTERNAME");
                    String formfilename=obj.getString("FORMFILENAME");
                    String printer_encoding=obj.getString("PRINTER_ENCODING");
                    String pricefld=obj.getString("PRICEFLD");
                    String flags=obj.getString("FLAGS");
                    String copies=obj.getString("COPIES");
                    String surid=obj.getString("SURID");
                    String numgroup=obj.getString("NUMGROUP");
                    String pkid=obj.getString("PKDID");
                    String ptrid=obj.getString("PTRID");
                    String rotid=obj.getString("ROTID");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");
                    String policies=obj.getString("POLICIES");
                    String copycodeid=obj.getString("COPYCODEID");
                    String trsid=obj.getString("TRSID");
                    String activeretail=obj.getString("ACTIVERETAIL");
                    String stoid=obj.getString("STOID");
                    String shcid=obj.getString("SHCID");
                    String shvid=obj.getString("SHVID");
                    String domaintypeinsert=obj.getString("DOMAINTYPEINSERT");
                    String clientgroup=obj.getString("CLIENTGROUP");
                    String taxescode=obj.getString("TAXESCODE");


                    if (mydb.existsDocseries(domaintype,codeid,docseries)) {

                        if (mydb.updateDocseries(domaintype, codeid, docseries, dockind, docseriesdescr, braid, ord,mode, qmode, nextdocnum, cancelseries,
                                defaultcustomercode, paymentdsrid, interfacetype, printername, formfilename, printer_encoding, pricefld, flags,copies, surid,
                                numgroup, pkid,ptrid, rotid,isvalid,rv,policies,copycodeid,trsid,activeretail,stoid,shcid,shvid,domaintypeinsert,clientgroup,taxescode)) {

                            last_docseries_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Σειρών Παραστατικών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertDocseries(domaintype, codeid, docseries, dockind, docseriesdescr, braid, ord,mode, qmode, nextdocnum, cancelseries,
                                defaultcustomercode, paymentdsrid, interfacetype, printername, formfilename, printer_encoding, pricefld, flags,copies, surid,
                                numgroup, pkid,ptrid, rotid,isvalid,rv,policies,copycodeid,trsid,activeretail,stoid,shcid,shvid,domaintypeinsert,clientgroup,taxescode)) {

                          last_docseries_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Σειρών Παραστατικών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastDocseriesRv(1,last_docseries_rv)) {

                    AsyncCallWSGetCustomers task31 = new AsyncCallWSGetCustomers();
                    task31.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Σειρών Παραστατικών!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getdocseriessurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetCustomers extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String rcustid = obj.getString("ID");
                    String code=obj.getString("CODE");
                    String name=obj.getString("NAME");
                    String occupation=obj.getString("OCCUPATION");
                    String street1=obj.getString("STREET1");
                    String city1=obj.getString("CITY1");
                    String zipcode1=obj.getString("ZIPCODE1");
                    String phone11=obj.getString("PHONE11");
                    String phone12 = obj.getString("PHONE12");
                    String fax1=obj.getString("FAX1");

                    String telex1=obj.getString("TELEX1");
                    String district1=obj.getString("DISTRICT1");
                    String street2=obj.getString("STREET2");
                    String city2=obj.getString("CITY2");
                    String zipcode2=obj.getString("ZIPCODE2");
                    String phone21=obj.getString("PHONE21");
                    String phone22=obj.getString("PHONE22");
                    String fax2=obj.getString("FAX2");
                    String telex2=obj.getString("TELEX2");
                    String district2=obj.getString("DISTRICT2");

                    String identitynum=obj.getString("IDENTITYNUM");
                    String afm=obj.getString("AFM");
                    String doyid=obj.getString("DOYID");
                    String doy=obj.getString("DOY");
                    String difaultdiscount=obj.getString("DEFAULTDISCOUNT");
                    String shvid=obj.getString("SHVID");
                    String npgid=obj.getString("NPGID");
                    String prcid=obj.getString("PRCID");
                    String ocpid=obj.getString("OCPID");
                    String grpid=obj.getString("GRPID");

                    String fpastatus=obj.getString("FPASTATUS");
                    String fldid1=obj.getString("FLDID1");
                    String cntid=obj.getString("CNTID");
                    String carid=obj.getString("CARID");
                    String rotid=obj.getString("ROTID");
                    String delivman=obj.getString("DELIVMAN");
                    String ptrid=obj.getString("PTRID");
                    String colidsalesman=obj.getString("COLIDSALESMAN");
                    String manager=obj.getString("MANAGER");
                    String acccrdlimit=obj.getString("ACCCRDLIMIT");

                    String pos=obj.getString("POS");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");



                    if (mydb.existsCustomer(rcustid)) {

                        if (mydb.updateCustomers(rcustid, code, name, occupation, street1, city1, zipcode1,phone11, phone12, fax1, telex1,
                                district1, street2, city2, zipcode2, phone21, phone22, fax2, telex2,district2, identitynum,
                                afm, doyid,doy, difaultdiscount,shvid,npgid,prcid,ocpid,grpid,fpastatus,fldid1,cntid,carid,rotid,delivman,
                                ptrid,colidsalesman,manager,acccrdlimit,pos,isvalid,rv)) {

                            last_retailcustomer_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Πελατών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertCustomers(rcustid, code, name, occupation, street1, city1, zipcode1,phone11, phone12, fax1, telex1,
                                district1, street2, city2, zipcode2, phone21, phone22, fax2, telex2,district2, identitynum,
                                afm, doyid,doy, difaultdiscount,shvid,npgid,prcid,ocpid,grpid,fpastatus,fldid1,cntid,carid,rotid,delivman,
                                ptrid,colidsalesman,manager,acccrdlimit,pos,isvalid,rv)) {

                            last_retailcustomer_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Πελατών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastCustomersRv(1,last_retailcustomer_rv)) {

                    AsyncCallWSGetSupToSup task3 = new AsyncCallWSGetSupToSup();
                    task3.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Πελατών!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getetailcustomerurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetSupToSup extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String zstsid = obj.getString("ID");
                    String comid=obj.getString("COMID");
                    String supid=obj.getString("SUPID");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsSupToSup(zstsid,comid)) {

                        if (mydb.updateSupToSup(zstsid, comid, supid, isvalid, rv)) {


                            last_suptosup_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertSupToSup(zstsid, comid, supid, isvalid, rv)) {

                            last_suptosup_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastSupToSupRv(1,last_suptosup_rv)) {

                    AsyncCallWSGetSupToSupLines task4 = new AsyncCallWSGetSupToSupLines();
                    task4.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραληπτηρίων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getsuptosupurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetSupToSupLines extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);



                    String zstslinesid = obj.getString("ID");
                    String comid=obj.getString("COMID");
                    String zstsid=obj.getString("ZSTSID");
                    String supid=obj.getString("SUPID");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsSupToSupLines(zstslinesid,comid)) {

                        if (mydb.updateSupToSupLines(zstslinesid, zstsid, comid, supid, isvalid, rv)) {

                            last_suptosuplines_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertSupToSupLines(zstslinesid, zstsid, comid, supid, isvalid, rv)) {

                            last_suptosuplines_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastSupToSupLinesRv(1,last_suptosuplines_rv)) {

                    AsyncCallWSGetSupToSupLines2 task5 = new AsyncCallWSGetSupToSupLines2();
                    task5.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραγωγών Παραληπτηρίων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getsuptosuplinesurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetSupToSupLines2 extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);



                    String zstslines2id = obj.getString("ID");
                    String comid=obj.getString("COMID");
                    String zstsid=obj.getString("ZSTSID");
                    String iteid=obj.getString("ITEID");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsSupToSupLines2(zstslines2id,comid)) {

                        if (mydb.updateSupToSupLines2(zstslines2id, zstsid, comid, iteid, isvalid, rv)) {

                            last_suptosuplines2_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Προιόντων Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertSupToSupLines2(zstslines2id, zstsid, comid, iteid, isvalid, rv)) {

                            last_suptosuplines2_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Προιόντων Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastSupToSupLines2Rv(1,last_suptosuplines2_rv)) {

                    AsyncCallWSGetSupToBarcode task6 = new AsyncCallWSGetSupToBarcode();
                    task6.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Προιόντων Παραληπτηρίων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getsuptosuplines2url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetSupToBarcode extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);


                    String zstsid=obj.getString("ZSTSID");
                    String zpagolekaniid2=obj.getString("ZPAGOLEKANIID2");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsSupToBarcode(zstsid)) {

                        if (mydb.updateSupToSupBarcode(zpagolekaniid2, zstsid, isvalid, rv)) {

                            last_suptobarcode_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Barcode Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertSupToSupBarcode(zpagolekaniid2, zstsid, isvalid, rv)) {

                            last_suptobarcode_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Barcode Παραληπτηρίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastSupToBarcodeRv(1,last_suptobarcode_rv)) {

                    AsyncCallWSGetSalesman task7 = new AsyncCallWSGetSalesman();
                    task7.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Barcode Παραληπτηρίων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getsuptobarcodeurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetSalesman extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);


                    String salsmid=obj.getString("ID");
                    String code=obj.getString("CODE");
                    String name=obj.getString("NAME");
                    String braid=obj.getString("BRAID");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsSalesman(salsmid)) {

                        if (mydb.updateSalesman(salsmid, code, name, braid, isvalid, rv)) {

                            last_salesman_rv = rv;



                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αγοραστών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertSalesman(salsmid, code, name, braid, isvalid, rv)) {

                            last_salesman_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αγοραστών!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastSalesmanRv(1,last_salesman_rv)) {

                    AsyncCallWSGetShipVia task8 = new AsyncCallWSGetShipVia();
                    task8.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αγοραστών!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getsalesmanurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetShipVia extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String codeid=obj.getString("CODEID");
                    String descr=obj.getString("DESCR");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsShipvia(codeid)) {

                        if (mydb.updateShipvia(codeid, descr, isvalid, rv)) {

                            last_shipvia_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Δρομολογίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertShipvia(codeid, descr, isvalid, rv)) {

                            last_shipvia_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Δρομολογίων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastShipviaRv(1,last_shipvia_rv)) {

                    AsyncCallWSGetTransportation task9 = new AsyncCallWSGetTransportation();
                    task9.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Δρομολογίων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(getshipviaurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }

    private class AsyncCallWSGetTransportation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json.equals("no connection")) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Σύνδεση με το Κεντρικό!!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String codeid=obj.getString("CODEID");
                    String code=obj.getString("CODE");
                    String descr=obj.getString("DESCR");
                    String isvalid=obj.getString("ISVALID");
                    String rv=obj.getString("RV");


                    if (mydb.existsTransportation(codeid)) {

                        if (mydb.updateTransportation(codeid, "panagos", descr, isvalid, rv)) {

                            last_transportation_rv = rv;


                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αυτοκινήτων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    else
                    {
                        if (mydb.insertTransportation(codeid, code, descr, isvalid, rv)) {

                            last_transportation_rv = rv;

                        } else {

                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αυτοκινήτων!!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                }

                pg.setVisibility(View.INVISIBLE);

                if (mydb.updateLastTransportationRv(1,last_transportation_rv)) {
                    Toast.makeText(getApplicationContext(), "Επιτυχής Ενημέρωση Παραμέτρων!!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση Ρυθμίσεων!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Αυτοκινήτων!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(gettransportationurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                //pg.setVisibility(View.INVISIBLE);
                return "no connection";
            }
        }
    }
}


