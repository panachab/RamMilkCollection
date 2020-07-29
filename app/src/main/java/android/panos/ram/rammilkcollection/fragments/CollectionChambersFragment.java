package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.adapters.CollectionChambersCustomAdapter;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.panos.ram.rammilkcollection.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;


public class CollectionChambersFragment extends Fragment {

    DBHelper mydb;

    private TextView collectionamount;
    private TextView chambersamount;
    private ListView listView;
    private Button updateButton;
    private CollectionChambersCustomAdapter collectionchambersCustomAdapter;
    private EditText et;

    String station;
    String stationid;
    String barcode;
    String rtcodeid;
    
    String chamberList[];
    String chambidList[];
    String quantityList[];
    
    public CollectionChambersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_collection_chambers, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Παραλαβή Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Collection");

        listView=(ListView)  getView().findViewById(R.id.chambersListView);
        collectionamount=(TextView)  getView().findViewById(R.id.collectionamount);
        chambersamount=(TextView)  getView().findViewById(R.id.chambersamount);

        Intent in =  getActivity().getIntent();
        station = String.valueOf(in.getExtras().getString("station"));
        stationid = String.valueOf(in.getExtras().getString("stationid"));
        barcode = String.valueOf(in.getExtras().getString("barcode"));
        rtcodeid = String.valueOf(in.getExtras().getString("rtcodeid"));

        collectionamount.setText((CharSequence) getCollectionAmount());
        chambersamount.setText((CharSequence) getChambersAmount());

        updateButton = (Button) getView().findViewById(R.id.updateButton);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChambers();
            }
        });


        GetChambersData();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {


            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();

        }
    }

    public String getCollectionAmount() {

        String collamount="";

        mydb=new DBHelper(getActivity());

        Cursor rs = mydb.getCollectionData();
        rs.moveToFirst();

        if (!rs.isAfterLast()) {

            if (rs.moveToFirst()) {

                do {
                    collamount = rs.getString(rs.getColumnIndex("amount"));

                } while (rs.moveToNext());//Move the cursor to the next row.

            }
        }

        if (!rs.isClosed()) {
            rs.close();
        }

     return  collamount;
    }

    public String getChambersAmount() {

        String champamount="";
        int totalquantity = 0;

        mydb=new DBHelper(getActivity());

        Cursor rs = mydb.getChambersData();
        rs.moveToFirst();

        if (!rs.isAfterLast()) {

            if (rs.moveToFirst()) {

                do {
                      if (rs.getString(rs.getColumnIndex("amount")) != "") {
                          totalquantity = totalquantity + Integer.parseInt(rs.getString(rs.getColumnIndex("amount")));
                      }
                } while (rs.moveToNext());
            }

            champamount =Integer.toString(totalquantity);

        }

        if (!rs.isClosed()) {
            rs.close();
        }

        return  champamount;


    }

    public void updateChambers() {

            String message="";
            Boolean milkinchambers=false;
            boolean notupdated=false;
            mydb=new DBHelper(getActivity());
            int totalquantity = 0;
            int aa = 1;

            if (collectionamount.getText().toString()=="") {

                message="Δεν έχει γίνει Παραλαβή Γάλακτος!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

              return;

            }


            String finalquantityList[];

            finalquantityList=collectionchambersCustomAdapter.getquantityArray();


            for (int i = 0; i < finalquantityList.length; i++) {
                if (finalquantityList[i] != "") {
                    totalquantity=totalquantity + Integer.parseInt(finalquantityList[i]);
                    milkinchambers = true;
                }
            }

            if (milkinchambers == false) {

                message="Δεν υπάρχουν Ποσότητες στους Θαλάμους για Καταχώρηση!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                mydb.deleteTable("collectionchamberlines");

                return;

            }
            else
            {
                chambersamount.setText((CharSequence) Integer.toString(totalquantity));
            }

            if (!(Integer.parseInt(collectionamount.getText().toString()) == Integer.parseInt(chambersamount.getText().toString()))) {

                message="Η Ποσότητα της Παραλαβής είναι Διαφορετική από την Ποσότητα των Θαλάμων!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;
            }


            mydb.deleteTable("collectionchamberlines");

            for (int i = 0; i < finalquantityList.length; i++) {

                if (finalquantityList[i] != "") {

                    if (mydb.InsertCollectionChamberLines
                            ( Integer.toString(aa),finalquantityList[i])) {
                    } else {
                        notupdated=true;
                    }

                }

                aa++;
            }


            if(notupdated) {

                mydb.deleteTable("collectionchamberlines");

                chambersamount.setText((CharSequence) "");

                message="Πρόβλημα στην Ενημέρωση των Ποσοτήτων στους Θαλάμους!!";
            }
            else {

                message="Επιτυχής Ενημέρωση των Ποσοτήτων στους Θαλάμους!!";

            }


        AlertDialog.Builder diaBox = MessageAlert(message);


    }

    public void GetChambersData() {

        chamberList=new String[]{};
        chambidList=new String[]{};
        quantityList=new String[]{};

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

                for (int i=0;i<15;i++) {

                    chambidList = Arrays.copyOf(chambidList, chambidList.length + 1);
                    chamberList = Arrays.copyOf(chamberList, chamberList.length + 1);
                    quantityList = Arrays.copyOf(quantityList, quantityList.length + 1);

                    chambidList[i] = "";
                    chamberList[i] = "Διαμέρισμα " + Integer.toString(i+1);
                    quantityList[i] = "";

                }

                Cursor c= mydb.getChambersData();
                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {

                        do {
                            chambidList[arrayindx] = c.getString(c.getColumnIndex("chamberid"));
                            quantityList[arrayindx] = c.getString(c.getColumnIndex("amount"));
                            arrayindx++;
                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

            collectionchambersCustomAdapter = new CollectionChambersCustomAdapter(getActivity(),chamberList,quantityList);
            listView.setAdapter(collectionchambersCustomAdapter);

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

