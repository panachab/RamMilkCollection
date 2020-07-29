package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.TradeCheckTabsActivity;
import android.panos.ram.rammilkcollection.activities.TransferTabsActivity;
import android.panos.ram.rammilkcollection.adapters.TradeChecksCustomAdapter;
import android.panos.ram.rammilkcollection.models.TradeCheckHeaderInfo;
import android.panos.ram.rammilkcollection.models.TradeCheckInfo;
import android.panos.ram.rammilkcollection.models.TradeCheckLinesInfo;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.panos.ram.rammilkcollection.models.UploadTrades;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class TradeChecksFragment extends Fragment {

  String todaydate;
  String printer;
  String ventorwerks;

  String diakinisitradeid = "0";

  Boolean tradecreated=false;

  String doctrade[];

  String doctradelines[];
  String milktypegroups[][];
  int milktypegroupqtys[][];

  String tradelines[][];
  String message="";

  String factoriesList[];
  String vendorwerksList[];

   String dialogchoiceList [];
   String dialogchoicetextList[];
    
   int displayheight;

    private Button newcheckButton;
    private Button newdiakinisiButton;
    private Button newtransferButton;

    private TradeChecksCustomAdapter tradechecksCustomAdapter;
    private ArrayList<TradeCheckHeaderInfo> tradecheckList = new ArrayList<TradeCheckHeaderInfo>();
    
    private LinkedHashMap<String, TradeCheckHeaderInfo> tradechecksubjects = new LinkedHashMap<String, TradeCheckHeaderInfo>();
    LinkedHashMap<String, TradeCheckInfo> tradesubjects = new LinkedHashMap<String, TradeCheckInfo>();
    LinkedHashMap<String, TradeCheckLinesInfo> tradelinessubjects = new LinkedHashMap<String, TradeCheckLinesInfo>();
    
    private ExpandableListView tradechecksListView;

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

    public TradeChecksFragment() {}

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                int resultCode = bundle.getInt(UploadTrades.RESULT);

                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(),"Επιτυχής Αποστολή των Παραστατικών στο Κεντρικό!!" , Toast.LENGTH_LONG).show();
                }
                else if   (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Ανεπιτυχής Αποστολή των Παραστατικών στο Κεντρικό!Ελέγξτε την Σύνδεσή σας στο Internet!!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Δεν έχετε δικαίωμα Αποστολής Παραστατικών στο Κεντρικό!Επικοινωνήστε με τον Διαχειριστή της Εφαρμογής!!", Toast.LENGTH_LONG).show();
                }

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tradechecks, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(UploadTrades.NOTIFICATION));
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        
        super.onActivityCreated(savedInstanceState);

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaydate = dateFormatter.format(newCalendar.getTime());


        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        displayheight = dm.heightPixels;

         handler=new DBHelper(getActivity());

         newcheckButton = (Button) getView().findViewById(R.id.newcheckButton);

        newcheckButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              collapseAll();

              Cursor rs = handler.getNewTradesToCheck(todaydate);
              rs.moveToFirst();

              if (rs.isAfterLast()) {
                  if (!rs.isClosed()) {
                      rs.close();
                  }

                  message = "Τα Δρομολόγια έχουν Ελεγχθεί όλα!!";
                  AlertDialog.Builder diaBox = MessageAlert(message);


              } else {
                  if (!rs.isClosed()) {
                      rs.close();
                  }
                  Intent in = new Intent(getContext(), TradeCheckTabsActivity.class);

                  in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(in);

              }

          }
        });

        newdiakinisiButton = (Button) getView().findViewById(R.id.newdiakinisiButton);

        newdiakinisiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collapseAll();

                Cursor tcrs = handler.getTradeChecksData(todaydate);

                tcrs.moveToFirst();

                if (tcrs.isAfterLast()) {
                    if (!tcrs.isClosed()) {
                        tcrs.close();
                    }

                    message = "Δεν υπάρχουν Ελεγμένα Δρομολόγια!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;
                }

                if (!tcrs.isClosed()) {
                    tcrs.close();
                }

                factoriesList = new String[]{};
                vendorwerksList= new String[]{};

                Cursor frs = handler.getFactoriesData();

                frs.moveToFirst();

                int arrinx=0;

                if (!frs.isAfterLast()) {

                    do {

                        if (!frs.getString(frs.getColumnIndex("codeid")).equals(ventorwerks)) {
                            factoriesList = Arrays.copyOf(factoriesList, factoriesList.length + 1);
                            vendorwerksList = Arrays.copyOf(vendorwerksList, vendorwerksList.length + 1);
                            factoriesList[arrinx]=frs.getString(frs.getColumnIndex("descr"));
                            vendorwerksList[arrinx]=frs.getString(frs.getColumnIndex("codeid"));

                            arrinx++;
                        }

                    } while (frs.moveToNext());
                }
                if (!frs.isClosed()) {
                    frs.close();


                }


                TradeCheckHeaderInfo tradecheckInfo= tradecheckList.get(0);
                //get the child info

                final String tradecheck_aa=tradecheckInfo.getAA();

                final Dialog dialog = new Dialog(getContext());
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.factories_dialog_listview);

                Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
                btndialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ListView listView = (ListView) dialog.findViewById(R.id.listview);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.factories_list_item, R.id.factory, factoriesList);
                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CreateDiakinisiDocument(tradecheck_aa,vendorwerksList[position]);
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        newtransferButton = (Button) getView().findViewById(R.id.newtransferButton);

        newtransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collapseAll();

                Cursor tcrs = handler.getTradeChecksData(todaydate);

                tcrs.moveToFirst();

                if (tcrs.isAfterLast()) {
                    if (!tcrs.isClosed()) {
                        tcrs.close();
                    }

                    message = "Δεν υπάρχουν Ελεγμένα Δρομολόγια!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;
                }

                if (!tcrs.isClosed()) {
                    tcrs.close();
                }

                Cursor rs = handler.getNewTradesToCheck(todaydate);
                rs.moveToFirst();

                if (!rs.isAfterLast()) {
                    if (!rs.isClosed()) {
                        rs.close();
                    }

                    message = "Υπάρχουν Παραλαβές οι οποίες δεν έχουν Ελεγχθεί!" + "\n" + "Αδύνατη η Μετάγγιση Γάλατος!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;
                }
                if (!rs.isClosed()) {
                    rs.close();
                }

                TradeCheckHeaderInfo tradecheckInfo= tradecheckList.get(0);

                final String tradecheck_aa=tradecheckInfo.getAA();

                String iscanceled = "0";
                String trchkid = "0";

                handler = new DBHelper(getActivity());

                Cursor trchk = handler.getTradeCheckIscanceled(todaydate, tradecheck_aa);

                if (!trchk.isAfterLast()) {

                    if (trchk.moveToFirst()) {

                        do {

                            iscanceled = trchk.getString(trchk.getColumnIndex("iscanceled"));
                            trchkid = trchk.getString(trchk.getColumnIndex("id"));

                        } while (trchk.moveToNext());

                    }
                    if (!trchk.isClosed()) {
                        trchk.close();
                    }
                }

                if (iscanceled.equals("1")) {

                    message = "Ο Έλεγxος του Τελευταίου Δρομολογίου είναι Ακυρωμένος!"+ "\n" + "Αδύνατη η Μετάγγιση Γάλατος!!";;
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;

                }

                String last_rtchk_trid = "0";

                Cursor ltr = handler.getLastTradeIdOfTradeCheck(trchkid);

                if (!ltr.isAfterLast()) {

                    if (ltr.moveToFirst()) {

                        do {

                            last_rtchk_trid = ltr.getString(ltr.getColumnIndex("lasttradeid"));

                        } while (ltr.moveToNext());

                    }
                    if (!ltr.isClosed()) {
                        ltr.close();
                    }
                }

                String ismetaggisi = "0";
                String ismetaggisicheck = "0";
                String isdiakinisi = "0";
                String iscancel = "0";

                Cursor lcdiak = handler.getLastTradeDataOfTradeCheck(last_rtchk_trid);

                if (!lcdiak.isAfterLast()) {

                    if (lcdiak.moveToFirst()) {

                        do {

                            isdiakinisi = lcdiak.getString(lcdiak.getColumnIndex("isdiakinisi"));
                            iscancel = lcdiak.getString(lcdiak.getColumnIndex("iscancel"));
                            ismetaggisi = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisi"));
                            ismetaggisicheck = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisicheck"));

                        } while (lcdiak.moveToNext());

                    }
                    if (!lcdiak.isClosed()) {
                        lcdiak.close();
                    }
                }

                if (ismetaggisi.equals("1") || ismetaggisicheck.equals("1")) {

                    message = "Έχει ήδη εκδοθεί Παραστατικό Μετάγγισης στο Τελευταίο Δρομολόγιο!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;

                }



                if (isdiakinisi.equals("1") && iscancel.equals("0")) {

                    message = "Έχει εκδοθεί Παραστατικό Ενδοδιακίνησης στο Τελευταίο Δρομολόγιο!" + "\n" + "Αδύνατη η Μετάγγιση Γάλατος!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;

                }


                handler.deleteTable("transferchamberlines");
                handler.deleteTable("sourcetransferchamberdata");
                handler.deleteTable("destinationtransferdata");

                for (int i=1;i<=15;i++) {

                    if (!handler.insertDestinationTransferChamberdata(Integer.toString(i), "0", "0", "")) {

                        message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                        AlertDialog.Builder diaBox = MessageAlert(message);

                        return;


                    }
                }

                if (handler.numberOfTableRows("destinationtransferdata") == 0) {

                    message = "Πρόβλημα στην δημιουργία των Αρχείων Μετάγγισης (Προορισμού)!!";
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    return;


                }

                Cursor trdlrs = handler.getDestinationTransferChambersAmount(last_rtchk_trid);

                if (!trdlrs.isAfterLast()) {

                    if (trdlrs.moveToFirst()) {

                        do {

                            String initialqty = trdlrs.getString(trdlrs.getColumnIndex("amount"));
                            String destinationchamberid = trdlrs.getString(trdlrs.getColumnIndex("chamberid"));

                            if (!handler.updateDestinationTransferChamberdata(destinationchamberid,initialqty,"0","")) {

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

                final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();


                Intent in = new Intent(getContext(), TransferTabsActivity.class);
                in.putExtra("tradeid", last_rtchk_trid);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });

        tradechecksListView = (ExpandableListView) getView().findViewById(R.id.tradechecksList);
        tradechecksListView.getLayoutParams().height = 10*displayheight;

        tradechecksCustomAdapter = new TradeChecksCustomAdapter(getContext(), tradecheckList);

         tradechecksListView.setAdapter(tradechecksCustomAdapter);

         // OPTIONAL : Show one list at a time
        tradechecksListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;

            @Override
             public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                   tradechecksListView.collapseGroup(previousGroup);
                   previousGroup = groupPosition;
                }

             }
        });


        tradechecksListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Boolean expandcollapse=false;

                if (groupPosition >= 0) {

                  tradechecksCustomAdapter.setheaderclickposition(groupPosition);

                  long viewId = v.getId();

                  if (viewId == R.id.cancelButton) {

                      collapseAll();

                      expandcollapse=true;

                      TradeCheckHeaderInfo tradecheckInfo= tradecheckList.get(groupPosition);

                      final String tradecheck_aa=tradecheckInfo.getAA();

                      final Dialog dialog = new Dialog(getContext());

                      dialogchoiceList = new String[]{"1","2"};
                      dialogchoicetextList= new String[]{"ΠΑΡΑΣΤΑΤΙΚΟΥ ΕΛΕΓΧΟΥ","ΠΑΡΑΣΤΑΤΙΚΟΥ ΕΝΔΟΔΙΑΚΙΝΗΣΗΣ"};

                      dialog.setCancelable(false);
                      dialog.setContentView(R.layout.canceldocument_dialog_listview);

                      Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
                      btndialog.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                          }
                      });

                      ListView listView = (ListView) dialog.findViewById(R.id.listview);
                      ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.canceldocument_list_item, R.id.document, dialogchoicetextList);
                      listView.setAdapter(arrayAdapter);

                      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              if (dialogchoiceList[position].equals("1")) {
                                  CancelTradeChekDocument(tradecheck_aa);
                              }
                              else
                              {
                                  CancelDiakinisiDocument(tradecheck_aa);
                              }
                              dialog.dismiss();

                          }
                      });

                      dialog.show();

                  }

                  if (viewId == R.id.printButton) {

                      collapseAll();

                      expandcollapse=true;

                      TradeCheckHeaderInfo tradecheckInfo= tradecheckList.get(groupPosition);

                      final String tradecheck_aa=tradecheckInfo.getAA();

                      PrintDocuments(tradecheck_aa,"0");

                  }

                }

                return expandcollapse;
            }
        });

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

            }else {

                if (!rsc.isClosed()) {
                    rsc.close();
                }

                if ((rs.getString(rs.getColumnIndex("salsmid")).equals("0")) || (rs.getString(rs.getColumnIndex("transpid")).equals(""))) {


                    message = getResources().getString(R.string.drivernotexists);
                    AlertDialog.Builder diaBox = MessageAlert(message);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).commit();


                } else {

                    ventorwerks = rs.getString(rs.getColumnIndex("vendorwerks"));

                    if (!rs.isClosed()) {
                        rs.close();
                    }

                    LoadTradeCheksData();

                }

            }

        }

        final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
        final String callingForm=globalVariables.getcallingForm();
        final String new_tradecheck_aa=globalVariables.getnewtradecheck_aa();
        final String tradech_aa=globalVariables.gettradecheck_aa();
        final String trid=globalVariables.gettradeid();
        final String metagtrid=globalVariables.getmetaggisitradeid();

        if (callingForm=="NewDiakinisiReload") {

            if (tradech_aa != null) {
                if (!tradech_aa.equals("0")) {
                    if (trid != null) {
                        if (!trid.equals("0")) {
                            tradecreated=false;
                            message = "Το Παραστατικό Ενδοδιακίνησης Δημιουργήθηκε με Επιτυχία!!";
                            AlertDialog.Builder diaBox = MessageAlert(message);
                            Intent intent = new Intent(getContext(), UploadTrades.class);
                            getActivity().startService(intent);
                            PrintDocuments(tradech_aa, trid);
                        }
                    }
                }
            }
        }

        if (callingForm=="CancelTradeCheckReload") {

            if (tradech_aa != null) {
                if (!tradech_aa.equals("0")) {
                    if (trid != null) {
                        if (!trid.equals("0")) {
                            tradecreated=false;
                            message = "Το Ακυρωτικό Παραστατικό Ελέγχου Δημιουργήθηκε με Επιτυχία!!";
                            AlertDialog.Builder diaBox = MessageAlert(message);
                            Intent intent = new Intent(getContext(), UploadTrades.class);
                            getActivity().startService(intent);
                            PrintDocuments(tradech_aa, trid);
                        }
                    }
                }
            }
        }

        if (callingForm=="CancelDiakinisiReload") {

            if (tradech_aa != null) {
                if (!tradech_aa.equals("0")) {
                    if (trid != null) {
                        if (!trid.equals("0")) {
                            tradecreated=false;
                            message = "Το Ακυρωτικό Παραστατικό Ενδοδιακίνησης Δημιουργήθηκε με Επιτυχία!!";
                            AlertDialog.Builder diaBox = MessageAlert(message);
                            Intent intent = new Intent(getContext(), UploadTrades.class);
                            getActivity().startService(intent);
                            PrintDocuments(tradech_aa, trid);
                        }
                    }
                }
            }
        }

        if (callingForm !=null) {

            if (callingForm=="TradesCheck") {
                if (new_tradecheck_aa != null) {
                    if (!new_tradecheck_aa.equals("0")) {
                        if (trid != null) {
                            if (!trid.equals("0")) {
                                tradecreated=false;
                                message = "Το Παραστατικό Ελέγχου Δημιουργήθηκε με Επιτυχία!!";
                                Intent intent = new Intent(getContext(), UploadTrades.class);
                                getActivity().startService(intent);
                                AlertDialog.Builder diaBox = MessageAlert(message);
                                PrintDocuments(new_tradecheck_aa, trid);
                            }
                        }
                    }
                }
            }

            if (callingForm=="Metaggisi") {
                if (new_tradecheck_aa != null) {
                    if (!new_tradecheck_aa.equals("0")) {
                        if (trid != null && metagtrid != null) {
                            if (!trid.equals("0") && !metagtrid.equals("0")) {
                                tradecreated=false;
                                message = "Τα Παραστατικά της Μετάγγισης Δημιουργήθηκαν με Επιτυχία!!";
                                AlertDialog.Builder diaBox = MessageAlert(message);
                                Intent intent = new Intent(getContext(), UploadTrades.class);
                                getActivity().startService(intent);
                                PrintDocuments(new_tradecheck_aa, metagtrid);
                                PrintDocuments(new_tradecheck_aa, trid);
                            }
                        }
                    }
                }
            }

        }

        globalVariables.setcallingForm("TradesCheck");
        globalVariables.setnewcollection_aa("0");
        globalVariables.setcollection_aa("0");
        globalVariables.setnewtradecheck_aa("0");
        globalVariables.settradecheck_aa("0");
        globalVariables.settradeid("0");
        globalVariables.setmetaggisitradeid("0");

    }

  //method to collapse all groups
     private void collapseAll() {
       int count = tradechecksCustomAdapter.getGroupCount();
       for (int i = 0; i < count; i++){
              tradechecksListView.collapseGroup(i);
       }
     }


    private int addTradeCheck(String trch_aa, String fromtradecode,String uptotradecode,
                            String tradecode, String tr_fromtradecode,String tr_uptotradecode,String transportation,String milktype,String tr_quantity,
                              String trl_aa,String chamber, String trl_quantity,String trl_sample,String tr_isdiakinisi,String tr_ismetaggisi,String tr_ismetaggisicheck,
                              String tr_fromkeb,String tr_tokeb,String tr_fromfactory,String tr_tofactory){


        int groupPosition = 0;

      //check the hash map if the group already exists
      TradeCheckHeaderInfo tradecheckInfo =  tradechecksubjects.get(trch_aa);
      //add the group if doesn't exists
      if(tradecheckInfo == null) {
          tradecheckInfo = new TradeCheckHeaderInfo();
          tradecheckInfo.setAA(trch_aa);
          tradecheckInfo.setFromTradecode(fromtradecode);
          tradecheckInfo.setUpToTradecode(uptotradecode);
          tradechecksubjects.put(trch_aa, tradecheckInfo);
          tradecheckList.add(tradecheckInfo);
      }
      //get the subgroup for the group
      ArrayList<TradeCheckInfo> tradeslist = tradecheckInfo.getTradesList();
      //create a new subgroup and add that to the group
      TradeCheckInfo tradeInfo = tradesubjects.get(tradecode);
      if(tradeInfo == null) {
          tradeInfo = new TradeCheckInfo();
          tradeInfo.setTradecode(tradecode);
          tradeInfo.setTransportation(transportation);
          tradeInfo.setQuantity(tr_quantity);
          if (tr_ismetaggisi.equals("1") || tr_ismetaggisicheck.equals("1")) {
              tradeInfo.setFromTradecode(tr_fromkeb);
              tradeInfo.setUptoTradecode(tr_tokeb);
          }
          else if(tr_isdiakinisi.equals("1")) {
              tradeInfo.setFromTradecode(tr_fromfactory);
              tradeInfo.setUptoTradecode(tr_tofactory);
          }
          else
          {
              tradeInfo.setFromTradecode(tr_fromtradecode);
              tradeInfo.setUptoTradecode(tr_uptotradecode);
          }
          tradeInfo.setIsdiakinisi(tr_isdiakinisi);
          tradeInfo.setIsmetaggisi(tr_ismetaggisi);
          tradeInfo.setIsmetaggisicheck(tr_ismetaggisicheck);

          tradesubjects.put(tradecode, tradeInfo);
          tradeslist.add(tradeInfo);
          tradecheckInfo.setTradesList(tradeslist);
      }
      //get the child for the group
      ArrayList<TradeCheckLinesInfo> tradelineslist = tradeInfo.getTradeLinesList();

      //create a new child and add that to the group
      TradeCheckLinesInfo tradelinesInfo = tradelinessubjects.get(tradecode + "-" + trl_aa);
      if(tradelinesInfo == null) {
          tradelinesInfo = new TradeCheckLinesInfo();
          tradelinesInfo.setAA(trl_aa);
          tradelinesInfo.setMilktype(milktype);
          tradelinesInfo.setChamber(chamber);
          tradelinesInfo.setQuantity(trl_quantity);
          tradelinesInfo.setSample(trl_sample);
          tradelinessubjects.put(tradecode + "-" + trl_aa, tradelinesInfo);
          tradelineslist.add(tradelinesInfo);
          tradeInfo.setTradeLinesList(tradelineslist);
      }
      //find the group position inside the list
      groupPosition = tradecheckList.indexOf(tradecheckInfo);
      return groupPosition;
    }
    
   public void CreateDiakinisiDocument(String tradecheck_aa,String secstoid) {

        if (!tradecreated) {

           String iscanceled = "0";
           String trchkid = "0";

           handler = new DBHelper(getActivity());

           Cursor trchk = handler.getTradeCheckIscanceled(todaydate, tradecheck_aa);

           if (!trchk.isAfterLast()) {

               if (trchk.moveToFirst()) {

                   do {

                       iscanceled = trchk.getString(trchk.getColumnIndex("iscanceled"));
                       trchkid = trchk.getString(trchk.getColumnIndex("id"));

                   } while (trchk.moveToNext());

               }
               if (!trchk.isClosed()) {
                   trchk.close();
               }
           }

           if (iscanceled.equals("1")) {

               message = "Ο Έλεγχος του Τελευταίου Δρομολογίου είναι Ακυρωμένος!!";
               AlertDialog.Builder diaBox = MessageAlert(message);

               return;

           }

           String last_id = "0";

           Cursor ltrchk = handler.getLastTodayTradeCheck(todaydate);

           if (!ltrchk.isAfterLast()) {

               if (ltrchk.moveToFirst()) {

                   do {

                       last_id = ltrchk.getString(ltrchk.getColumnIndex("last_id"));

                   } while (ltrchk.moveToNext());

               }
               if (!ltrchk.isClosed()) {
                   ltrchk.close();
               }
           }

           if (!trchkid.equals(last_id)) {

               message = "Το Δρομολόγιο πρέπει να είναι το Τελευταίο της Ημέρας!!";
               AlertDialog.Builder diaBox = MessageAlert(message);

               return;

           }
           String last_rtchk_trid = "0";

           Cursor ldiak = handler.getLastTradeIdOfTradeCheck(trchkid);

           if (!ldiak.isAfterLast()) {

               if (ldiak.moveToFirst()) {

                   do {

                       last_rtchk_trid = ldiak.getString(ldiak.getColumnIndex("lasttradeid"));

                   } while (ldiak.moveToNext());

               }
               if (!ldiak.isClosed()) {
                   ldiak.close();
               }
           }

           String ismetaggisi = "0";
           String ismetaggisicheck = "0";
           String isdiakinisi = "0";
           String iscancel = "0";

           Cursor lcdiak = handler.getLastTradeDataOfTradeCheck(last_rtchk_trid);

           if (!lcdiak.isAfterLast()) {

               if (lcdiak.moveToFirst()) {

                   do {

                       isdiakinisi = lcdiak.getString(lcdiak.getColumnIndex("isdiakinisi"));
                       iscancel = lcdiak.getString(lcdiak.getColumnIndex("iscancel"));
                       ismetaggisi = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisi"));
                       ismetaggisicheck = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisicheck"));

                   } while (lcdiak.moveToNext());

               }
               if (!lcdiak.isClosed()) {
                   lcdiak.close();
               }
           }

           if (isdiakinisi.equals("1") && iscancel.equals("0")) {

               message = "Έχει Εκδοθεί ήδη Παραστατικό Ενδοδιακίνησης στο Τελευταίο Δρομολόγιο!!";
               AlertDialog.Builder diaBox = MessageAlert(message);

               return;

           }

            if (ismetaggisi.equals("1") || ismetaggisicheck.equals("1")) {

                message = "Έχει Εκδοθεί Παραστατικό Mετάγγισης στο Τελευταίο Δρομολόγιο!" + "\n" + "Αδύνατη η Ενδοδιακίνησης!!";
                AlertDialog.Builder diaBox = MessageAlert(message);

                return;

            }

           Cursor rs = handler.getNewTradesToCheck(todaydate);

           rs.moveToFirst();

           if (!rs.isAfterLast()) {

               message = "Υπάρχουν Παραλαβές οι οποίες δεν έχουν Ελεγχθεί!" + "\n" + "Αδύνατη η Έκδοση Παραστατικού Ενδοδιακίνησης!!";
               AlertDialog.Builder diaBox = MessageAlert(message);

               return;
           }

           if (!rs.isClosed()) {
               rs.close();
           }


           String trid = "";

           String client = "";
           String domaintype = "2";
           String dsrcod = "";
           String dsrno = "";
           String transpid = "";
           String salsmid = "";
           String fromtradecode = "";
           String uptotradecode = "";
           String dromologionum = "";
           String shvid = "";
           String fromkeb = "";
           String custid = "";
           String stoid = "";
           String newdsrno = "";

           //get docseries,lastdsrno,stoid from config
           Cursor cfrs = handler.getConfigData(1);

           if (!cfrs.isAfterLast()) {

               if (cfrs.moveToFirst()) {
                   do {

                       dsrcod = cfrs.getString(cfrs.getColumnIndex("diakinisidsrcode"));
                       dsrno = cfrs.getString(cfrs.getColumnIndex("diakinisidsrnum"));
                       stoid = cfrs.getString(cfrs.getColumnIndex("vendorwerks"));

                   } while (cfrs.moveToNext());
               }
               if (!cfrs.isClosed()) {
                   cfrs.close();
               }
           }

           //get customer id from retailcustomer
           Cursor custrs = handler.getCusromerId(stoid, secstoid);

           if (!custrs.isAfterLast()) {

               if (custrs.moveToFirst()) {
                   do {

                       custid = custrs.getString(custrs.getColumnIndex("rcustid"));

                   } while (custrs.moveToNext());
               }
               if (!custrs.isClosed()) {
                   custrs.close();
               }
           } else {

               String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ενδοδιακίνησης!" + "\n" + "Δεν βρέθηκαν τα Στοιχεια του Εργοστασίου!!";
               AlertDialog.Builder diaBox = MessageAlert(errormessage);
               return;

           }

           Cursor trc;

           trc = handler.getTradeCheckTradesDataforDiakinisi(trchkid);

           if (!trc.isAfterLast()) {
               if (trc.moveToFirst()) {

                   do {

                       trid = trc.getString(trc.getColumnIndex("id"));
                       client = trc.getString(trc.getColumnIndex("cid"));
                       transpid = trc.getString(trc.getColumnIndex("trsid"));
                       salsmid = trc.getString(trc.getColumnIndex("salesmanid"));
                       fromtradecode = trc.getString(trc.getColumnIndex("fromtradecode"));
                       uptotradecode = trc.getString(trc.getColumnIndex("uptotradecode"));
                       dromologionum = trc.getString(trc.getColumnIndex("dromologionum"));
                       fromkeb = trc.getString(trc.getColumnIndex("tradecode"));
                       shvid = trc.getString(trc.getColumnIndex("shvid"));

                       int lastdsrno = Integer.parseInt(dsrno);

                       diakinisitradeid = "0";

                       newdsrno = Integer.toString(lastdsrno + 1);

                       String tradecode = dsrcod + "-" + newdsrno;

                       Calendar newCalendar = Calendar.getInstance();
                       SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
                       String nowtime=timeFormatter.format(newCalendar.getTime());

                       if (handler.InsertDiakinisiTrade(client, domaintype, trchkid, dsrcod, newdsrno, tradecode, todaydate,nowtime, transpid, salsmid,
                               fromtradecode, uptotradecode, dromologionum, shvid, fromkeb, stoid, secstoid, custid, "", "0", "0")) {

                           Cursor trrs = handler.getDiakinisiTradeData(domaintype, dsrcod, newdsrno, custid, "0");

                           if (!trrs.isAfterLast()) {

                               if (trrs.moveToFirst()) {

                                   do {
                                       diakinisitradeid = trrs.getString(trrs.getColumnIndex("id"));
                                   } while (trrs.moveToNext());

                               }

                               if (!trrs.isClosed()) {
                                   trrs.close();
                               }

                               Cursor trlrs = handler.getTradeCheckTradeLines(trid);

                               if (!trlrs.isAfterLast()) {

                                   if (trlrs.moveToFirst()) {

                                       do {

                                           String trl_aa = trlrs.getString(trlrs.getColumnIndex("trl_aa"));
                                           String trl_qty = trlrs.getString(trlrs.getColumnIndex("trl_qty"));
                                           String trl_sample = trlrs.getString(trlrs.getColumnIndex("trl_sample"));
                                           String trl_chamber = trlrs.getString(trlrs.getColumnIndex("trl_chamber"));
                                           String trl_iteid = trlrs.getString(trlrs.getColumnIndex("trl_iteid"));

                                           if (handler.InsertDiakinisiTradeLines(trl_aa, diakinisitradeid, trl_iteid, trl_qty, trl_chamber, trl_sample)) {

                                               if (handler.getTradeLineRowCount(trl_aa, diakinisitradeid) == 0) {

                                                   handler.DeleteTradeLines("tradelines", diakinisitradeid);
                                                   handler.DeleteRowFromTable("trades", diakinisitradeid);

                                                   String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ενδοδιακίνησης!!";
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
                               String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ενδοδιακίνησης!!";
                               AlertDialog.Builder diaBox = MessageAlert(errormessage);
                               return;
                           }


                       } else {
                           String errormessage = "Πρόβλημα στην Δημιουργία του Παραστατικού Ενδοδιακίνησης!!";
                           AlertDialog.Builder diaBox = MessageAlert(errormessage);
                           return;
                       }


                   } while (trc.moveToNext());

               }
               if (!trc.isClosed()) {
                   trc.close();
               }
           }


           if (handler.updateConfiglastDiakinisidsr(1, newdsrno)) {

               tradecreated=true;

               final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
               final String callingForm=globalVariables.getcallingForm();

               globalVariables.setcallingForm("NewDiakinisiReload");
               globalVariables.settradecheck_aa(tradecheck_aa);
               globalVariables.settradeid(diakinisitradeid);


               FragmentTransaction ft = getFragmentManager().beginTransaction();
               ft.detach(this).attach(this).commit();
           }

        }
   }

   public void CancelTradeChekDocument(String tradecheck_aa) {

      if (!tradecreated) {

        String iscanceled = "0";
        String trchkid = "0";

        handler = new DBHelper(getActivity());

        Cursor trchk = handler.getTradeCheckIscanceled(todaydate, tradecheck_aa);

        if (!trchk.isAfterLast()) {

            if (trchk.moveToFirst()) {

                do {

                    iscanceled = trchk.getString(trchk.getColumnIndex("iscanceled"));
                    trchkid = trchk.getString(trchk.getColumnIndex("id"));

                } while (trchk.moveToNext());

            }
            if (!trchk.isClosed()) {
                trchk.close();
            }
        }

        if (iscanceled.equals("1")) {

            message = "Ο Έλεγχος Δρομολογίου είναι Ακυρωμένος!!";
            AlertDialog.Builder diaBox = MessageAlert(message);

            return;

        }

        String last_id = "0";

        Cursor ltrchk = handler.getLastTodayTradeCheck(todaydate);

        if (!ltrchk.isAfterLast()) {

            if (ltrchk.moveToFirst()) {

                do {

                    last_id = ltrchk.getString(ltrchk.getColumnIndex("last_id"));

                } while (ltrchk.moveToNext());

            }
            if (!ltrchk.isClosed()) {
                ltrchk.close();
            }
        }

        if (!trchkid.equals(last_id)) {

            message = "Το Δρομολόγιο πρέπει να είναι το Τελευταίο της Ημέρας!!";
            AlertDialog.Builder diaBox = MessageAlert(message);

            return;

        }

        String last_rtchk_trid = "0";

        Cursor ldiak = handler.getLastTradeIdOfTradeCheck(trchkid);

        if (!ldiak.isAfterLast()) {

            if (ldiak.moveToFirst()) {

                do {

                    last_rtchk_trid = ldiak.getString(ldiak.getColumnIndex("lasttradeid"));

                } while (ldiak.moveToNext());

            }
            if (!ldiak.isClosed()) {
                ldiak.close();
            }
        }


          String ismetaggisi = "0";
          String ismetaggisicheck = "0";
          String isdiakinisi = "0";
          String iscancel = "0";

        Cursor lcdiak = handler.getLastTradeDataOfTradeCheck(last_rtchk_trid);

        if (!lcdiak.isAfterLast()) {

            if (lcdiak.moveToFirst()) {

                do {

                    isdiakinisi = lcdiak.getString(lcdiak.getColumnIndex("isdiakinisi"));
                    iscancel = lcdiak.getString(lcdiak.getColumnIndex("iscancel"));
                    ismetaggisi = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisi"));
                    ismetaggisicheck = lcdiak.getString(lcdiak.getColumnIndex("ismetaggisicheck"));

                } while (lcdiak.moveToNext());

            }
            if (!lcdiak.isClosed()) {
                lcdiak.close();
            }
        }

        if (isdiakinisi.equals("1") && iscancel.equals("0")) {

            message = "Έχει εκδοθεί Παραστατικό Ενδοδιακίνησης στο Τελευταίο Δρομολόγιο!" + "\n" + "Δεν μπορεί να εκδοθεί Ακυρωτικό Ελέγχου!!";
            AlertDialog.Builder diaBox = MessageAlert(message);

            return;

        }
          if (ismetaggisi.equals("1") || ismetaggisicheck.equals("1")) {

              message = "Έχει εκδοθεί Παραστατικό Μετάγγισης στο Τελευταίο Δρομολόγιο!" + "\n" + "Δεν μπορεί να εκδοθεί Ακυρωτικό Ελέγχου!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }


        String trid = "";

        String client = "";
        String domaintype = "";
        String dsrcod = "";
        String dsrno = "";
        String tradecode = "";
        String transpid = "";
        String salsmid = "";
        String fromtradecode = "";
        String uptotradecode = "";
        String dromologionum = "";
        String shvid = "";
        String xlm = "";
        String notes = "";

        String canceltrid = "";

        Cursor trc;


        trc = handler.getTradeCheckTradesDataToCancel(trchkid);

        if (!trc.isAfterLast()) {
            if (trc.moveToFirst()) {

                do {

                    trid = trc.getString(trc.getColumnIndex("id"));
                    client = trc.getString(trc.getColumnIndex("cid"));
                    domaintype = trc.getString(trc.getColumnIndex("domaintype"));
                    dsrcod = trc.getString(trc.getColumnIndex("cancelseries"));
                    dsrno = trc.getString(trc.getColumnIndex("docnum"));
                    tradecode = dsrcod + "-" + dsrno;
                    transpid = trc.getString(trc.getColumnIndex("trsid"));
                    salsmid = trc.getString(trc.getColumnIndex("salesmanid"));
                    fromtradecode = trc.getString(trc.getColumnIndex("fromtradecode"));
                    uptotradecode = trc.getString(trc.getColumnIndex("uptotradecode"));
                    dromologionum = trc.getString(trc.getColumnIndex("dromologionum"));
                    shvid = trc.getString(trc.getColumnIndex("shvid"));
                    xlm = trc.getString(trc.getColumnIndex("xlm"));
                    notes = trc.getString(trc.getColumnIndex("tradecode"));

                    Calendar newCalendar = Calendar.getInstance();
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
                    String nowtime=timeFormatter.format(newCalendar.getTime());

                    if (handler.InsertTradeCheckTrade(client, domaintype, trchkid, dsrcod, dsrno, tradecode, todaydate,nowtime, transpid, salsmid, fromtradecode, uptotradecode, dromologionum, shvid, xlm, notes, "1", "0")) {

                        Cursor trrs = handler.getTradeData(domaintype, dsrcod, dsrno, transpid, "1");

                        if (!trrs.isAfterLast()) {

                            if (trrs.moveToFirst()) {

                                do {
                                    canceltrid = trrs.getString(trrs.getColumnIndex("id"));
                                } while (trrs.moveToNext());

                            }

                            if (!trrs.isClosed()) {
                                trrs.close();
                            }

                            Cursor trlrs = handler.getTradeCheckTradeLines(trid);

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

                                                String errormessage = "Πρόβλημα στην Δημιουργία του Ακυρωτικoύ Παραστατικού Ελέγχου!!";
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
                            String errormessage = "Πρόβλημα στην Δημιουργία Ακυρωτικoύ Παραστατικού Ελέγχου!!";
                            AlertDialog.Builder diaBox = MessageAlert(errormessage);
                            return;
                        }


                    } else {
                        String errormessage = "Πρόβλημα στην Δημιουργία Ακυρωτικoύ Παραστατικού Ελέγχου!!";
                        AlertDialog.Builder diaBox = MessageAlert(errormessage);
                        return;
                    }


                } while (trc.moveToNext());

            }
            if (!trc.isClosed()) {
                trc.close();
            }
        }

        if (handler.UpdateCanceledTradeCheckTradesIschecked(todaydate, trchkid, "0")) {

            if (handler.UpdateCanceledCheckTradeTrades(trchkid, "1")) {

                if (handler.UpdateTradeCheckIscancel(trchkid, "1")) {

                    tradecreated=true;

                    final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
                    globalVariables.setcallingForm("CancelTradeCheckReload");
                    globalVariables.settradecheck_aa(tradecheck_aa);
                    globalVariables.settradeid(canceltrid);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                }
            }
        }
      }

   }
   
   public void CancelDiakinisiDocument(String tradecheck_aa) {

      if(!tradecreated) {

          String iscanceled = "0";
          String trchkid = "0";

          handler = new DBHelper(getActivity());

          Cursor trchk = handler.getTradeCheckIscanceled(todaydate, tradecheck_aa);

          if (!trchk.isAfterLast()) {

              if (trchk.moveToFirst()) {

                  do {

                      iscanceled = trchk.getString(trchk.getColumnIndex("iscanceled"));
                      trchkid = trchk.getString(trchk.getColumnIndex("id"));

                  } while (trchk.moveToNext());

              }
              if (!trchk.isClosed()) {
                  trchk.close();
              }
          }

          if (iscanceled.equals("1")) {

              message = "Ο Έλεγχος Δρομολογίου είναι Ακυρωμένος!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }

          String last_id = "0";

          Cursor ltrchk = handler.getLastTodayTradeCheck(todaydate);

          if (!ltrchk.isAfterLast()) {

              if (ltrchk.moveToFirst()) {

                  do {

                      last_id = ltrchk.getString(ltrchk.getColumnIndex("last_id"));

                  } while (ltrchk.moveToNext());

              }
              if (!ltrchk.isClosed()) {
                  ltrchk.close();
              }
          }

          if (!trchkid.equals(last_id)) {

              message = "Το Δρομολόγιο πρέπει να είναι το Τελευταίο της Ημέρας!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }

          String last_rtchk_trid = "0";

          Cursor ldiak = handler.getLastTradeIdOfTradeCheck(trchkid);

          if (!ldiak.isAfterLast()) {

              if (ldiak.moveToFirst()) {

                  do {

                      last_rtchk_trid = ldiak.getString(ldiak.getColumnIndex("lasttradeid"));

                  } while (ldiak.moveToNext());

              }
              if (!ldiak.isClosed()) {
                  ldiak.close();
              }
          }

          String ismetaggisi = "0";
          String ismetaggisicheck = "0";

          String isdiakinisi = "0";
          String iscancel = "0";

          Cursor lcdiak = handler.getLastTradeDataOfTradeCheck(last_rtchk_trid);

          if (!lcdiak.isAfterLast()) {

              if (lcdiak.moveToFirst()) {

                  do {

                      isdiakinisi = lcdiak.getString(lcdiak.getColumnIndex("isdiakinisi"));
                      iscancel = lcdiak.getString(lcdiak.getColumnIndex("iscancel"));

                  } while (lcdiak.moveToNext());

              }
              if (!lcdiak.isClosed()) {
                  lcdiak.close();
              }
          }

          if (isdiakinisi.equals("0")) {

              message = "Δεν βρέθηκε Παραστατικό Ενδοδιακίνησης!" + "\n" + "Δεν μπορεί να εκδοθεί Ακυρωτικό!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }

          if (iscancel.equals("1")) {

              message = "To Παραστατικό Ενδοδιακίνησης είναι ήδη Ακυρωμένο!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }

          if (ismetaggisi.equals("1") ||ismetaggisicheck.equals("1") ) {

              message = "Έχει Εκδοθεί Παραστατικό Μετάγγισης!" + "\n" + "Αδύνατη η Ακύρωση της Ενδοδιακίνησης!!";
              AlertDialog.Builder diaBox = MessageAlert(message);

              return;

          }

          //get docseries,lastdsrno,stoid from config
          String dsrno = "0";

          Cursor cfrs = handler.getConfigData(1);

          if (!cfrs.isAfterLast()) {

              if (cfrs.moveToFirst()) {
                  do {

                      dsrno = cfrs.getString(cfrs.getColumnIndex("diakinisidsrnum"));

                  } while (cfrs.moveToNext());
              }
              if (!cfrs.isClosed()) {
                  cfrs.close();
              }
          }

          int lastdsrno = Integer.parseInt(dsrno);

          String newdsrno = Integer.toString(lastdsrno + 1);

          String trid = "";

          String client = "";
          String domaintype = "";
          String dsrcod = "";
          String tradecode = "";
          String transpid = "";
          String salsmid = "";
          String fromtradecode = "";
          String uptotradecode = "";
          String dromologionum = "";
          String shvid = "";
          String fromkeb = "";
          String stoid = "";
          String secstoid = "";
          String notes = "";
          String custid = "";

          String canceltrid = "";

          Cursor trc;


          trc = handler.getTradeCheckDiakinisiDataToCancel(last_rtchk_trid);

          if (!trc.isAfterLast()) {
              if (trc.moveToFirst()) {

                  do {

                      trid = trc.getString(trc.getColumnIndex("id"));
                      client = trc.getString(trc.getColumnIndex("cid"));
                      domaintype = trc.getString(trc.getColumnIndex("domaintype"));
                      dsrcod = trc.getString(trc.getColumnIndex("docseries"));
                      tradecode = dsrcod + "-" + newdsrno;
                      transpid = trc.getString(trc.getColumnIndex("trsid"));
                      salsmid = trc.getString(trc.getColumnIndex("salesmanid"));
                      fromtradecode = trc.getString(trc.getColumnIndex("fromtradecode"));
                      uptotradecode = trc.getString(trc.getColumnIndex("uptotradecode"));
                      dromologionum = trc.getString(trc.getColumnIndex("dromologionum"));
                      shvid = trc.getString(trc.getColumnIndex("shvid"));
                      fromkeb = trc.getString(trc.getColumnIndex("fromkeb"));
                      stoid = trc.getString(trc.getColumnIndex("secstoid"));
                      secstoid = trc.getString(trc.getColumnIndex("stoid"));
                      notes = trc.getString(trc.getColumnIndex("tradecode"));

                      Cursor custrs = handler.getCusromerId(stoid, secstoid);

                      if (!custrs.isAfterLast()) {

                          if (custrs.moveToFirst()) {
                              do {

                                  custid = custrs.getString(custrs.getColumnIndex("rcustid"));

                              } while (custrs.moveToNext());
                          }
                          if (!custrs.isClosed()) {
                              custrs.close();
                          }
                      } else {

                          String errormessage = "Πρόβλημα στην Δημιουργία του Ακυρωτικού Παραστατικού Ενδοδιακίνησης!" + "\n" + "Δεν βρέθηκαν τα Στοιχεια του Εργοστασίου!!";
                          AlertDialog.Builder diaBox = MessageAlert(errormessage);
                          return;

                      }

                      Calendar newCalendar = Calendar.getInstance();
                      SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
                      String nowtime=timeFormatter.format(newCalendar.getTime());

                      if (handler.InsertDiakinisiTrade(client, domaintype, trchkid, dsrcod, newdsrno, tradecode, todaydate,nowtime, transpid, salsmid,
                              fromtradecode, uptotradecode, dromologionum, shvid, fromkeb, stoid, secstoid, custid, notes, "1", "0")) {

                          Cursor trrs = handler.getDiakinisiTradeData(domaintype, dsrcod, newdsrno, custid, "1");

                          if (!trrs.isAfterLast()) {

                              if (trrs.moveToFirst()) {

                                  do {
                                      canceltrid = trrs.getString(trrs.getColumnIndex("id"));
                                  } while (trrs.moveToNext());

                              }

                              if (!trrs.isClosed()) {
                                  trrs.close();
                              }

                              Cursor trlrs = handler.getTradeCheckTradeLines(trid);

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

                                                  String errormessage = "Πρόβλημα στην Δημιουργία του Ακυρωτικoύ Παραστατικού Ενδοδιακίνησης!!";
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
                              String errormessage = "Πρόβλημα στην Δημιουργία Ακυρωτικoύ Παραστατικού Ενδοδιακίνησης!!";
                              AlertDialog.Builder diaBox = MessageAlert(errormessage);
                              return;
                          }


                      } else {
                          String errormessage = "Πρόβλημα στην Δημιουργία Ακυρωτικoύ Παραστατικού Ενδοδιακίνησης!!";
                          AlertDialog.Builder diaBox = MessageAlert(errormessage);
                          return;
                      }


                  } while (trc.moveToNext());

              }
              if (!trc.isClosed()) {
                  trc.close();
              }
          }


          if (handler.UpdateCanceledDiakinisiTrades(trchkid, "1")) {

              if (handler.updateConfiglastDiakinisidsr(1, newdsrno)) {

                  tradecreated=true;

                  final GlobalClass globalVariables = (GlobalClass) getActivity().getApplicationContext();
                  globalVariables.setcallingForm("CancelDiakinisiReload");
                  globalVariables.settradecheck_aa(tradecheck_aa);
                  globalVariables.settradeid(canceltrid);

                  FragmentTransaction ft = getFragmentManager().beginTransaction();
                  ft.detach(this).attach(this).commit();
              }

          }
      }
   }

   public void PrintDocuments(String tradecheck_aa,String tradeid) {


        Cursor rsc = handler.getConfigData(1);

        rsc.moveToFirst();

        if (!rsc.isAfterLast()) {

            printer=rsc.getString(rsc.getColumnIndex("printer"));

        }

        if (!rsc.isClosed()) {
            rsc.close();
        }


        if (mmDevice == null) {

          try {
              mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

              if (mBluetoothAdapter == null) {

                  Toast.makeText(getContext(),getResources().getString(R.string.bt_notexists_msg) , Toast.LENGTH_SHORT).show();
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
              Toast.makeText(getContext(),getResources().getString(R.string.printer_search_problem) , Toast.LENGTH_SHORT).show();
              return;
          }
      }

      if (mmDevice == null) {

          Toast.makeText(getContext(),getResources().getString(R.string.printer_notfount_msg) , Toast.LENGTH_SHORT).show();
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
              Toast.makeText(getContext(),getResources().getString(R.string.conn_problem_msg) , Toast.LENGTH_SHORT).show();
              return;
          }

      }

      String trchid="";

      String tridList[]={};
      String tr_qtyList[]={};

      String tradecode="";
      String hmer="";
      String time="";

      String transportation="";
      String fromtradecode="";
      String uptotradecode="";
      String docdescr="";
      String driver="";
      String isdiakinisi="";
      String ismetaggisi="";
      String ismetaggisicheck="";
      String fromkeb="";
      String tokeb="";

      String frombranch="";
      String tobranch="";

       String fromfactory="";
       String tofactory="";



      doctrade = new String[]{};

      Integer arrayindx = 0;

      if (!mmSocket.isConnected()) {
          Toast.makeText(getContext(),getResources().getString(R.string.conn_fail_msg) , Toast.LENGTH_SHORT).show();
          return;
      }
      else

      {

          handler = new DBHelper(getActivity());


          Cursor rscm = handler.getCompanyData(1);

          rscm.moveToFirst();

          String company_name="" ;
          String company_afm="";
          String company_doy="";


          if (!rscm.isAfterLast()) {

              company_name=rscm.getString(rscm.getColumnIndex("name"));
              company_afm=rscm.getString(rscm.getColumnIndex("afm"));
              company_doy = rscm.getString(rscm.getColumnIndex("doy"));


          }

          if (!rscm.isClosed()) {
              rscm.close();
          }

          Cursor trch = handler.getTradeCheckId(todaydate,tradecheck_aa);

          if (!trch.isAfterLast()) {

              if (trch.moveToFirst()) {

                  do {

                      trchid = trch.getString(trch.getColumnIndex("id"));

                  } while (trch.moveToNext());

              }
              if (!trch.isClosed()) {
                  trch.close();
              }
          }


          Cursor trc;

           if (tradeid.equals("0")) {
               trc = handler.getTradeCheckTradesData(trchid);
           }
           else
           {
               trc = handler.getTradeCheckTrade(tradeid);
           }

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

                      transportation = trc.getString(trc.getColumnIndex("transp_descr"));
                      fromtradecode = trc.getString(trc.getColumnIndex("tr_fromtradecode"));
                      uptotradecode = trc.getString(trc.getColumnIndex("tr_uptotradecode"));
                      docdescr = trc.getString(trc.getColumnIndex("docdescr"));
                      driver = trc.getString(trc.getColumnIndex("driver"));

                      isdiakinisi = trc.getString(trc.getColumnIndex("isdiakinisi"));
                      ismetaggisi = trc.getString(trc.getColumnIndex("ismetaggisi"));
                      ismetaggisicheck = trc.getString(trc.getColumnIndex("ismetaggisicheck"));

                      frombranch = trc.getString(trc.getColumnIndex("frombranch"));
                      tobranch = trc.getString(trc.getColumnIndex("tobranch"));

                      fromfactory = trc.getString(trc.getColumnIndex("fromfactory"));
                      tofactory = trc.getString(trc.getColumnIndex("tofactory"));

                      fromkeb = trc.getString(trc.getColumnIndex("fromkeb"));
                      tokeb = trc.getString(trc.getColumnIndex("tokeb"));

                      String tradecount="";

                      if (fromtradecode != null && !fromtradecode.equals("") && uptotradecode != null && !uptotradecode.equals("") ) {
                          String fromtrc[]=fromtradecode.split("-");
                          String uptotrc[]=uptotradecode.split("-");
                          tradecount = Integer.toString(Integer.parseInt(uptotrc[1]) - Integer.parseInt(fromtrc[1]) + 1);

                      }

                      if (isdiakinisi.equals("1")) {
                          doctrade[arrayindx] = company_name;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "Α.Φ.Μ. : " +company_afm+ " - Δ.Ο.Υ. : " + company_doy;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      }
                      if (isdiakinisi.equals("1")) {
                          doctrade[arrayindx] = doctrade[arrayindx] + hmer + "      " + time;
                      }
                      else
                      {
                          doctrade[arrayindx] = hmer + "      " + time;
                      }
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + tradecode;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "ΑΡ ΑΥΤΟΚΙΝΗΤΟΥ : " + transportation;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "ΟΔΗΓΟΣ : " + driver;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + docdescr;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";

                      if (isdiakinisi.equals("1")) {
                          doctrade[arrayindx] = doctrade[arrayindx] + "ΕΝΔΟΔΙΑΚΙΝΗΣΗ";
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "ΑΠΟ : " + fromfactory ;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "ΠΡΟΟΡΙΣΜΟΣ : " + tofactory;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      }

                      if (ismetaggisi.equals("1") || ismetaggisicheck.equals("1")) {
                          doctrade[arrayindx] = doctrade[arrayindx] + "ΑΠΟ ΚΕΒ :¨" + fromkeb ;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "ΣΕ ΚΕΒ  : " + tokeb;
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                          doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      }


                      doctrade[arrayindx] = doctrade[arrayindx] + "ΔΡΟΜΟΛΟΓΙΟ : " + tradecheck_aa;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "ΑΡΙΘΜΟΣ ΠΑΡΑΣΤΑΤΙΚΩΝ : " + tradecount;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "ΑΠΟ ΠΑΡΑΣΤΑΤΙΚΟ : " + fromtradecode;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "ΕΩΣ ΠΑΡΑΣΤΑΤΙΚΟ : " + uptotradecode;
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";
                      doctrade[arrayindx] = doctrade[arrayindx] + "\n";


                      arrayindx++;

                  } while (trc.moveToNext());

              }
              if (!trc.isClosed()) {
                  trc.close();
              }
          }
          //create documents trade lines
          for (int lineno = 0; lineno < tridList.length; lineno++) {

              Cursor trlc;

              trlc = handler.getGroupedTradeCheckTradeLines(tridList[lineno]);

              String trl_qty[]={};
              String trl_sample[]={};
              String trl_chamber[]={};
              String trl_milktype[]={};

              arrayindx=0;

              if (!trlc.isAfterLast()) {

                  if (trlc.moveToFirst()) {

                      do {

                          trl_sample = Arrays.copyOf(trl_sample, trl_sample.length + 1);
                          trl_chamber = Arrays.copyOf(trl_chamber, trl_chamber.length + 1);
                          trl_qty = Arrays.copyOf(trl_qty, trl_qty.length + 1);
                          trl_milktype = Arrays.copyOf(trl_milktype, trl_milktype.length + 1);

                          trl_qty[arrayindx] = trlc.getString(trlc.getColumnIndex("trl_qty"));
                          trl_sample[arrayindx] = trlc.getString(trlc.getColumnIndex("trl_sample"));
                          trl_chamber[arrayindx] = trlc.getString(trlc.getColumnIndex("trl_chamber"));
                          trl_milktype[arrayindx] = trlc.getString(trlc.getColumnIndex("mat_descr2"));


                          arrayindx++;

                      } while (trlc.moveToNext());

                  }
                  if (!trlc.isClosed()) {
                      trlc.close();
                  }
              }

              //create chamberdataList (lines with unique chamber) and milktypesList (unique milktype)

              String chamberdataList[]={};

              String chamberclone[];

              String milktypesList[]={};

              Boolean newmilktype;

              chamberclone = Arrays.copyOf(trl_chamber, trl_chamber.length);

              arrayindx=0;

              int arrinx=0;

              for (int i=0; i<trl_chamber.length; i++) {

                  if (chamberclone[i].equals("")) continue;

                  String chamber = trl_chamber[i];

                  chamberdataList = Arrays.copyOf(chamberdataList, chamberdataList.length + 1);

                  chamberdataList[arrayindx]=chamber ;

                   for (int j = 0; j < chamberclone.length; j++) {

                      if (chamberclone[j].equals(chamber)) {
                          chamberdataList[arrayindx]= chamberdataList[arrayindx] + "*" + trl_milktype[j] + "," + trl_qty[j];
                          chamberclone[j]="";
                          newmilktype=true;
                           for (int k = 0; k < milktypesList.length; k++) {
                             if (milktypesList[k].equals(trl_milktype[j])) {
                               newmilktype=false;
                               break;

                             }
                             
                           }
                           if (newmilktype.equals(true)) {
                               milktypesList = Arrays.copyOf(milktypesList, milktypesList.length + 1);
                               milktypesList[arrinx]=trl_milktype[j];
                               arrinx++;
                           }
                      }

                   }

                  arrayindx++;

              }

               //create linegroups with 4 milktypes each
               int tradelinegroups = (int) Math.ceil((double) milktypesList.length/4);

               milktypegroups = new String [tradelinegroups][4];

              for (int i=0; i<tradelinegroups; i++) {

                  for (int j=0; j<4; j++) {
                      milktypegroups[i][j]="     ";
                  }

              }

              milktypegroupqtys = new int [tradelinegroups][4];

              for (int i=0; i<tradelinegroups; i++) {

                  for (int j=0; j<4; j++) {
                      milktypegroupqtys[i][j]=0;
                  }

              }

              int arrinxy=0;
              int arrinxx=0;

              for (int i=0; i<milktypesList.length; i++) {
                  if (arrinxy<4) {
                      milktypegroups[arrinxx][arrinxy]=milktypesList[i];
                      arrinxy++;
                  }
                  else
                  {
                      arrinxy=0;
                      arrinxx++;
                      milktypegroups[arrinxx][arrinxy]=milktypesList[i];
                      arrinxy++;
                  }
              }

               //create two dimensional array tradelines
               tradelines = new String [tradelinegroups*(chamberdataList.length+1)][7];

               arrayindx = 0;


               //create header rows for each line group
              for (int i=0; i<tradelinegroups; i++) {

                  int step=(chamberdataList.length+1);

                  tradelines[i*step][0]="ΔΜ";;
                  tradelines[i*step][1]=milktypegroups[i][0];
                  tradelines[i*step][2]=milktypegroups[i][1];
                  tradelines[i*step][3]=milktypegroups[i][2];
                  tradelines[i*step][4]=milktypegroups[i][3];
                  tradelines[i*step][5]="ΣΥΝΟΛΟ";
                  tradelines[i*step][6]="ΔΕΙΓΜΑ";
              }

              //fill tradelines with chamber and zero all other trade lines except header lines
              for (int i=0; i<tradelinegroups; i++) {

                  int step=(chamberdataList.length+1);

                  for (int j=0; j<chamberdataList.length; j++) {

                      String[] dataofchamber=chamberdataList[j].split("\\*");

                      tradelines[(i*step)+j+1][0]=dataofchamber[0];
                      tradelines[(i*step)+j+1][1]="0";
                      tradelines[(i*step)+j+1][2]="0";
                      tradelines[(i*step)+j+1][3]="0";
                      tradelines[(i*step)+j+1][4]="0";
                      tradelines[(i*step)+j+1][5]="0";
                      tradelines[(i*step)+j+1][6]="       ";
                  }


              }
              //fill trade lines with quantities
              for (int i=0; i<tradelinegroups; i++) {

                  for (int j=0; j<4; j++) {

                      String milktype=milktypegroups[i][j];

                       if (!milktype.equals("     ")) {

                           for (int k = 0; k < chamberdataList.length; k++) {

                               String[] dataofchamber = chamberdataList[k].split("\\*");

                               for (int l = 1; l < dataofchamber.length; l++) {

                                   String[] dataofmilktype = dataofchamber[l].split(",");

                                   if (dataofmilktype[0].equals(milktype)) {

                                       tradelines[(i*chamberdataList.length)+k+1][j+1]=dataofmilktype[1];
                                       milktypegroupqtys[i][j]=milktypegroupqtys[i][j] + Integer.parseInt(dataofmilktype[1]);

                                   }
                               }

                           }
                       }

                  }

              }


              //fill trade lines with samples
              for (int i=0; i<tradelines.length; i++) {

                 String chamber= tradelines[i][0];

                 if (!chamber.equals("ΔΜ")) {

                     tradelines[i][5]= Integer.toString(Integer.parseInt(tradelines[i][1]) + Integer.parseInt(tradelines[i][2]) + Integer.parseInt(tradelines[i][3]) + Integer.parseInt(tradelines[i][4]));

                     for (int j=0; j<trl_chamber.length; j++) {

                         if (chamber.equals(trl_chamber[j])) {
                             tradelines[i][6]= trl_sample[j] ;
                             break;
                         }
                     }

                 }

              }
              //fill tradelaines with progressive total quantities
              int totalqty[]=new int[chamberdataList.length];

              for (int j=0; j<chamberdataList.length; j++) {

                  totalqty[j]=0;

                  for (int i = 0; i < tradelinegroups; i++) {

                      int step = ((chamberdataList.length + 1)*i);

                      totalqty[j]=Integer.parseInt(tradelines[(j+1) + step][5]) + totalqty[j];

                      tradelines[(j+1) + step][5] = Integer.toString(totalqty[j]);


                  }

              }

              //calculate grand total of quantity
              int grandtotalqty=0;

              for (int i = 0; i < totalqty.length; i++) {
                  grandtotalqty=grandtotalqty + totalqty[i];
              }

              int emptymilktypes=(tradelinegroups*4)-milktypesList.length;

              if (emptymilktypes>0) {


                  for (int i=0; i<emptymilktypes; i++) {

                      for (int j=tradelines.length-1;j>=tradelines.length-chamberdataList.length;j--) {

                          tradelines[j][4-i]="";

                      }

                  }

              }

              String grandtolalquantity=Integer.toString(grandtotalqty);

              //create doctradelaines
              String headerargs="%1$2s %2$6s %3$6s %4$6s %5$6s %6$7s %7$-8s";

              arrayindx=0;

              doctradelines = new String[]{};

              for (int i=0; i<tradelines.length; i++) {

                          doctradelines = Arrays.copyOf(doctradelines, doctradelines.length + 1);

                          doctradelines[arrayindx] = String.format(headerargs,tradelines[i][0],tradelines[i][1],
                                  tradelines[i][2],tradelines[i][3],tradelines[i][4],tradelines[i][5],
                                  tradelines[i][6]);

                          arrayindx++;

              }


              for (int i=0; i<doctradelines.length; i++) {
                  doctrade[lineno]=doctrade[lineno] + doctradelines[i];
                  doctrade[lineno]=doctrade[lineno] + "\n";
              }

              doctrade[lineno]=doctrade[lineno] + "------------------------------------------------";
              doctrade[lineno]=doctrade[lineno] + "\n";

              for (int i=0; i<tradelinegroups; i++) {

                  for (int j = 0; j < 4; j++) {

                      String milktype=milktypegroups[i][j];

                      if (!milktype.equals("     ")) {
                          doctrade[lineno]=doctrade[lineno] + String.format("%1$-23s %2$6s %3$7s","   ΣΥΝΟΛΙΚΗ ΠΟΣΟΤΗΤΑ : " ,milktypegroups[i][j] , Integer.toString(milktypegroupqtys[i][j])) ;
                          doctrade[lineno]=doctrade[lineno] + "\n";

                      }

                  }

              }

              doctrade[lineno]=doctrade[lineno] + "------------------------------------------------";
              doctrade[lineno]=doctrade[lineno] + "\n";
              doctrade[lineno]=doctrade[lineno] + String.format("%1$-23s %2$6s %3$7s","   ΓΕΝΙΚΟ ΣΥΝΟΛΟ     : " ,"     " , grandtolalquantity) ;
              doctrade[lineno]=doctrade[lineno] + "\n\n";
              doctrade[lineno]=doctrade[lineno] + "\n\n";

              String doccontent=doctrade[lineno];



              try {


                 mmOutputStream.write(doccontent.getBytes("ISO-8859-7"));

                  Thread.sleep(4000);

              } catch (Exception e) {
                  Toast.makeText(getContext(), getResources().getString(R.string.print_error_msg), Toast.LENGTH_SHORT).show();
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

   public void LoadTradeCheksData() {



        String trch_aa, trch_fromtradecode,trch_uptotradecode,tr_tradecode,tr_fromtradecode,tr_uptotradecode,transp_descr,
                mat_descr ,tr_qty,trl_aa,trl_chamber,trl_qty,trl_sample,tr_isdiakinisi,tr_ismetaggisi,tr_ismetaggisicheck,tr_fromkeb,tr_tokeb,tr_fromfactory,tr_tofactory="";


      Cursor rs = handler.getGroupedTradeChecksData(todaydate);

      rs.moveToFirst();

      if (!rs.isAfterLast()) {

          do {

              trch_aa = rs.getString(rs.getColumnIndex("trch_aa"));
              trch_fromtradecode = rs.getString(rs.getColumnIndex("trch_fromtradecode"));
              trch_uptotradecode = rs.getString(rs.getColumnIndex("trch_uptotradecode"));
              tr_tradecode = rs.getString(rs.getColumnIndex("tr_tradecode"));
              transp_descr = rs.getString(rs.getColumnIndex("transp_descr"));
              tr_qty = rs.getString(rs.getColumnIndex("tr_qty"));
              mat_descr = rs.getString(rs.getColumnIndex("mat_descr"));
              trl_aa = rs.getString(rs.getColumnIndex("trl_aa"));
              trl_chamber = rs.getString(rs.getColumnIndex("trl_chamber"));
              trl_qty = rs.getString(rs.getColumnIndex("trl_qty"));
              trl_sample = rs.getString(rs.getColumnIndex("trl_sample"));
              tr_fromtradecode = rs.getString(rs.getColumnIndex("tr_fromtradecode"));
              tr_uptotradecode = rs.getString(rs.getColumnIndex("tr_uptotradecode"));
              tr_isdiakinisi = rs.getString(rs.getColumnIndex("tr_isdiakinisi"));
              tr_ismetaggisi = rs.getString(rs.getColumnIndex("tr_ismetaggisi"));
              tr_ismetaggisicheck = rs.getString(rs.getColumnIndex("tr_ismetaggisicheck"));
              tr_fromkeb = rs.getString(rs.getColumnIndex("tr_fromkeb"));
              tr_tokeb = rs.getString(rs.getColumnIndex("tr_tokeb"));
              tr_fromfactory = rs.getString(rs.getColumnIndex("tr_fromfactory"));
              tr_tofactory = rs.getString(rs.getColumnIndex("tr_tofactory"));

              addTradeCheck(trch_aa,trch_fromtradecode,trch_uptotradecode,tr_tradecode,tr_fromtradecode,tr_uptotradecode,transp_descr,
                      mat_descr,tr_qty,trl_aa,trl_chamber,trl_qty,trl_sample,tr_isdiakinisi,tr_ismetaggisi,tr_ismetaggisicheck,tr_fromkeb,tr_tokeb,tr_fromfactory,tr_tofactory);



          } while (rs.moveToNext());//Move the cursor to the next row.


      } else {
          Toast.makeText(getContext(), "Δεν Βρέθηκαν Έλεγχοι Δρομολογίων!!", Toast.LENGTH_LONG).show();
          return;
      }


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

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {

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
                                               // myLabel.setText(data);
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
