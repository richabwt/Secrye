package com.example.bosswebtech.secrye;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bosswebtech.secrye.Model.Incident.DataItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyIncidents extends AppCompatActivity {
    RecyclerView recyclerViewMyIncident;
    RecyclerView.Adapter recyclerViewAdapterMyIncident;
    RecyclerView.LayoutManager recylerViewLayoutManagerMyIncident;
    List<DataItem> dataItem = new ArrayList<>();

    String userId,accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_incidents);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("My Incidents");

        recyclerViewMyIncident = (RecyclerView)findViewById(R.id.recyclerview_myincident);

        Intent intent = getIntent();
        dataItem = (List<DataItem>)intent.getSerializableExtra("LIST");
        userId = intent.getStringExtra("userId");
        accessToken = intent.getStringExtra("accessToken");

        Map<String,String> map = new HashMap<>();
        map.put("Authorization",accessToken);

        List<DataItem> dataItemAssigned = new ArrayList<>();
        for(int i=0;i<dataItem.size();i++){
            if(dataItem.get(i).getIsAssigned().equals("1")){
                dataItemAssigned.add(dataItem.get(i));
            }
        }

        recylerViewLayoutManagerMyIncident= new LinearLayoutManager(MyIncidents.this);
        recyclerViewMyIncident.setLayoutManager(recylerViewLayoutManagerMyIncident);
        recyclerViewAdapterMyIncident = new RecyclerViewAdapterIncidentDetails(MyIncidents.this,dataItemAssigned,map,userId);
        recyclerViewMyIncident.setAdapter(recyclerViewAdapterMyIncident);
    }
}
