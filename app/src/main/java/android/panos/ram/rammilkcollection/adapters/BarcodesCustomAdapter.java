package android.panos.ram.rammilkcollection.adapters;

import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BarcodesCustomAdapter extends BaseAdapter {


    String stationList[];
    String barcodeList[];



    LayoutInflater inflter;

    public BarcodesCustomAdapter(Context applicationContext, String[] stationList, String[] barcodeList) {

        this.stationList = stationList;
        this.barcodeList = barcodeList;



        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return stationList.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.barcodes_list_view, null);

        TextView station = (TextView) view.findViewById(R.id.station);
        TextView barcode = (TextView) view.findViewById(R.id.barcode);

        station.setText(stationList[i]);
        barcode.setText(barcodeList[i]);

        return view;
    }


}