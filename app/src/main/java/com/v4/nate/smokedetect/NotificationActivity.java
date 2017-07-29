package com.v4.nate.smokedetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;


public class NotificationActivity extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "NotificationActivity";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent sendIntent = new Intent(getApplicationContext(), SendToDevicesActivity.class);
        sendIntent.putExtra("methodName", "hush");

        PendingIntent sendPendingIntent = PendingIntent.getActivity(this, 0, sendIntent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        int type = Integer.parseInt(remoteMessage.getData().get("messageType"));

        if (type == MessageType.LOW_BATTERY.ordinal()) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(remoteMessage.getData().get("body"))
                            .setBigContentTitle(remoteMessage.getData().get("title"))
                            .setSummaryText(remoteMessage.getData().get("summary")));
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(123, notification.build());

        } else if (type == MessageType.ACTIVE_ALARM.ordinal()) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setSmallIcon(R.drawable.ic_smoke_free_black_24dp)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Silence Active Alarm", sendPendingIntent)
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
        ACTIVE_ALARM
    }
}
