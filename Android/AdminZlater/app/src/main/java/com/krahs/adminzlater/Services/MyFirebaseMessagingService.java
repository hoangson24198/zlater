package com.krahs.adminzlater.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.krahs.adminzlater.Activity.MainActivity;
import com.krahs.adminzlater.R;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Log.d("HS::","from"+ remoteMessage.getFrom());
        Log.e("HS::", "received message");
        sendNotification(notification, data);
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));

//        Intent intent = new Intent(this, BillActivity.class);
//        intent.putExtras(bundle);

        SharedPreferences sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
       String user= sharedPreferences.getString("username","");
        PendingIntent pendingIntent;
        if(user.equals("admin@zlater.com")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtras(bundle);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtras(bundle);
            pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                    .setContentIntent(pendingIntent)
                    .setContentInfo("Hello")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setColor(getColor(R.color.colorAccent))
                    .setLights(Color.RED, 1000, 300)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setNumber(++numMessages)
                    .setSmallIcon(R.drawable.logo);
            Log.e("HS::","Create notification android N");
        }

        try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            Log.e("HS::","Create notification android 9");
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }
}