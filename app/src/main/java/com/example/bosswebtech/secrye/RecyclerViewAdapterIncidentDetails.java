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
public class RecyclerViewAdapterIncidentDetails extends RecyclerView.Adapter<RecyclerViewAdapterIncidentDetails.ViewHolder> {

    private static Context context;
    View view1;
    ViewHolder viewHolder1;
    List<DataItem> dataItems = new ArrayList<>();
    List<DataItem> singleDataItems = new ArrayList<>();
    Map<String,String> map;
    String userId,incidentId,status,groupId;


    public RecyclerViewAdapterIncidentDetails(Context context1,List<DataItem>dataItems,Map<String,String>map,String userId) {

        this.context = context1;
        this.dataItems = dataItems;
        this.userId = userId;
        this.map = map;
        }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_dateTime,txt_clientName,txt_status,txt_clientAddress,txt_incidentDetails,txt_accept,txt_viewMore,txt_update,txt_accepted;

        public ViewHolder(View v) {
            super(v);
            txt_dateTime = (TextView) v.findViewById(R.id.txt_dateTime);
            txt_clientName = (TextView) v.findViewById(R.id.txt_clientName);
            txt_clientAddress = (TextView) v.findViewById(R.id.txt_clientAddress);
            txt_status = (TextView) v.findViewById(R.id.txt_status);
            txt_incidentDetails = (TextView) v.findViewById(R.id.txt_incidentDetails);
            txt_accept = (TextView) v.findViewById(R.id.txt_accept);
            txt_update = (TextView) v.findViewById(R.id.txt_update);
            txt_viewMore = (TextView) v.findViewById(R.id.txt_viewMore);
            txt_accepted = (TextView)v.findViewById(R.id.txt_accepted);

        }
    }

    @Override
    public RecyclerViewAdapterIncidentDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewincidents, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

    holder.txt_status.setText(dataItems.get(position).getIncidentStatus());
    holder.txt_dateTime.setText(dataItems.get(position).getCreateTime());
    holder.txt_clientName.setText(dataItems.get(position).getClientName());
    holder.txt_clientAddress.setText(dataItems.get(position).getClientAddress());
    holder.txt_incidentDetails.setText(dataItems.get(position).getIncident());

    if(dataItems.get(position).getIncidentStatus().equals("Open")) {
        if (dataItems.get(position).getIsAccepted().equals("0") && dataItems.get(position).getIsAssigned().equals("0")) {
            holder.txt_accept.setVisibility(View.VISIBLE);
            holder.txt_accepted.setVisibility(View.GONE);
            holder.txt_update.setVisibility(View.GONE);

        }
        if (dataItems.get(position).getIsAccepted().equals("1") && dataItems.get(position).getIsAssigned().equals("0")) {
            holder.txt_accepted.setVisibility(View.VISIBLE);
            holder.txt_accept.setVisibility(View.GONE);
            holder.txt_update.setVisibility(View.GONE);
        }

        if (dataItems.get(position).getIsAccepted().equals("1") && dataItems.get(position).getIsAssigned().equals("1")) {
            holder.txt_update.setVisibility(View.VISIBLE);
            holder.txt_accept.setVisibility(View.GONE);
            holder.txt_accepted.setVisibility(View.GONE);

        }
    }else {
        holder.txt_accept.setVisibility(View.GONE);
        holder.txt_accepted.setVisibility(View.GONE);
        holder.txt_update.setVisibility(View.GONE);
    }




    holder.txt_viewMore.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentIncidentDetails = new Intent(context,IncidentDetails.class);
            singleDataItems=new ArrayList<>();
           for(int j=0;j< dataItems.size();j++){
               if(dataItems.get(j).getId().equals(dataItems.get(position).getId())){
                   singleDataItems.add(dataItems.get(position));
               }
           }
            intentIncidentDetails.putExtra("LIST", (Serializable) singleDataItems);
            context.startActivity(intentIncidentDetails);

        }
    });

    holder.txt_accept.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            incidentId = dataItems.get(position).getId();
            RFInterface api = RFClient.getApiService();
            Call<ResponseAcceptIncident> call = api.accept_incident(map,incidentId,userId);
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please wait, it may take some time ");
            // show it
            progressDoalog.show();
            call.enqueue(new Callback<ResponseAcceptIncident>() {
                @Override
                public void onResponse(Call<ResponseAcceptIncident> call, Response<ResponseAcceptIncident> response) {
                    if (response.isSuccessful()) {
                        status = response.body().getStatus();
                       if(status.equals("success")) {
                           Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();

                       }else{
                           Toast.makeText(context, "error" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                       }

                        progressDoalog.dismiss();

                    } else {
                        Toast.makeText(context, "There is some error while accepting the incident", Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAcceptIncident> call, Throwable t) {
                    progressDoalog.dismiss();
                }
            });

        }
    });

    }




    @Override
    public int getItemCount() {

        return dataItems.size();
    }
}
