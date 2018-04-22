package com.v4.nate.smokedetect.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*  Service to enable interaction with notifications without
* opening up the app */
public class HushService extends IntentService {

    public static final String ACTION1 = "ACTION1";

    public HushService() {
        super("HushService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(1);
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("1376hh").child("Nate1");
            database.child("var").child("hush").setValue(true);
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}
