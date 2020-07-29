package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.panos.ram.rammilkcollection.models.KeyPairBoolData;
import android.panos.ram.rammilkcollection.models.SingleSpinnerSearch;
import android.panos.ram.rammilkcollection.models.SpinnerListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private EditText editdriverid;
    DBHelper handler;

    public HomeFragment() {}

    Integer salesmanidx = 0;
    Integer transportationidx = 0;
    EditText drivercode;
    String salesmancodeid;
    String salesmancode;
    String selectedsalesmancodeid;
    String SalesmenIdArray[] ={};
    String SalesmenCodeArray[] ={};
    String SalesmenArray[] ={};
    Boolean watchdrivercode=true;

    SingleSpinnerSearch searchSingleSpinner;


    final List<KeyPairBoolData> listArray2 = new ArrayList<>();

    FloatingActionButton clear_driver_id;
    String transportationcodeid;
    String selectedtransportationcodeid;
    String TransportationsCodesArray[] ={};

    Boolean newsalesmansearch=true;
    Boolean newtransportationsearch=true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      
        super.onActivityCreated(savedInstanceState);

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Home");

        drivercode = (EditText) getView().findViewById(R.id.Driver_ID);

        drivercode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!watchdrivercode) {

                    watchdrivercode=true;

                }

                else

                    {

                          if (drivercode.getText().length() < 6) {

                            salesmanidx = 0;
                            selectedsalesmancodeid = "0";

                                if (!handler.updateConfigSalesman(1, selectedsalesmancodeid)) {
                                    Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Οδηγού στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                                }

                            RefreshSalesmenSpinner();

                          }

                          else {

                              salesmancode = drivercode.getText().toString();

                              Cursor rs = handler.getSalesmanData(salesmancode);

                              rs.moveToFirst();

                              if (!rs.isAfterLast()) {

                                  salesmancodeid = rs.getString(rs.getColumnIndex("salsmid"));

                                  for (int i = 0; i < SalesmenIdArray.length; i++) {

                                      if (SalesmenIdArray[i].equals(salesmancodeid)) {

                                          salesmanidx = i;
                                          selectedsalesmancodeid = SalesmenIdArray[i];

                                          if (!handler.updateConfigSalesman(1, selectedsalesmancodeid)) {
                                              Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Οδηγού στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                                          }

                                          RefreshSalesmenSpinner();


                                      }
                                  }


                              }


                                  if (!rs.isClosed()) {
                                      rs.close();
                                  }

                          }
                    }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

        });




        searchSingleSpinner = (SingleSpinnerSearch) getView().findViewById(R.id.salesmenSpinner);

        clear_driver_id = (FloatingActionButton) getView().findViewById(R.id.clear_driver_id);

        clear_driver_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drivercode.setText("");
                salesmancode=drivercode.getText().toString();
                salesmanidx=0;
                selectedsalesmancodeid="0";
                if (!handler.updateConfigSalesman(1, selectedsalesmancodeid)) {
                    Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Οδηγού στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                }

                RefreshSalesmenSpinner();
                
                

            }
        });

        //String SalesmenArray[] ={};


        String TransportationsArray[] ={};

        Integer arrayindx = 0;


        handler=new DBHelper(getActivity());

        Cursor rs = handler.getConfigData(1);

        rs.moveToFirst();

        if (rs.isAfterLast()) {

            if (!rs.isClosed()) {
                rs.close();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.confignotexists);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).commit();


        } else {

            salesmancodeid = "";
            transportationcodeid = "";

            if (rs.getString(rs.getColumnIndex("salsmid")) != null && !rs.getString(rs.getColumnIndex("salsmid")).equals("")) {
                salesmancodeid=rs.getString(rs.getColumnIndex("salsmid"));

            }

            if (rs.getString(rs.getColumnIndex("transpid")) != null && !rs.getString(rs.getColumnIndex("transpid")).equals("")) {
                transportationcodeid=rs.getString(rs.getColumnIndex("transpid"));

            }

            if (!salesmancodeid.equals("")) {
                newsalesmansearch = false;
            }

            if (!transportationcodeid.equals("")) {
                newtransportationsearch = false;
            }


            if (!rs.isClosed()) {
                rs.close();
            }


            SalesmenArray = Arrays.copyOf(SalesmenArray, SalesmenArray.length + 1);
            SalesmenArray[arrayindx] = "";
            SalesmenIdArray = Arrays.copyOf(SalesmenIdArray, SalesmenIdArray.length + 1);
            SalesmenCodeArray = Arrays.copyOf(SalesmenCodeArray, SalesmenCodeArray.length + 1);
            SalesmenIdArray[arrayindx] = "0";
            SalesmenCodeArray[arrayindx] = "";
            String slid = "0";
            salesmancode="";
            selectedsalesmancodeid="0";
            salesmanidx = arrayindx;
            arrayindx = 1;

            Cursor c2 = handler.getSalesmenData();

            if (c2.moveToFirst()) {

                do {


                    SalesmenArray = Arrays.copyOf(SalesmenArray, SalesmenArray.length + 1);
                    SalesmenArray[arrayindx] = c2.getString(c2.getColumnIndex("name"));
                    SalesmenIdArray = Arrays.copyOf(SalesmenIdArray, SalesmenIdArray.length + 1);
                    SalesmenCodeArray = Arrays.copyOf(SalesmenCodeArray, SalesmenCodeArray.length + 1);
                    SalesmenIdArray[arrayindx] = c2.getString(c2.getColumnIndex("salsmid"));
                    SalesmenCodeArray[arrayindx] = c2.getString(c2.getColumnIndex("code")).substring(4);
                    slid = c2.getString(c2.getColumnIndex("salsmid"));
                    salesmancode=c2.getString(c2.getColumnIndex("code"));
                    salesmancode=salesmancode.substring(4);
                    if (salesmanidx == arrayindx && newsalesmansearch == true) {
                        salesmancodeid = slid;
                        selectedsalesmancodeid = salesmancodeid;
                        drivercode.setText(salesmancode);

                        if (!handler.updateConfigSalesman(1, selectedsalesmancodeid)) {
                            Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Οδηγού στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (salesmancodeid.equals(slid) && newsalesmansearch == false) {
                            salesmanidx = arrayindx;
                            drivercode.setText(salesmancode);
                        }

                    }
                    arrayindx++;
                } while (c2.moveToNext());

            }


            RefreshSalesmenSpinner();




            arrayindx = 0;

            final List<KeyPairBoolData> listArray3 = new ArrayList<>();

            Cursor c3 = handler.getTransportationsData();

            if (c3.moveToFirst()) {

                do {


                    TransportationsArray = Arrays.copyOf(TransportationsArray, TransportationsArray.length + 1);
                    TransportationsArray[arrayindx] = c3.getString(c3.getColumnIndex("descr"));
                    TransportationsCodesArray = Arrays.copyOf(TransportationsCodesArray, TransportationsCodesArray.length + 1);
                    TransportationsCodesArray[arrayindx] = c3.getString(c3.getColumnIndex("codeid"));
                    String trid = c3.getString(c3.getColumnIndex("codeid"));
                    if (transportationidx == arrayindx && newtransportationsearch == true) {
                        transportationcodeid = trid;
                        selectedtransportationcodeid = transportationcodeid;
                        if (!handler.updateTransportation(1, selectedtransportationcodeid)) {
                            Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Αυτοκινήτου στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (transportationcodeid.equals(trid) && newtransportationsearch == false) {
                            transportationidx = arrayindx;
                        }

                    }
                    arrayindx++;
                } while (c3.moveToNext());

            }

            final List<String> list3 = Arrays.asList(TransportationsArray);

            for (int i = 0; i < list3.size(); i++) {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(i + 1);
                h.setName(list3.get(i));
                if (i == transportationidx) {
                    h.setSelected(true);
                } else {
                    h.setSelected(false);
                }

                listArray3.add(h);
            }
            SingleSpinnerSearch searchSingleSpinner1 = (SingleSpinnerSearch) getView().findViewById(R.id.transportationsSpinner);

            searchSingleSpinner1.setItems(listArray3, -1, new SpinnerListener() {

                @Override
                public void onItemsSelected(List<KeyPairBoolData> items) {

                    for (int i = 0; i < items.size(); i++) {

                        if (items.get(i).isSelected()) {

                            selectedtransportationcodeid = TransportationsCodesArray[i];

                            if (handler.updateTransportation(1, selectedtransportationcodeid)) {


                            } else {

                                Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Αυτοκινήτου στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

        }
    }


    public void RefreshSalesmenSpinner() {

        watchdrivercode=false;

        final List<KeyPairBoolData> listArray2 = new ArrayList<>();
        final List<String> list2 = Arrays.asList(SalesmenArray);

        for (int i = 0; i < list2.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list2.get(i));
            if (i == salesmanidx) {
                h.setSelected(true);
            } else {
                h.setSelected(false);
            }

            listArray2.add(h);
        }

        searchSingleSpinner.setItems(listArray2, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {

                    if (items.get(i).isSelected()) {

                        selectedsalesmancodeid = SalesmenIdArray[i];
                        drivercode.setText(SalesmenCodeArray[i]);

                        if (handler.updateConfigSalesman(1, selectedsalesmancodeid)) {

                        } else {

                            Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Οδηγού στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

 }
