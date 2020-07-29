package android.panos.ram.rammilkcollection.models;

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

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import javax.net.ssl.HttpsURLConnection;

public class UploadTrades extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "android.panos.ram.rammilkcollection.receiver";
    private DBHelper mydb;
    String updatedtrades;

    public UploadTrades() {
        super("UploadTrades");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {

        String webserviceurl="";
        String posttradesurl="";
        String deviceId="";
        String checkuserurl="";

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mydb = new DBHelper(this);

        Cursor rs = mydb.getConfigData(1);


        rs.moveToFirst();

        webserviceurl=rs.getString(rs.getColumnIndex("webserviceurl"));
        posttradesurl=webserviceurl + "?TRADES";
        checkuserurl=webserviceurl + "?checklic=" + rs.getString(rs.getColumnIndex("clientid")) + "&CKEY=" + deviceId ;

        if (!rs.isClosed()) {
            rs.close();
        }



        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnectionExist = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnectionExist) {

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

                if (!response.equals("OK")) {

                    result = Activity.RESULT_FIRST_USER;
                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

            }

        }


        if (isConnectionExist && result != Activity.RESULT_FIRST_USER) {


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

                String response = "";

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();

                    response = sb.toString().trim();
                }

                if (response.equals("OK")) {

                    updatedtrades=updatedtrades.substring(0,updatedtrades.length()-1);

                    mydb = new DBHelper(this);

                    if (mydb.UpdateUploadedTrades(updatedtrades)) {
                        result = Activity.RESULT_OK;
                    }


                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

            }

        }


        publishResults(result);

    }


    protected String encodeParams(){

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String todaydate=dateFormatter.format(newCalendar.getTime());

        StringBuilder resultsb = new StringBuilder();

        String tradesdatastring="";

        updatedtrades="";

        mydb = new DBHelper(this);

        Cursor trrs = mydb.getNotUpdatedTradesData(todaydate);


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
                updatedtrades=updatedtrades + id + ",";

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

        resultsb.append(tradesdatastring);

        return resultsb.toString();
    }


    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}


