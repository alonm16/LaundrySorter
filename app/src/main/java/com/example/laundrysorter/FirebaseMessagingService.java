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
        String check = remoteMessage.getData().get("basket1");
        int basket1 = Integer.parseInt(remoteMessage.getData().get("basket1"));
        int basket2 = Integer.parseInt(remoteMessage.getData().get("basket2"));
        int basket3 = Integer.parseInt(remoteMessage.getData().get("basket3"));
        int basket1Before = Integer.parseInt(remoteMessage.getData().get("basket1Before"));
        int basket2Before = Integer.parseInt(remoteMessage.getData().get("basket2Before"));
        int basket3Before = Integer.parseInt(remoteMessage.getData().get("basket3Before"));
        String content;
        if( (basket1Before==1 && basket1==0) || (basket2Before==1 && basket2==0) || (basket3Before==1 && basket3==0)){
            return;
        }

        else if (basket1Before==0 && basket1==1)
        {
            content = "Basket 1 is full!";
        }
        else if (basket2Before==0 && basket2==1)
        {
            content = "Basket 2 is full!";
        }
        else if (basket3Before==0 && basket3==1)
        {
            content = "Basket 3 is full!";
        }
        else
            return;
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
                .setContentText(content).setAutoCancel(true).setContentIntent(resultPendingIntent);

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
