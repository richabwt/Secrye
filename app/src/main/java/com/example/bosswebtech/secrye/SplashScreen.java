package com.example.bosswebtech.secrye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bosswebtech.secrye.Model.ResponseToken;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.Profile.LoginActivity;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        }, 2000);

        SharedPreferences sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(UrlConfig.LOGGEDIN_SHARED_PREF, false);

        if (loggedIn) {
            Intent mainintent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(mainintent);
            finish();
        }else{
            Intent mainintent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(mainintent);
            finish();
        }

    }


}
