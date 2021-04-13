package com.cotzero.autoresponder;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.cotzero.autoresponder.App.CHANNEL_ID;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationReceiver extends NotificationListenerService {

    List<String> packages;
    Function function;
    String lastSender="";

    public NotificationReceiver() {
        packages = new ArrayList<>();
        packages.add("com.whatsapp");
        packages.add("com.facebook.orca");
        packages.add("org.telegram.messenger");
        packages.add("com.instagram.android");

        function = new Function(NotificationReceiver.this);

    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        Intent intent = new Intent(NotificationReceiver.this,FirstScreen.class);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(NotificationReceiver.this,1,intent,1);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Waiting for new message")
                .setContentTitle("Hi, i am here to reply your messages")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        stopForeground(true);
    }

    long lastMillis=0;

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        //super.onNotificationPosted(statusBarNotification);


        if(!statusBarNotification.isOngoing()) {

            try {

                if (statusBarNotification.getPackageName().equals("com.instagram.android")) {

                    Date date = new Date();
                    long timeMilli = date.getTime();

                    if(timeMilli-lastMillis>3000){
                        processNotification(statusBarNotification);
                    }
                    lastMillis = timeMilli;

                }else {
                    processNotification(statusBarNotification);
                }

            }catch (Exception e){

            }




        }



    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Intent sintent = new Intent(this,MainActivity.class);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Hi, i am here to reply your messages")
                .setContentTitle("Waiting for new message")
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        stopForeground(true);
    }

    private void processNotification(StatusBarNotification statusBarNotification){

        String packageName = statusBarNotification.getPackageName();
        String title = statusBarNotification.getNotification().extras.getString("android.title");
        String text = statusBarNotification.getNotification().extras.getString("android.text");

        Log.i("reply title",title);
        Log.i("reply text",text);
        Log.i("reply text",statusBarNotification.getPackageName());

        if(packages.contains(statusBarNotification.getPackageName())){
            if(function.isOn(packages.indexOf(packageName))){

                try {

                    JSONArray jsonArray = function.getList(packages.indexOf(packageName));

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if(function.isOnIgnoredContacts(jsonObject,title))return;

                        if(function.isOnContacts(jsonObject,title)){

                           // Log.i("reply",title);
                            if(function.isOnMsg(jsonObject,text)){

                            //    Log.i("reply",text);

                                if(jsonObject.getInt("receiver")==2){

                                    replyToNotification(statusBarNotification,function.getReply(jsonObject));
                                    //reply
                                    Log.i("reply",function.getReply(jsonObject));

                                }
                                if(jsonObject.getInt("receiver")==1){

                                    if(isGroup(title,packages.indexOf(packageName))){
                                        //reply
                                        replyToNotification(statusBarNotification,function.getReply(jsonObject));
                                        Log.i("reply1",function.getReply(jsonObject));
                                    }

                                }
                                if(jsonObject.getInt("receiver")==0){

                                    if(!isGroup(title,packages.indexOf(packageName))){
                                        //reply
                                        replyToNotification(statusBarNotification,function.getReply(jsonObject));
                                        Log.i("reply0",function.getReply(jsonObject));
                                    }

                                }

                            }

                        }




                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private Boolean isGroup(String text,int type){

        if(type==3)return true;
       // if(text.contains())
        Log.i("reply",text);
        if(text.contains(":"))return true;
        return false;
    }







    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void replyToNotification(StatusBarNotification statusBarNotification, String message) {

        Context context = NotificationReceiver.this;
        Log.i("test","1");

        Bundle localBundle = statusBarNotification.getNotification().extras;

        NotificationCompat.Action act = getAction(statusBarNotification);

        if (act != null && act.getRemoteInputs() != null) {

            Log.d("TAG", "NotificationUtils replyToNotification act.getTitle: " + act.getTitle());

            for (RemoteInput remoteInput : act.getRemoteInputs()) {
                Log.d("TAG", "NotificationUtils replyToNotification remoteInput.getLabel: " + remoteInput.getLabel());
                localBundle.putCharSequence(remoteInput.getResultKey(), message);
            }

            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            RemoteInput.addResultsToIntent(act.getRemoteInputs(), localIntent, localBundle);
            try {
                act.actionIntent.send(context, 0, localIntent);
            } catch (PendingIntent.CanceledException e) {
                Log.e("TAG", "NotificationUtils replyToNotification error: " + e.getLocalizedMessage());
            }
        }

    }


    private NotificationCompat.Action getAction(StatusBarNotification statusBarNotification){

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(statusBarNotification.getNotification());
        List<NotificationCompat.Action> actions = wearableExtender.getActions();

        for (NotificationCompat.Action act : actions) {

            Log.i("test","2");

            Log.i("test",act.title.toString());

            if (act != null && act.getRemoteInputs() != null) {

               if(isOk(act))return act;
            }
        }

        for(int i=0;i<NotificationCompat.getActionCount(statusBarNotification.getNotification()); i++){
            NotificationCompat.Action act = NotificationCompat.getAction(statusBarNotification.getNotification(),i);

            if (act != null && act.getRemoteInputs() != null) {

                if(isOk(act)) {

                    return act;

                }
            }
        }

        return null;

    }

    private Boolean isOk(NotificationCompat.Action action){
        if(action.getRemoteInputs()==null)return false;

        for(RemoteInput free : action.getRemoteInputs()){
            if(free.getAllowFreeFormInput()){
                return true;
            }
        }
        return false;
    }

}