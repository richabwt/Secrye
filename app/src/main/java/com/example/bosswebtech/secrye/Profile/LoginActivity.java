package com.example.bosswebtech.secrye.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bosswebtech.secrye.MainActivity;
import com.example.bosswebtech.secrye.Model.Login.Data;
import com.example.bosswebtech.secrye.Model.Login.ResponseLogin;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_forgotPassword) TextView _forgotpasswordLink;
    private Data userList;
    String status,email,password,androidDeviceId,fcmtoken;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
       // Toast.makeText(this, "hahaha"+ SharedPrefManager.getInstance(this).getDeviceToken(), Toast.LENGTH_SHORT).show();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(UrlConfig.CHANNEL_ID, UrlConfig.CHANNEL_NAME, importance);
//            mChannel.setDescription(UrlConfig.CHANNEL_DESCRIPTION);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mNotificationManager.createNotificationChannel(mChannel);
//        }
//        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();

                if (email.trim().length() == 0) {
                    _emailText.requestFocus();
                    _emailText.setError("FIELD CANNOT BE EMPTY");
                }  else if(password.trim().length() == 0){
                    _passwordText.requestFocus();
                    _passwordText.setError("FIELD CANNOT BE EMPTY");
                }else{

                    insertUser();
                    _emailText.setText("");
                    _passwordText.setText("");
                }


            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _forgotpasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotpassworddIntent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(forgotpassworddIntent);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    private void insertUser() {
        fcmtoken = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(this, "hello login"+fcmtoken, Toast.LENGTH_SHORT).show();
        RFInterface api = RFClient.getApiService();
        Call<ResponseLogin> call = api.getUser(email, password,androidDeviceId,fcmtoken,"1");

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Please wait, it may take some time");
        progressDoalog.show();

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()) {
                    status = response.body().getMsg();
                    userList = response.body().getData();

                    Intent intentuser = new Intent(LoginActivity.this, MainActivity.class);
                    Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                    startActivity(intentuser);

                    SharedPreferences sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(UrlConfig.LOGGEDIN_SHARED_PREF,true);
                    editor.putString("Email", userList.getEmail());
                    editor.putInt("User_id",userList.getUserId());
                    editor.putString("Name",userList.getName());
                    editor.putString("Password",password);
                    editor.putString("Group_id",userList.getGroupId());
                    editor.putString("Address",userList.getAddress());
                    editor.putString("MemberTypeId",userList.getMemberTypeId());
                    if(userList.getMobile()!=null) {
                        editor.putString("Phone", userList.getMobile().toString());
                    }else {
                        editor.putString("Phone", "null");
                    }
                    if(userList.getUserimage() != null) {
                            editor.putString("UserImage",userList.getUserimage().toString());
                        }else{
                        editor.putString("UserImage","");
                    }


                    editor.commit();

                    progressDoalog.dismiss();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in Login", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });

    }

}
