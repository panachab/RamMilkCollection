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


public class CollectionChambersCustomAdapter extends  BaseAdapter {

    String chamberList[];
    String quantityList[];
    LayoutInflater inflter;

    public CollectionChambersCustomAdapter(Context applicationContext, String[] chamberList, String[] quantityList) {

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




            view = inflter.inflate(R.layout.collection_chambers_list_view, null);

            TextView chamber = (TextView) view.findViewById(R.id.chamber);
            final EditText quantity = (EditText) view.findViewById(R.id.quantity);

            chamber.setText(chamberList[position]);
            quantity.setText(quantityList[position]);


            quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(quantity.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                    quantityList[position] = quantity.getText().toString();
                }
                else{
                    quantityList[position] = "";
                }


            }
        });




        return view;


    }



}