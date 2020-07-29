package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.Collections;
import android.panos.ram.rammilkcollection.adapters.BarcodesCustomAdapter;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class CoolingTanksFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton fab;
    private RelativeLayout mRelativeLayout;
    private TextView driverView;
    private TextView transportationView;
    FloatingActionButton clearpagolekani;
    private EditText editPagolekani;
    String pagolekani;

    DBHelper handler;

    String filteredStationList[];
    String filteredStationIdList[];
    String filtereBarCodeidList[];
    String message="";

    public CoolingTanksFragment() {}

    Integer routeidx = 0;

    String vendorwerks;
    String routecodeid;
    String selectedroutecodeid;
    String RoutesCodesArray[] ={};
    Integer pagolekanes_count = 0;

    Boolean newsearch=true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      
        super.onActivityCreated(savedInstanceState);

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Collection");

        listView=(ListView) getView().findViewById(R.id.routesListView);
        mRelativeLayout = (RelativeLayout) getView().findViewById(R.id.rl);

        driverView=(TextView)  getView().findViewById(R.id.driver);
        transportationView=(TextView)  getView().findViewById(R.id.transportation);
        editPagolekani = (EditText) getView().findViewById(R.id.editPagolekani);
        // Capture Text in EditText
        editPagolekani.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                pagolekani=editPagolekani.getText().toString();
                FillBarcodesList(routecodeid,pagolekani);
                if (pagolekanes_count==1) {


                    Intent in = new Intent(getContext(), Collections.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    in.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("station",filteredStationList[0]);
                    in.putExtra("stationid",filteredStationIdList[0]);
                    in.putExtra("barcode",filtereBarCodeidList[0]);
                    in.putExtra("rtcodeid",routecodeid);
                    startActivity(in);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
            }
        });

        clearpagolekani = (FloatingActionButton) getView().findViewById(R.id.clearpagolekani);

        clearpagolekani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPagolekani.setText("");
                pagolekani=editPagolekani.getText().toString();
                FillBarcodesList(routecodeid,pagolekani);
            }
        });

        String RoutessArray[] ={};

        Integer arrayindx = 0;

        final List<KeyPairBoolData> listArray2 = new ArrayList<>();


        handler=new DBHelper(getActivity());

        Cursor rs = handler.getExtendConfigData(1);

        rs.moveToFirst();

        if (rs.isAfterLast()) {

            if (!rs.isClosed()) {
                rs.close();
            }

            message = getResources().getString(R.string.confignotexists);
            AlertDialog.Builder diaBox = MessageAlert(message);


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).commit();


        } else {


            Cursor rsc = handler.getCompanyData(1);

            rsc.moveToFirst();

            if (rsc.isAfterLast()) {

                if (!rsc.isClosed()) {
                    rsc.close();
                }

                message = getResources().getString(R.string.companynotexists);
                AlertDialog.Builder diaBox = MessageAlert(message);


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).commit();


            } else {

                if (!rsc.isClosed()) {
                    rsc.close();
                }

                if ((rs.getString(rs.getColumnIndex("salsmid")).equals("0")) || (rs.getString(rs.getColumnIndex("transpid")).equals(""))) {

                    message = getResources().getString(R.string.drivernotexists);
                    AlertDialog.Builder diaBox = MessageAlert(message);


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).commit();


                } else {

                    driverView.setText((CharSequence) rs.getString(rs.getColumnIndex("driver")));
                    transportationView.setText((CharSequence) rs.getString(rs.getColumnIndex("transportation")));
                    vendorwerks = rs.getString(rs.getColumnIndex("vendorwerks"));

                    routecodeid = "";

                    if (rs.getString(rs.getColumnIndex("routeid")) != null && !rs.getString(rs.getColumnIndex("routeid")).equals("")) {
                        routecodeid = rs.getString(rs.getColumnIndex("routeid"));
                    }


                    if (!routecodeid.equals("")) {
                        newsearch = false;
                    }

                    if (!rs.isClosed()) {
                        rs.close();
                    }


                    Cursor c2 = handler.getRoutesData(vendorwerks);

                    if (c2.moveToFirst()) {

                        do {

                            RoutessArray = Arrays.copyOf(RoutessArray, RoutessArray.length + 1);
                            RoutessArray[arrayindx] = c2.getString(c2.getColumnIndex("descr"));
                            RoutesCodesArray = Arrays.copyOf(RoutesCodesArray, RoutesCodesArray.length + 1);
                            RoutesCodesArray[arrayindx] = c2.getString(c2.getColumnIndex("codeid"));
                            String rtid = c2.getString(c2.getColumnIndex("codeid"));
                            if (routeidx == arrayindx && newsearch == true) {
                                routecodeid = rtid;
                                selectedroutecodeid = routecodeid;
                                if (!handler.updateRoute(1, selectedroutecodeid)) {
                                    Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Δρομολογίου στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (routecodeid.equals(rtid) && newsearch == false) {
                                    routeidx = arrayindx;
                                }

                            }
                            arrayindx++;
                        } while (c2.moveToNext());

                    }

                    final List<String> list2 = Arrays.asList(RoutessArray);

                    for (int i = 0; i < list2.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();
                        h.setId(i + 1);
                        h.setName(list2.get(i));
                        if (i == routeidx) {
                            h.setSelected(true);
                        } else {
                            h.setSelected(false);
                        }

                        listArray2.add(h);
                    }

                    SingleSpinnerSearch searchSingleSpinner = (SingleSpinnerSearch) getView().findViewById(R.id.routesSpinner);

                    searchSingleSpinner.setItems(listArray2, -1, new SpinnerListener() {

                        @Override
                        public void onItemsSelected(List<KeyPairBoolData> items) {

                            for (int i = 0; i < items.size(); i++) {

                                if (items.get(i).isSelected()) {

                                    selectedroutecodeid = RoutesCodesArray[i];

                                    if (handler.updateRoute(1, selectedroutecodeid)) {

                                        pagolekani = editPagolekani.getText().toString();

                                        routecodeid=selectedroutecodeid;

                                        FillBarcodesList(routecodeid, pagolekani);

                                    } else {

                                        Toast.makeText(getActivity(), "Ανεπιτυχής Ενημέρωση Δρομολογίου στο Αρχείο Ρυθμίσεων", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                    pagolekani = editPagolekani.getText().toString();
                    FillBarcodesList(routecodeid, pagolekani);
                }

            }
        }
    }

    public void FillBarcodesList(String rtcodeid,String pagolekani) {

        filteredStationList = new String[]{};
        filteredStationIdList = new String[]{};
        filtereBarCodeidList = new String[]{};

        String stationList[]={};
        String barcodeList[]={};
        
        Integer arrayindx = 0;

        handler=new DBHelper(getActivity());
        pagolekanes_count=0;

        Cursor c;

        Cursor rs = handler.getConfigData(1);
        rs.moveToFirst();

        if (rs.isAfterLast()) {
            if (!rs.isClosed()) {
                rs.close();
            }

            message = getResources().getString(R.string.confignotexists);
            AlertDialog.Builder diaBox = MessageAlert(message);

        } else {
            if (!rs.isClosed()) {
                rs.close();
            }

            try {
                    c = handler.getBarcodesData(rtcodeid,pagolekani);

                if (c.moveToFirst()) {

                    do {

                        filteredStationList = Arrays.copyOf(filteredStationList, filteredStationList.length + 1);
                        filtereBarCodeidList = Arrays.copyOf(filtereBarCodeidList, filtereBarCodeidList.length + 1);
                        filteredStationIdList = Arrays.copyOf(filteredStationIdList, filteredStationIdList.length + 1);

                        stationList = Arrays.copyOf(stationList, stationList.length + 1);
                        barcodeList = Arrays.copyOf(barcodeList, barcodeList.length + 1);


                        filteredStationList[arrayindx] = c.getString(c.getColumnIndex("station"));
                        filtereBarCodeidList[arrayindx] = c.getString(c.getColumnIndex("barcode"));
                        filteredStationIdList[arrayindx] = c.getString(c.getColumnIndex("stationid"));

                        stationList[arrayindx] = c.getString(c.getColumnIndex("station"));
                        barcodeList[arrayindx] = c.getString(c.getColumnIndex("barcode"));


                        arrayindx++;
                    } while (c.moveToNext());//Move the cursor to the next row.
                    BarcodesCustomAdapter barcodesCustomAdapter = new BarcodesCustomAdapter(getContext(), stationList, barcodeList);
                    listView.setAdapter(barcodesCustomAdapter);


                } else {
                    BarcodesCustomAdapter barcodesCustomAdapter = new BarcodesCustomAdapter(getContext(), stationList, barcodeList);
                    listView.setAdapter(barcodesCustomAdapter);
                    Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        pagolekanes_count=filteredStationList.length;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (position >= 0) {

                    final String station = filteredStationList[position];
                    final String barcode = filtereBarCodeidList[position];
                    final String stationid = filteredStationIdList[position];

                    String rtcodeid="0";

                    Cursor rs = handler.getBarcodeData(barcode);

                    rs.moveToFirst();

                    if (!rs.isAfterLast()) {

                        rtcodeid=rs.getString(rs.getColumnIndex("rtcodeid"));

                        if (!rs.isClosed()) {
                            rs.close();
                        }

                        if (!rtcodeid.equals(routecodeid)) {

                            message="Η Παγολεκάνη δεν ανοίκει στο Επιλεγμένο Δρομολόγιο!" + "\n" + "Να προχωρήσω στην Παραλαβή Γάλακτος;";

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(R.string.app_name);
                            builder.setMessage(message);
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setPositiveButton("NAI", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent in = new Intent(getContext(), Collections.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    in.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra("station",station);
                                    in.putExtra("stationid",stationid);
                                    in.putExtra("barcode",barcode);
                                    in.putExtra("rtcodeid",routecodeid);
                                    startActivity(in);
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
                        else{

                            Intent in = new Intent(getContext(), Collections.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            in.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("station",station);
                            in.putExtra("stationid",stationid);
                            in.putExtra("barcode",barcode);
                            in.putExtra("rtcodeid",routecodeid);
                            startActivity(in);

                        }


                    } else {
                        if (!rs.isClosed()) {
                            rs.close();
                        }

                        message = "H Παγολεκάνη δεν Βρέθηκε!!";
                        AlertDialog.Builder diaBox = MessageAlert(message);

                    }
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private AlertDialog.Builder MessageAlert(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
