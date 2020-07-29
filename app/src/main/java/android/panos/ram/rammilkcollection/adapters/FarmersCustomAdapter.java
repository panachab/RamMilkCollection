package android.panos.ram.rammilkcollection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import android.panos.ram.rammilkcollection.R;


public class FarmersCustomAdapter extends BaseAdapter {

    String nameList[];
    String codeList[];
    String afmList[];
    String phone11List[];

    LayoutInflater inflter;

    public FarmersCustomAdapter(Context applicationContext, String[] nameList, String[] codeList, String[] afmList,String[] phone11List) {

        this.nameList = nameList;
        this.codeList = codeList;
        this.afmList = afmList;
        this.phone11List = phone11List;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return nameList.length;
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
        view = inflter.inflate(R.layout.farmers_list_view, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView code = (TextView) view.findViewById(R.id.code);
        TextView afm = (TextView) view.findViewById(R.id.afm);
        TextView phone11 = (TextView) view.findViewById(R.id.phone11);

        name.setText(nameList[i]);
        code.setText(codeList[i]);
        afm.setText(afmList[i]);
        phone11.setText(phone11List[i]);


        return view;
    }


}