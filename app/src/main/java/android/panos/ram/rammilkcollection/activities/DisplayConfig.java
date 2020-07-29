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
import android.widget.EditText;
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

import javax.net.ssl.HttpsURLConnection;


public class DisplayConfig extends AppCompatActivity {

    private DBHelper mydb ;

    EditText clientid ;
    EditText ventorwerks ;
    EditText webserviceurl;
    EditText dsr0code;
    EditText dsr1code ;
    EditText checkdsrcode;
    EditText diakinisidsrcode;
    EditText metaggisidsrcode;
    EditText checkmetaggisidsrcode;
    EditText dsr0num;
    EditText dsr1num;
    EditText checkdsrnum;
    EditText diakinisidsrnum;
    EditText metaggisidsrnum;
    EditText checkmetaggisidsrnum;
    EditText printer;

    Boolean missingidorurl=false;
    String deviceId ="";
    String  webserv="";
    String chkmessage="";

    int id_To_Display = 1;
    int UpdateConfig = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Ρυθμίσεις</small>"));

        clientid = (EditText) findViewById(R.id.editClientId);
        ventorwerks = (EditText) findViewById(R.id.editVentorWerks);
        webserviceurl = (EditText) findViewById(R.id.editWebServiceUrl);
        dsr0code = (EditText) findViewById(R.id.editDsr0Code);
        dsr1code = (EditText) findViewById(R.id.editDsr1Code);
        checkdsrcode = (EditText) findViewById(R.id.editCheckDsrCode);
        diakinisidsrcode = (EditText) findViewById(R.id.editDiakinisiDsrCode);
        metaggisidsrcode = (EditText) findViewById(R.id.editMetaggisiDsrCode);
        checkmetaggisidsrcode = (EditText) findViewById(R.id.editCheckMetaggisiDsrCode);
        dsr0num = (EditText) findViewById(R.id.editDsr0Num);
        dsr1num = (EditText) findViewById(R.id.editDsr1Num);
        checkdsrnum = (EditText) findViewById(R.id.editCheckDsrNum);
        diakinisidsrnum = (EditText) findViewById(R.id.editDiakinisiDsrNum);
        metaggisidsrnum = (EditText) findViewById(R.id.editMetaggisiDsrNum);
        checkmetaggisidsrnum = (EditText) findViewById(R.id.editCheckMetaggisiDsrNum);

        printer = (EditText) findViewById(R.id.editPrinter);

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("Config");

        deviceId= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mydb = new DBHelper(this);

        Cursor rs = mydb.getConfigData(id_To_Display);

        rs.moveToFirst();

        String ventwerks ;
        String client ;
        String webservice;
        String dsr0cod;
        String dsr1cod;
        String checkdsrcod;
        String diakinisidsrcod;
        String checkmetaggisidsrcod;
        String metaggisidsrcod;
        String dsr0nu;
        String dsr1nu;
        String checkdsrnu;
        String diakinisidsrnu;
        String checkmetaggisidsrnu;
        String metaggisidsrnu;


        String printr;

        if (!rs.isAfterLast()) {


            client=rs.getString(rs.getColumnIndex("clientid"));
            ventwerks=rs.getString(rs.getColumnIndex("vendorwerks"));
            webservice = rs.getString(rs.getColumnIndex("webserviceurl"));
            dsr0cod = rs.getString(rs.getColumnIndex("dsr0code"));
            dsr1cod=rs.getString(rs.getColumnIndex("dsr1code"));
            checkdsrcod = rs.getString(rs.getColumnIndex("checkdsrcode"));
            diakinisidsrcod = rs.getString(rs.getColumnIndex("diakinisidsrcode"));
            metaggisidsrcod = rs.getString(rs.getColumnIndex("metaggisidsrcode"));
            checkmetaggisidsrcod = rs.getString(rs.getColumnIndex("checkmetaggisidsrcode"));
            dsr0nu = rs.getString(rs.getColumnIndex("dsr0num"));
            dsr1nu = rs.getString(rs.getColumnIndex("dsr1num"));
            checkdsrnu = rs.getString(rs.getColumnIndex("checkdsrnum"));
            diakinisidsrnu = rs.getString(rs.getColumnIndex("diakinisidsrnum"));
            metaggisidsrnu = rs.getString(rs.getColumnIndex("metaggisidsrnum"));
            checkmetaggisidsrnu = rs.getString(rs.getColumnIndex("checkmetaggisidsrnum"));
            printr = rs.getString(rs.getColumnIndex("printer"));

            UpdateConfig=1;
        }

            else{

            client = "";
            ventwerks = "";
            webservice = "";
            dsr0cod = "";
            dsr1cod = "";
            checkdsrcod = "";
            diakinisidsrcod = "";
            checkmetaggisidsrcod= "";
            metaggisidsrcod= "";
            dsr0nu = "";
            dsr1nu = "";
            checkdsrnu = "";
            diakinisidsrnu = "";
            checkmetaggisidsrnu= "";
            metaggisidsrnu= "";
            printr = "";




            UpdateConfig=0;
        }

        if (!rs.isClosed()) {
            rs.close();
        }



                clientid.setText((CharSequence) client);
                clientid.setEnabled(true);
                clientid.setFocusableInTouchMode(true);
                clientid.setClickable(true);

                ventorwerks.setText((CharSequence) ventwerks);
                ventorwerks.setEnabled(true);
                ventorwerks.setFocusableInTouchMode(true);
                ventorwerks.setClickable(true);

                webserviceurl.setText((CharSequence) webservice);
                webserviceurl.setEnabled(true);
                webserviceurl.setFocusableInTouchMode(true);
                webserviceurl.setClickable(true);

                dsr0code.setText((CharSequence) dsr0cod);
                dsr0code.setEnabled(true);
                dsr0code.setFocusableInTouchMode(true);
                dsr0code.setClickable(true);

                dsr1code.setText((CharSequence) dsr1cod);
                dsr1code.setEnabled(true);
                dsr1code.setFocusableInTouchMode(true);
                dsr1code.setClickable(true);

                checkdsrcode.setText((CharSequence) checkdsrcod);
                checkdsrcode.setEnabled(true);
                checkdsrcode.setFocusableInTouchMode(true);
                checkdsrcode.setClickable(true);

                diakinisidsrcode.setText((CharSequence) diakinisidsrcod);
                diakinisidsrcode.setEnabled(true);
                diakinisidsrcode.setFocusableInTouchMode(true);
                diakinisidsrcode.setClickable(true);
                
                metaggisidsrcode.setText((CharSequence) metaggisidsrcod);
                metaggisidsrcode.setEnabled(true);
                metaggisidsrcode.setFocusableInTouchMode(true);
                metaggisidsrcode.setClickable(true);
                
                checkmetaggisidsrcode.setText((CharSequence) checkmetaggisidsrcod);
                checkmetaggisidsrcode.setEnabled(true);
                checkmetaggisidsrcode.setFocusableInTouchMode(true);
                checkmetaggisidsrcode.setClickable(true);

                dsr0num.setText((CharSequence) dsr0nu);
                dsr0num.setEnabled(true);
                dsr0num.setFocusableInTouchMode(true);
                dsr0num.setClickable(true);

                dsr1num.setText((CharSequence) dsr1nu);
                dsr1num.setEnabled(true);
                dsr1num.setFocusableInTouchMode(true);
                dsr1num.setClickable(true);

                checkdsrnum.setText((CharSequence) checkdsrnu);
                checkdsrnum.setEnabled(true);
                checkdsrnum.setFocusableInTouchMode(true);
                checkdsrnum.setClickable(true);

                diakinisidsrnum.setText((CharSequence) diakinisidsrnu);
                diakinisidsrnum.setEnabled(true);
                diakinisidsrnum.setFocusableInTouchMode(true);
                diakinisidsrnum.setClickable(true);

                metaggisidsrnum.setText((CharSequence) metaggisidsrnu);
                metaggisidsrnum.setEnabled(true);
                metaggisidsrnum.setFocusableInTouchMode(true);
                metaggisidsrnum.setClickable(true);
        
                checkmetaggisidsrnum.setText((CharSequence) checkmetaggisidsrnu);
                checkmetaggisidsrnum.setEnabled(true);
                checkmetaggisidsrnum.setFocusableInTouchMode(true);
                checkmetaggisidsrnum.setClickable(true);
                        
                printer.setText((CharSequence) printr);
                printer.setEnabled(true);
                printer.setFocusableInTouchMode(true);
                printer.setClickable(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            //onBackPressed();
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


    private class AsyncCallWSPostCredentials extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            webserv= webserviceurl.getText().toString();


           if (webserviceurl.getText().toString().trim().equals("") || clientid.getText().toString().trim().equals("")) {

               chkmessage="Πρέπει να συμπληρώσετε το Client ID και το WebService Url.";

                missingidorurl=true;

            }

        }


        @Override
        protected void onPostExecute(String response) {

          super.onPostExecute(response);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(DisplayConfig.this);
            builder1.setTitle(R.string.app_name);
            builder1.setMessage(chkmessage);
            builder1.setIcon(R.mipmap.ic_launcher);
            builder1.setNegativeButton("OK", null);
            AlertDialog dialog = builder1.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);


        }

        @Override

        protected String doInBackground(Void... voids) {

           if (missingidorurl==false) {

                 chkmessage="Επιτυχία. Επαληθεύτηκε το Client ID.";

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnectionExist = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnectionExist) {

                    try {


                        String checkuserurl=webserv + "?checklic=" +  clientid.getText().toString() + "&CKEY=" + deviceId ;

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

                        if (response.equals("NOK")) {

                            chkmessage="Δεν επαληθεύτηκε το Client ID. Επικοινωνήστε με τον διαχειριστή σας.";
                        }
                        else
                        {
                            chkmessage="Επαληθεύτηκε το Client ID.";

                        }

                    } catch (Exception e) {


                        chkmessage="Πρόβλημα με την επαλήθευση του ID σας. Επικοινωνήστε με το διαχειριστή σας.";

                        e.printStackTrace();

                    } finally {

                    }

                }
                else
                {
                    chkmessage="Δεν έχετε πρόσβαση στο διαδίκτυο.";
                }



         }


            return null;

        }



    }

    public void checkid(View view) {

        chkmessage="";
        missingidorurl=false;
        
        AsyncCallWSPostCredentials task = new AsyncCallWSPostCredentials();
        task.execute();



    }


    public void run(View view)
    {

          if (clientid.getText().toString().length()==0 || ventorwerks.getText().toString().length()==0 || webserviceurl.getText().toString().length()==0 || dsr0code.getText().toString().length()==0 || dsr1code.getText().toString().length()==0 ||
                  checkdsrcode.getText().toString().length()==0 || diakinisidsrcode.getText().toString().length()==0 || metaggisidsrcode.getText().toString().length()==0 || checkmetaggisidsrcode.getText().toString().length()==0 ||
                  dsr0num.getText().toString().length()==0 || dsr1num.getText().toString().length()==0 || checkdsrnum.getText().toString().length()==0  || diakinisidsrnum.getText().toString().length()==0 ||
                  metaggisidsrnum.getText().toString().length()==0 || checkmetaggisidsrnum.getText().toString().length()==0 || printer.getText().toString().length()==0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(R.string.app_name);
            builder1.setMessage("Πρέπει να συμπληρώσετε όλα τα υποχρεωτικά πεδία του Αρχείου Ρυθμίσεων!!");
            builder1.setIcon(R.mipmap.ic_launcher);
            builder1.setNegativeButton("OK", null);
            AlertDialog dialog = builder1.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);

          } else {

               if (UpdateConfig == 1) {

                      if (mydb.updateConfig(1, clientid.getText().toString(), ventorwerks.getText().toString(),webserviceurl.getText().toString(),dsr0code.getText().toString(),dsr1code.getText().toString(),
                              checkdsrcode.getText().toString(),diakinisidsrcode.getText().toString(),checkmetaggisidsrcode.getText().toString(),metaggisidsrcode.getText().toString(),
                              dsr0num.getText().toString(),dsr1num.getText().toString(),checkdsrnum.getText().toString(),diakinisidsrnum.getText().toString(),checkmetaggisidsrnum.getText().toString(),metaggisidsrnum.getText().toString(),printer.getText().toString())) {


                          Toast.makeText(getApplicationContext(), "Επιτυχής Αποθήκευση Αρχείου Ρυθμίσεων", Toast.LENGTH_SHORT).show();


                      } else {

                          Toast.makeText(getApplicationContext(), "Ανεπιτυχής Αποθήκευση Αρχείου Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                      }

               } else {

                       if (mydb.insertConfig(clientid.getText().toString(), ventorwerks.getText().toString(),webserviceurl.getText().toString(),dsr0code.getText().toString(),dsr1code.getText().toString(),
                           checkdsrcode.getText().toString(),diakinisidsrcode.getText().toString(),checkmetaggisidsrcode.getText().toString(),metaggisidsrcode.getText().toString(),
                           dsr0num.getText().toString(),dsr1num.getText().toString(),checkdsrnum.getText().toString(),diakinisidsrnum.getText().toString(),checkmetaggisidsrnum.getText().toString()
                               ,metaggisidsrnum.getText().toString(),printer.getText().toString(),"0")) {


                          Toast.makeText(getApplicationContext(), "Επιτυχής Αποθήκευση Αρχείου Ρυθμίσεων", Toast.LENGTH_SHORT).show();


                      } else {
                          Toast.makeText(getApplicationContext(), "Ανεπιτυχής Αποθήκευση Αρχείου Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                      }

               }



      }
    }
}

