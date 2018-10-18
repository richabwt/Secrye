package com.example.bosswebtech.secrye.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bosswebtech.secrye.Model.ResponseChangePassword;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String getOldPassword,getNewPassword,getRenewPassword,user_id,ckeckold_pass,email,message,status,accessToken;

    @BindView(R.id.input_oldpassword) EditText _oldPasswordText;
    @BindView(R.id.input_newpassword) EditText _passwordText;
    @BindView(R.id.input_renewpassword) EditText _repasswordText;
    @BindView(R.id.btn_changePassword) Button _changePasswordButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Change Password");

        SharedPreferences sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ckeckold_pass = sharedPreferences.getString("Password","");
        email = sharedPreferences.getString("Email","");

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");

        _changePasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //login();
                getOldPassword = _oldPasswordText.getText().toString();
                getNewPassword = _passwordText.getText().toString();
                getRenewPassword = _repasswordText.getText().toString();

                if (getOldPassword.trim().length() == 0) {
                    _oldPasswordText.requestFocus();
                    _oldPasswordText.setError("FIELD CANNOT BE EMPTY");
                }  else if(getNewPassword.trim().length() == 0) {
                    _passwordText.requestFocus();
                    _passwordText.setError("FIELD CANNOT BE EMPTY");
                }else if(getRenewPassword.trim().length() == 0) {
                    _repasswordText.requestFocus();
                    _repasswordText.setError("FIELD CANNOT BE EMPTY");
                }else if(!getOldPassword.equals(ckeckold_pass)){
                    Toast.makeText(ChangePassword.this, "Please enter correct old password", Toast.LENGTH_SHORT).show();
                }else {
                    changePassword();
                }
                }
                });
    }

    private void changePassword() {
       // accessToken = "Bearer"+" "+accessToken;
        Map<String, String> map = new HashMap<>();
        map.put("Authorization",accessToken);
        RFInterface api = RFClient.getApiService();
        Call<ResponseChangePassword> call = api.changePassword(map,email, getOldPassword,getNewPassword,getRenewPassword);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ChangePassword.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseChangePassword>() {
            @Override
            public void onResponse(Call<ResponseChangePassword> call, Response<ResponseChangePassword> response) {
                if (response.isSuccessful()) {
                    message = response.body().getMsg();
                    status = response.body().getStatus();

                    if(status.equals("success")) {
                        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                        Toast.makeText(ChangePassword.this, ""+message, Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences =getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        //editor.commit();
                        startActivity(intent);
                        progressDoalog.dismiss();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseChangePassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }


}
