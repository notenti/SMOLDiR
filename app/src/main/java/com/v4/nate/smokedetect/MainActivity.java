package com.v4.nate.smokedetect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String HomeID = sharedPref.getString("HomeID", "smokeDetect");

        FirebaseMessaging.getInstance().subscribeToTopic(HomeID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = HomeID;
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }


    }
}




