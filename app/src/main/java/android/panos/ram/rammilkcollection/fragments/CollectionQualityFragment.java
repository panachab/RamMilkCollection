package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.Collections;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import android.text.TextWatcher;


public class CollectionQualityFragment extends Fragment {

    DBHelper mydb;

    private Button updateButton;

    String station;
    String stationid;
    String barcode;
    String rtcodeid;
    String pagolekani="";
    String newcollection_aa="0";

    Boolean tdadescreated=false;

    EditText samplecode;
    EditText fat;
    EditText temperature;
    EditText ph;

    String todaydate;

    public CollectionQualityFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_collection_quality, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Παραλαβή Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        globalVariables.setcallingForm("Collection");
        globalVariables.setnewcollection_aa(newcollection_aa);

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaydate = dateFormatter.format(newCalendar.getTime());

        Intent in =  getActivity().getIntent();

        station = String.valueOf(in.getExtras().getString("station"));
        stationid = String.valueOf(in.getExtras().getString("stationid"));
        barcode = String.valueOf(in.getExtras().getString("barcode"));
        rtcodeid = String.valueOf(in.getExtras().getString("rtcodeid"));

        samplecode = (EditText) getActivity().findViewById(R.id.samplecode);
        fat = (EditText) getActivity().findViewById(R.id.fat);
        temperature = (EditText) getActivity().findViewById(R.id.temperature);
        ph = (EditText) getActivity().findViewById(R.id.ph);

        samplecode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                if (samplecode.length()== 8)
                    checkData();
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

        });

        updateButton = (Button) getView().findViewById(R.id.updateButton);
        updateButton.setVisibility(View.INVISIBLE);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tdadescreated) {
                      checkData();
                }
            }
        });


            GetQualityData();

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

    public void checkData() {

            String message = "";


            mydb = new DBHelper(getActivity());


            String collectionamount = getCollectionAmount();
            String chambersamount = getChambersAmount();


            if (collectionamount == "") {

                message = "Δεν έχει γίνει Παραλαβή Γάλακτος!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

            if (chambersamount == "") {

                message = "Δεν υπάρχουν Καταχωρημένες Ποσότητες στους Θαλάμους!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

            if (!(Integer.parseInt(getCollectionAmount()) == Integer.parseInt(getChambersAmount()))) {

                message = "Η Ποσότητα της Παραλαβής δεν συμφωνεί με την Ποσότητα των Θαλάμων!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

            if (samplecode.getText().toString().length() == 0) {

                message = "Πρέπει να Καταχωρήσετε Κωδικό Δείγματος!!";
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;

            }

            if (samplecode.getText().toString().length() < 8) {

                message = "Ο Κωδικός Δείγματος πρέπει να είναι οκταψήφιος!!";
                AlertDialog.Builder diaBox = MessageAlert(message);
                return;
            }

            String sametrades="";

            Cursor csb = mydb.getSameBarcode(samplecode.getText().toString());

            if (!csb.isAfterLast()) {

                if (csb.moveToFirst()) {

                    do {

                        sametrades=sametrades + "(" + csb.getString(csb.getColumnIndex("tr_code"));
                        sametrades=sametrades + " " + csb.getString(csb.getColumnIndex("tr_date")) + ")";
                        sametrades=sametrades + "\n";

                    } while (csb.moveToNext());

                }

                if (!csb.isClosed()) {
                    csb.close();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);
                builder.setMessage("O Κωδικός Δείγματος είναι ήδη καταχωρημένος στα Παραστατικά  " + "\n" + sametrades + "\n" + "Να Καταχωρήσω την Παραλαβή;");
                builder.setIcon(R.mipmap.ic_launcher);

                builder.setPositiveButton("NAI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        updateQuality();

                    }
                });
                builder.setNegativeButton("OXI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);

            }
            else
            {
                if (!csb.isClosed()) {
                    csb.close();
                }

                updateQuality();
            }

    }

   public void updateQuality() {

          String message = "";

          boolean notupdated = false;


          if (!mydb.UpdateCollectionQuality(1, samplecode.getText().toString(), fat.getText().toString(), temperature.getText().toString(), ph.getText().toString())) {
              notupdated = true;
          }

          if (notupdated) {

              message = "Πρόβλημα στην Ενημέρωση του Δείγματος και της Ποιότητας!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }



     /*get parameters from config table*/

          String client = "";
          String dsr1cod = "";
          String dsr1nu = "";
          String newdsrno = "";
          String transpid = "";
          String salsmid = "";

          Cursor cfrs = mydb.getConfigData(1);

          if (!cfrs.isAfterLast()) {

              if (cfrs.moveToFirst()) {

                  do {
                      client = cfrs.getString(cfrs.getColumnIndex("clientid"));
                      dsr1cod = cfrs.getString(cfrs.getColumnIndex("dsr1code"));
                      dsr1nu = cfrs.getString(cfrs.getColumnIndex("dsr1num"));
                      transpid = cfrs.getString(cfrs.getColumnIndex("transpid"));
                      salsmid = cfrs.getString(cfrs.getColumnIndex("salsmid"));

                  } while (cfrs.moveToNext());

              }

              if (!cfrs.isClosed()) {
                  cfrs.close();
              }
          }

        /*get current collection data*/

          String routeid = "";
          String stationid = "";
          String barcode = "";
          String fat = "";
          String temperature = "";
          String ph = "";
          String amount = "";
          String domaintype = "4";

          Cursor clrs = mydb.getCollectionData();

          if (!clrs.isAfterLast()) {

              if (clrs.moveToFirst()) {

                  do {

                      routeid = clrs.getString(clrs.getColumnIndex("routeid"));
                      stationid = clrs.getString(clrs.getColumnIndex("stationid"));
                      pagolekani = clrs.getString(clrs.getColumnIndex("pagolekani"));
                      barcode = clrs.getString(clrs.getColumnIndex("barcode"));
                      fat = clrs.getString(clrs.getColumnIndex("fat"));
                      temperature = clrs.getString(clrs.getColumnIndex("temperature"));
                      ph = clrs.getString(clrs.getColumnIndex("ph"));
                      amount = clrs.getString(clrs.getColumnIndex("amount"));

                  } while (clrs.moveToNext());

              }

              if (!clrs.isClosed()) {
                  clrs.close();
              }

          }


          Integer arrayindx = 0;

          String supidList[] = new String[]{};
          String iteidList[] = new String[]{};
          String quantityList[] = new String[]{};
          String remarksList[] = new String[]{};

          Cursor cllrs = mydb.getCollectionLinesData();

          if (!cllrs.isAfterLast()) {

              if (cllrs.moveToFirst()) {

                  do {

                      supidList = Arrays.copyOf(supidList, supidList.length + 1);
                      iteidList = Arrays.copyOf(iteidList, iteidList.length + 1);
                      quantityList = Arrays.copyOf(quantityList, quantityList.length + 1);
                      remarksList = Arrays.copyOf(remarksList, remarksList.length + 1);
                      supidList[arrayindx] = cllrs.getString(cllrs.getColumnIndex("supid"));
                      iteidList[arrayindx] = cllrs.getString(cllrs.getColumnIndex("iteid"));
                      quantityList[arrayindx] = cllrs.getString(cllrs.getColumnIndex("amount"));
                      remarksList[arrayindx] = cllrs.getString(cllrs.getColumnIndex("remarks"));

                      arrayindx++;

                  } while (cllrs.moveToNext());

              }

              if (!cllrs.isClosed()) {
                  cllrs.close();
              }

          }

         /*get last aa of permanet collections */

          newcollection_aa = "1";
          String collid = "0";

          Cursor lastcol = mydb.getLastTodayCollection_aa(todaydate, routeid, stationid, pagolekani);

          if (!lastcol.isAfterLast()) {

              if (lastcol.moveToFirst()) {

                  do {

                      String last_aa = lastcol.getString(lastcol.getColumnIndex("last_aa"));
                      if (last_aa != null) {
                          newcollection_aa = Integer.toString(Integer.parseInt(last_aa) + 1);
                      }
                  } while (lastcol.moveToNext());

              }
              if (!lastcol.isClosed()) {
                  lastcol.close();
              }
          }

        /*insert current collectioon to permanent collection and get id of permanet collection */

          if (mydb.InsertPermanentCollection(todaydate, newcollection_aa, routeid, stationid, pagolekani, barcode, fat, temperature, ph, amount)) {

              Cursor percol = mydb.getCollectionId(todaydate, newcollection_aa, routeid, stationid, pagolekani);

              if (!percol.isAfterLast()) {

                  if (percol.moveToFirst()) {

                      do {

                          collid = percol.getString(percol.getColumnIndex("id"));

                      } while (percol.moveToNext());

                  }

                  if (!percol.isClosed()) {
                      percol.close();
                  }
              } else {
                  String errormessage = "Πρόβλημα στην Kαταχώρηση της Παραλαβής!!";
                  AlertDialog.Builder diaBox = MessageAlert(errormessage);
                  return;
              }
          }

        /*get last aa  tradecheck (dromologionum) */

          String dromologionum = "1";
          String trchkiscanceled = "0";

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

                                  } while (trch.moveToNext());

                              }

                              if (!trch.isClosed()) {
                                  trch.close();
                              }
                          }

                          if (trchkiscanceled.equals("1")) {

                              dromologionum = Integer.toString(Integer.parseInt(last_aa));

                          } else {
                              dromologionum = Integer.toString(Integer.parseInt(last_aa) + 1);
                          }

                      }
                  } while (lasttrch.moveToNext());

              }
              if (!lasttrch.isClosed()) {
                  lasttrch.close();
              }
          }

        /*insert collection trades and tradelines */

          newdsrno = dsr1nu;

          int lastdsrno = Integer.parseInt(dsr1nu);

          Calendar newCalendar = Calendar.getInstance();
          SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
          String nowtime=timeFormatter.format(newCalendar.getTime());

          for (int i = 0; i < supidList.length; i++) {

              if (quantityList[i].length() != 0) {

                  String tradeid = "0";

                  newdsrno = Integer.toString(lastdsrno + 1);

                  String tradecode = dsr1cod + "-" + newdsrno;


                  if (mydb.InsertCollectionTrade(client, domaintype, collid, dsr1cod, newdsrno, tradecode, todaydate,nowtime, supidList[i], routeid, pagolekani, fat, temperature, ph, remarksList[i], transpid, salsmid, dromologionum, "0", "0", "0")) {

                      Cursor trrs = mydb.getTradeData(domaintype, dsr1cod, newdsrno, supidList[i], "0");

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

                          Cursor chrrs = mydb.getChambersData();

                          if (!chrrs.isAfterLast()) {

                              if (chrrs.moveToFirst()) {

                                  do {
                                      String chamberid = chrrs.getString(chrrs.getColumnIndex("chamberid"));
                                      String chambequantity = chrrs.getString(chrrs.getColumnIndex("amount"));
                                      double qtyfraction = Double.parseDouble(quantityList[i]) / Double.parseDouble(amount);
                                      long tradelineqty = Math.round(Double.parseDouble(chambequantity) * qtyfraction);
                                      String tradelineqtystring = Long.toString(tradelineqty);
                                      String aa = Integer.toString(lineno);
                                      lineno++;

                                      if (mydb.InsertCollectionTradeLines(aa, tradeid, iteidList[i], tradelineqtystring, chamberid, barcode)) {

                                          if (mydb.getTradeLineRowCount(aa, tradeid) == 0) {

                                              mydb.DeleteTradeLines("tradelines", tradeid);
                                              mydb.DeleteRowFromTable("trades", tradeid);

                                              String errormessage = "Πρόβλημα στην Δημιουργία των Παραστατικών!!";
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
                          String errormessage = "Πρόβλημα στην Δημιουργία των Παραστατικών!!";
                          AlertDialog.Builder diaBox = MessageAlert(errormessage);
                          return;
                      }

                  } else {
                      String errormessage = "Πρόβλημα στην Δημιουργία των Παραστατικών!!";
                      AlertDialog.Builder diaBox = MessageAlert(errormessage);
                      return;
                  }

                  lastdsrno++;
              }

          }


          mydb.updateConfiglastdsr(1, newdsrno);

          tdadescreated = true;

          final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
          globalVariables.setnewcollection_aa(newcollection_aa);

          Intent in = new Intent(getContext(), Collections.class);
          in.putExtra("station", station);
          in.putExtra("stationid", stationid);
          in.putExtra("barcode", pagolekani);
          in.putExtra("rtcodeid", rtcodeid);
          in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(in);
          getActivity().finish();
   }

    public void GetQualityData() {

        String samplecodestring="";
        String fatstring="";
        String temperaturestring="";
        String phstring="";

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

                Cursor c= mydb.getCollectionData();

                if (!c.isAfterLast()) {
                    if (c.moveToFirst()) {

                        do {

                            samplecodestring= c.getString(c.getColumnIndex("barcode"));
                            fatstring= c.getString(c.getColumnIndex("fat"));
                            temperaturestring= c.getString(c.getColumnIndex("temperature"));
                            phstring= c.getString(c.getColumnIndex("ph"));


                        } while (c.moveToNext());//Move the cursor to the next row.

                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

                samplecode.setText((CharSequence) samplecodestring);
                fat.setText((CharSequence) fatstring);
                temperature.setText((CharSequence) temperaturestring);
                ph.setText((CharSequence) phstring);

            } catch (Exception e) {
                //Toast.makeText(getActivity(), "Δεν Βρέθηκαν Στοιχεία για τους Θαλάμους " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
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

