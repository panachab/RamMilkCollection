package android.panos.ram.rammilkcollection.adapters;
import android.app.Dialog;
import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.CollectionHeaderInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.SecondLevelExpandableListView;
import android.panos.ram.rammilkcollection.models.TradeCheckHeaderInfo;
import android.panos.ram.rammilkcollection.models.TradeCheckInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TradeChecksCustomAdapter extends BaseExpandableListAdapter {


    private Context context;
    private ArrayList<TradeCheckHeaderInfo> tradechecksList;
    private int headerclickposition;


    public TradeChecksCustomAdapter(Context context, ArrayList<TradeCheckHeaderInfo> tradechecksList) {

        this.context = context;
        this.tradechecksList = tradechecksList;
        this.headerclickposition = -1;

    }

    public void setheaderclickposition(int headerposition) {
        this.headerclickposition = headerposition;
    }

    public int getheaderclickposition() {
        return headerclickposition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tradechecksList.get(groupPosition);

    }


    @Override
    public int getGroupCount() {
        return tradechecksList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

       // no idea why this code is working

        return 1;

    }


    @Override
    public Object getChild(int group, int child) {

        return child;

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public ArrayList<TradeCheckInfo> getTrList(int groupPosition) {
        return tradechecksList.get(groupPosition).getTradesList();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {

                TradeCheckHeaderInfo tradecheckInfo = (TradeCheckHeaderInfo) getGroup(groupPosition);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.trade_check_header, null);
                TextView aa = (TextView) convertView.findViewById(R.id.aa);
                TextView fromtradecode = (TextView) convertView.findViewById(R.id.fromtradecode);
                TextView uptotradecode = (TextView) convertView.findViewById(R.id.uptotradecode);

                Button cancelButton  = (Button) convertView.findViewById((R.id.cancelButton));


                cancelButton.setTag(new Integer(groupPosition));
                cancelButton.setOnClickListener(null);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((ExpandableListView) parent).performItemClick(v,groupPosition, 0);
                    }

                });


                Button printButton  = (Button) convertView.findViewById((R.id.printButton));

                printButton.setTag(new Integer(groupPosition));
                printButton.setOnClickListener(null);

                printButton.setTag(new Integer(groupPosition));
                printButton.setOnClickListener(null);
                printButton.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {

                        ((ExpandableListView) parent).performItemClick(v,groupPosition, 0);
                    }

                });


                if (groupPosition>0) {
                    cancelButton.setVisibility(View.INVISIBLE);
                }

                aa.setText(tradecheckInfo.getAA().trim());
                fromtradecode.setText(tradecheckInfo.getFromTradecode().trim());
                uptotradecode.setText(tradecheckInfo.getUpToTradecode().trim());

                return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        secondLevelELV.setAdapter(new TradeChecksSecondLevelCustomAdapter(context, getTrList(groupPosition)));

        secondLevelELV.setGroupIndicator(null);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

/*
        secondLevelELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (groupPosition >= 0) {

                    final String[] myImageNameList = new String[]{"ΟΛΥΜΠΟΣ","ΤΥΡΑΣ","ΡΟΔΟΠΗ"};


                    TradeCheckHeaderInfo tradecheckInfo= tradechecksList.get(getheaderclickposition());
                    //get the child info
                    TradeCheckInfo tradeInfo =  tradecheckInfo.getTradesList().get(groupPosition);

                    String tradecode=tradeInfo.getTradecode();

                    final Dialog dialog = new Dialog(context);
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
                    ArrayAdapter arrayAdapter = new ArrayAdapter(context,R.layout.factories_list_item, R.id.tv, myImageNameList);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String panagos="You have clicked : " + myImageNameList[position];
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }


                return false;

            }
        });
*/
        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
