package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.MainActivity;
import android.panos.ram.rammilkcollection.adapters.CollectionChambersCustomAdapter;
import android.panos.ram.rammilkcollection.adapters.TradeCheckChambersCustomAdapter;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class TradeCheckFragment extends Fragment {

    DBHelper mydb;

    String todaydate;

    String newtradecheck_aa;
    String tradeid = "0";

    private TextView collectionamount;
    private EditText xlm;
    private ListView listView;
    private Button updateButton;
    private TradeCheckChambersCustomAdapter tradecheckchambersCustomAdapter;

    Boolean tdadecheckcreated=false;

    String chamberList[];
    String chambidList[];
    String quantityList[];
    String sampleList[];

    public TradeCheckFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_tradecheck, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Έλεγχος Δρομολογίου</small>"));


        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("TradesCheck");

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaydate = dateFormatter.format(newCalendar.getTime());

        listView = (ListView) getView().findViewById(R.id.chambersListView);
        collectionamount = (TextView) getView().findViewById(R.id.collectionamount);
        xlm = (EditText) getView().findViewById(R.id.xlm);

        collectionamount.setText((CharSequence) getTradeCheckAmount());

        updateButton = (Button) getView().findViewById(R.id.updateButton);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChambers();
            }
        });

        GetChambersData();

    }

    public String getTradeCheckAmount() {

        String trchamount="";

        mydb=new DBHelper(getActivity());

        Cursor rs = mydb.getTradeCheckAmount(todaydate);
        rs.moveToFirst();

        if (!rs.isAfterLast()) {

            if (rs.moveToFirst()) {

                do {

                    trchamount = rs.getString(rs.getColumnIndex("amount"));

                } while (rs.moveToNext());//Move the cursor to the next row.

            }
        }

        if (!rs.isClosed()) {
            rs.close();
        }

        return  trchamount;
    }


    public void updateChambers() {

        if (!tdadecheckcreated) {

            String message = "";

            Boolean sampleinchambers = true;

            Boolean sampleproperlength = true;

            mydb = new DBHelper(getActivity());


            String finalsampleList[];

            finalsampleList = tradecheckchambersCustomAdapter.getsampleArray();


            for (int i = 0; i < finalsampleList.length; i++) {
                if (finalsampleList[i] == "") {
                    sampleinchambers = false;
                }
            }

            if (sampleinchambers == false) {

                message = "Πρέπει να Καταχωρήσετε Δείγμα σε όλους τους Θαλάμους!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

            for (int i = 0; i < finalsampleList.length; i++) {

                if (finalsampleList[i].length() < 8) {
                    sampleproperlength = false;
                }
            }

            if (sampleproperlength == false) {

                message = "Τα Δείγματα των Θαλάμων πρέπει να είνα οκταψήφια!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

            String searchsample;
            Boolean foundsame=false;

            for (int i = 0; i < finalsampleList.length; i++) {

                searchsample=finalsampleList[i];
                foundsame=false;

                for (int j = i+1; j < finalsampleList.length; j++) {

                    if (searchsample.equals(finalsampleList[j])) {

                        message = "Τα Δείγματα των Θαλάμων (" + Integer.toString(i+1) + ") και (" + Integer.toString(j+1) + ") είναι ιδια!!Αδύνατη η καταχώρηση!!";
                        foundsame=true;
                        break;

                    }

                }

                if (foundsame) {
                    break;
                }
            }

            if (foundsame) {
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }

            Boolean foundsampleintrades=false;

            for (int i = 0; i < finalsampleList.length; i++) {


                foundsampleintrades=false;

                Cursor csb = mydb.getSameBarcode(finalsampleList[i]);

                if (!csb.isAfterLast()) {


                    if (!csb.isClosed()) {
                        csb.close();
                    }
                    message = "Το Δείγμα του Θαλάμου (" + Integer.toString(i+1) + ") είναι Καταχωρημένο ήδη σε άλλο Παραστατικό!!Αδύνατη η καταχώρηση!!";
                    foundsampleintrades=true;
                    break;

                }

                if (!csb.isClosed()) {
                    csb.close();
                }

            }


            if (foundsampleintrades) {
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }


            /*get parameters from config table*/

            String client = "";
            String checkdsrcode = "";
            String checkdsrnum = "";
            String newdsrno = "";
            String domaintype = "4";
            String transpid = "";
            String salsmid = "";
            String routeid = "";

            Cursor cfrs = mydb.getConfigData(1);


            if (!cfrs.isAfterLast()) {

                if (cfrs.moveToFirst()) {
                    do {

                        client = cfrs.getString(cfrs.getColumnIndex("clientid"));
                        checkdsrcode = cfrs.getString(cfrs.getColumnIndex("checkdsrcode"));
                        checkdsrnum = cfrs.getString(cfrs.getColumnIndex("checkdsrnum"));
                        transpid = cfrs.getString(cfrs.getColumnIndex("transpid"));
                        salsmid = cfrs.getString(cfrs.getColumnIndex("salsmid"));
                        routeid = cfrs.getString(cfrs.getColumnIndex("routeid"));

                    } while (cfrs.moveToNext());
                }
                if (!cfrs.isClosed()) {
                    cfrs.close();
                }
            }

            /*get last aa  tradecheck */

            newtradecheck_aa = "1";
            String trchkid = "0";
            String trchkiscanceled = "0";
            Boolean insertnewtrchk = true;

            Cursor lasttrch = mydb.getLastTodayTradeCheck_aa(todaydate);

            if (!lasttrch.isAfterLast()) {

                if (lasttrch.moveToFirst()) {

                    do {

                        String last_aa = lasttrch.getString(lasttrch.getColumnIndex("last_aa"));

                        if (last_aa != null) {

                            Cursor trch = mydb.getTradeCheckIscanceled(todaydate, last_aa);

                            if (!trch.isAfterLast()) {

                                if (trch.moveToFirst()) {

                                    do {

                                        trchkiscanceled = trch.getString(trch.getColumnIndex("iscanceled"));
                                        trchkid = trch.getString(trch.getColumnIndex("id"));

                                    } while (trch.moveToNext());

                                }
                                if (!trch.isClosed()) {
                                    trch.close();
                                }
                            }

                            if (trchkiscanceled.equals("1")) {

                                newtradecheck_aa = Integer.toString(Integer.parseInt(last_aa));
                                insertnewtrchk = false;
                            } else {
                                newtradecheck_aa = Integer.toString(Integer.parseInt(last_aa) + 1);
                            }

                        }
                    } while (lasttrch.moveToNext());

                }
                if (!lasttrch.isClosed()) {
                    lasttrch.close();
                }
            }


            /*get fromtradecode  and  uptotradecode*/
            String fromtradecode = "";
            String uptotradecode = "";

            Cursor trch = mydb.getFromUptoTradecodesToCheck(todaydate);

            if (!trch.isAfterLast()) {

                if (trch.moveToFirst()) {

                    do {

                        fromtradecode = trch.getString(trch.getColumnIndex("fromtradecode"));
                        uptotradecode = trch.getString(trch.getColumnIndex("uptotradecode"));

                    } while (trch.moveToNext());

                }
                if (!trch.isClosed()) {
                    trch.close();
                }
            }




            /*update tradecheck  */
            trchkiscanceled = "1";

            if (insertnewtrchk.equals(true) && mydb.InsertTradeCheck(todaydate, newtradecheck_aa, fromtradecode, uptotradecode)) {

                Cursor trchk = mydb.getTradeCheckId(todaydate, newtradecheck_aa);

                if (!trchk.isAfterLast()) {

                    if (trchk.moveToFirst()) {

                        do {

                            trchkid = trchk.getString(trchk.getColumnIndex("id"));

                        } while (trchk.moveToNext());

                    }
                    if (!trchk.isClosed()) {
                        trchk.close();
                    }
                } else {

                    String errormessage = "Πρόβλημα στην Kαταχώρηση του Ελέγχου Δρομολογίου!!";
                    AlertDialog.Builder diaBox = MessageAlert(errormessage);
                    return;

                }
            }


            if (insertnewtrchk.equals(false) && mydb.UpdateTradeCheck(trchkid, uptotradecode, "0")) {

                Cursor trchk = mydb.getTradeCheckIscanceled(todaydate, newtradecheck_aa);

                if (!trchk.isAfterLast()) {

                    if (trchk.moveToFirst()) {

                        do {

                            trchkiscanceled = trchk.getString(trchk.getColumnIndex("iscanceled"));

                        } while (trchk.moveToNext());

                    }
                    if (!trchk.isClosed()) {
                        trchk.close();
                    }
                }
                if (trchkiscanceled.equals("1")) {

                    String errormessage = "Πρόβλημα στην Ενημέρωση του Ελέγχου Δρομολογίου!!";
                    AlertDialog.Builder diaBox = MessageAlert(errormessage);
                    return;

                }
            }


            /*insert tradecheck trade and tradelines */

            int lastdsrno = Integer.parseInt(checkdsrnum);

            tradeid = "0";

            newdsrno = Integer.toString(lastdsrno + 1);

            String tradecode = checkdsrcode + "-" + newdsrno;
            String notes= "";

            Calendar newCalendar = Calendar.getInstance();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
            String nowtime=timeFormatter.format(newCalendar.getTime());
            String xiliometra=xlm.getText().toString();

            if (mydb.InsertTradeCheckTrade(client, domaintype, trchkid, checkdsrcode, newdsrno, tradecode, todaydate,nowtime, transpid, salsmid, fromtradecode, uptotradecode, newtradecheck_aa, routeid, xiliometra,notes, "0", "0")) {

                Cursor trrs = mydb.getTradeData(domaintype, checkdsrcode, newdsrno, transpid, "0");

                if (!trrs.isAfterLast()) {

                    if (trrs.moveToFirst()) {

                        do {
                            tradeid = trrs.getString(trrs.getColumnIndex("id"));
                        } while (trrs.moveToNext());

                    }

                    if (!trrs.isClosed()) {
                        trrs.close();
                    }

                    int lineno = 1;

                    Cursor chrrs = mydb.getNewTradeCheckLines(todaydate);

                    if (!chrrs.isAfterLast()) {

                        if (chrrs.moveToFirst()) {

                            do {


                                String chamberid = chrrs.getString(chrrs.getColumnIndex("z_wpid"));
                                String chambequantity = chrrs.getString(chrrs.getColumnIndex("qty"));
                                String iteid = chrrs.getString(chrrs.getColumnIndex("iteid"));
                                String sample = "";

                                for (int i = 0; i < chambidList.length; i++) {
                                    if (chambidList[i].equals(chamberid)) {
                                        sample = finalsampleList[i];
                                    }
                                }


                                String aa = Integer.toString(lineno);

                                lineno++;

                                if (mydb.InsertTradeCheckTradeLines(aa, tradeid, iteid, chambequantity, chamberid, sample)) {

                                    if (mydb.getTradeLineRowCount(aa, tradeid) == 0) {

                                        mydb.DeleteTradeLines("tradelines", tradeid);
                                        mydb.DeleteRowFromTable("trades", tradeid);

                                        String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου!!";
                                        AlertDialog.Builder diaBox = MessageAlert(errormessage);
                                        return;

                                    }
                                }

                            } while (chrrs.moveToNext());


                        }

                        if (!chrrs.isClosed()) {
                            chrrs.close();
                        }


                    }
                } else {
                    String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου!!";
                    AlertDialog.Builder diaBox = MessageAlert(errormessage);
                    return;
                }


            } else {
                String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου!!";
                AlertDialog.Builder diaBox = MessageAlert(errormessage);
                return;
            }


            if (mydb.UpdateTradeCheckTradesIsChecked(todaydate, trchkid, "1")) {

                if (mydb.updateConfiglastChkdsr(1, newdsrno)) {

                    tdadecheckcreated=true;

                    final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
                    globalVariables.setcallingForm("TradesCheck");
                    globalVariables.setnewtradecheck_aa(newtradecheck_aa);
                    globalVariables.settradeid(tradeid);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                }

            }
        }

    }

    public void GetChambersData() {

        chamberList=new String[]{};
        chambidList=new String[]{};
        quantityList=new String[]{};
        sampleList=new String[]{};

        Integer arrayindx = 0;


        mydb=new DBHelper(getActivity());

        Cursor rs = mydb.getConfigData(1);
        rs.moveToFirst();

        if (rs.isAfterLast()) {
            if (!rs.isClosed()) {
                rs.close();
            }
            AlertDialog.Builder diaBox = MessageAlert(getString(R.string.confignotexists));
        } else {
            if (!rs.isClosed()) {
                rs.close();
            }
            try {

                Cursor c= mydb.getTradeCheckChambersAmount(todaydate);

                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {
                        do {

                            chambidList = Arrays.copyOf(chambidList, chambidList.length + 1);
                            chamberList = Arrays.copyOf(chamberList, chamberList.length + 1);
                            quantityList = Arrays.copyOf(quantityList, quantityList.length + 1);
                            sampleList = Arrays.copyOf(sampleList, sampleList.length + 1);


                            chambidList[arrayindx] = c.getString(c.getColumnIndex("chamberid"));
                            chamberList[arrayindx] = "Διαμέρισμα " + chambidList[arrayindx];
                            quantityList[arrayindx] = c.getString(c.getColumnIndex("amount"));
                            sampleList[arrayindx]="";
                            arrayindx++;
                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

                tradecheckchambersCustomAdapter = new TradeCheckChambersCustomAdapter(getActivity(),chamberList,quantityList,sampleList);
                listView.setAdapter(tradecheckchambersCustomAdapter);

            } catch (Exception e) {
                //Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία για τους Θαλάμους " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

    private AlertDialog.Builder MessageAlert(String message)

    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton("OK", null);
        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        return builder;

    }

}

