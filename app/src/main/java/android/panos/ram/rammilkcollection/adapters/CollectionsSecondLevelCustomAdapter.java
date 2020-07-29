package android.panos.ram.rammilkcollection.adapters;


import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradeLinesInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CollectionsSecondLevelCustomAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CollectionTradesInfo> trsList = new ArrayList<CollectionTradesInfo>();

    public CollectionsSecondLevelCustomAdapter(Context context, ArrayList<CollectionTradesInfo> trsList) {
        this.context = context;
        this.trsList = trsList;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.trsList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.trsList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            CollectionTradesInfo trInfo = (CollectionTradesInfo) getGroup(groupPosition);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.collection_trades, null);
            TextView tradecode = (TextView) convertView.findViewById(R.id.tradecode);
            TextView supplier = (TextView) convertView.findViewById(R.id.supplier);
            TextView milktype = (TextView) convertView.findViewById(R.id.milktype);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
            tradecode.setText(trInfo.getTradecode().trim());
            supplier.setText(trInfo.getSupplier().trim());
            milktype.setText(trInfo.getMilktype().trim());
            quantity.setText(trInfo.getQuantity().trim());

            return convertView;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<CollectionTradeLinesInfo> trlinesList=new ArrayList<CollectionTradeLinesInfo>();
        trlinesList = this.trsList.get(groupPosition).getTradeLinesList();
        return trlinesList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            CollectionTradeLinesInfo trlinesInfo = (CollectionTradeLinesInfo) getChild(groupPosition, childPosition);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.collection_tradelines, null);
            TextView chamber = (TextView) convertView.findViewById(R.id.chamber);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);

            chamber.setText(trlinesInfo.getChamber().trim());
            quantity.setText(trlinesInfo.getQuantity().trim());

            return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<CollectionTradeLinesInfo> trlinesList=new ArrayList<CollectionTradeLinesInfo>();
        trlinesList = this.trsList.get(groupPosition).getTradeLinesList();
        return trlinesList.size();

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
