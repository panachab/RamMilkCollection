package android.panos.ram.rammilkcollection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.fragments.CollectionChambersFragment;
import android.panos.ram.rammilkcollection.fragments.CollectionDataFragment;
import android.panos.ram.rammilkcollection.fragments.CollectionQualityFragment;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


import android.panos.ram.rammilkcollection.R;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;

public class CollectionTabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String station;
    String stationid;
    String barcode;
    String rtcodeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        setContentView(R.layout.collection_activity_scrollable_tabs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Παραλαβή Γάλακτος</small>"));

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("Collection");

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent in =  this.getIntent();
        station = String.valueOf(in.getExtras().getString("station"));
        stationid = String.valueOf(in.getExtras().getString("stationid"));
        barcode = String.valueOf(in.getExtras().getString("barcode"));
        rtcodeid = String.valueOf(in.getExtras().getString("rtcodeid"));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CollectionDataFragment(), "ΠΑΡΑΛΑΒΗ");
        adapter.addFrag(new CollectionChambersFragment(), "ΘΑΛΑΜΟΙ");
        adapter.addFrag(new CollectionQualityFragment(), "ΔΕΙΓΜΑ-ΠΟΙΟΤΗΤΑ");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return false;
        }
        else {
            return true;
        }
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), Collections.class);
            intent.putExtra("station",station);
            intent.putExtra("stationid",stationid);
            intent.putExtra("barcode",barcode);
            intent.putExtra("rtcodeid",rtcodeid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            //onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
