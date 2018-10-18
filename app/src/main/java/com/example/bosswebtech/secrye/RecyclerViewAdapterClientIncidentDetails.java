package com.example.bosswebtech.secrye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bosswebtech.secrye.IncidentDetails.IncidentDetails;
import com.example.bosswebtech.secrye.Model.Incident.DataItem;
import com.example.bosswebtech.secrye.Model.ResponseAcceptIncident;
import com.example.bosswebtech.secrye.Network.Retrofit.RFClient;
import com.example.bosswebtech.secrye.Network.Retrofit.RFInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JUNED on 6/10/2016.
 */
public class RecyclerViewAdapterClientIncidentDetails extends RecyclerView.Adapter<RecyclerViewAdapterClientIncidentDetails.ViewHolder> {

    private static Context context;
    View view1;
    ViewHolder viewHolder1;
    List<DataItem> dataItems = new ArrayList<>();
    List<DataItem> singleDataItems = new ArrayList<>();
    Map<String,String> map;
    String userId,incidentId,status,groupId;


    public RecyclerViewAdapterClientIncidentDetails(Context context1,List<DataItem>dataItems,Map<String,String>map,String userId) {

        this.context = context1;
        this.dataItems = dataItems;
        this.userId = userId;
        this.map = map;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_dateTime,txt_status,txt_incidentDetails,txt_viewMore;

        public ViewHolder(View v) {
            super(v);
            txt_dateTime = (TextView) v.findViewById(R.id.txt_dateTime);
            txt_status = (TextView) v.findViewById(R.id.txt_status);
            txt_incidentDetails = (TextView) v.findViewById(R.id.txt_incidentDetails);
            txt_viewMore = (TextView) v.findViewById(R.id.txt_viewMore);

        }
    }

    @Override
    public RecyclerViewAdapterClientIncidentDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewclientincidents, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_status.setText(dataItems.get(position).getIncidentStatus());
        holder.txt_dateTime.setText(dataItems.get(position).getCreateTime());
        holder.txt_incidentDetails.setText(dataItems.get(position).getIncident());

        holder.txt_viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentIncidentDetails = new Intent(context, IncidentDetails.class);
                singleDataItems = new ArrayList<>();
                for (int j = 0; j < dataItems.size(); j++) {
                    if (dataItems.get(j).getId().equals(dataItems.get(position).getId())) {
                        singleDataItems.add(dataItems.get(position));
                    }
                }
                intentIncidentDetails.putExtra("LIST", (Serializable) singleDataItems);
                context.startActivity(intentIncidentDetails);

            }
        });

    }
    @Override
    public int getItemCount() {

        return dataItems.size();
    }
}
