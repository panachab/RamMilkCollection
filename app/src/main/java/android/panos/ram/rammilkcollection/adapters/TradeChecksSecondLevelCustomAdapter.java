package android.panos.ram.rammilkcollection.adapters;


import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.TradeCheckInfo;
import android.panos.ram.rammilkcollection.models.TradeCheckLinesInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TradeChecksSecondLevelCustomAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<TradeCheckInfo> trsList = new ArrayList<TradeCheckInfo>();

    public TradeChecksSecondLevelCustomAdapter(Context context, ArrayList<TradeCheckInfo> trsList) {
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


            TradeCheckInfo trInfo = (TradeCheckInfo) getGroup(groupPosition);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tradecheck_trades, null);
            TextView tradecode = (TextView) convertView.findViewById(R.id.tradecode);
            TextView transportation = (TextView) convertView.findViewById(R.id.transportation);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
            TextView fromtradecode = (TextView) convertView.findViewById(R.id.fromtradecode);
            TextView uptotradecode = (TextView) convertView.findViewById(R.id.uptotradecode);
            TextView fromtradecodetext = (TextView) convertView.findViewById(R.id.fromtradecodetext);
            TextView uptotradecodetext = (TextView) convertView.findViewById(R.id.uptotradecodetext);


            if (trInfo.getIsmetaggisi().trim().equals("1") || trInfo.getIsmetaggisicheck().trim().equals("1")) {
                fromtradecodetext.setText("Από ΚΕΒ:");
                uptotradecodetext.setText("Σε ΚΕΒ:");
            }
            else if (trInfo.getIsdiakinisi().trim().equals("1")){
                fromtradecodetext.setText("Από:");
                uptotradecodetext.setText("Προορισμός:");
            }
            else
            {
                fromtradecodetext.setText("Από Δελτίο:");
                uptotradecodetext.setText("Εως Δελτίο:");
            }
            tradecode.setText(trInfo.getTradecode().trim());
            transportation.setText(trInfo.getTransportation().trim());
            quantity.setText(trInfo.getQuantity().trim());
            if (trInfo.getFromTradecode() == null) {
                fromtradecode.setText("");
            }
            else
            {
                fromtradecode.setText(trInfo.getFromTradecode().trim());
            }
            if (trInfo.getUptoTradecode() == null) {
                uptotradecode.setText("");
            }
            else
            {
                uptotradecode.setText(trInfo.getUptoTradecode().trim());
            }


            return convertView;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<TradeCheckLinesInfo> trlinesList=new ArrayList<TradeCheckLinesInfo>();
        trlinesList = this.trsList.get(groupPosition).getTradeLinesList();
        return trlinesList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


            TradeCheckLinesInfo trlinesInfo = (TradeCheckLinesInfo) getChild(groupPosition, childPosition);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tradecheck_tradelines, null);
            TextView chamber = (TextView) convertView.findViewById(R.id.chamber);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
            TextView milktype = (TextView) convertView.findViewById(R.id.milktype);
            TextView sample = (TextView) convertView.findViewById(R.id.sample);

            chamber.setText(trlinesInfo.getChamber().trim());
            quantity.setText(trlinesInfo.getQuantity().trim());
            milktype.setText(trlinesInfo.getMilktype().trim());
            sample.setText(trlinesInfo.getSample().trim());

            return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<TradeCheckLinesInfo> trlinesList=new ArrayList<TradeCheckLinesInfo>();
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
