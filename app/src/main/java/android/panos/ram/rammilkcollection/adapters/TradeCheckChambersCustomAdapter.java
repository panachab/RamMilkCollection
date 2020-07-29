package android.panos.ram.rammilkcollection.adapters;

import android.content.Context;
import android.panos.ram.rammilkcollection.R;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


public class TradeCheckChambersCustomAdapter extends  BaseAdapter {

    String chamberList[];
    String quantityList[];
    String sampleList[];

    LayoutInflater inflter;

    public TradeCheckChambersCustomAdapter(Context applicationContext, String[] chamberList, String[] quantityList,String[] sampleList) {

        this.chamberList = chamberList;
        this.quantityList = quantityList;
        this.sampleList = sampleList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    public String [] getsampleArray() {
        return sampleList;
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




            view = inflter.inflate(R.layout.tradecheck_chambers_list_view, null);

            TextView chamber = (TextView) view.findViewById(R.id.chamber);
            TextView quantity = (TextView) view.findViewById(R.id.quantity);
            final EditText sample = (EditText) view.findViewById(R.id.sample);

            chamber.setText(chamberList[position]);
            quantity.setText(quantityList[position]);
            sample.setText(sampleList[position]);


            sample.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(sample.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    sampleList[position] = sample.getText().toString();
                }
                else{
                    sampleList[position] = "";
                }


            }
        });




        return view;


    }



}