package com.v4.nate.smokedetect.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.v4.nate.smokedetect.R;
import com.v4.nate.smokedetect.services.HushService;


public class NotificationActivity extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "NotificationActivity";
    SharedPreferences customSharedPreferences;
    SharedPreferences defaultSharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        System.out.println(remoteMessage);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        customSharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);

        Intent sendIntent = new Intent(this, HushService.class);
        sendIntent.setAction(HushService.ACTION1);
//        sendIntent.setClass(this, HushActivity.class);
//        sendIntent.putExtra("hush", true);
        PendingIntent sendPendingIntent = PendingIntent.getService(this, 0, sendIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        int type = Integer.parseInt(remoteMessage.getData().get("messageType"));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.channel_id), "SMOLDiR Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_exclamation_mark_alert, "Silence Active Alarm", sendPendingIntent).build();


        if (type == 10) { //Low battery
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Low Battery")
                    .setContentText("Time to charge your battery")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));


            notificationManager.notify(123, notification.build());

        } else if (type == 11) { //Very low battery
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Very Low Battery")
                    .setContentText("Time to change your battery")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(123, notification.build());
        } else if (type == 12) { //Battery disconnected
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Battery Disconnected")
                    .setContentText("Connect your battery")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(123, notification.build());
        } else if (type == 20) { //IR Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("IR Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(123, notification.build());
        } else if (type == 21) { //Smoke/IR Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Smoke/IR Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_exclamation_mark_alert, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(123, notification.build());
        } else if (type == 22) { //Smoke Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Smoke Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_exclamation_mark_alert, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(123, notification.build());
        } else if (type == 23) { //Interconnected Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Interconnected Fire Detected")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_exclamation_mark_alert, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(123, notification.build());
        } else if (type == 30) { //Smoke/IR Warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Smoke/IR warning")
                    .setContentText("Issuing a warning")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));


            notificationManager.notify(123, notification.build());
        } else if (type == 31) { //Smoke only warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Smoke Only Warning")
                    .setContentText("Smoke is present, stove situation")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));


            notificationManager.notify(1, notification.build());
        } else if (type == 40) { //CO alarm
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Carbon Monoxide Alarm")
                    .setContentText("Detected carbon monoxide")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_exclamation_mark_alert, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));


            notificationManager.notify(1, notification.build());
        } else if (type == 50) { //button test
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Button Test Alarm")
                    .setContentText("Button-Triggered Test Alarm")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_test)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(action)
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(1, notification.build());
        } else if (type == 51) { //remote test
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Remote Test Alarm")
                    .setContentText("Remote-Triggered Test Alarm")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_test)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_exclamation_mark_test, "Silence Active Alarm", sendPendingIntent)
                    .setChannelId(getString(R.string.channel_id));
            notificationManager.notify(1, notification.build());
        } else if (type == 60) { //Power warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle("Power warning")
                    .setContentText("Power has been disconnected")
                    .setSmallIcon(R.drawable.ic_exclamation_mark_warning)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setChannelId(getString(R.string.channel_id));

            notificationManager.notify(1, notification.build());
        } else {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "SMOLDiR")
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setSmallIcon(R.drawable.ic_exclamation_mark_alert)
                    .setChannelId(getString(R.string.channel_id));
            notificationManager.notify(1, notification.build());


        }


    }


}
