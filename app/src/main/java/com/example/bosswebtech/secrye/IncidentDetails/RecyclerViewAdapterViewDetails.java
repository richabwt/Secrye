package com.example.bosswebtech.secrye.IncidentDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import com.example.bosswebtech.secrye.Model.Incident.NotesItem;
import com.example.bosswebtech.secrye.R;
import com.example.bosswebtech.secrye.Utils.UrlConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUNED on 6/10/2016.
 */
public class RecyclerViewAdapterViewDetails extends RecyclerView.Adapter<RecyclerViewAdapterViewDetails.ViewHolder> {

    private static Context context;
    View view1;
    ViewHolder viewHolder1;
    List<NotesItem> notesItems = new ArrayList<>();
    List<Object> photos;

    public RecyclerViewAdapterViewDetails(Context context1, List<NotesItem>notesItems) {

        this.context = context1;
        this.notesItems = notesItems;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_memberName,txt_updatedDateTime,txt_incidentNotes;
        public de.hdodenhof.circleimageview.CircleImageView circleImageView;
        public GridView gridView;

        public ViewHolder(View v) {
            super(v);
            txt_memberName = (TextView) v.findViewById(R.id.txt_memberName);
            txt_updatedDateTime = (TextView) v.findViewById(R.id.txt_updatedDateTime);
            txt_incidentNotes = (TextView) v.findViewById(R.id.txt_incidentNotes);
            circleImageView = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.member_image);
            gridView = (GridView)v.findViewById(R.id.img_notesImage);


        }
    }

    @Override
    public RecyclerViewAdapterViewDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewincidentsdetails, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_memberName.setText(notesItems.get(position).getName());
        holder.txt_updatedDateTime.setText(notesItems.get(position).getCreatedOn());
        holder.txt_incidentNotes.setText(notesItems.get(position).getNote());
        Picasso.with(context).load(notesItems.get(position).getUserimage()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(viewHolder1.circleImageView);

       photos =  notesItems.get(position).getPhotos();

       holder.gridView.setAdapter(new ImageAdapterGridView(context,R.layout.grid_layout,photos));

        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(position == 0)
                {
                    //your code
                }
            }
        });




    }


    @Override
    public int getItemCount() {

        return notesItems.size();
    }
}
