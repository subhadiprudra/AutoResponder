package com.cotzero.autoresponder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.cotzero.autoresponder.App.CHANNEL_ID;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationReceiver extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        //super.onNotificationPosted(statusBarNotification);

        if(!statusBarNotification.isOngoing()) {

            Log.i("getPackageName",statusBarNotification.getPackageName());

            if(statusBarNotification.getPackageName().equals("org.telegram.messenger")){

                String title = statusBarNotification.getNotification().extras.getString("android.title");
                String text = statusBarNotification.getNotification().extras.getString("android.text");

                if(title.contains(":")){
                    replyToNotification(NotificationReceiver.this,statusBarNotification,"@");
                }
                Log.i("extras",title+" + "+text);


            }

        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Running...")
                .setContentTitle("Annoy")
                .build();

        startForeground(1, notification);



    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

  /*  @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Intent sintent = new Intent(this,MainActivity.class);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Running...")
                .setContentTitle("Annoy")
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }*/









    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void replyToNotification(Context context, StatusBarNotification statusBarNotification, String message) {

        Bundle localBundle = statusBarNotification.getNotification().extras;

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(statusBarNotification.getNotification());
        List<NotificationCompat.Action> actions = wearableExtender.getActions();

        for (NotificationCompat.Action act : actions) {

            if (act != null && act.getRemoteInputs() != null) {

                Log.d(TAG, "NotificationUtils replyToNotification act.getTitle: " + act.getTitle());

                for (RemoteInput remoteInput : act.getRemoteInputs()) {
                    Log.d(TAG, "NotificationUtils replyToNotification remoteInput.getLabel: " + remoteInput.getLabel());
                    localBundle.putCharSequence(remoteInput.getResultKey(), message);
                }

                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RemoteInput.addResultsToIntent(act.getRemoteInputs(), localIntent, localBundle);
                try {
                    act.actionIntent.send(context, 0, localIntent);
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "NotificationUtils replyToNotification error: " + e.getLocalizedMessage());
                }
            }
        }


    }

}