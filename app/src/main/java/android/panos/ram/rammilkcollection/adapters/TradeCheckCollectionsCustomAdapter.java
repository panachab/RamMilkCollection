package android.panos.ram.rammilkcollection.adapters;
import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.CollectionHeaderInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.SecondLevelExpandableListView;
import android.panos.ram.rammilkcollection.models.TradeCheckCollectionHeaderInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TradeCheckCollectionsCustomAdapter extends BaseExpandableListAdapter {


    private Context context;
    private ArrayList<TradeCheckCollectionHeaderInfo> collectionList;

    public TradeCheckCollectionsCustomAdapter(Context context, ArrayList<TradeCheckCollectionHeaderInfo> collectionList) {

        this.context = context;
        this.collectionList = collectionList;

    }


    @Override
    public Object getGroup(int groupPosition) {
        return collectionList.get(groupPosition);

    }

    @Override
    public int getGroupCount() {
        return collectionList.size();
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

    public ArrayList<CollectionTradesInfo> getTrList(int groupPosition) {
        return collectionList.get(groupPosition).getTradesList();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {

                TradeCheckCollectionHeaderInfo collectionInfo = (TradeCheckCollectionHeaderInfo) getGroup(groupPosition);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tradecheck_collection_header, null);
                TextView station = (TextView) convertView.findViewById(R.id.station);
                TextView pagolekani = (TextView) convertView.findViewById(R.id.pagolekani);
                TextView sample = (TextView) convertView.findViewById(R.id.sample);
                TextView amount = (TextView) convertView.findViewById(R.id.amount);


                station.setText(collectionInfo.getStation().trim());
                sample.setText(collectionInfo.getSample().trim());
                pagolekani.setText(collectionInfo.getPagolekani().trim());
                amount.setText(collectionInfo.getAmount().trim());

            return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        secondLevelELV.setAdapter(new CollectionsSecondLevelCustomAdapter(context, getTrList(groupPosition)));

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


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
