package com.example.bosswebtech.secrye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bosswebtech.secrye.Model.Login.Data;
import com.example.bosswebtech.secrye.Model.ResponseChangePassword;
import com.example.bosswebtech.secrye.Model.ResponseChoosePlan;
import com.example.bosswebtech.secrye.Model.SelectPlans.DataItem;
import com.example.bosswebtech.secrye.Model.SelectPlans.ResponseSelectPlans;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.Profile.ChangePassword;
import com.example.bosswebtech.secrye.Profile.LoginActivity;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlans extends AppCompatActivity {

    RadioButton radioButtonPlans;
    RadioGroup radioGroupPlans;
    String selectedPlans,status,accessToken,userId,msg;
    private List<DataItem> listPlan= new ArrayList<DataItem>();
    private List<DataItem> listPlanSingle= new ArrayList<DataItem>();
    Array radiobutton;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_plans);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Select Plans");

        btn_submit = (Button)findViewById(R.id.btn_submitPlan);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        userId = intent.getStringExtra("userId");

        getPlans();
btn_submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        submitPlan();
    }
});

    }



    private void getPlans() {
        // accessToken = "Bearer"+" "+accessToken;
        Map<String, String> map = new HashMap<>();
        map.put("Authorization",accessToken);
        RFInterface api = RFClient.getApiService();
        Call<ResponseSelectPlans> call = api.client_plan(map,userId);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SelectPlans.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseSelectPlans>() {
            @Override
            public void onResponse(Call<ResponseSelectPlans> call, Response<ResponseSelectPlans> response) {
                if (response.isSuccessful()) {
                    status = response.body().getStatus();

                    if(status.equals("success")) {

                        Toast.makeText(SelectPlans.this, "success", Toast.LENGTH_SHORT).show();
                      listPlan = response.body().getData();

                      for(int row = 0; row <1; row++) {
                          radioGroupPlans = new RadioGroup(SelectPlans.this);
                          radioGroupPlans.setOrientation(LinearLayout.VERTICAL);
                          for (int i = 0; i < listPlan.size(); i++) {
                            radioButtonPlans = new RadioButton(SelectPlans.this);
                              radioButtonPlans.setId(i);
                              radioButtonPlans.setText(listPlan.get(i).getPlan()+"   ("+"$ "+listPlan.get(i).getPrice()+"/Month)");
                              radioButtonPlans.setTextSize(18);
                              radioGroupPlans.addView(radioButtonPlans);
                          }
                          ((ViewGroup)findViewById(R.id.radioGroupPlans)).addView(radioGroupPlans);

                          radioGroupPlans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                              @Override
                              public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                                  RadioButton btn = (RadioButton)findViewById(checkedId);
                                  String s = (String) btn.getText();
                                listPlanSingle.add(listPlan.get(checkedId));

                              }
                          });
                      }
                        progressDoalog.dismiss();

                    }else{
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseSelectPlans> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }

    private void submitPlan() {
        // accessToken = "Bearer"+" "+accessToken;
        final Map<String, String> map = new HashMap<>();
        map.put("Authorization",accessToken);
        RFInterface api = RFClient.getApiService();
        Call<ResponseChoosePlan> call = api.select_plan(map,userId,listPlanSingle.get(0).getId());

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SelectPlans.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseChoosePlan>() {
            @Override
            public void onResponse(Call<ResponseChoosePlan> call, Response<ResponseChoosePlan> response) {
                if (response.isSuccessful()) {
                    status = response.body().getStatus();
                    msg = response.body().getMsg();

                    if(status.equals("success")) {

                        Toast.makeText(SelectPlans.this, ""+msg, Toast.LENGTH_SHORT).show();
                        status = response.body().getMsg();
                        progressDoalog.dismiss();

                    }else{
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseChoosePlan> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }
}
