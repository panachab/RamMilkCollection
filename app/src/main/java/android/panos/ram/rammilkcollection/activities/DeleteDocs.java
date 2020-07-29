package android.panos.ram.rammilkcollection.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.DBHelper;
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
public class DeleteDocs extends AppCompatActivity implements View.OnClickListener {

    private DBHelper mydb;





    String deviceId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        setContentView(R.layout.activity_clear_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Ενημέρωση του Κεντρικού</small>"));

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("DeleteDocs");




    }

    @Override
    public void onClick(View view) {
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

    public void clear_data(View view) {

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



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τα παραστατικά;");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setPositiveButton("NAI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mydb.deleteTable("trades");
                    mydb.deleteTable("tradelines");
                    mydb.deleteTable("collection");
                    mydb.deleteTable("collectionlines");
                    mydb.deleteTable("collectionchamberlines");
                    mydb.deleteTable("allcollections");
                    mydb.deleteTable("tradechecks");
                    mydb.deleteTable("diakinisi");
                    mydb.deleteTable("destinationtransferdata");
                    mydb.deleteTable("transferchamberlines");
                    mydb.deleteTable("sourcetransferchamberdata");
                    mydb.deleteTable("transfersourcetrade");

                    Toast.makeText(getApplicationContext(), "Διαγράφηκαν όλα τα παραστατικά από τη συσκευή.", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("OXI", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);





            }
        }

    }






