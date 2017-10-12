package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabasePushActivity extends AppCompatActivity {

    String homeID;
    String deviceID;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);
        homeID = sharedPreferences.getString("HomeID", null);
        deviceID = sharedPreferences.getString("DeviceID", null);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID).child(deviceID);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //Do nothing
                System.out.print("Nothing");
            } else if (extras.getBoolean("hush")) {
                //Push to database
                System.out.println("In the database thing");
                database.child("var").child("hush").setValue(true);
            }
        }
        setContentView(R.layout.activity_database_push);
    }
}
