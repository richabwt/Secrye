package com.example.bosswebtech.secrye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bosswebtech.secrye.Model.Incident.DataItem;
import com.example.bosswebtech.secrye.Model.Incident.ResponseIncident;
import com.example.bosswebtech.secrye.Model.ResponseToken;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;
import com.example.bosswebtech.secrye.Profile.EditProfile;
import com.example.bosswebtech.secrye.Profile.LoginActivity;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String tokenType,expiresIn,accessToken,refreshToken,cmplt_accessToken,email,password,androidDeviceId,message,status,userName,group_id,userId;
    Integer user_id;
    RecyclerView recyclerViewIncidentDetails;
    RecyclerView.Adapter recyclerViewAdapterIncident;
    RecyclerView.LayoutManager recylerViewLayoutManagerIncident;
    List<DataItem> dataItem = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerViewIncidentDetails = (RecyclerView)findViewById(R.id.recyclerview_incidentDetails);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("User_id",0);
        userName = sharedPreferences.getString("Name","");
        email = sharedPreferences.getString("Email","");
        password = sharedPreferences.getString("Password","");
        group_id = sharedPreferences.getString("Group_id","");
        userId = Integer.toString(user_id);

        setSupportActionBar(toolbar);
        final ActionBar bar = getSupportActionBar();
        bar.setTitle("Incidents");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.design_actionbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item = menuNav.findItem(R.id.incidents);
        MenuItem nav_plans = menuNav.findItem(R.id.selectPlans);

        if(group_id.equals("3")){
            nav_item.setVisible(false);
        }
        if(group_id.equals("4")){
            nav_plans.setVisible(false);
        }
        View hView = navigationView.getHeaderView(0);
        TextView txtuserName = (TextView)hView.findViewById(R.id.txtuserName);
        txtuserName.setText(userName);
        navigationView.setNavigationItemSelectedListener(this);

        getToken();

}
    private void getToken() {
        RFInterface api = RFClient.getApiService();
        Call<ResponseToken> call = api.generateToken("password", "2", "4FL3GF0g0DQpJxPE9gKB6AmSG8HlGUDAMXTO56sF", email, password);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Please wait, it may take some time ");
        // show it
        progressDoalog.show();
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.isSuccessful()) {
                    tokenType = response.body().getTokenType();
                    accessToken = response.body().getAccessToken();
                    refreshToken = response.body().getRefreshToken();
                    //Toast.makeText(MainActivity.this, "success"+accessToken, Toast.LENGTH_SHORT).show();
                    callApiIncidentDetails();
                    setUpSwipeReferesh();
                    progressDoalog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "There is some error while generating your token. Please try again.", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });



    }

    private void setUpSwipeReferesh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataItem = new ArrayList<>();
                callApiIncidentDetails();
            }
        });
    }

    private void callApiIncidentDetails() {
        cmplt_accessToken = "Bearer"+" "+accessToken;
        final Map<String,String>map = new HashMap<>();
        map.put("Authorization",cmplt_accessToken);
        swipeRefreshLayout.setRefreshing(true);
        RFInterface api = RFClient.getApiService();
        Call<ResponseIncident> call = api.incidentDetails(map,userId, group_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseIncident>() {
            @Override
            public void onResponse(Call<ResponseIncident> call, Response<ResponseIncident> response) {
                if (response.isSuccessful()) {
                    status = response.body().getStatus();
                    dataItem = response.body().getData();

                    recylerViewLayoutManagerIncident= new LinearLayoutManager(MainActivity.this);
                    recyclerViewIncidentDetails.setLayoutManager(recylerViewLayoutManagerIncident);

                    if(group_id.equals("3")){
                        recyclerViewAdapterIncident = new RecyclerViewAdapterClientIncidentDetails(MainActivity.this,dataItem,map,userId);
                    }else{
                        recyclerViewAdapterIncident = new RecyclerViewAdapterIncidentDetails(MainActivity.this,dataItem,map,userId);
                    }
                    recyclerViewIncidentDetails.setAdapter(recyclerViewAdapterIncident);
                    swipeRefreshLayout.setRefreshing(false);
                    progressDoalog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseIncident> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in fetching incident Details", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                progressDoalog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        item.setEnabled(false);
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if (id == R.id.action_changePassword){
//
//        }
//        if(id == R.id.action_logout){
//
//
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intentProfile = new Intent(MainActivity.this, EditProfile.class);
            intentProfile.putExtra("accessToken",cmplt_accessToken);
            startActivity(intentProfile);
        } else if (id == R.id.incidents) {
            Intent intentMyIncident  = new Intent(MainActivity.this,MyIncidents.class);
            intentMyIncident.putExtra("userId",userId);
            intentMyIncident.putExtra("LIST", (Serializable) dataItem);
            intentMyIncident.putExtra("accessToken",cmplt_accessToken);
            startActivity(intentMyIncident);

        } else if (id == R.id.payments) {

            }else if(id == R.id.logout){
            Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);

            SharedPreferences preferences =getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(intentLogout);
            logout();
            finish();
        }else if(id == R.id.selectPlans){
            Intent selectPlanIntent = new Intent(MainActivity.this,SelectPlans.class);
            selectPlanIntent.putExtra("accessToken",cmplt_accessToken);
            selectPlanIntent.putExtra("userId",userId);
            startActivity(selectPlanIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        //cmplt_accessToken = "Bearer"+" "+accessToken;
        Map<String,String>map = new HashMap<>();
        map.put("Authorization",accessToken);

        RFInterface api = RFClient.getApiService();
        Call<ResponseBody> call = api.logOut(map,user_id, androidDeviceId);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        //progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please wait it will take some time ");
        //progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "success logout", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "error"+message, Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error in PasswordChange", Toast.LENGTH_SHORT).show();
                progressDoalog.dismiss();
            }
        });
    }
}
