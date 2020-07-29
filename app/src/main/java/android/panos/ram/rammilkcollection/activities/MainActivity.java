package android.panos.ram.rammilkcollection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.panos.ram.rammilkcollection.fragments.CoolingTanksFragment;
import android.panos.ram.rammilkcollection.fragments.HomeFragment;
import android.panos.ram.rammilkcollection.fragments.TradeChecksFragment;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private String stavros;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_COLLECTION = "collection";
    private static final String TAG_TRADESCHECK = "tradescheck ";

    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final GlobalClass globalVariables = (GlobalClass) getApplicationContext();
        final String callingForm=globalVariables.getcallingForm();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // initializing navigation menu
        setUpNavigationView();

            if (callingForm != null) {
                switch (callingForm) {
                    case "Collection":
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_COLLECTION;
                        selectNavMenu();
                        loadHomeFragment();
                        break;
                    case "Metaggisi":
                    case "TradesCheck":
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_TRADESCHECK ;
                        selectNavMenu();
                        loadHomeFragment();
                        break;

                    case "UpdateMobile":
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                    case "UpdateCentral":
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                    case "UpdateApplication":
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                    case "Config":
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                    case "Company":
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                    case "DeleteDocs":
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        navItemIndex = 0;
                        loadHomeFragment();
                        break;
                }

            }
            else
                {
                    if (savedInstanceState == null) {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        selectNavMenu();
                        loadHomeFragment();
                    }


            }


    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
       // selectNavMenu();

        // set toolbar title
        setToolbarTitle();


        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                CoolingTanksFragment coolingTanksFragment7 = new CoolingTanksFragment();
                return coolingTanksFragment7;


            case 2:
                TradeChecksFragment tradeChecksFragment = new TradeChecksFragment();
                return tradeChecksFragment;

            default:
                return new HomeFragment();

        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
       navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_collection:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_COLLECTION;
                        break;
                    case R.id.nav_tradescheck:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_TRADESCHECK;
                        break;
                    case R.id.nav_mobile_update:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, UpdateMobile.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_central_update:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, UpdateCentral.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_application_update:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, UpdateApplication.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_settings:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, DisplayConfig.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_company:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, CompanyData.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.delete_docs:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_HOME;
                        loadHomeFragment();
                        startActivity(new Intent(MainActivity.this, DeleteDocs.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                //drawer.closeDrawers();
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


}
