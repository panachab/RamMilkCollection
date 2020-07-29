package android.panos.ram.rammilkcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.adapters.TradeCheckCollectionsCustomAdapter;
import android.panos.ram.rammilkcollection.models.CollectionTradeLinesInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.TradeCheckCollectionHeaderInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;


public class TradeCheckCollections extends Fragment {


  String todaydate;

  int displayheight;

  private TradeCheckCollectionsCustomAdapter collectionsCustomAdapter;
  private ArrayList<TradeCheckCollectionHeaderInfo> collectionList = new ArrayList<TradeCheckCollectionHeaderInfo>();

  private LinkedHashMap<String, TradeCheckCollectionHeaderInfo> collectionsubjects = new LinkedHashMap<String, TradeCheckCollectionHeaderInfo>();
  LinkedHashMap<String, CollectionTradesInfo> tradesubjects = new LinkedHashMap<String, CollectionTradesInfo>();
  LinkedHashMap<String, CollectionTradeLinesInfo> tradelinessubjects = new LinkedHashMap<String, CollectionTradeLinesInfo>();

  private ExpandableListView collectionsListView;

  DBHelper handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tradecheck_collections, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Έλεγχος Δρομολογίου</small>"));


      Calendar newCalendar = Calendar.getInstance();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
      todaydate = dateFormatter.format(newCalendar.getTime());

      Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      DisplayMetrics dm = new DisplayMetrics();
      display.getMetrics(dm);
      displayheight = dm.heightPixels;


      handler=new DBHelper(getActivity());


      LoadCollectionsData();

      collectionsListView = (ExpandableListView) getView().findViewById(R.id.collectionsList);
      collectionsListView.getLayoutParams().height = 20*displayheight;

      collectionsCustomAdapter = new TradeCheckCollectionsCustomAdapter(getActivity(), collectionList);

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




  }


  //method to collapse all groups
  private void collapseAll() {
      int count = collectionsCustomAdapter.getGroupCount();
      for (int i = 0; i < count; i++){
          collectionsListView.collapseGroup(i);
      }
  }

  private int addCollection(String coll_id,String station,String pagolekani,String amount ,String sample,String fat, String temperature,String ph,
                            String tradecode, String supplier,String milktype,String tr_quantity,
                            String trl_aa ,String chamber, String trl_quantity){


      int groupPosition = 0;

      //check the hash map if the group already exists
      TradeCheckCollectionHeaderInfo collectionInfo =  collectionsubjects.get(coll_id);
      //add the group if doesn't exists
      if(collectionInfo == null) {
          collectionInfo = new TradeCheckCollectionHeaderInfo();
          collectionInfo.setid(coll_id);
          collectionInfo.setSample(sample);
          collectionInfo.setStation(station);
          collectionInfo.setPagolekani(pagolekani);
          collectionInfo.setAmount(amount);
          collectionsubjects.put(coll_id, collectionInfo);
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


   public void LoadCollectionsData() {


        String coll_id,coll_station,coll_pagolekani,coll_qty,coll_samplecode,coll_fat,coll_temperature,coll_ph,
              tr_code,sup_name,mat_descr ,tr_qty,trl_aa,trl_chamber,trl_qty="";



      Cursor rs = handler.getCollectionsDataToCheck(todaydate);

      rs.moveToFirst();

      if (!rs.isAfterLast()) {

          do {

              coll_id = rs.getString(rs.getColumnIndex("coll_id"));
              coll_station = rs.getString(rs.getColumnIndex("coll_station"));
              coll_qty = rs.getString(rs.getColumnIndex("coll_qty"));
              coll_pagolekani = rs.getString(rs.getColumnIndex("coll_pagolekani"));
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

              addCollection(coll_id,coll_station,coll_pagolekani,coll_qty,coll_samplecode,coll_fat,coll_temperature,coll_ph,
                      tr_code,sup_name,mat_descr,tr_qty,trl_aa,trl_chamber,trl_qty);

          } while (rs.moveToNext());//Move the cursor to the next row.


      } else {
          Toast.makeText(getActivity(), "Δεν Βρέθηκαν Παραλαβές!!", Toast.LENGTH_LONG).show();
          return;
      }


    }

    private AlertDialog.Builder MessageAlert(String message) {

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
