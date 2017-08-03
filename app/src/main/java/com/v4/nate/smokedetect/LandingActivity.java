package com.v4.nate.smokedetect;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {


    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        listView = findViewById(R.id.navList);
        drawerLayout = findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    private void addDrawerItems() {
        String[] navBarOptions = {"Notifications", "Detector Health", "History", "Preferences"};
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navBarOptions);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                Fragment detectorHealth = new DetectorHealthFragment();
                Fragment historyFragment = new HistoryFragment();
                Fragment generalPreferencesFragment = new GeneralPreferencesFragment();
                Fragment notificationPreferencesFragment = new NotificationPreferencesFragment();
                switch (i) {
                    case 0:
                        fragmentTransaction.replace(R.id.frameLayout, notificationPreferencesFragment);
                        fragmentTransaction.addToBackStack("notificationPreferences");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.frameLayout, detectorHealth);
                        fragmentTransaction.addToBackStack("detector");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.frameLayout, historyFragment);
                        fragmentTransaction.addToBackStack("history");
                        fragmentTransaction.commit();
                        listView.setItemChecked(i, true);
                        drawerLayout.closeDrawer(listView);
                        break;
                    case 3:
                        fragmentTransaction.replace(R.id.frameLayout, generalPreferencesFragment);
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
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

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
