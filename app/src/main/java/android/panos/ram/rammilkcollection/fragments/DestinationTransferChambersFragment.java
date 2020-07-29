package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.MainActivity;
import android.panos.ram.rammilkcollection.adapters.DestinationTransferChambersCustomAdapter;
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
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class DestinationTransferChambersFragment extends Fragment {

    DBHelper mydb;

    private TextView previousamount;
    private TextView transferedamount;
    private TextView totalamount;

    private ListView listView;
    private Button updateButton;
    
    private DestinationTransferChambersCustomAdapter destinationtransferchambersCustomAdapter;

    Boolean tdadecheckcreated=false;

    String tradeid;
    String todaydate;
    String currenttime;
    String newtradecheck_aa="0";
    String chamberList[];
    String chambidList[];
    String prevquantityList[];
    String transquantityList[];
    String totaquantityList[];

    String metaggisichktradeid;
    String metaggisitradeid;

    String sampleList[];

    public DestinationTransferChambersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_destination_transfer, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Μετάγγιση Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Metaggisi");

        listView=(ListView)  getView().findViewById(R.id.chambersListView);
        previousamount=(TextView)  getView().findViewById(R.id.previousamount);
        transferedamount=(TextView)  getView().findViewById(R.id.transferedamount);
        totalamount=(TextView)  getView().findViewById(R.id.totalamount);

        Intent in =  getActivity().getIntent();
        tradeid = String.valueOf(in.getExtras().getString("tradeid"));
        
        Calendar newCalendar = Calendar.getInstance();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaydate=dateFormatter.format(newCalendar.getTime());

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        currenttime=timeFormatter.format(newCalendar.getTime());

        updateButton = (Button) getView().findViewById(R.id.updateButton);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChambers();
            }
        });

        GetTotalQuantities();

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

    public void GetTotalQuantities() {

        mydb=new DBHelper(getActivity());

        Cursor rs = mydb.getDestinationTransferTotalAmount();
        rs.moveToFirst();

        if (!rs.isAfterLast()) {

            if (rs.moveToFirst()) {

                do {

                 previousamount.setText((CharSequence) rs.getString(rs.getColumnIndex("initialqty")));
                 transferedamount.setText((CharSequence) rs.getString(rs.getColumnIndex("transqty")));
                 totalamount.setText((CharSequence) rs.getString(rs.getColumnIndex("totaqty")));


                } while (rs.moveToNext());//Move the cursor to the next row.

            }
        }

        if (!rs.isClosed()) {
            rs.close();
        }

    }

    public void updateChambers() {



      if (!tdadecheckcreated) {

            String message="";

         if (Integer.parseInt(transferedamount.getText().toString())== 0) {
              message="Δεν Έχει γίνει κάποια Μετάγγιση Γάλακτος!!";
              AlertDialog.Builder diaBox = MessageAlert(message);
              return;
         }

        if (Integer.parseInt(getSourceChambersAmount()) != Integer.parseInt(transferedamount.getText().toString())) {
            message="Yπάρχει Yπολειπόμενη Ποσότητα στους Θαλάμους του Βυτίου Προέλευσης!!";
            AlertDialog.Builder diaBox = MessageAlert(message);
            return;
        }

        Boolean sampleinchambers = true;
        Boolean sampleproperlength = true;


        String finalsampleList[];

        finalsampleList = destinationtransferchambersCustomAdapter.getsampleArray();


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



          //get source chambers quantities per chamber and iteid
        int arrayinx=0;

        String sourceChamberIdList[]=new String[]{};
        String sourseItemIdList[]=new String[]{};
        String sourseQuantityList[]=new String[]{};
        String sourceSampleList[]=new String[]{};

        Cursor strlrs = mydb.getSourceTransferChambersLines();


        if (!strlrs.isAfterLast()) {

            if (strlrs.moveToFirst()) {

                do {

                    sourceChamberIdList = Arrays.copyOf(sourceChamberIdList, sourceChamberIdList.length + 1);
                    sourseItemIdList = Arrays.copyOf(sourseItemIdList, sourseItemIdList.length + 1);
                    sourseQuantityList = Arrays.copyOf(sourseQuantityList, sourseQuantityList.length + 1);
                    sourceSampleList = Arrays.copyOf(sourceSampleList, sourceSampleList.length + 1);

                    sourceChamberIdList[arrayinx] = strlrs.getString(strlrs.getColumnIndex("sourcechamberid"));
                    sourseItemIdList[arrayinx] = strlrs.getString(strlrs.getColumnIndex("iteid"));
                    sourseQuantityList[arrayinx] = strlrs.getString(strlrs.getColumnIndex("sourceqty"));
                    sourceSampleList[arrayinx] = strlrs.getString(strlrs.getColumnIndex("sourcesample"));

                    arrayinx++;


                } while (strlrs.moveToNext());


            }

            if (!strlrs.isClosed()) {
                strlrs.close();
            }


        }



            //get destination chambers initial quantities per chamber and iteid
        arrayinx=0;

        String destinationChamberIdList[]=new String[]{};
        String destinationItemIdList[]=new String[]{};
        String destinationInitialQuantityList[]=new String[]{};
        String destinationInitialSampleList[]=new String[]{};

        Cursor trlrs = mydb.getTradeCheckTradeLines(tradeid);

        if (!trlrs.isAfterLast()) {

                if (trlrs.moveToFirst()) {

                    do {

                        destinationChamberIdList = Arrays.copyOf(destinationChamberIdList, destinationChamberIdList.length + 1);
                        destinationItemIdList = Arrays.copyOf(destinationItemIdList, destinationItemIdList.length + 1);
                        destinationInitialQuantityList = Arrays.copyOf(destinationInitialQuantityList, destinationInitialQuantityList.length + 1);
                        destinationInitialSampleList = Arrays.copyOf(destinationInitialSampleList, destinationInitialSampleList.length + 1);

                        destinationChamberIdList[arrayinx] = trlrs.getString(trlrs.getColumnIndex("trl_chamber"));
                        destinationItemIdList[arrayinx] = trlrs.getString(trlrs.getColumnIndex("trl_iteid"));
                        destinationInitialQuantityList[arrayinx] = trlrs.getString(trlrs.getColumnIndex("trl_qty"));
                        destinationInitialSampleList[arrayinx] = trlrs.getString(trlrs.getColumnIndex("trl_sample"));

                        arrayinx++;


                    } while (trlrs.moveToNext());


                }

                if (!trlrs.isClosed()) {
                    trlrs.close();
                }

        }

        //calculate real transfered qualities per destination chamber and item and add them to finaltradelinesarray
        String finalDestinationChamberIdList[] = new String[]{};
        String finalSourceChamberIdList[] = new String[]{};
        String finalSourceSampleList[] = new String[]{};
        String finalItemIdList[] = new String[]{};
        String finalQuantityList[] = new String[]{};

            Cursor strd;
            arrayinx = 0;

        for (int i = 0; i < sourceChamberIdList.length; i++) {

                strd = mydb.getTransferedChamberDataOfsourceChamber(sourceChamberIdList[i]);

                if (!strd.isAfterLast()) {

                    if (strd.moveToFirst()) {

                        do {

                            String transqty = strd.getString(strd.getColumnIndex("transqty"));
                            String sourcechamberid = strd.getString(strd.getColumnIndex("sourcechamberid"));
                            String sourcesample = strd.getString(strd.getColumnIndex("sourcesample"));
                            String destinationchamp1qty = strd.getString(strd.getColumnIndex("destinationchamp1qty"));
                            String destinationchamp2qty = strd.getString(strd.getColumnIndex("destinationchamp2qty"));
                            String destinationchamp3qty = strd.getString(strd.getColumnIndex("destinationchamp3qty"));
                            String destinationchamp4qty = strd.getString(strd.getColumnIndex("destinationchamp4qty"));
                            String destinationchamp5qty = strd.getString(strd.getColumnIndex("destinationchamp5qty"));
                            String destinationchamp6qty = strd.getString(strd.getColumnIndex("destinationchamp6qty"));
                            String destinationchamp7qty = strd.getString(strd.getColumnIndex("destinationchamp7qty"));
                            String destinationchamp8qty = strd.getString(strd.getColumnIndex("destinationchamp8qty"));
                            String destinationchamp9qty = strd.getString(strd.getColumnIndex("destinationchamp9qty"));
                            String destinationchamp10qty = strd.getString(strd.getColumnIndex("destinationchamp10qty"));
                            String destinationchamp11qty = strd.getString(strd.getColumnIndex("destinationchamp11qty"));
                            String destinationchamp12qty = strd.getString(strd.getColumnIndex("destinationchamp12qty"));
                            String destinationchamp13qty = strd.getString(strd.getColumnIndex("destinationchamp13qty"));
                            String destinationchamp14qty = strd.getString(strd.getColumnIndex("destinationchamp14qty"));
                            String destinationchamp15qty = strd.getString(strd.getColumnIndex("destinationchamp15qty"));

                            if (!destinationchamp1qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "1";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp1qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp2qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "2";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp2qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp3qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "3";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp3qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp4qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "4";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp4qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp5qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "5";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp5qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }

                            if (!destinationchamp6qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "6";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp6qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp7qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "7";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp7qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp8qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "8";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp8qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp9qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "9";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp9qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp10qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "10";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp10qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp11qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "11";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp11qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp12qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "12";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp12qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp13qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "13";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp13qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp14qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "14";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp14qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }
                            if (!destinationchamp15qty.equals("0")) {
                                finalDestinationChamberIdList = Arrays.copyOf(finalDestinationChamberIdList, finalDestinationChamberIdList.length + 1);
                                finalSourceChamberIdList = Arrays.copyOf(finalSourceChamberIdList, finalSourceChamberIdList.length + 1);
                                finalSourceSampleList = Arrays.copyOf(finalSourceSampleList, finalSourceSampleList.length + 1);
                                finalItemIdList = Arrays.copyOf(finalItemIdList, finalItemIdList.length + 1);
                                finalQuantityList = Arrays.copyOf(finalQuantityList, finalQuantityList.length + 1);
                                finalDestinationChamberIdList[arrayinx] = "15";
                                finalSourceChamberIdList[arrayinx] = sourcechamberid;
                                finalSourceSampleList[arrayinx] = sourcesample;
                                finalItemIdList[arrayinx] = sourseItemIdList[i];
                                double qtyfraction = Double.parseDouble(sourseQuantityList[i]) / Double.parseDouble(transqty);
                                long finallineqty = Math.round(Double.parseDouble(destinationchamp15qty) * qtyfraction);
                                finalQuantityList[arrayinx] = Long.toString(finallineqty);
                                arrayinx++;
                            }


                        } while (strd.moveToNext());

                    }

                    if (!strd.isClosed()) {
                        strd.close();
                    }

                }

                if (!strd.isClosed()) {
                    strd.close();
                }


        }

           //add initial qualities per destination chamber and item to finaltradelinesarray
            String finaltotalChamberIdList[] = finalDestinationChamberIdList.clone();
            String finaltotalItemIdList[] = finalItemIdList.clone();
            String finaltotalQuantityList[] = finalQuantityList.clone();

            arrayinx=0;

            String finaltotalSampleList[] = new String[]{};

        for (int i = 0; i < finaltotalChamberIdList.length; i++) {

              for (int j=0;j<chamberList.length;j++) {
                  if  (finaltotalChamberIdList[i].equals(chamberList[j])) {
                      finaltotalSampleList = Arrays.copyOf(finaltotalSampleList, finaltotalSampleList.length + 1);
                      finaltotalSampleList[arrayinx] = finalsampleList[j];
                      break;
                  }
        }

              arrayinx++;

        }

        arrayinx = finaltotalChamberIdList.length;

        for (int i = 0; i < destinationChamberIdList.length; i++) {

                finaltotalChamberIdList = Arrays.copyOf(finaltotalChamberIdList, finaltotalChamberIdList.length + 1);
                finaltotalItemIdList = Arrays.copyOf(finaltotalItemIdList, finaltotalItemIdList.length + 1);
                finaltotalQuantityList = Arrays.copyOf(finaltotalQuantityList, finaltotalQuantityList.length + 1);
                finaltotalSampleList = Arrays.copyOf(finaltotalSampleList, finaltotalSampleList.length + 1);
                finaltotalChamberIdList[arrayinx] = destinationChamberIdList[i];
                finaltotalItemIdList[arrayinx] = destinationItemIdList[i];
                finaltotalQuantityList[arrayinx] = destinationInitialQuantityList[i];

                for (int j=0;j<chamberList.length;j++) {
                   if  (finaltotalChamberIdList[arrayinx].equals(chamberList[j])) {
                       finaltotalSampleList[arrayinx] = finalsampleList[j];
                       break;
                   }
                }

                arrayinx++;

        }




          //create metaggisi trade
        /*get parameters from config table*/


          String client = "";
          String checkmetaggisidsrcode = "";
          String checkmetaggisidsrnum = "";
          String metaggisidsrcode = "";
          String metaggisidsrnum = "";
          String newchceckmetaggisidsrno = "0";
          String newmetaggisidsrno = "0";
          String domaintype = "2";
          String transpid = "";
          String salsmid = "";
          String routeid = "";

          Cursor cfrs = mydb.getConfigData(1);


          if (!cfrs.isAfterLast()) {

              if (cfrs.moveToFirst()) {
                  do {

                      client = cfrs.getString(cfrs.getColumnIndex("clientid"));
                      checkmetaggisidsrcode = cfrs.getString(cfrs.getColumnIndex("checkmetaggisidsrcode"));
                      checkmetaggisidsrnum = cfrs.getString(cfrs.getColumnIndex("checkmetaggisidsrnum"));
                      metaggisidsrcode = cfrs.getString(cfrs.getColumnIndex("metaggisidsrcode"));
                      metaggisidsrnum = cfrs.getString(cfrs.getColumnIndex("metaggisidsrnum"));
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

          Cursor lasttrch = mydb.getLastTodayTradeCheck_aa(todaydate);

          if (!lasttrch.isAfterLast()) {

              if (lasttrch.moveToFirst()) {

                  do {
                      String last_aa = lasttrch.getString(lasttrch.getColumnIndex("last_aa"));
                      newtradecheck_aa = Integer.toString(Integer.parseInt(last_aa));
                  } while (lasttrch.moveToNext());

              }
              if (!lasttrch.isClosed()) {
                  lasttrch.close();
              }
          }

           /*get last aa  tradecheck id  */
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
          }

                      /*get source keb data*/

          String fromkeb = "";
          String fromtransportation = "";
          String dabfrom = "";
          String dabto = "";

          Cursor strch = mydb.getTransferSourceTradeCheck();

          if (!strch.isAfterLast()) {

              if (strch.moveToFirst()) {

                  do {

                      fromkeb = strch.getString(strch.getColumnIndex("tradecode"));
                      fromtransportation = strch.getString(strch.getColumnIndex("transdescr"));
                      dabfrom = strch.getString(strch.getColumnIndex("fromtradecode"));
                      dabto = strch.getString(strch.getColumnIndex("uptotradecode"));

                  } while (strch.moveToNext());

              }
              if (!strch.isClosed()) {
                  strch.close();
              }
          }

             /*get destination keb data*/

          String tokeb = "";

          Cursor dtrch = mydb.getTradeCheckTrade(tradeid);

          if (!dtrch.isAfterLast()) {

              if (dtrch.moveToFirst()) {

                  do {

                      tokeb = dtrch.getString(dtrch.getColumnIndex("tradecode"));

                  } while (dtrch.moveToNext());

              }
              if (!dtrch.isClosed()) {
                  dtrch.close();
              }
          }

           /*get secstoid keb data*/
          String secstoid = "";

          Cursor dcs = mydb.getDocseriesData(checkmetaggisidsrcode);

          if (!dcs.isAfterLast()) {

              if (dcs.moveToFirst()) {

                  do {

                      secstoid = dcs.getString(dcs.getColumnIndex("stoid"));

                  } while (dcs.moveToNext());

              }
              if (!dcs.isClosed()) {
                  dcs.close();
              }
          }


         String fromtradecode="00ΔΑΒ-" + dabfrom;
         String uptotradecode="00ΔΑΒ-" + dabto;

          /*get custid*/
          String defaultcustomercode = "";

          Cursor cdcs = mydb.getDocseriesData(metaggisidsrcode);

          if (!cdcs.isAfterLast()) {

              if (cdcs.moveToFirst()) {

                  do {

                      defaultcustomercode = cdcs.getString(cdcs.getColumnIndex("defaultcustomercode"));

                  } while (cdcs.moveToNext());

              }
              if (!cdcs.isClosed()) {
                  cdcs.close();
              }
          }

          String custid = "0";

          Cursor cust = mydb.getCusromerIdFromCode(defaultcustomercode);

          if (!cust.isAfterLast()) {

              if (cust.moveToFirst()) {

                  do {

                      custid = cust.getString(cust.getColumnIndex("rcustid"));

                  } while (cust.moveToNext());

              }
              if (!cust.isClosed()) {
                  cust.close();
              }
          }


              /*insert metaggisi trade and tradelines */

          int lastmetaggisidsrno = Integer.parseInt(metaggisidsrnum);

          int lastcheckmetaggisidsrno = Integer.parseInt(checkmetaggisidsrnum);

          newchceckmetaggisidsrno = Integer.toString(lastcheckmetaggisidsrno + 1);

          newmetaggisidsrno = Integer.toString(lastmetaggisidsrno + 1);

          String newmetaggisitradecode = metaggisidsrcode + "-" + newmetaggisidsrno;

          metaggisitradeid="0";

          if (mydb.InsertMetaggisiTrade(client, domaintype, trchkid, metaggisidsrcode, newmetaggisidsrno, newmetaggisitradecode,
                  todaydate,currenttime, transpid, salsmid, fromtradecode, uptotradecode,newtradecheck_aa, routeid,fromkeb,tokeb,fromtransportation,custid, "0", "0")) {

              Cursor trrs = mydb.getTradeData(domaintype, metaggisidsrcode, newmetaggisidsrno, transpid, "0");

              if (!trrs.isAfterLast()) {

                  if (trrs.moveToFirst()) {

                      do {
                          metaggisitradeid = trrs.getString(trrs.getColumnIndex("id"));
                      } while (trrs.moveToNext());

                  }

                  if (!trrs.isClosed()) {
                      trrs.close();
                  }

                  int lineno = 1;



                  for (int i = 0; i < finalSourceChamberIdList.length; i++) {

                      String fromchamberid = finalSourceChamberIdList[i];
                      String chamberid = finalDestinationChamberIdList[i];
                      String chambequantity = finalQuantityList[i];
                      String iteid = finalItemIdList[i];
                      String sample = finalSourceSampleList[i];

                      String aa = Integer.toString(lineno);

                      lineno++;

                      if (mydb.InsertMetaggisiTradeLines(aa, metaggisitradeid, iteid, chambequantity, chamberid, sample,fromchamberid)) {

                          if (mydb.getTradeLineRowCount(aa, metaggisitradeid) == 0) {

                              mydb.DeleteTradeLines("tradelines", metaggisitradeid);
                              mydb.DeleteRowFromTable("trades", metaggisitradeid);

                              String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Μετάγγισης!!";
                              AlertDialog.Builder diaBox = MessageAlert(errormessage);
                              return;

                          }
                      }
                  }

              }


          } else {
              String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου!!";
              AlertDialog.Builder diaBox = MessageAlert(errormessage);
              return;
          }


          //create check trade
          /*get fromtradecode  and  uptotradecode*/

          fromtradecode = "";
          uptotradecode = "";

          Cursor trch = mydb.getLastTradeDataOfTradeCheck(tradeid);

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

         if (fromtradecode == null) {
             fromtradecode = "";
         }

          if (uptotradecode == null) {
              uptotradecode = "";
          }



              /*insert metaggisi tradecheck trade and tradelines */
          String newchceckmetaggisitradecode = checkmetaggisidsrcode + "-" + newchceckmetaggisidsrno;


          metaggisichktradeid="0";


            if (mydb.InsertMetaggisiTradeCheckTrade(client, domaintype, trchkid, checkmetaggisidsrcode, newchceckmetaggisidsrno, newchceckmetaggisitradecode,
                  todaydate,currenttime, transpid, salsmid, fromtradecode, uptotradecode,newtradecheck_aa, routeid,newmetaggisidsrno,fromkeb,tokeb,fromtransportation,secstoid, "0", "0")) {

              Cursor trrs = mydb.getTradeData(domaintype, checkmetaggisidsrcode, newchceckmetaggisidsrno, transpid, "0");

              if (!trrs.isAfterLast()) {

                  if (trrs.moveToFirst()) {

                      do {
                          metaggisichktradeid = trrs.getString(trrs.getColumnIndex("id"));
                      } while (trrs.moveToNext());

                  }

                  if (!trrs.isClosed()) {
                      trrs.close();
                  }

                  int lineno = 1;

                  for (int i = 0; i < finaltotalChamberIdList.length; i++) {

                      String chamberid = finaltotalChamberIdList[i];
                      String chambequantity = finaltotalQuantityList[i];
                      String iteid = finaltotalItemIdList[i];
                      String sample = finaltotalSampleList[i];

                      String aa = Integer.toString(lineno);

                      lineno++;

                      if (mydb.InsertTradeCheckTradeLines(aa, metaggisichktradeid, iteid, chambequantity, chamberid, sample)) {

                          if (mydb.getTradeLineRowCount(aa, metaggisichktradeid) == 0) {

                              mydb.DeleteTradeLines("tradelines", metaggisichktradeid);
                              mydb.DeleteRowFromTable("trades", metaggisichktradeid);

                              String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου της Μετάγγισης!!";
                              AlertDialog.Builder diaBox = MessageAlert(errormessage);
                              return;

                          }
                      }
                  }

              }


        } else {
              String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ελέγχου!!";
              AlertDialog.Builder diaBox = MessageAlert(errormessage);
              return;
        }


        if (mydb.updateConfiglastMetagChkdsr(1, newchceckmetaggisidsrno)) {

            if (mydb.updateConfiglastMetagdsr(1, newmetaggisidsrno)) {

                tdadecheckcreated = true;

                final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
                globalVariables.setcallingForm("Metaggisi");
                globalVariables.setnewtradecheck_aa(newtradecheck_aa);
                globalVariables.settradeid(metaggisichktradeid);
                globalVariables.setmetaggisitradeid(metaggisitradeid);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        }

      }

    }

    public void GetChambersData() {

        chamberList=new String[]{};
        chambidList=new String[]{};
        prevquantityList=new String[]{};
        transquantityList=new String[]{};
        totaquantityList=new String[]{};

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

                
                
                Cursor c= mydb.getDestinationChambersAmount();

                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {
                        do {

                            chambidList = Arrays.copyOf(chambidList, chambidList.length + 1);
                            chamberList = Arrays.copyOf(chamberList, chamberList.length + 1);
                            prevquantityList = Arrays.copyOf(prevquantityList, prevquantityList.length + 1);
                            transquantityList= Arrays.copyOf(transquantityList, transquantityList.length + 1);
                            totaquantityList= Arrays.copyOf(totaquantityList, totaquantityList.length + 1);
                            sampleList = Arrays.copyOf(sampleList, sampleList.length + 1);



                            chambidList[arrayindx] = c.getString(c.getColumnIndex("destinationchamberid"));
                            chamberList[arrayindx] = chambidList[arrayindx];
                            prevquantityList[arrayindx] = c.getString(c.getColumnIndex("initialqty"));
                            transquantityList[arrayindx] =   c.getString(c.getColumnIndex("transqty"));
                            totaquantityList[arrayindx] =Integer.toString(Integer.parseInt(prevquantityList[arrayindx]) + Integer.parseInt(transquantityList[arrayindx]));

                            sampleList[arrayindx]="";

                            arrayindx++;

                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

                destinationtransferchambersCustomAdapter = new DestinationTransferChambersCustomAdapter(getActivity(),chamberList,prevquantityList,transquantityList,totaquantityList,sampleList);
                listView.setAdapter(destinationtransferchambersCustomAdapter);

            } catch (Exception e) {
                //Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία για τους Θαλάμους " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
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

