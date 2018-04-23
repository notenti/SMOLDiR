package com.v4.nate.smokedetect.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.fragments.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        WelcomeFragment welcomeFragment = new WelcomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.welcomeFrameLayout, welcomeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void setNewFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_to_center, R.anim.center_to_left, R.anim.left_to_center, R.anim.center_to_right);
        fragmentTransaction.replace(R.id.welcomeFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

