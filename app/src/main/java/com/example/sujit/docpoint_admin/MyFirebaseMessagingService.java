package com.example.sujit.docpoint_admin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        showNotification(remoteMessage.getData().get("message"));





    }

    private void showNotification(String message) {

        Intent i = new Intent(this,HomeScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setAutoCancel(true).setContentTitle("FCM TEST")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);

        NotificationManager manager  =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());




    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
