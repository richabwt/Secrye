package com.example.bosswebtech.secrye.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bosswebtech.secrye.Model.ResponseChangePassword;
import com.example.bosswebtech.secrye.Model.ResponseForgotPassword;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String getEmailAddress;

    @BindView(R.id.input_emailAddress)
    EditText _emailAddressText;
    @BindView(R.id.btn_forgotPassword)
    Button _forgotPasswordButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Forgot Password");

        _forgotPasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //login();
                getEmailAddress = _emailAddressText.getText().toString();

                if (getEmailAddress.trim().length() == 0) {
                    _emailAddressText.requestFocus();
                    _emailAddressText.setError("FIELD CANNOT BE EMPTY");
                } else {
                    forgotPassword();
                }
            }
        });
    }

    private void forgotPassword() {

        RFInterface api = RFClient.getApiService();
        Call<ResponseForgotPassword> call = api.forgotPassword(getEmailAddress);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ForgotPassword.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseForgotPassword>() {
            @Override
            public void onResponse(Call<ResponseForgotPassword> call, Response<ResponseForgotPassword> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMsg();
                    String status = response.body().getStatus();

                    if (status.equals("success")) {

                        Toast.makeText(ForgotPassword.this, "" + message, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseForgotPassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }
}
