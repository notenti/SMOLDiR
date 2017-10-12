package com.v4.nate.smokedetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;


public class NotificationActivity extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "NotificationActivity";
    SharedPreferences customSharedPreferences;
    SharedPreferences defaultSharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        customSharedPreferences = this.getSharedPreferences("ID", Context.MODE_PRIVATE);
        final String deviceName = customSharedPreferences.getString("DeviceName", null);
        Boolean hush = defaultSharedPreferences.getBoolean("hush", false);
        Boolean localized = defaultSharedPreferences.getBoolean("localized", false);

        Intent sendIntent = new Intent(getApplicationContext(), SendToDevicesActivity.class);
        sendIntent.putExtra("methodName", "hush");

        PendingIntent sendPendingIntent = PendingIntent.getActivity(this, 0, sendIntent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        int type = Integer.parseInt(remoteMessage.getData().get("messageType"));

        if (type == 10) { //Low battery
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Low Battery")
                    .setContentText("Time to charge your battery")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());

        } else if (type == 11) { //Very low battery
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Very Low Battery")
                    .setContentText("Time to change your battery")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 12) { //Battery disconnected
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Battery Disconnected")
                    .setContentText("Connect your battery")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 20) { //IR Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("IR Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 21) { //Smoke/IR Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Smoke/IR Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 22) { //Smoke Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Smoke Detected Fire")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 23) { //Interconnected Detected Fire
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Interconnected Fire Detected")
                    .setContentText("Fire detected!!!!")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 30) { //Smoke/IR Warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Smoke/IR warning")
                    .setContentText("Issuing a warning")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));


            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 31) { //Smoke only warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Smoke Only Warning")
                    .setContentText("Smoke is present, stove situation")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));


            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 40) { //CO alarm
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Carbon Monoxide Alarm")
                    .setContentText("Detected carbon monoxide")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);


            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 50) { //button test
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("button test alarm")
                    .setContentText("button test alarm body")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 51) { //remote test
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("remote test alarm")
                    .setContentText("remote test alarm body")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else if (type == 60) { //Power warning
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Power warning")
                    .setContentText("Power has been disconnected")
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());
        } else {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary));
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());


        }


    }

    //Create simple notification containing the received FCM message.
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                .setContentTitle("Quarters")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(android.R.drawable.ic_menu_compass, "Details", pendingIntent)
                .addAction(android.R.drawable.ic_menu_directions, "Show House", pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notif.build());
    }


    public enum MessageType {
        LOW_BATTERY,
        VERY_LOW_BATTERY,
        BATTERY_DISCONNECTED,
        IR_DETECTED_FIRE,
        SMOKE_IR_DETECTED_FIRE,
        SMOKE_DETECTED_FIRE,
        INTERCONNECTED_DETECTED_FIRE,
        CO_DETECTED,
        TEST_ALARM
    }
}
