package com.example.bosswebtech.secrye.PushNotification;

import android.content.Intent;
import android.util.Log;

import com.example.bosswebtech.secrye.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Belal on 12/8/2017.
 */

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log.d("MyNotification", remoteMessage.getNotification().toString());
        if (remoteMessage.getData().size() > 0) {
            String title = "Secrye";
            // String body = remoteMessage.getData().get("message");
            Map<String, String> data = remoteMessage.getData();

            //you can get your text message here.
            String body= remoteMessage.getData().get("message");
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData().get("message"));
            MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);

        } else {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);

        }
        /*String title = "PRESENT MA'AM";
        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);
        Log.d("MyNotification", remoteMessage.getNotification().toString());*/



    }


}

