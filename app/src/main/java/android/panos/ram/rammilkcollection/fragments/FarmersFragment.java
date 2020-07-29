package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.activities.CollectionTabsActivity;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.adapters.FarmersCustomAdapter;
import android.panos.ram.rammilkcollection.models.DBHelper;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FarmersFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton fab;
    private RelativeLayout mRelativeLayout;

    DBHelper handler;
    String filteredList[];

    public FarmersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmers, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Field");

        listView=(ListView) getView().findViewById(R.id.farmersListView);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        mRelativeLayout = (RelativeLayout) getView().findViewById(R.id.rl);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.farmer_filter_popup, null);

                final PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                if(Build.VERSION.SDK_INT>=21){
                    popupWindow.setElevation(5.0f);
                }

                popupWindow.showAtLocation(mRelativeLayout,Gravity.TOP,0,170);
                popupWindow.setFocusable(true);
                popupWindow.update();

                final EditText name = (EditText) popupView.findViewById(R.id.editName);
                final EditText code = (EditText) popupView.findViewById(R.id.editCode);
                final EditText afm = (EditText) popupView.findViewById(R.id.editAfm);
                final EditText phone11 = (EditText) popupView.findViewById(R.id.editPhone11);


                Button searcbutton = (Button) popupView.findViewById(R.id.searchButton);
                searcbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        FillFarmersList(name.getText().toString(),code.getText().toString(),afm.getText().toString(),phone11.getText().toString());
                    }
                });

                FloatingActionButton closeFab = (FloatingActionButton) popupView.findViewById(R.id.popupfab);
                 closeFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                public void onClick(View view) {
                   popupWindow.dismiss();
              }
             });


            }
        });


        FillFarmersList("","","","");

    }

    

    public void FillFarmersList(String s_name,String s_code,String s_afm,String s_phone11) {

        filteredList = new String[]{};
        String nameList[]={};
        String codeList[]={};
        String afmList[]={};
        String phone11List[]={};


        Integer arrayindx = 0;

        handler=new DBHelper(getActivity());

        Cursor c;

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
        } else {
            if (!rs.isClosed()) {
                rs.close();
            }
            try {
                    c = handler.getSuppliersData(s_name,s_code,s_afm,s_phone11);


                if (c.moveToFirst()) {

                    do {

                        filteredList = Arrays.copyOf(filteredList, filteredList.length + 1);
                        nameList = Arrays.copyOf(nameList, nameList.length + 1);
                        codeList = Arrays.copyOf(codeList, codeList.length + 1);
                        afmList = Arrays.copyOf(afmList, afmList.length + 1);
                        phone11List = Arrays.copyOf(phone11List, phone11List.length + 1);


                        filteredList[arrayindx] = c.getString(c.getColumnIndex("id"));

                        nameList[arrayindx] = c.getString(c.getColumnIndex("name"));
                        codeList[arrayindx] = c.getString(c.getColumnIndex("code"));
                        afmList[arrayindx] = c.getString(c.getColumnIndex("afm"));
                        phone11List[arrayindx] = c.getString(c.getColumnIndex("phone11"));

                        arrayindx++;

                    } while (c.moveToNext());

                    FarmersCustomAdapter farmersCustomAdapter = new FarmersCustomAdapter(getContext(),  nameList, codeList, afmList, phone11List);

                    listView.setAdapter(farmersCustomAdapter);
                } else {
                    FarmersCustomAdapter farmersCustomAdapter = new FarmersCustomAdapter(getContext(),  nameList, codeList, afmList, phone11List);
                    listView.setAdapter(farmersCustomAdapter);
                    Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία!!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία!!" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position >= 0) {
                    int supplierid = Integer.parseInt(filteredList[position]);
                    Cursor rs = handler.getRecordData("supplier", supplierid);
                    rs.moveToFirst();
                    if (!rs.isAfterLast()) {
                        if (!rs.isClosed()) {
                            rs.close();
                        }
                                Intent i = new Intent(getContext(), CollectionTabsActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("id",supplierid);
                                startActivity(i);

                    } else {
                        if (!rs.isClosed()) {
                            rs.close();
                        }
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setTitle(R.string.app_name);
                        builder1.setMessage("Ο Παραγωγός δεν βρέθηκε!!");
                        builder1.setIcon(R.mipmap.ic_launcher);
                        builder1.setNegativeButton("OK", null);
                        AlertDialog dialog = builder1.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
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
