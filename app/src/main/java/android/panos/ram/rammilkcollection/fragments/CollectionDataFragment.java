package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.adapters.BarcodeSuppliersCustomAdapter;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.panos.ram.rammilkcollection.R;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class CollectionDataFragment extends Fragment {

    private TextView stationView;
    private TextView barcodeView;
    private ListView listView;
    private Button updateButton;

    private BarcodeSuppliersCustomAdapter barcodesuppliesCustomAdapter;

    DBHelper handler;

    String station;
    String stationid;
    String barcode;
    String rtcodeid;


    String supplierList[];
    String supidList[];
    String iteidList[];
    String milktypeList[];
    String quantityList[];
    String remarksList[];


    public CollectionDataFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Παραλαβή Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Collection");


        listView=(ListView)  getView().findViewById(R.id.suppliersListView);

        stationView=(TextView)  getView().findViewById(R.id.station);
        barcodeView=(TextView)  getView().findViewById(R.id.barcode);

        Intent in =  getActivity().getIntent();
        station = String.valueOf(in.getExtras().getString("station"));
        stationid = String.valueOf(in.getExtras().getString("stationid"));
        barcode = String.valueOf(in.getExtras().getString("barcode"));
        rtcodeid = String.valueOf(in.getExtras().getString("rtcodeid"));

        stationView.setText((CharSequence) station);
        barcodeView.setText((CharSequence) barcode);

        updateButton = (Button) getView().findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCollection();
            }
        });

        GetBarcodeSuppliersData();

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

    public void updateCollection() {

            String message="";
            Boolean milkcollected=false;
            boolean notupdated=false;
            handler=new DBHelper(getActivity());
            int totalquantity = 0;
            int aa = 1;
            String totalqty = "";


            String finalquantityList[];
            String finalremarksList[];

            finalquantityList=barcodesuppliesCustomAdapter.getquantityArray();
            finalremarksList=barcodesuppliesCustomAdapter.getremarksArray();

            for (int i = 0; i < finalquantityList.length; i++) {
                if (!finalquantityList[i].equals("")) {
                    totalquantity = totalquantity + Integer.parseInt(finalquantityList[i]);
                    milkcollected = true;
                }
            }

            if (milkcollected == false) {

                handler.deleteTable("collection");
                handler.deleteTable("collectionlines");

                message="Δεν υπάρχει Παραλαβή Γάλακτος για Καταχώρηση!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;
            }

           int collectionrows=handler.getCollectionRows();

           handler.deleteTable("collectionlines");

           for (int i = 0; i < finalquantityList.length; i++) {
                        if (handler.InsertCollectionLines
                                ( Integer.toString(aa),supidList[i],iteidList[i],finalremarksList[i],finalquantityList[i])) {
                        } else {
                            notupdated=true;
                        }
                        aa++;
           }

           if (milkcollected == true) {
               totalqty= Integer.toString(totalquantity);
           }

           if (collectionrows == 0) {


               if (handler.InsertCollection(1, rtcodeid, stationid, barcode, totalqty)) {

                   if (handler.getCollectionRows()==0) {
                       notupdated = true;
                   }
               }

           }else {

                   if (!handler.UpdateCollectionQuantity(1, totalqty)) {
                       notupdated = true;
                   }

           }

           if (notupdated) {

               handler.deleteTable("collection");
               handler.deleteTable("collectionlines");

               message = "Πρόβλημα στην Ενημέρωση της Παραλαβής Γάλακτος!!";

           } else {

               message = "Επιτυχής Ενημέρωση της Παραλαβής Γάλακτος!!";

           }


        AlertDialog.Builder diaBox = MessageAlert(message);
    }

    public void GetBarcodeSuppliersData() {

        supplierList=new String[]{};
        supidList=new String[]{};
        iteidList=new String[]{};
        milktypeList=new String[]{};
        quantityList=new String[]{};
        remarksList=new String[]{};

        Integer arrayindx = 0;

        handler=new DBHelper(getActivity());


        Cursor rs = handler.getConfigData(1);
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


                Cursor c = handler.getBarcodeFarmersData(barcode);
                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {

                        do {

                            supidList = Arrays.copyOf(supidList, supidList.length + 1);
                            iteidList = Arrays.copyOf(iteidList, iteidList.length + 1);
                            supplierList = Arrays.copyOf(supplierList, supplierList.length + 1);
                            milktypeList = Arrays.copyOf(milktypeList, milktypeList.length + 1);
                            quantityList = Arrays.copyOf(quantityList, quantityList.length + 1);
                            remarksList = Arrays.copyOf(remarksList, remarksList.length + 1);

                            supidList[arrayindx] = c.getString(c.getColumnIndex("supid"));
                            iteidList[arrayindx] = c.getString(c.getColumnIndex("iteid"));
                            supplierList[arrayindx] = c.getString(c.getColumnIndex("supplier"));
                            milktypeList[arrayindx] = c.getString(c.getColumnIndex("milktype"));
                            quantityList[arrayindx] = "";
                            remarksList[arrayindx] = "";
                            arrayindx++;
                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

                Cursor rsl = handler.getCollectionLinesData();

                arrayindx = 0;

                if (!rsl.isAfterLast()) {

                    if (rsl.moveToFirst()) {

                        do {
                            quantityList[arrayindx] = rsl.getString(rsl.getColumnIndex("amount"));
                            remarksList[arrayindx] = rsl.getString(rsl.getColumnIndex("remarks"));
                            arrayindx++;
                        } while (rsl.moveToNext());

                    }
                }
                if (!rsl.isClosed()) {
                    rsl.close();
                }

                barcodesuppliesCustomAdapter = new BarcodeSuppliersCustomAdapter(getActivity(),supplierList,milktypeList,quantityList,remarksList);
                listView.setAdapter(barcodesuppliesCustomAdapter);

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Πρόβλημα στην Ανεύρεση των Παραγωγών!!", Toast.LENGTH_LONG).show();
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
