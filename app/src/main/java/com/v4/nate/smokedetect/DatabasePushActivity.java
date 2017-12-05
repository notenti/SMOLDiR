package com.v4.nate.smokedetect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabasePushActivity extends AppCompatActivity {

    String homeID;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);
        homeID = sharedPreferences.getString("HomeID", null);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(homeID);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //Do nothing
                System.out.print("Nothing");
            } else if (extras.getBoolean("hush")) {
                database.child("var").child("hush").getRef().setValue(true);

            }
        }
        setContentView(R.layout.activity_database_push);
    }

}
