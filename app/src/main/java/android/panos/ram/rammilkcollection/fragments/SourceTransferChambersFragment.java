package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.adapters.SourceTransferChambersCustomAdapter;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class SourceTransferChambersFragment extends Fragment {

    DBHelper mydb;

    private TextView chambersamount;
    private TextView transferedamount;
    private TextView restamount;

    private ListView listView;
    private SourceTransferChambersCustomAdapter sourcetransferchambersCustomAdapter;
    private FloatingActionButton getKeb;
    private EditText editKeb;
    private RelativeLayout mRelativeLayout;

    String webserviceurl;

    String tradeid;
    String message="";

    String chamberList[];
    String quantityList[];

    String transferedDataList[][];

    String destinationChamberQuantityList[];
    String destinationChamberIdList[];

    public SourceTransferChambersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_source_transfer, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Μετάγγιση Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Metaggisi");

        mRelativeLayout = (RelativeLayout) getView().findViewById(R.id.rl);

        editKeb=(EditText)  getView().findViewById(R.id.editKeb);
        getKeb = (FloatingActionButton) getView().findViewById(R.id.getKeb);

        getKeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editKeb.getText().toString().trim().equals("")) {

                    message = "Πρέπει να εισάγετε Κατάσταση Ελέγχου του Βυτίου Προέλευσης!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                }
                else {
                    CreateChambersData();

                }
            }
        });



        listView=(ListView)  getView().findViewById(R.id.chambersListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                if (position >= 0) {

                    long viewId = v.getId();

                    if (viewId == R.id.getKeb) {

                        final String transferqtystring=quantityList[position];
                        final String sourcechamberid=chamberList[position];

                        final int transferqty=Integer.parseInt(quantityList[position]);

                        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.transfer_popup, null);

                        final PopupWindow popupWindow = new PopupWindow(popupView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        if(Build.VERSION.SDK_INT>=21){
                            popupWindow.setElevation(5.0f);
                        }

                        popupWindow.showAtLocation(mRelativeLayout,Gravity.TOP,0,180);
                        popupWindow.setFocusable(true);
                        popupWindow.update();


                        final EditText editTransqty1 = (EditText) popupView.findViewById(R.id.editTransqty1);
                        final EditText editTransqty2 = (EditText) popupView.findViewById(R.id.editTransqty2);
                        final EditText editTransqty3 = (EditText) popupView.findViewById(R.id.editTransqty3);
                        final EditText editTransqty4 = (EditText) popupView.findViewById(R.id.editTransqty4);
                        final EditText editTransqty5 = (EditText) popupView.findViewById(R.id.editTransqty5);
                        final EditText editTransqty6 = (EditText) popupView.findViewById(R.id.editTransqty6);
                        final EditText editTransqty7 = (EditText) popupView.findViewById(R.id.editTransqty7);
                        final EditText editTransqty8 = (EditText) popupView.findViewById(R.id.editTransqty8);
                        final EditText editTransqty9 = (EditText) popupView.findViewById(R.id.editTransqty9);
                        final EditText editTransqty10 = (EditText) popupView.findViewById(R.id.editTransqty10);
                        final EditText editTransqty11 = (EditText) popupView.findViewById(R.id.editTransqty11);
                        final EditText editTransqty12 = (EditText) popupView.findViewById(R.id.editTransqty12);
                        final EditText editTransqty13 = (EditText) popupView.findViewById(R.id.editTransqty13);
                        final EditText editTransqty14 = (EditText) popupView.findViewById(R.id.editTransqty14);
                        final EditText editTransqty15 = (EditText) popupView.findViewById(R.id.editTransqty15);

                        final TextView soursechamber = (TextView) popupView.findViewById(R.id.soursechamber);
                        final TextView chamberqty = (TextView) popupView.findViewById(R.id.chamberqty);

                        soursechamber.setText((CharSequence) sourcechamberid);
                        chamberqty.setText((CharSequence) transferqtystring);


                        Button updateButton = (Button) popupView.findViewById(R.id.updateButton);



                        Cursor trslrs= mydb.getSourceChamberAmount(sourcechamberid);
                        if (!trslrs.isAfterLast()) {
                            if (trslrs.moveToFirst()) {

                                do {

                                    String destinationchamp1qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp1qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp1qty"));
                                    String destinationchamp2qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp2qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp2qty"));
                                    String destinationchamp3qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp3qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp3qty"));
                                    String destinationchamp4qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp4qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp4qty"));
                                    String destinationchamp5qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp5qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp5qty"));
                                    String destinationchamp6qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp6qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp6qty"));
                                    String destinationchamp7qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp7qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp7qty"));
                                    String destinationchamp8qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp8qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp8qty"));
                                    String destinationchamp9qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp9qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp9qty"));
                                    String destinationchamp10qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp10qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp10qty"));
                                    String destinationchamp11qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp11qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp11qty"));
                                    String destinationchamp12qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp12qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp12qty"));
                                    String destinationchamp13qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp13qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp13qty"));
                                    String destinationchamp14qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp14qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp14qty"));
                                    String destinationchamp15qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp15qty")).equals("0") ? "" : trslrs.getString(trslrs.getColumnIndex("destinationchamp15qty"));

                                     editTransqty1.setText((CharSequence) destinationchamp1qty);
                                     editTransqty2.setText((CharSequence) destinationchamp2qty);
                                     editTransqty3.setText((CharSequence) destinationchamp3qty);
                                     editTransqty4.setText((CharSequence) destinationchamp4qty);
                                     editTransqty5.setText((CharSequence) destinationchamp5qty);
                                     editTransqty6.setText((CharSequence) destinationchamp6qty);
                                     editTransqty7.setText((CharSequence) destinationchamp7qty);
                                     editTransqty8.setText((CharSequence) destinationchamp8qty);
                                     editTransqty9.setText((CharSequence) destinationchamp9qty);
                                     editTransqty10.setText((CharSequence) destinationchamp10qty);
                                     editTransqty11.setText((CharSequence) destinationchamp11qty);
                                     editTransqty12.setText((CharSequence) destinationchamp12qty);
                                     editTransqty13.setText((CharSequence) destinationchamp13qty);
                                     editTransqty14.setText((CharSequence) destinationchamp14qty);
                                     editTransqty15.setText((CharSequence) destinationchamp15qty);
                                    
                                } while (trslrs.moveToNext());//Move the cursor to the next row.

                            }
                        }
                        if (!trslrs.isClosed()) {
                            trslrs.close();
                        }


                        updateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                destinationChamberQuantityList=new String[]{};
                                destinationChamberIdList=new String[]{};
                                int arrayindx=0;

                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="1";
                                destinationChamberQuantityList[arrayindx]=editTransqty1.getText().toString().equals("") ? "0" : editTransqty1.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="2";
                                destinationChamberQuantityList[arrayindx]=editTransqty2.getText().toString().equals("") ? "0" : editTransqty2.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="3";
                                destinationChamberQuantityList[arrayindx]=editTransqty3.getText().toString().equals("") ? "0" : editTransqty3.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="4";
                                destinationChamberQuantityList[arrayindx]=editTransqty4.getText().toString().equals("") ? "0" : editTransqty4.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="5";
                                destinationChamberQuantityList[arrayindx]=editTransqty5.getText().toString().equals("") ? "0" : editTransqty5.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="6";
                                destinationChamberQuantityList[arrayindx]=editTransqty6.getText().toString().equals("") ? "0" : editTransqty6.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="7";
                                destinationChamberQuantityList[arrayindx]=editTransqty7.getText().toString().equals("") ? "0" : editTransqty7.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="8";
                                destinationChamberQuantityList[arrayindx]=editTransqty8.getText().toString().equals("") ? "0" : editTransqty8.getText().toString();
                                arrayindx++;

                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="9";
                                destinationChamberQuantityList[arrayindx]=editTransqty9.getText().toString().equals("") ? "0" : editTransqty9.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="10";
                                destinationChamberQuantityList[arrayindx]=editTransqty10.getText().toString().equals("") ? "0" : editTransqty10.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="11";
                                destinationChamberQuantityList[arrayindx]=editTransqty11.getText().toString().equals("") ? "0" : editTransqty11.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="12";
                                destinationChamberQuantityList[arrayindx]=editTransqty12.getText().toString().equals("") ? "0" : editTransqty12.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="13";
                                destinationChamberQuantityList[arrayindx]=editTransqty13.getText().toString().equals("") ? "0" : editTransqty13.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="14";
                                destinationChamberQuantityList[arrayindx]=editTransqty14.getText().toString().equals("") ? "0" : editTransqty14.getText().toString();
                                arrayindx++;
                                destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                                destinationChamberQuantityList = Arrays.copyOf(destinationChamberQuantityList, destinationChamberQuantityList.length + 1);
                                destinationChamberIdList[arrayindx]="15";
                                destinationChamberQuantityList[arrayindx]=editTransqty15.getText().toString().equals("") ? "0" : editTransqty15.getText().toString();
                                arrayindx++;


                                int tottranstofchambers=0;

                                for (int i=0;i<destinationChamberIdList.length;i++) {
                                    tottranstofchambers=tottranstofchambers+Integer.parseInt(destinationChamberQuantityList[i]);
                                }

                                if (tottranstofchambers !=transferqty) {

                                    message = "H Μεταγγισθείσα Ποσότητα στους Θαλάμους πρέπει να είναι αθροιστικά " + transferqtystring + " Λίτρα!!";
                                    AlertDialog.Builder diaBox = MessageAlert(message);

                                }
                                else
                                {

                                if (!mydb.updateSourceTransferChamberdata(sourcechamberid,destinationChamberQuantityList)) {

                                message = "Πρόβλημα στην ενημέρωση των Αρχείων Μετάγγισης (Προέλευση) !!";
                                AlertDialog.Builder diaBox = MessageAlert(message);

                                return;

                                }

                                    //get source transfered quantity for each chamber

                                    transferedDataList=new String[15][17];


                                    for (int i=0; i<15; i++) {

                                            transferedDataList[i][0]=Integer.toString(i+1);

                                    }

                                    for (int i=0; i<15; i++) {

                                        transferedDataList[i][1]="";

                                    }

                                    for (int i=0; i<15; i++) {

                                            for (int j=2; j<17; j++) {
                                                transferedDataList[i][j]="0";
                                            }

                                    }


                                    int arrayindy=0;

                                    Cursor trslrs = mydb.getTransferedChamberData();

                                    if (!trslrs.isAfterLast()) {

                                        if (trslrs.moveToFirst()) {

                                            do {


                                                arrayindx=Integer.parseInt(trslrs.getString(trslrs.getColumnIndex("sourcechamberid")))-1;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("sourcechamberid"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("sourcesample"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp1qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp2qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp3qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp4qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp5qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp6qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp7qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp8qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp9qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp10qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp11qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp12qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp13qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp14qty"));
                                                arrayindy++;
                                                transferedDataList[arrayindx][arrayindy] = trslrs.getString(trslrs.getColumnIndex("destinationchamp15qty"));
                                                arrayindy=0;


                                            } while (trslrs.moveToNext());


                                        }

                                    }

                                    if (!trslrs.isClosed()) {
                                        trslrs.close();
                                    }

                                    int transqty1=0;
                                    int transqty2=0;
                                    int transqty3=0;
                                    int transqty4=0;
                                    int transqty5=0;
                                    int transqty6=0;
                                    int transqty7=0;
                                    int transqty8=0;
                                    int transqty9=0;
                                    int transqty10=0;
                                    int transqty11=0;
                                    int transqty12=0;
                                    int transqty13=0;
                                    int transqty14=0;
                                    int transqty15=0;

                                    for (int i=0; i<15; i++) {

                                        for (int j=2; j<17; j++) {

                                            if (j==2) {
                                                transqty1=transqty1+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==3) {
                                                transqty2=transqty2+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==4) {
                                                transqty3=transqty3+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==5) {
                                                transqty4=transqty4+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==6) {
                                                transqty5=transqty5+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==7) {
                                                transqty6=transqty6+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==8) {
                                                transqty7=transqty7+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==9) {
                                                transqty8=transqty8+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==10) {
                                                transqty9=transqty9+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==11) {
                                                transqty10=transqty10+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==12) {
                                                transqty11=transqty11+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==13) {
                                                transqty12=transqty12+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==14) {
                                                transqty13=transqty13+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==15) {
                                                transqty14=transqty14+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                            if (j==16) {
                                                transqty15=transqty15+Integer.parseInt(transferedDataList[i][j]);
                                            }
                                        }

                                    }

                                    if (!mydb.updateDestinationTransferedQuantities(transqty1,transqty2,transqty3,transqty4,transqty5,transqty6,transqty7,transqty8,
                                            transqty9,transqty10,transqty11,transqty12,transqty13,transqty14,transqty15)) {

                                        message = "Πρόβλημα στην ενημέρωση των Αρχείων Μετάγγισης (Προορισμός) !!";
                                        AlertDialog.Builder diaBox = MessageAlert(message);

                                        return;

                                    }


                                    popupWindow.dismiss();

                                    chambersamount.setText((CharSequence) getSourceChambersAmount());
                                    transferedamount.setText((CharSequence) getSourceTransferedChambersAmount());
                                    restamount.setText((CharSequence) Integer.toString(Integer.parseInt(chambersamount.getText().toString())-Integer.parseInt(transferedamount.getText().toString())));

                                }


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



                }
            }
        });


        chambersamount=(TextView)  getView().findViewById(R.id.chambersamount);
        transferedamount=(TextView)  getView().findViewById(R.id.transferedamount);
        restamount=(TextView)  getView().findViewById(R.id.restamount);


        Intent in =  getActivity().getIntent();
        tradeid = String.valueOf(in.getExtras().getString("tradeid"));

        mydb=new DBHelper(getActivity());

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


    public String getSourceChambersAmount() {

        String  champamount="0";

        Cursor c= mydb.getSourceTotalChambersAmount();
        if (!c.isAfterLast()) {
            if (c.moveToFirst()) {

                do {

                    champamount = c.getString(c.getColumnIndex("totchambersamount"));

                } while (c.moveToNext());

            }
        }
        if (!c.isClosed()) {
            c.close();
        }


        if (champamount == null) {
            champamount="0";
        }

        return  champamount;


    }

    public String getSourceTransferedChambersAmount() {

        String  champamountstring="0";

        int  champamount=0;

        Cursor trslrs = mydb.getTransferedChamberData();

        if (!trslrs.isAfterLast()) {

            if (trslrs.moveToFirst()) {

                do {

                    String destinationchamp1qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp1qty"));
                    String destinationchamp2qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp2qty"));
                    String destinationchamp3qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp3qty"));
                    String destinationchamp4qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp4qty"));
                    String destinationchamp5qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp5qty"));
                    String destinationchamp6qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp6qty"));
                    String destinationchamp7qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp7qty"));
                    String destinationchamp8qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp8qty"));
                    String destinationchamp9qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp9qty"));
                    String destinationchamp10qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp10qty"));
                    String destinationchamp11qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp11qty"));
                    String destinationchamp12qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp12qty"));
                    String destinationchamp13qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp13qty"));
                    String destinationchamp14qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp14qty"));
                    String destinationchamp15qty=trslrs.getString(trslrs.getColumnIndex("destinationchamp15qty"));

                    champamount=champamount + Integer.parseInt(destinationchamp1qty) + Integer.parseInt(destinationchamp2qty) +
                            Integer.parseInt(destinationchamp3qty) + Integer.parseInt(destinationchamp4qty) +
                            Integer.parseInt(destinationchamp5qty) + Integer.parseInt(destinationchamp6qty) +
                            Integer.parseInt(destinationchamp7qty) + Integer.parseInt(destinationchamp8qty) +
                            Integer.parseInt(destinationchamp9qty) + Integer.parseInt(destinationchamp10qty) +
                            Integer.parseInt(destinationchamp11qty) + Integer.parseInt(destinationchamp12qty) +
                            Integer.parseInt(destinationchamp13qty) + Integer.parseInt(destinationchamp14qty) +
                            Integer.parseInt(destinationchamp15qty);

                    champamountstring=Integer.toString(champamount);

                } while (trslrs.moveToNext());

            }

        }

        if (!trslrs.isClosed()) {
            trslrs.close();
        }

        return  champamountstring;


    }

    public void GetChambersData() {

           chamberList=new String[]{};
           quantityList=new String[]{};


           int arrayindx=0;

            Cursor c= mydb.getSourceChambersAmount();
                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {

                        do {
                            chamberList = Arrays.copyOf(chamberList, chamberList.length + 1);
                            quantityList = Arrays.copyOf(quantityList, quantityList.length + 1);
                            chamberList[arrayindx] = c.getString(c.getColumnIndex("sourcechamberid"));
                            quantityList[arrayindx] = c.getString(c.getColumnIndex("transqty"));
                            arrayindx++;
                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

            chambersamount.setText((CharSequence) getSourceChambersAmount());
            transferedamount.setText((CharSequence) getSourceTransferedChambersAmount());
            restamount.setText((CharSequence) Integer.toString(Integer.parseInt(chambersamount.getText().toString())-Integer.parseInt(transferedamount.getText().toString())));

            sourcetransferchambersCustomAdapter = new SourceTransferChambersCustomAdapter(getActivity(),chamberList,quantityList);
            listView.setAdapter(sourcetransferchambersCustomAdapter);




    }

    public void CreateChambersData() {


        Cursor rs = mydb.getConfigData(1);
        rs.moveToFirst();

        if (rs.isAfterLast()) {
            if (!rs.isClosed()) {
                rs.close();
            }
            AlertDialog.Builder diaBox = MessageAlert(getString(R.string.confignotexists));
        } else {

            webserviceurl=rs.getString(rs.getColumnIndex("webserviceurl"));

            if (!rs.isClosed()) {
                rs.close();
            }

            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnectionExist = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (!isConnectionExist) {

                message = "Η Συσκευή σας δεν έχει πρόσβαση στο Internet!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;
            }

            AsyncCallWSGetTrade task = new AsyncCallWSGetTrade();
            task.execute();


        }


    }

    private class AsyncCallWSGetTrade extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onPostExecute(String json) {

            super.onPostExecute(json);

            if (json.equals("no connection")) {
                message = "Ανεπιτυχής Σύνδεση με το Κεντρικό!!";
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }

            json=json.substring(0,json.length()-2);

            if (json.equals("[]")) {
                message = "H Κατάσταση Ελέγχου του Βυτίου Προέλευσης δεν βρέθηκε!!";
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }

            try {


                mydb.deleteTable("transfersourcetrade");
                mydb.deleteTable("transferchamberlines");


                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String cid = obj.getString("CID");
                    String domaintype=obj.getString("DOMAINTYPE");
                    String xrisi=obj.getString("XRISI");
                    String docseries=obj.getString("DOCSERIES");
                    String docnum = obj.getString("DOCNUM");
                    String tradecode=obj.getString("TRADECODE");
                    String hmer=obj.getString("HMER").substring(8,10) + "/" + obj.getString("HMER").substring(5,7)+ "/" +obj.getString("HMER").substring(0,4);
                    String salesmanid=obj.getString("SALESMANID");
                    String notes=obj.getString("NOTES");
                    String supid = obj.getString("SUPID");
                    String trsid=obj.getString("TRSID");
                    String shvid=obj.getString("SHVID");
                    String dromologionum=obj.getString("DROMOLOGIONUM");
                    String fromtradecode=obj.getString("DABFROM");
                    String uptotradecode=obj.getString("DABTO");
                    String aa=obj.getString("AA");
                    String iteid=obj.getString("ITEID");
                    long qtylong = Math.round(Double.parseDouble(obj.getString("QTY")));
                    String sourceqty = Long.toString(qtylong);
                    String sourcesample=obj.getString("Z_AUTONUM");
                    String sourcechamberid=obj.getString("Z_WPID");

                    if (i==0) {

                        if (!mydb.insertTransferSourceTrade(cid,domaintype,xrisi,docseries,docnum,tradecode,hmer,salesmanid,notes,supid,trsid,shvid,dromologionum,fromtradecode,uptotradecode)) {

                            message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προέλευση) !!";
                            AlertDialog.Builder diaBox = MessageAlert(message);

                            return;


                        }


                    }

                    if (!mydb.insertTransferChamberLines(aa,iteid,sourceqty,sourcesample,sourcechamberid)) {

                        message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προέλευση) !!";
                        AlertDialog.Builder diaBox = MessageAlert(message);

                        return;


                    }

                }


                if (mydb.numberOfTableRows("transfersourcetrade") == 0) {

                    message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;


                }

                if (mydb.numberOfTableRows("transferchamberlines") == 0) {

                    message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;


                }


                mydb.deleteTable("sourcetransferchamberdata");

                Cursor trslrs = mydb.getSourceTransferChambersAmount();

                if (!trslrs.isAfterLast()) {

                    if (trslrs.moveToFirst()) {

                        do {

                            String transqty = trslrs.getString(trslrs.getColumnIndex("amount"));
                            String sourcechamberid = trslrs.getString(trslrs.getColumnIndex("chamberid"));
                            String sourcesample = trslrs.getString(trslrs.getColumnIndex("sample"));

                            if (!mydb.insertSourceTransferChamberdata(sourcechamberid,sourcesample,transqty,"0","0","0","0","0","0","0","0",
                                    "0","0","0","0","0","0","0")) {

                                message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προέλευση) !!";
                                AlertDialog.Builder diaBox = MessageAlert(message);

                                return;


                            }
                        } while (trslrs.moveToNext());


                    }

                }

                if (!trslrs.isClosed()) {
                    trslrs.close();
                }


                if (mydb.numberOfTableRows("sourcetransferchamberdata") == 0) {

                    message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;


                }

                mydb.deleteTable("destinationtransferdata");

                for (int i=1;i<=15;i++) {

                    if (!mydb.insertDestinationTransferChamberdata(Integer.toString(i), "0", "0", "")) {

                        message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                        AlertDialog.Builder diaBox = MessageAlert(message);

                        return;


                    }
                }

                if (mydb.numberOfTableRows("destinationtransferdata") == 0) {

                    message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;


                }


                Cursor trdlrs = mydb.getDestinationTransferChambersAmount(tradeid);

                if (!trdlrs.isAfterLast()) {

                    if (trdlrs.moveToFirst()) {

                        do {

                            String initialqty = trdlrs.getString(trdlrs.getColumnIndex("amount"));
                            String destinationchamberid = trdlrs.getString(trdlrs.getColumnIndex("chamberid"));

                            if (!mydb.updateDestinationTransferChamberdata(destinationchamberid,initialqty,"0","")) {

                                message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                                AlertDialog.Builder diaBox = MessageAlert(message);

                                return;


                            }

                        } while (trdlrs.moveToNext());


                    }

                }

                if (!trdlrs.isClosed()) {
                    trdlrs.close();
                }


                chambersamount.setText((CharSequence) getSourceChambersAmount());
                transferedamount.setText((CharSequence) getSourceTransferedChambersAmount());
                restamount.setText((CharSequence) Integer.toString(Integer.parseInt(chambersamount.getText().toString())-Integer.parseInt(transferedamount.getText().toString())));

                GetChambersData();

            } catch (JSONException e) {

                message = "Πρόβλημα στην παραλαβή της Κατάστασης Ελέγχου του Βυτίου Προέλευσης!!";
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String gettranskeb=webserviceurl + "?TRADE=" + editKeb.getText().toString().trim();
            try {
                URL url = new URL(gettranskeb);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(300000);
                con.setReadTimeout(300000);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"ISO-8859-7"),40950);
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return "no connection";
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

