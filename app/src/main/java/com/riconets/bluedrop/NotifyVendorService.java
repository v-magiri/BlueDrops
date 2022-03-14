package com.riconets.bluedrop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class NotifyVendorService extends FirebaseMessagingService {
    private static final String TAG = "OnMessagedReceived";
    private final String ADMIN_CHANNEL_ID ="admin_channel";
    Uri defaultSound;

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String notificationTitle="";
        String notificationBody="";
        String notificationData="";
        if(remoteMessage!=null){
            notificationData=remoteMessage.getData().toString();
            notificationBody=remoteMessage.getNotification().getBody();
            notificationTitle=remoteMessage.getNotification().getTitle();
            sendNotification(notificationTitle,notificationBody);
        }else{
            Log.d(TAG, "onMessageReceived: ");
        }
    }

    private void sendNotification(String notificationTitle, String notificationBody) {
        final String channel_Id="com.riconets.bluedropsvendor;";
        Intent intent=new Intent(this,CustomerHome.class);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notification_id=new Random().nextInt(3000);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
        defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channel_Id)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationBody)
                        .setAutoCancel(true)
                        .setColor(Color.BLUE)
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent);
        notificationManager.notify(notification_id , notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {

    }
}