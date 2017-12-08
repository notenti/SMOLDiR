package com.v4.nate.smokedetect.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 12/7/17.
 */

public class HushService extends IntentService {

    public static final String ACTION1 = "ACTION1";
    public static final String ACTION2 = "ACTION2";

    public HushService() {
        super("HushService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("1376hh").child("Nate1");
            database.child("var").child("hush").setValue(true);
        } else if (ACTION2.equals(action)) {
            // do some other stuff...
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}
