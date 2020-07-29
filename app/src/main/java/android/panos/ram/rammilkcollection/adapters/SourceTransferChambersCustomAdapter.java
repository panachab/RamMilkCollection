package android.panos.ram.rammilkcollection.adapters;

import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class SourceTransferChambersCustomAdapter extends  BaseAdapter {

    String chamberList[];
    String quantityList[];
    LayoutInflater inflter;

    public SourceTransferChambersCustomAdapter(Context applicationContext, String[] chamberList, String[] quantityList) {

        this.chamberList = chamberList;
        this.quantityList = quantityList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    public String [] getquantityArray() {
        return quantityList;
    }


    @Override
    public int getCount() {
        return chamberList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {


            view = inflter.inflate(R.layout.source_transfer_chambers_list_view, null);

            TextView chamber = (TextView) view.findViewById(R.id.chamber);
            final TextView quantity = (TextView) view.findViewById(R.id.quantity);

            FloatingActionButton getKeb = (FloatingActionButton) view.findViewById(R.id.getKeb);



            getKeb.setTag(new Integer(position));
            getKeb.setOnClickListener(null);
            getKeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((ListView) viewGroup).performItemClick(v,position, 0);
                }

            });

            chamber.setText(chamberList[position]);
            quantity.setText(quantityList[position]);



        return view;


    }



}