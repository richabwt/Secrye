package com.example.bosswebtech.secrye.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bosswebtech.secrye.GeoLocation.GeocodingLocation;
import com.example.bosswebtech.secrye.Model.Login.Data;
import com.example.bosswebtech.secrye.Model.ResponseSignUp;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    RadioButton radioButtonCategory;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.radioCategory) RadioGroup radioCategory;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    String name,email,mobile,password,reEnterPassword,address,category;
    String androidDeviceId,fcmtoken,status,latitude,longitude;
    private Data userList;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //Toast.makeText(this, "hello login"+fcmtoken, Toast.LENGTH_SHORT).show();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    signup();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        radioCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButtonCategory = (RadioButton)radioGroup.findViewById(checkedId);
                if (null != radioButtonCategory && checkedId > -1) {
                     //Toast.makeText(SignupActivity.this, radioButtonCategory.getText(), Toast.LENGTH_SHORT).show();
                    category = radioButtonCategory.getText().toString();
                }


            }
        });
    }

    public void signup() {

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();
        address = _addressText.getText().toString();
        //category = radioButtonCategory.getText().toString();

        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(address,getApplicationContext(),new GeocoderHandler());
       // insertSignupUser();

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            _addressText.setText(locationAddress);
            String[] addString = locationAddress.split("\n");
            latitude = addString[0];
            longitude = addString[1];

                insertSignupUser();


        }
    }
    private void insertSignupUser() {
//        fcmtoken = FirebaseInstanceId.getInstance().getToken();
//        Toast.makeText(this, "hello sign up"+fcmtoken, Toast.LENGTH_SHORT).show();
        if (category.equals("Client")){
            category = "3";
        }else{
            category = "4";
        }
        RFInterface api = RFClient.getApiService();
        Call<ResponseSignUp> call = api.signUp(name, email, mobile, password,address,category,latitude,longitude);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SignupActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Please wait, it may take some time");
        progressDoalog.show();

        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                if (response.isSuccessful()) {
                    status = response.body().getMsg();
                    userList = response.body().getData();

                    Intent intentuser = new Intent(SignupActivity.this, LoginActivity.class);
                    Toast.makeText(SignupActivity.this, status, Toast.LENGTH_SHORT).show();
                    startActivity(intentuser);

                    progressDoalog.dismiss();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in Signing", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }



    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
        if (radioCategory.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
            valid = false;
        }else{
            Toast.makeText(this, ""+radioButtonCategory.getText().toString()+"is selected", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}