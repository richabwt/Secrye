package com.example.bosswebtech.secrye.IncidentDetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bosswebtech.secrye.Model.Incident.DataItem;
import com.example.bosswebtech.secrye.Model.Incident.NotesItem;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;

import java.util.ArrayList;
import java.util.List;

public class IncidentDetails extends AppCompatActivity {
    List<DataItem> dataItems = new ArrayList<>();
    List<NotesItem>notesItems = new ArrayList<>();
    TextView client,status,time,location,viewmap;
    RecyclerView recyclerViewDetails;
    RecyclerView.Adapter recyclerViewAdapterViewDetails;
    RecyclerView.LayoutManager recylerViewLayoutManagerViewDetails;
    String groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_details);

        final ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Incident Details");

        SharedPreferences sharedPreferences = getSharedPreferences(UrlConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        groupId = sharedPreferences.getString("Group_id","");

        client = (TextView)findViewById(R.id.txt_client);
        status = (TextView)findViewById(R.id.txt_status);
        time = (TextView)findViewById(R.id.txt_time);
        location = (TextView)findViewById(R.id.txt_clientAddress);
        recyclerViewDetails = (RecyclerView)findViewById(R.id.recyclerview_incidentFullDetails);
        viewmap = (TextView)findViewById(R.id.txt_viewmap) ;

        Intent intent = getIntent();
        dataItems = (List<DataItem>)intent.getSerializableExtra("LIST");

        if(groupId.equals("3")){
            client.setVisibility(View.GONE);
            location.setVisibility(View.GONE);
            viewmap.setVisibility(View.GONE);
        }

        client.setText(dataItems.get(0).getClientName());
        status.setText(dataItems.get(0).getIncidentStatus());
        time.setText(dataItems.get(0).getCreateTime());
        location.setText(dataItems.get(0).getClientAddress());
        notesItems = dataItems.get(0).getNotes();

        recylerViewLayoutManagerViewDetails= new LinearLayoutManager(IncidentDetails.this);
        recyclerViewDetails.setLayoutManager(recylerViewLayoutManagerViewDetails);
        recyclerViewAdapterViewDetails = new RecyclerViewAdapterViewDetails(IncidentDetails.this,notesItems);
        recyclerViewDetails.setAdapter(recyclerViewAdapterViewDetails);

        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriAppointment = "geo:0,0?q="+location.getText();
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriAppointment)));
            }
        });




    }
}
