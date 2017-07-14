package com.v4.nate.smokedetect;

/**
 * Created by nate on 7/11/17.
 */

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
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationActivity extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

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

        if (remoteMessage.getData().get("ID").equals("1")) {
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
                    .setColor(ContextCompat.getColor(this, R.color.primary))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Detector ID: " + remoteMessage.getData().get("ID"))
                            .setBigContentTitle(remoteMessage.getData().get("title"))
                            .setSummaryText("+3 more"));
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
                    .addAction(R.drawable.ic_smoke_free_black_24dp, "Hush", sendPendingIntent)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 500, 500)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setFullScreenIntent(pendingIntent, true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.primary))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("TEST 1")
                            .addLine("TEST 2")
                            .setBigContentTitle(remoteMessage.getData().get("title"))
                            .setSummaryText("+3 more"));
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
}
