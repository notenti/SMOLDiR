package com.v4.nate.smokedetect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v4.nate.smokedetect.R;

public class HushActivity extends AppCompatActivity {

    String homeID = "1376hh";
    String deviceID = "Nate1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(deviceID);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                System.out.println("HHHHHHHsdsdfsdfsdfsd");
            } else if (extras.getBoolean("hush")) {
                System.out.println("in the pending intent");
                database.child("var").child("hush").setValue(true);

            }
        }
        setContentView(R.layout.activity_database_push);
    }

}
