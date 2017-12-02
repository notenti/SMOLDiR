package com.v4.nate.smokedetect;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {

    DatabaseQuery databaseQuery = new DatabaseQuery();
    SharedPreferences sharedPreferences;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);


        sharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);
//        String homeID = sharedPreferences.getString("HomeID", null);
//        String deviceID = sharedPreferences.getString("DeviceID", null);

        String homeID = "1376hh";
        String deviceID = "12ab12";


        listView = findViewById(R.id.navList);
        drawerLayout = findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        View view = inflater.inflate(R.layout.custom_actionbar, null);
//        TextView textView = view.findViewById(R.id.title_text);
//        textView.setText("Home");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment landingFragment = new LandingFragment();

        fragmentTransaction.add(R.id.landing_fragment, landingFragment);
        fragmentTransaction.addToBackStack("testFragment");
        fragmentTransaction.addToBackStack("landingFragment");
        fragmentTransaction.commit();

//        actionBar.setCustomView(view);
//        actionBar.setDisplayShowCustomEnabled(true);


    }

    private void addDrawerItems() {
        String[] navBarOptions = {"Notifications", "History", "Preferences"};
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navBarOptions);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                Fragment historyFragment = new HistoryFragment();
                Fragment generalPreferencesFragment = new GeneralPreferencesFragment();
                Fragment notificationPreferencesFragment = new NotificationPreferencesFragment();
                switch (i) {
                    case 0:
                        fragmentTransaction.replace(R.id.landing_fragment, notificationPreferencesFragment);
                        fragmentTransaction.addToBackStack("notificationPreferences");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.landing_fragment, historyFragment);
                        fragmentTransaction.addToBackStack("history");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.landing_fragment, generalPreferencesFragment);
                        fragmentTransaction.addToBackStack("generalPreferences");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    default:
                        Toast.makeText(LandingActivity.this, "4", Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });
    }

    public void setNewFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slideup, 0, 0, R.anim.slidedown);
//        fragmentTransaction.setCustomAnimations(R.anim.right_to_center, R.anim.center_to_left, R.anim.left_to_center, R.anim.center_to_right);
        fragmentTransaction.add(R.id.landing_fragment, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            //Called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                listView.bringToFront();
                listView.requestLayout();
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); //creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

//    @Override
//    public void onBackPressed() {
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
