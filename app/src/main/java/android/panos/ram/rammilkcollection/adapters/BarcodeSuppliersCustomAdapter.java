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


public class BarcodeSuppliersCustomAdapter extends  BaseAdapter {

    String milktypeList[];
    String supplierList[];
    String quantityList[];
    String remarksList[];

    LayoutInflater inflter;

    public BarcodeSuppliersCustomAdapter(Context applicationContext, String[] supplierList, String[] milktypeList,String[] quantityList,String[] remarksList) {

        this.milktypeList = milktypeList;
        this.supplierList = supplierList;
        this.quantityList = quantityList;
        this.remarksList = remarksList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    public String [] getquantityArray() {
        return quantityList;
    }

    public String [] getremarksArray() {
        return remarksList;
    }

    @Override
    public int getCount() {
        return supplierList.length;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

            view = inflter.inflate(R.layout.barcode_suppliers_list_view, null);

            TextView supplier = (TextView) view.findViewById(R.id.supplier);
            TextView milktype = (TextView) view.findViewById(R.id.milktype);

            final EditText quantity = (EditText) view.findViewById(R.id.quantity);
            final EditText remarks = (EditText) view.findViewById(R.id.remarks);

            supplier.setText(supplierList[position]);
            milktype.setText(milktypeList[position]);
            quantity.setText(quantityList[position]);
            remarks.setText(remarksList[position]);

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

        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                remarksList[position]=remarks.getText().toString();
            }
        });



        return view;


    }



}