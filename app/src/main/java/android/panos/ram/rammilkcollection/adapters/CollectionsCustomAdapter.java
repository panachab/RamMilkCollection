package android.panos.ram.rammilkcollection.adapters;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.activities.Collections;
import android.panos.ram.rammilkcollection.models.CollectionHeaderInfo;
import android.panos.ram.rammilkcollection.models.CollectionTradesInfo;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.SecondLevelExpandableListView;
import android.view.Gravity;
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

public class CollectionsCustomAdapter extends BaseExpandableListAdapter {


    private Context context;
    private ArrayList<CollectionHeaderInfo> collectionList;

    public CollectionsCustomAdapter(Context context, ArrayList<CollectionHeaderInfo> collectionList) {

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

                CollectionHeaderInfo collectionInfo = (CollectionHeaderInfo) getGroup(groupPosition);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.collection_header, null);
                TextView aa = (TextView) convertView.findViewById(R.id.aa);
                TextView sample = (TextView) convertView.findViewById(R.id.sample);
                TextView fat = (TextView) convertView.findViewById(R.id.fat);
                TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
                TextView ph = (TextView) convertView.findViewById(R.id.ph);

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

            aa.setText(collectionInfo.getAA().trim());
            sample.setText(collectionInfo.getSample().trim());
            fat.setText(collectionInfo.getFat().trim());
            temperature.setText(collectionInfo.getTemperature().trim());
            ph.setText(collectionInfo.getPh().trim());

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
