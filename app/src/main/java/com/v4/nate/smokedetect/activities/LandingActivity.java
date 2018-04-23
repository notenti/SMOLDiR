package com.v4.nate.smokedetect.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.fragments.LandingFragment;

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment landingFragment = new LandingFragment();

        fragmentTransaction.add(R.id.landing_fragment, landingFragment);
        fragmentTransaction.addToBackStack("landingFragment");
        fragmentTransaction.commit();
    }

    public void setNewFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slideup, 0, 0, R.anim.slidedown);
        fragmentTransaction.add(R.id.landing_fragment, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


}
