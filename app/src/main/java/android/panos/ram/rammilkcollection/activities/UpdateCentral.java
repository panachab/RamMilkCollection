package android.panos.ram.rammilkcollection.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.provider.Settings;
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

import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.DBHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

//added for git test again and again
public class UpdateCentral extends AppCompatActivity implements View.OnClickListener {

    private DBHelper mydb;

    CheckBox tradescheckBox;

    ProgressBar pg;

    String webserviceurl;
    String posttradesurl;
    String deviceId="";
    String clientId="";
    String checkuserurl;
    Boolean permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        setContentView(R.layout.activity_update_central);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Ενημέρωση του Κεντρικού</small>"));

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("UpdateCentral");

        /*tradescheckBox = (CheckBox) findViewById(R.id.tradescheckBox);

        pg = (ProgressBar) findViewById(R.id.progressBar1);

         tradescheckBox.setOnClickListener(this); */


    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
           // onBackPressed();
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

    public void updatecentral(View view) {

        mydb = new DBHelper(this);

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


            webserviceurl=rs.getString(rs.getColumnIndex("webserviceurl"));
            clientId=rs.getString(rs.getColumnIndex("clientid"));

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnectionExist = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


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
                clientId=rs.getString(rs.getColumnIndex("clientid"));

                if (!rs.isClosed()) {
                    rs.close();
                }


                if (tradescheckBox.isChecked()) {

                    posttradesurl=webserviceurl + "?TRADES";
                    checkuserurl=webserviceurl + "?checklic=" + clientId + "&CKEY=" + deviceId ;

                    AsyncCallWSPostTrades task = new AsyncCallWSPostTrades();
                    task.execute();

                }

            }
        }

    }

    private class AsyncCallWSPostTrades extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String response) {

            super.onPostExecute(response);

            pg.setVisibility(View.INVISIBLE);

            if (permission) {

                if (response.equals("OK")) {
                    Toast.makeText(getApplicationContext(), "Επιτυχής Ενημέρωση των Παραστατικών!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραστατικών!!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Δεν έχετε δικαίωμα Αποστολής Παραστατικών στο Κεντρικό!Επικοινωνήστε με τον Διαχειριστή της Εφαρμογής!!", Toast.LENGTH_LONG).show();

            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            permission = false;

            try {


                URL url = new URL(checkuserurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(20000);
                con.setConnectTimeout(20000);

                StringBuilder sb = new StringBuilder();

                int responseCode = con.getResponseCode();

                String response = "";


                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "ISO-8859-7"), 40950);

                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    bufferedReader.close();

                    response = sb.toString().trim();

                }

                if (response.equals("OK")) {
                    permission = true;
                }

            } catch (Exception e) {

                pg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραστατικών!!", Toast.LENGTH_SHORT).show();
                return null;

            }

             if (permission) {

                 try {


                     URL url = new URL(posttradesurl);
                     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                     conn.setReadTimeout(20000);
                     conn.setConnectTimeout(20000);
                     conn.setRequestMethod("POST");
                     conn.setDoInput(true);
                     conn.setDoOutput(true);

                     OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "ISO-8859-7"));
                     writer.write(encodeParams());
                     writer.flush();
                     writer.close();
                     os.close();

                     int responseCode = conn.getResponseCode();

                     if (responseCode == HttpsURLConnection.HTTP_OK) {

                         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                         StringBuffer sb = new StringBuffer("");
                         String line = "";
                         while ((line = in.readLine()) != null) {
                             sb.append(line);
                             break;
                         }
                         in.close();
                         return sb.toString().trim();
                     }

                     return null;

                     } catch (Exception e) {
                         pg.setVisibility(View.INVISIBLE);
                         Toast.makeText(getApplicationContext(), "Ανεπιτυχής Ενημέρωση των Παραστατικών!!", Toast.LENGTH_SHORT).show();
                         return null;
                     }

                 }

             else
             {
                 return null;
             }


        }

    }

    protected String encodeParams(){

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String todaydate=dateFormatter.format(newCalendar.getTime());

        StringBuilder result = new StringBuilder();

        String tradesdatastring="";

        mydb = new DBHelper(this);

        Cursor trrs = mydb.getTradesData(todaydate);


        trrs.moveToFirst();

        if (trrs.isAfterLast()) {

            if (!trrs.isClosed()) {
                trrs.close();
            }


        } else {

            tradesdatastring=tradesdatastring + "{"+ "\n";
            tradesdatastring=tradesdatastring + "\"TRADE\":" + "\n";
            tradesdatastring=tradesdatastring + "["+ "\n";

            do {


                String id = trrs.getString(trrs.getColumnIndex("id"));
                String cid = trrs.getString(trrs.getColumnIndex("cid"));
                String domaintype = trrs.getString(trrs.getColumnIndex("domaintype"));
                String tradecode = trrs.getString(trrs.getColumnIndex("tradecode"));
                String docseries = trrs.getString(trrs.getColumnIndex("docseries"));
                String docnum = trrs.getString(trrs.getColumnIndex("docnum"));
                String supid = trrs.getString(trrs.getColumnIndex("supid"));
                String shvid = trrs.getString(trrs.getColumnIndex("shvid"));
                String xlm = trrs.getString(trrs.getColumnIndex("xlm"));
                String trsid = trrs.getString(trrs.getColumnIndex("trsid"));
                String salesmanid = trrs.getString(trrs.getColumnIndex("salesmanid"));
                String salesmancode = trrs.getString(trrs.getColumnIndex("salesmancode"));
                String grhmer = trrs.getString(trrs.getColumnIndex("hmer"));
                String trtime = trrs.getString(trrs.getColumnIndex("time"));
                String zpagolekaniid2 = trrs.getString(trrs.getColumnIndex("zpagolekaniid2"));
                String dromologionum = trrs.getString(trrs.getColumnIndex("dromologionum"));
                String zfat = trrs.getString(trrs.getColumnIndex("zfat"));
                String ztemperature = trrs.getString(trrs.getColumnIndex("ztemperature"));
                String zph = trrs.getString(trrs.getColumnIndex("zph"));
                String notes = trrs.getString(trrs.getColumnIndex("notes"));
                String ddafrom = trrs.getString(trrs.getColumnIndex("daafrom"));
                String ddato = trrs.getString(trrs.getColumnIndex("daato"));
                String fromkeb = trrs.getString(trrs.getColumnIndex("fromkeb"));
                String tokeb = trrs.getString(trrs.getColumnIndex("fromkeb"));
                String secjustificationfrom = trrs.getString(trrs.getColumnIndex("secjustificationfrom"));


                String fromtradecode = trrs.getString(trrs.getColumnIndex("fromtradecode"));
                String uptotradecode = trrs.getString(trrs.getColumnIndex("uptotradecode"));

                String isdiakinisi=trrs.getString(trrs.getColumnIndex("isdiakinisi"));
                String stoid=trrs.getString(trrs.getColumnIndex("stoid"));
                String secstoid=trrs.getString(trrs.getColumnIndex("secstoid"));
                String custid=trrs.getString(trrs.getColumnIndex("custid"));

                String frtrc="";
                String uttrc="";

                if (fromtradecode != null && !fromtradecode.equals("") ) {
                    String frmtrcode[] = fromtradecode.split("-");
                    frtrc=frmtrcode[1];
                }
                if (uptotradecode != null && !uptotradecode.equals("")) {
                    String upttrcode[] = uptotradecode.split("-");
                    uttrc=upttrcode[1];
                }

                String ddafr="0";
                String ddat="0";

                if (ddafrom != null && ddafrom != "" ) {
                    ddafr = ddafrom;
                }
                if (ddato != null && ddato !="" ) {
                    ddat = ddato;
                }


                String datearray[] = grhmer.split("/");
                String hmer = datearray[2] + "-" + datearray[1] +  "-" + datearray[0];
                String xrisi = datearray[2];

                tradesdatastring=tradesdatastring + "{"+ "\n";
                tradesdatastring=tradesdatastring + "\"ID\":" + id + ",\n";
                tradesdatastring=tradesdatastring + "\"CID\":" + cid + ",\n";
                tradesdatastring=tradesdatastring + "\"DOMAINTYPE\":" + domaintype + ",\n";
                tradesdatastring=tradesdatastring + "\"TRADECODE\":\"" + tradecode + "\",\n";
                tradesdatastring=tradesdatastring + "\"DOCSERIES\":\"" + docseries + "\",\n";
                tradesdatastring=tradesdatastring + "\"DOCNUM\":" + docnum + ",\n";
                tradesdatastring=tradesdatastring + "\"XRISI\":" + xrisi + ",\n";
                tradesdatastring=tradesdatastring + "\"SUPID\":" + supid + ",\n";

                if (shvid != "" ) {
                    tradesdatastring=tradesdatastring + "\"SHVID\":" + shvid + ",\n";
                }

                if (xlm!=null && !xlm.equals("")) {
                    tradesdatastring = tradesdatastring + "\"XLM\":" + xlm + ",\n";
                }

                tradesdatastring=tradesdatastring + "\"TRSID\":" + trsid + ",\n";
                tradesdatastring=tradesdatastring + "\"SALESMANID\":" + salesmanid + ",\n";
                tradesdatastring=tradesdatastring + "\"SALESMANCODE\":\"" + salesmancode + "\",\n";
                tradesdatastring=tradesdatastring + "\"DROMOLOGIONUM\":" + dromologionum + ",\n";

                if (isdiakinisi.equals("1")) {
                    tradesdatastring=tradesdatastring + "\"STOID\":" + stoid + ",\n";
                    tradesdatastring=tradesdatastring + "\"SECSTOID\":" + secstoid + ",\n";
                    tradesdatastring=tradesdatastring + "\"CUSTID\":" + custid + ",\n";
                }

                if (!isdiakinisi.equals("1")) {

                    if (secstoid!=null && !secstoid.equals("")) {
                        tradesdatastring = tradesdatastring + "\"SECSTOID\":" + secstoid + ",\n";
                    }

                    if (custid!=null && !custid.equals("")) {
                        tradesdatastring = tradesdatastring + "\"CUSTID\":" + custid + ",\n";
                    }

                }

                if (zfat!=null && !zfat.equals("")) {
                    tradesdatastring = tradesdatastring + "\"ZFAT\":" + zfat + ",\n";
                }

                if (ztemperature!=null && !ztemperature.equals("")) {
                    tradesdatastring=tradesdatastring + "\"ZTEMPERATURE\":" + ztemperature + ",\n";
                }


                if (zph!=null && !zph.equals("")) {
                    tradesdatastring=tradesdatastring + "\"ZPH\":" + zph + ",\n";
                }

                if (notes!=null && !notes.equals("")) {
                    tradesdatastring = tradesdatastring +"\"NOTES\":\"" + notes + "\",\n";
                }

                if (!frtrc.equals("")) {
                    tradesdatastring=tradesdatastring + "\"DABFROM\":" + frtrc + ",\n";
                }

                if (!uttrc.equals("")) {
                    tradesdatastring=tradesdatastring + "\"DABTO\":" + uttrc + ",\n";
                }

                if (!ddafr.equals("0")) {
                    tradesdatastring = tradesdatastring + "\"DAAFROM\":" + ddafr + ",\n";
                }

                if (!ddafr.equals("0")) {
                    tradesdatastring = tradesdatastring + "\"DAATO\":" + ddat + ",\n";
                }

                if (zpagolekaniid2!=null && !zpagolekaniid2.equals("")) {
                    tradesdatastring=tradesdatastring + "\"ZPAGOLEKANIID2\":\"" + zpagolekaniid2 + "\",\n";
                }

                if (fromkeb!=null && !fromkeb.equals("")) {
                    tradesdatastring = tradesdatastring + "\"FROMKEB\":\"" + fromkeb + "\",\n";
                }
                if (tokeb!=null && !tokeb.equals("")) {
                    tradesdatastring = tradesdatastring + "\"TOKEB\":\"" + tokeb + "\",\n";
                }

                if (secjustificationfrom!=null && !secjustificationfrom.equals("")) {
                    tradesdatastring = tradesdatastring + "\"SECJUSTIFICATION\":\"" + secjustificationfrom + "\",\n";
                }



                tradesdatastring=tradesdatastring + "\"HMER\":\"" + hmer +" "+ trtime + "\",\n";


                tradesdatastring=tradesdatastring + "},"+ "\n";

            } while (trrs.moveToNext());

            if (!trrs.isClosed()) {
                trrs.close();
            }


            tradesdatastring=tradesdatastring.substring(0,tradesdatastring.length()-2);

            tradesdatastring=tradesdatastring + "\n" + "]"+ "\n";

            Cursor trlrs = mydb.getTradeLinesData(todaydate);

            trlrs.moveToFirst();

            if (trlrs.isAfterLast()) {

                if (!trlrs.isClosed()) {
                    trlrs.close();
                }


            } else {

                tradesdatastring = tradesdatastring + "," + "\n";
                tradesdatastring = tradesdatastring + "\"TRADELINES\":" + "\n";
                tradesdatastring = tradesdatastring + "[" + "\n";

                do {

                    String trid = trlrs.getString(trlrs.getColumnIndex("trid"));
                    String cid = trlrs.getString(trlrs.getColumnIndex("cid"));
                    String aa = trlrs.getString(trlrs.getColumnIndex("aa"));
                    String iteid = trlrs.getString(trlrs.getColumnIndex("iteid"));
                    String qty = trlrs.getString(trlrs.getColumnIndex("qty"));
                    String z_autonum = trlrs.getString(trlrs.getColumnIndex("z_autonum"));
                    String z_wpid = trlrs.getString(trlrs.getColumnIndex("z_wpid"));
                    String fromzwpid = trlrs.getString(trlrs.getColumnIndex("fromzwpid"));

                    tradesdatastring = tradesdatastring + "{" + "\n";
                    tradesdatastring = tradesdatastring + "\"TRADEID\":" + trid + ",\n";
                    tradesdatastring = tradesdatastring + "\"AA\":" + aa + ",\n";
                    tradesdatastring = tradesdatastring + "\"CID\":" + cid + ",\n";
                    tradesdatastring = tradesdatastring + "\"ITEID\":" + iteid + ",\n";
                    tradesdatastring = tradesdatastring + "\"QTY\":" + qty + ",\n";
                    tradesdatastring = tradesdatastring + "\"Z_AUTONUM\":" + z_autonum + ",\n";
                    tradesdatastring = tradesdatastring + "\"Z_WPID\":" + z_wpid + ",\n";

                    if (fromzwpid!=null && !fromzwpid.equals("")) {
                        tradesdatastring = tradesdatastring + "\"FROMZWPID\":" + fromzwpid + ",\n";
                    }

                    tradesdatastring = tradesdatastring + "}," + "\n";

                } while (trlrs.moveToNext());

                if (!trlrs.isClosed()) {
                    trlrs.close();
                }

                tradesdatastring=tradesdatastring.substring(0, tradesdatastring.length() - 2);

                tradesdatastring=tradesdatastring + "\n" + "]"+ "\n";

            }

            tradesdatastring=tradesdatastring + "}"+ "\n";


        }

        result.append(tradesdatastring);

        return result.toString();
    }

}



