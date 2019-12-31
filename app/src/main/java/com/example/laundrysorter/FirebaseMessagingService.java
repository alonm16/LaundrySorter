package com.example.laundrysorter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra("body",remoteMessage.getData().get("body"));
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        (int)System.currentTimeMillis(),
                        resultIntent,
                        PendingIntent.FLAG_ONE_SHOT
                );

        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(remoteMessage.getData().get("body")).setAutoCancel(true).setContentIntent(resultPendingIntent);

        builder.setContentIntent(resultPendingIntent);

        int notificationId = (int)System.currentTimeMillis();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(channelId);
            manager.createNotificationChannel(channel);
        }

        manager.notify(notificationId, builder.build());
    }
}
