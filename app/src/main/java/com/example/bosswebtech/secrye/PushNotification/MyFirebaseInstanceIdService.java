package com.example.bosswebtech.secrye.PushNotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.bosswebtech.secrye.Profile.LoginActivity;
import com.example.bosswebtech.secrye.Utils.SharedPrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class  MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
       String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyFirebaseToken", "Refreshed token: " + token);
        storeToken(token);

    }
    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

}
