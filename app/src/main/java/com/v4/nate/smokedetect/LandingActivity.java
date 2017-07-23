package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {


    Button firstFragment, secondFragment;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        listView = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }

    private void addDrawerItems() {
        String[] navBarOptions = {"Notifications", "Detector Health", "History", "Preferences"};
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navBarOptions);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Fragment();
                switch (i) {
                    case 0:
                        fragment = new NotificationConfigureFragment();
                        break;
                    case 1:
                        fragment = new DetectorHealthFragment();
                        break;
                    case 2:
                        Toast.makeText(LandingActivity.this, "3", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(LandingActivity.this, "4", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
                listView.setItemChecked(i, true);
                drawerLayout.closeDrawer(listView);

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

            //Called when a drawer has settled in a completely closed state
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
