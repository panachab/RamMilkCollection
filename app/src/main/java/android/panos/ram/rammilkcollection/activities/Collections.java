package android.panos.ram.rammilkcollection.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.adapters.CollectionsCustomAdapter;
import android.panos.ram.rammilkcollection.models.CollectionHeaderInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradeLinesInfo;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.panos.ram.rammilkcollection.models.UploadTrades;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


  public class Collections extends AppCompatActivity {

    String station;
    String stationid;
    String barcode;
    String rtcodeid;

    String todaydate;
    String printer;
    String route;
    String collection_aa="0";
    String message="";

    Boolean canceltdadescreated=false;

    String dialogchoiceList [];
    String dialogchoicetextList[];

    String docheader="";
    String doctrade[];
      
    int displayheight;
    private TextView stationView;
    private TextView barcodeView;

    private Button newcollectionButton;
    private CollectionsCustomAdapter collectionsCustomAdapter;
    private ArrayList<CollectionHeaderInfo> collectionList = new ArrayList<CollectionHeaderInfo>();

    private LinkedHashMap<String, CollectionHeaderInfo> collectionsubjects = new LinkedHashMap<String, CollectionHeaderInfo>();
    LinkedHashMap<String, CollectionTradesInfo> tradesubjects = new LinkedHashMap<String, CollectionTradesInfo>();
    LinkedHashMap<String, CollectionTradeLinesInfo> tradelinessubjects = new LinkedHashMap<String, CollectionTradeLinesInfo>();

    private ExpandableListView collectionsListView;

    DBHelper handler;

      BluetoothAdapter mBluetoothAdapter;
      BluetoothSocket mmSocket;
      BluetoothDevice mmDevice=null;

      // needed for communication to bluetooth device / network
      OutputStream mmOutputStream;
      InputStream mmInputStream;
      Thread workerThread;

      byte[] readBuffer;
      int readBufferPosition;
      volatile boolean stopWorker;

      private BroadcastReceiver receiver = new BroadcastReceiver() {

          @Override
          public void onReceive(Context context, Intent intent) {
              Bundle bundle = intent.getExtras();
              if (bundle != null) {
                  int resultCode = bundle.getInt(UploadTrades.RESULT);
                  if (resultCode == RESULT_OK) {

                      Toast.makeText(getApplicationContext(),"Επιτυχής Αποστολή των Παραστατικών στο Κεντρικό!!" , Toast.LENGTH_LONG).show();

                      }

                      else if   (resultCode == RESULT_CANCELED) {

                          Toast.makeText(getApplicationContext(), "Ανεπιτυχής Αποστολή των Παραστατικών στο Κεντρικό!Ελέγξτε την Σύνδεσή σας στο Internet!!", Toast.LENGTH_LONG).show();
                      }

                      else {

                      Toast.makeText(getApplicationContext(), "Δεν έχετε δικαίωμα Αποστολής Παραστατικών στο Κεντρικό!Επικοινωνήστε με τον Διαχειριστή της Εφαρμογής!!", Toast.LENGTH_LONG).show();
                  }
              }
          }
      };


      @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collections);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Παραλαβές Γάλακτος</small>"));

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaydate = dateFormatter.format(newCalendar.getTime());

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        displayheight = dm.heightPixels;

        stationView=(TextView)  findViewById(R.id.station);
        barcodeView=(TextView)  findViewById(R.id.barcode);

        newcollectionButton = (Button) findViewById(R.id.newcollectionButton);

        handler=new DBHelper(this);

        newcollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handler.deleteTable("collection");
                handler.deleteTable("collectionlines");
                handler.deleteTable("collectionchamberlines");

                Intent in = new Intent(getApplicationContext(), CollectionTabsActivity.class);
                in.putExtra("station", station);
                in.putExtra("stationid", stationid);
                in.putExtra("barcode", barcode);
                in.putExtra("rtcodeid", rtcodeid);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();



            }
        });

        Intent in =  this.getIntent();
        station = String.valueOf(in.getExtras().getString("station"));
        stationid = String.valueOf(in.getExtras().getString("stationid"));
        barcode = String.valueOf(in.getExtras().getString("barcode"));
        rtcodeid = String.valueOf(in.getExtras().getString("rtcodeid"));

        stationView.setText((CharSequence) station);
        barcodeView.setText((CharSequence) barcode);




        LoadCollectionsData();

        collectionsListView = (ExpandableListView) findViewById(R.id.collectionsList);
        collectionsListView.getLayoutParams().height = 20*displayheight;

        collectionsCustomAdapter = new CollectionsCustomAdapter(this, collectionList);

        collectionsListView.setAdapter(collectionsCustomAdapter);

        // OPTIONAL : Show one list at a time
        collectionsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                    collectionsListView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }

            }
        });


        collectionsListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Boolean expandcollapse=false;

                if (groupPosition >= 0) {


                    long viewId = v.getId();

                    if (viewId == R.id.cancelButton) {

                        collapseAll();

                        expandcollapse=true;

                        CollectionHeaderInfo collectionInfo= collectionList.get(groupPosition);

                        collection_aa=collectionInfo.getAA();

                        message="Θέλετε να Ακυρώσετε την Παραλαβή " + collection_aa + " ;";

                        AlertDialog.Builder builder = new AlertDialog.Builder(Collections.this);
                        builder.setTitle(R.string.app_name);
                        builder.setMessage(message);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setPositiveButton("NAI", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                CancelDocuments(collection_aa);
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

                    if (viewId == R.id.printButton) {

                        collapseAll();

                        expandcollapse=true;
                        String coliscanceled ="0";

                        CollectionHeaderInfo collectionInfo= collectionList.get(groupPosition);

                        collection_aa=collectionInfo.getAA();

                        handler = new DBHelper(getApplicationContext());


                        Cursor col = handler.getCollectionIscanceled(todaydate,collection_aa,rtcodeid,stationid,barcode);

                        if (!col.isAfterLast()) {

                            if (col.moveToFirst()) {

                                do {

                                    coliscanceled = col.getString(col.getColumnIndex("iscanceled"));

                                } while (col.moveToNext());

                            }
                            if (!col.isClosed()) {
                                col.close();
                            }
                        }


                        if (coliscanceled.equals("0")) {

                            PrintDocuments(collection_aa,"0");

                        }
                        else {


                            final Dialog dialog = new Dialog(Collections.this);

                            dialogchoiceList = new String[]{"1","2"};
                            dialogchoicetextList= new String[]{"ΚΑΝΟΝΙΚΑ","ΑΚΥΡΩΤΙΚΑ"};

                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.printdocument_dialog_listview);

                            Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
                            btndialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            ListView listView = (ListView) dialog.findViewById(R.id.listview);
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.printdocument_list_item, R.id.documenttype, dialogchoicetextList);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (dialogchoiceList[position].equals("1")) {
                                        PrintDocuments(collection_aa, "0");;
                                    }
                                    else
                                    {
                                        PrintDocuments(collection_aa, "1");
                                    }
                                    dialog.dismiss();

                                }
                            });

                            dialog.show();


                        }

                    }

                }


                return expandcollapse;

            }
        });

        final GlobalClass globalVariables = (GlobalClass) getApplicationContext();
        final String callingForm=globalVariables.getcallingForm();
        final String new_collection_aa=globalVariables.getnewcollection_aa();
        final String collect_aa=globalVariables.getcollection_aa();

        if (callingForm=="CollectionsReload") {

            if (collect_aa != null) {
                if (!collect_aa.equals("0")) {
                    message = "Τα Ακυρωτικά Παραστατικά Δημιουργήθηκαν με Επιτυχία!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);
                    Intent intent = new Intent(this, UploadTrades.class);
                    startService(intent);
                    PrintDocuments(collect_aa, "1");
                }
            }
        }

        if (callingForm !=null) {

            if (callingForm=="Collection") {
                if (new_collection_aa != null) {
                    if (!new_collection_aa.equals("0")) {
                        message = "Επιτυχής Δημιουργία Παραστατικών!!";
                        AlertDialog.Builder diaBox = MessageAlert(message);
                        Intent intent = new Intent(this, UploadTrades.class);
                        startService(intent);
                        PrintDocuments(new_collection_aa, "0");
                    }
                }
            }
        }

        globalVariables.setcallingForm("Collection");
        globalVariables.setnewcollection_aa("0");
        globalVariables.setcollection_aa("0");

    }

      @Override
      protected void onResume() {
          super.onResume();
          registerReceiver(receiver, new IntentFilter(UploadTrades.NOTIFICATION));
      }
      @Override
      protected void onPause() {
          super.onPause();
          unregisterReceiver(receiver);
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

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.
          int id = item.getItemId();

          //noinspection SimplifiableIfStatement
          if (id == android.R.id.home) {
              // finish the activity
              onBackPressed();
              return true;
          }

          return super.onOptionsItemSelected(item);

      }

    //method to collapse all groups
    private void collapseAll() {
        int count = collectionsCustomAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            collectionsListView.collapseGroup(i);
        }
    }

    private int addCollection(String col_aa, String sample,String fat, String temperature,String ph,
                              String tradecode, String supplier,String milktype,String tr_quantity,
                              String trl_aa ,String chamber, String trl_quantity){

        int groupPosition = 0;

        //check the hash map if the group already exists
        CollectionHeaderInfo collectionInfo =  collectionsubjects.get(col_aa);
        //add the group if doesn't exists
        if(collectionInfo == null) {
            collectionInfo = new CollectionHeaderInfo();
            collectionInfo.setAA(col_aa);
            collectionInfo.setSample(sample);
            collectionInfo.setFat(fat);
            collectionInfo.setTemperature(temperature);
            collectionInfo.setPh(ph);
            collectionsubjects.put(col_aa, collectionInfo);
            collectionList.add(collectionInfo);
        }
        //get the subgroup for the group
        ArrayList<CollectionTradesInfo> tradeslist = collectionInfo.getTradesList();
        //create a new subgroup and add that to the group
        CollectionTradesInfo tradeInfo = tradesubjects.get(tradecode);
        if(tradeInfo == null) {
            tradeInfo = new CollectionTradesInfo();
            tradeInfo.setTradecode(tradecode);
            tradeInfo.setSupplier(supplier);
            tradeInfo.setMilktype(milktype);
            tradeInfo.setQuantity(tr_quantity);
            tradesubjects.put(tradecode, tradeInfo);
            tradeslist.add(tradeInfo);
            collectionInfo.setTradesList(tradeslist);
        }
        //get the child for the group
        ArrayList<CollectionTradeLinesInfo> tradelineslist = tradeInfo.getTradeLinesList();

        //create a new child and add that to the group
        CollectionTradeLinesInfo tradelinesInfo = tradelinessubjects.get(tradecode + "-" + trl_aa);
        if(tradelinesInfo == null) {
            tradelinesInfo = new CollectionTradeLinesInfo();
            tradelinesInfo.setAA(trl_aa);
            tradelinesInfo.setChamber(chamber);
            tradelinesInfo.setQuantity(trl_quantity);
            tradelinessubjects.put(tradecode + "-" + trl_aa, tradelinesInfo);
            tradelineslist.add(tradelinesInfo);
            tradeInfo.setTradeLinesList(tradelineslist);
        }
        //find the group position inside the list
        groupPosition = collectionList.indexOf(collectionInfo);
        return groupPosition;
    }

     public void CancelDocuments(String collection_aa) {

         if (!canceltdadescreated) {

             String iscanceled = "0";
             String collid = "0";

             handler = new DBHelper(this);

             Cursor col = handler.getCollectionIscanceled(todaydate, collection_aa, rtcodeid, stationid, barcode);

             if (!col.isAfterLast()) {

                 if (col.moveToFirst()) {

                     do {

                         iscanceled = col.getString(col.getColumnIndex("iscanceled"));
                         collid = col.getString(col.getColumnIndex("id"));

                     } while (col.moveToNext());

                 }
                 if (!col.isClosed()) {
                     col.close();
                 }
             }

             if (iscanceled.equals("1")) {

                 message = "Η Παραλαβή είναι ήδη Ακυρωμένη!!";
                 AlertDialog.Builder diaBox = MessageAlert(message);

                 return;

             }

             String last_id = "0";

             Cursor lcol = handler.getLastTodayCollection(todaydate);

             if (!lcol.isAfterLast()) {

                 if (lcol.moveToFirst()) {

                     do {

                         last_id = lcol.getString(lcol.getColumnIndex("last_id"));

                     } while (lcol.moveToNext());

                 }
                 if (!lcol.isClosed()) {
                     lcol.close();
                 }
             }

             /* Ακύρωση μόνο της τελευταίας καταγραφής

             if (!collid.equals(last_id)) {

                 message = "Η Παραλαβή πρέπει να είναι η Τελευταία της Ημέρας!!";
                 AlertDialog.Builder diaBox = MessageAlert(message);

                 return;

             }*/


             Cursor trcol = handler.getCollectionTradesIsChecked(collid);

             if (!trcol.isAfterLast()) {

                 if (!trcol.isClosed()) {
                     trcol.close();
                 }

                 message = "Έχει γίνει Έλεγχος Δρομολογίου για αυτήν την Παραλαβή!" + "\n" + "Αδύνατη η Ακύρωση!";
                 AlertDialog.Builder diaBox = MessageAlert(message);

                 return;

             }

             String trid = "";

             String client = "";
             String domaintype = "";
             String dsrcod = "";
             String dsrno = "";
             String tradecode = "";
             String supid = "";
             String routeid = "";
             String pagolekani = "";
             String fat = "";
             String temperature = "";
             String ph = "";
             String remarks = "";
             String transpid = "";
             String salsmid = "";
             String dromologionum = "";

             String canceltrid = "";
             String notes = "";

             Cursor trc;


             trc = handler.getCollectionTradesDataToCancel(collid);

             if (!trc.isAfterLast()) {
                 if (trc.moveToFirst()) {

                     do {

                         trid = trc.getString(trc.getColumnIndex("id"));
                         client = trc.getString(trc.getColumnIndex("cid"));
                         domaintype = trc.getString(trc.getColumnIndex("domaintype"));
                         dsrcod = trc.getString(trc.getColumnIndex("cancelseries"));
                         dsrno = trc.getString(trc.getColumnIndex("docnum"));
                         tradecode = dsrcod + "-" + dsrno;
                         supid = trc.getString(trc.getColumnIndex("supid"));
                         routeid = trc.getString(trc.getColumnIndex("shvid"));
                         pagolekani = trc.getString(trc.getColumnIndex("zpagolekaniid2"));
                         fat = trc.getString(trc.getColumnIndex("zfat"));
                         temperature = trc.getString(trc.getColumnIndex("ztemperature"));
                         ph = trc.getString(trc.getColumnIndex("zph"));
                         remarks = trc.getString(trc.getColumnIndex("tradecode"));
                         transpid = trc.getString(trc.getColumnIndex("trsid"));
                         salsmid = trc.getString(trc.getColumnIndex("salesmanid"));
                         dromologionum = trc.getString(trc.getColumnIndex("dromologionum"));



                         Calendar newCalendar = Calendar.getInstance();
                         SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
                         String nowtime=timeFormatter.format(newCalendar.getTime());

                         if (handler.InsertCollectionTrade(client, domaintype, collid, dsrcod, dsrno, tradecode, todaydate,nowtime, supid, routeid, pagolekani, fat, temperature, ph, remarks, transpid, salsmid, dromologionum, "1", "0", "0")) {

                             Cursor trrs = handler.getTradeData(domaintype, dsrcod, dsrno, supid, "1");

                             if (!trrs.isAfterLast()) {

                                 if (trrs.moveToFirst()) {

                                     do {
                                         canceltrid = trrs.getString(trrs.getColumnIndex("id"));
                                     } while (trrs.moveToNext());

                                 }

                                 if (!trrs.isClosed()) {
                                     trrs.close();
                                 }

                                 Cursor trlrs = handler.getCollectionTradeLines(trid);

                                 if (!trlrs.isAfterLast()) {

                                     if (trlrs.moveToFirst()) {

                                         do {

                                             String trl_aa = trlrs.getString(trlrs.getColumnIndex("trl_aa"));
                                             String trl_qty = trlrs.getString(trlrs.getColumnIndex("trl_qty"));
                                             String trl_sample = trlrs.getString(trlrs.getColumnIndex("trl_sample"));
                                             String trl_chamber = trlrs.getString(trlrs.getColumnIndex("trl_chamber"));
                                             String trl_iteid = trlrs.getString(trlrs.getColumnIndex("trl_iteid"));

                                             if (handler.InsertCollectionTradeLines(trl_aa, canceltrid, trl_iteid, trl_qty, trl_chamber, trl_sample)) {

                                                 if (handler.getTradeLineRowCount(trl_aa, canceltrid) == 0) {

                                                     handler.DeleteTradeLines("tradelines", canceltrid);
                                                     handler.DeleteRowFromTable("trades", canceltrid);

                                                     String errormessage = "Πρόβλημα στην Δημιουργία των Ακυρωτικών Παραστατικών!!";
                                                     AlertDialog.Builder diaBox = MessageAlert(errormessage);
                                                     return;

                                                 }
                                             }

                                         } while (trlrs.moveToNext());


                                     }

                                     if (!trlrs.isClosed()) {
                                         trlrs.close();
                                     }


                                 }
                             } else {
                                 String errormessage = "Πρόβλημα στην Δημιουργία των Ακυρωτικών Παραστατικών!!";
                                 AlertDialog.Builder diaBox = MessageAlert(errormessage);
                                 return;
                             }


                         } else {
                             String errormessage = "Πρόβλημα στην Δημιουργία των Ακυρωτικών Παραστατικών!!";
                             AlertDialog.Builder diaBox = MessageAlert(errormessage);
                             return;
                         }


                     } while (trc.moveToNext());

                 }
                 if (!trc.isClosed()) {
                     trc.close();
                 }
             }


             if (handler.UpdateCanceledCollectionTrades(collid, "1")) {

                 if (handler.UpdatePermanetCollectionIscancel(collid, "1")) {

                     canceltdadescreated=true;


                     final GlobalClass globalVariables = (GlobalClass) getApplicationContext();
                     globalVariables.setcallingForm("CollectionsReload");
                     globalVariables.setcollection_aa(collection_aa);

                     startActivity(getIntent());
                     finish();


                 }

             }

         }
     }

     public void PrintDocuments(String collection_aa,String iscancel) {


        if (mmDevice == null) {

            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter == null) {

                    message = getResources().getString(R.string.bt_notexists_msg);
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;

                }

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {

                        // printer is the name of the bluetooth printer device
                        // we got this name from the list of paired devices
                        if (device.getName().equals(printer)) {
                            mmDevice = device;
                            break;
                        }
                    }
                }


            } catch (Exception e) {

                message = getResources().getString(R.string.printer_search_problem);
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;
            }
        }

        if (mmDevice == null) {

            message = getResources().getString(R.string.printer_notfount_msg);
            AlertDialog.Builder diaBox = MessageAlert(message);

            return;

        }

        else

            {

            try {

                // Standard SerialPortService ID
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();

                //beginListenForData();

                //myLabel.setText("Bluetooth Opened");

            } catch (Exception e) {

                message = getResources().getString(R.string.conn_problem_msg);
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;
            }

        }

         Cursor rsc = handler.getCompanyData(1);

         rsc.moveToFirst();

         String company_name="" ;
         String company_address="" ;
         String company_city="";
         String company_zipcode="";
         String company_afm="";
         String company_doy="";
         String company_phone1="";
         String company_phone2="";
         String company_fax="";
         String company_argemi="";


         if (!rsc.isAfterLast()) {

             company_name=rsc.getString(rsc.getColumnIndex("name"));
             company_address=rsc.getString(rsc.getColumnIndex("address"));
             company_city = rsc.getString(rsc.getColumnIndex("city"));
             company_zipcode = rsc.getString(rsc.getColumnIndex("zipcode"));
             company_afm=rsc.getString(rsc.getColumnIndex("afm"));
             company_doy = rsc.getString(rsc.getColumnIndex("doy"));
             company_phone1 = rsc.getString(rsc.getColumnIndex("phone1"));
             company_phone2 = rsc.getString(rsc.getColumnIndex("phone2"));
             company_fax = rsc.getString(rsc.getColumnIndex("fax"));
             company_argemi = rsc.getString(rsc.getColumnIndex("argemi"));


         }

         if (!rsc.isClosed()) {
             rsc.close();
         }

         String phones=company_phone1.toString();

         if (!company_phone2.equals("")) {

             phones=phones + " , " + company_phone2.toString();
         }

        String collid="";

        String tridList[]={};
        String tr_qtyList[]={};

        String tradecode="";

        String hmer="";
        String time="";


        String sup_name="";
        String sup_street="";
        String sup_city="";
        String sup_zipcode="";
        String sup_afm="";
        String sup_doy="";
        String docdescr="";
        String tr_qty="";
        String driver="";
        String transportation="";

        String trl_aa="";
        String trl_qty="";
        String trl_sample="";
        String trl_chamber="";
        String mat_descr="";

         doctrade = new String[]{};

        Integer arrayindx = 0;

        docheader = company_name;
        docheader=docheader +"\n";
        docheader=docheader + "ΕΔΡΑ : " + company_address;
        docheader=docheader +"\n";
        docheader=docheader + "ΤΗΛ.ΚΕΝΤΡΟ : " + phones;
        docheader=docheader +"\n";
        docheader=docheader + "FAX: " + company_fax + " - " + company_city + " " + company_zipcode;
        docheader=docheader +"\n";
        docheader=docheader + "ΑΡ.Γ.Ε.ΜΗ. : " + company_argemi;
        docheader=docheader +"\n";
        docheader=docheader + "Α.Φ.Μ. : " + company_afm + " - Δ.Ο.Υ. : " + company_doy;
        docheader=docheader +"\n";
        docheader=docheader +"\n";
        docheader=docheader +"\n";

        if (!mmSocket.isConnected()) {

            message = getResources().getString(R.string.conn_fail_msg);
            AlertDialog.Builder diaBox = MessageAlert(message);

            return;
        }
        else

        {

            handler = new DBHelper(this);

            Cursor col = handler.getCollectionId(todaydate,collection_aa,rtcodeid,stationid,barcode);

            if (!col.isAfterLast()) {

                if (col.moveToFirst()) {

                    do {

                        collid = col.getString(col.getColumnIndex("id"));

                    } while (col.moveToNext());

                }
                if (!col.isClosed()) {
                    col.close();
                }
            }


            Cursor trc;

            trc = handler.getCollectionTradesData(collid,iscancel);

            if (!trc.isAfterLast()) {
                if (trc.moveToFirst()) {

                    do {

                        doctrade = Arrays.copyOf(doctrade, doctrade.length + 1);
                        tridList = Arrays.copyOf(tridList, tridList.length + 1);
                        tr_qtyList = Arrays.copyOf(tr_qtyList, tr_qtyList.length + 1);

                        tridList[arrayindx] = trc.getString(trc.getColumnIndex("trid"));
                        tr_qtyList[arrayindx] = trc.getString(trc.getColumnIndex("tr_qty"));

                        tradecode = trc.getString(trc.getColumnIndex("tradecode"));
                        hmer = trc.getString(trc.getColumnIndex("hmer"));
                        time = trc.getString(trc.getColumnIndex("time"));
                        sup_name = trc.getString(trc.getColumnIndex("sup_name"));
                        sup_city = trc.getString(trc.getColumnIndex("sup_city"));
                        sup_afm = trc.getString(trc.getColumnIndex("sup_afm"));
                        docdescr = trc.getString(trc.getColumnIndex("docdescr"));
                        driver = trc.getString(trc.getColumnIndex("driver"));
                        transportation = trc.getString(trc.getColumnIndex("tranportation"));

                        doctrade[arrayindx] = docheader + hmer + "      " + time;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + sup_city;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + docdescr;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + tradecode;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + "ONOMA: " + sup_name;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + "ΑΦΜ: " + sup_afm;
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                        doctrade[arrayindx] = doctrade[arrayindx] + String.format("%1$-21s %2$8s %3$12s %4$4s","ΕΙΔΟΣ","ΠΟΣΟΤΗΤΑ","ΔΕΙΓΜΑ","ΘΑΛ");
                        doctrade[arrayindx] = doctrade[arrayindx] + "\n";

                        arrayindx++;

                    } while (trc.moveToNext());

                }
                if (!trc.isClosed()) {
                    trc.close();
                }
            }


            for (int i = 0; i < tridList.length; i++) {

                Cursor trlc;

                trlc = handler.getCollectionTradeLines(tridList[i]);


                if (!trlc.isAfterLast()) {

                    if (trlc.moveToFirst()) {

                        do {

                            trl_aa = trlc.getString(trlc.getColumnIndex("trl_aa"));
                            trl_qty = trlc.getString(trlc.getColumnIndex("trl_qty"));
                            trl_sample = trlc.getString(trlc.getColumnIndex("trl_sample"));
                            trl_chamber = trlc.getString(trlc.getColumnIndex("trl_chamber"));
                            mat_descr = trlc.getString(trlc.getColumnIndex("mat_descr"));

                            doctrade[i] = doctrade[i] + String.format("%1$-21s %2$8s %3$12s %4$4s",mat_descr,trl_qty,trl_sample,trl_chamber);
                            doctrade[i] = doctrade[i] + "\n";


                        } while (trlc.moveToNext());

                    }
                    if (!trlc.isClosed()) {
                        trlc.close();
                    }
                }

                doctrade[i]=doctrade[i] + "\n\n";
                doctrade[i]=doctrade[i] + "ΣΥΝΟΛΙΚΗ ΠΟΣΟΤΗΤΑ    " + tr_qtyList[i];
                doctrade[i]=doctrade[i] + "\n\n";
                doctrade[i]=doctrade[i] + "ΟΔΗΓΟΣ: " + driver;
                doctrade[i]=doctrade[i] + "\n";
                doctrade[i]=doctrade[i] + "ΑΥΤΟΚΙΝΗΤΟ: " + transportation;
                doctrade[i]=doctrade[i] + "\n";
                doctrade[i]=doctrade[i] + "ΠΑΡΑΛΗΠΤΗΡΙΟ: " + station;
                doctrade[i]=doctrade[i] + "\n";

                String doccontent1=doctrade[i]+ "ΠΡΩΤΟΤΥΠΟ";
                String doccontent2=doctrade[i]+ "ΑΝΤΙΓΡΑΦΟ";

                doccontent1=doccontent1 + "\n";
                doccontent2=doccontent2 + "\n";

                doccontent1=doccontent1 + "ΔΡΟΜΟΛΟΓΙΟ " + route;
                doccontent2=doccontent2 + "ΔΡΟΜΟΛΟΓΙΟ " + route;

                doccontent1=doccontent1 + "\n\n";
                doccontent2=doccontent2 + "\n\n";

                doccontent1=doccontent1 + "\n\n";
                doccontent2=doccontent2 + "\n\n";

                String doccontent=doccontent1+doccontent2;



                try {


                   mmOutputStream.write(doccontent.getBytes("ISO-8859-7"));

                    Thread.sleep(4000);


                } catch (Exception e) {

                    message = getResources().getString(R.string.print_error_msg);
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;
                }


            }

            try {
                closeBT();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

     }

     public void LoadCollectionsData() {


          Cursor rsc = handler.getConfigData(1);

          rsc.moveToFirst();

          if (!rsc.isAfterLast()) {

              printer=rsc.getString(rsc.getColumnIndex("printer"));

          }

          if (!rsc.isClosed()) {
              rsc.close();
          }

          Cursor rsr = handler.getRouteData(rtcodeid);

          rsr.moveToFirst();

          if (!rsr.isAfterLast()) {

              route=rsr.getString(rsr.getColumnIndex("descr"));

          }

          if (!rsr.isClosed()) {
              rsr.close();
          }



          String coll_aa, coll_samplecode,coll_fat,coll_temperature,coll_ph,
                tr_code,sup_name,mat_descr ,tr_qty,trl_aa,trl_chamber,trl_qty="";



        Cursor rs = handler.getCollectionsData(todaydate,stationid,rtcodeid,barcode);

        rs.moveToFirst();

        if (!rs.isAfterLast()) {

            do {
                coll_aa = rs.getString(rs.getColumnIndex("coll_aa"));
                coll_samplecode = rs.getString(rs.getColumnIndex("coll_samplecode"));
                coll_fat = rs.getString(rs.getColumnIndex("coll_fat"));
                coll_temperature = rs.getString(rs.getColumnIndex("coll_temperature"));
                coll_ph = rs.getString(rs.getColumnIndex("coll_ph"));
                tr_code = rs.getString(rs.getColumnIndex("tr_code"));
                tr_qty = rs.getString(rs.getColumnIndex("tr_qty"));
                sup_name = rs.getString(rs.getColumnIndex("sup_name"));
                mat_descr = rs.getString(rs.getColumnIndex("mat_descr"));
                trl_aa = rs.getString(rs.getColumnIndex("trl_aa"));
                trl_chamber = rs.getString(rs.getColumnIndex("trl_chamber"));
                trl_qty = rs.getString(rs.getColumnIndex("trl_qty"));

                addCollection(coll_aa,coll_samplecode,coll_fat,coll_temperature,coll_ph,
                        tr_code,sup_name,mat_descr,tr_qty,trl_aa,trl_chamber,trl_qty);


            } while (rs.moveToNext());//Move the cursor to the next row.


        } else {
            Toast.makeText(this, "Δεν Βρέθηκαν Παραλαβές!!", Toast.LENGTH_LONG).show();
            return;
        }


      }

      private AlertDialog.Builder MessageAlert(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Collections.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton("OK", null);
        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);


        return builder;

      }

      void findBT() {

          try {
              mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

              if(mBluetoothAdapter == null) {

                  //myLabel.setText("No bluetooth adapter available");

              }

              if(!mBluetoothAdapter.isEnabled()) {
                  Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                  startActivityForResult(enableBluetooth, 0);
              }

              Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

              if(pairedDevices.size() > 0) {
                  for (BluetoothDevice device : pairedDevices) {

                      // printer is the name of the bluetooth printer device
                      // we got this name from the list of paired devices
                      if (device.getName().equals(printer)) {
                          mmDevice = device;
                          break;
                      }
                  }
              }

              //myLabel.setText("Bluetooth device found.");

          }catch(Exception e){
              e.printStackTrace();
          }
      }

      // tries to open a connection to the bluetooth printer device
      void openBT() throws IOException {
          try {

              // Standard SerialPortService ID
              UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
              mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
              mmSocket.connect();
              mmOutputStream = mmSocket.getOutputStream();
              mmInputStream = mmSocket.getInputStream();

              beginListenForData();

          } catch (Exception e) {
              e.printStackTrace();
          }
      }

      void closeBT() throws IOException {
          try {
              stopWorker = true;
              mmOutputStream.close();
              mmInputStream.close();
              mmSocket.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

      void beginListenForData() {
          try {
              final Handler handler = new Handler();

              // this is the ASCII code for a newline character
              final byte delimiter = 10;

              stopWorker = false;
              readBufferPosition = 0;
              readBuffer = new byte[1024];

              workerThread = new Thread(new Runnable() {
                  public void run() {

                      while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                          try {

                              int bytesAvailable = mmInputStream.available();

                              if (bytesAvailable > 0) {

                                  byte[] packetBytes = new byte[bytesAvailable];
                                  mmInputStream.read(packetBytes);

                                  for (int i = 0; i < bytesAvailable; i++) {

                                      byte b = packetBytes[i];
                                      if (b == delimiter) {

                                          byte[] encodedBytes = new byte[readBufferPosition];
                                          System.arraycopy(
                                                  readBuffer, 0,
                                                  encodedBytes, 0,
                                                  encodedBytes.length
                                          );

                                          // specify US-ASCII encoding
                                          final String data = new String(encodedBytes, "US-ASCII");
                                          readBufferPosition = 0;

                                          // tell the user data were sent to bluetooth printer device
                                          handler.post(new Runnable() {
                                              public void run() {
                                                 //myLabel.setText(data);

                                                  String pabnsgos=data;
                                                  String mitsos="pp";

                                              }
                                          });

                                      } else {
                                          readBuffer[readBufferPosition++] = b;
                                      }
                                  }
                              }

                          } catch (IOException ex) {
                              stopWorker = true;
                          }

                      }
                  }
              });

              workerThread.start();

          } catch (Exception e) {
              e.printStackTrace();
          }
      }

  }
