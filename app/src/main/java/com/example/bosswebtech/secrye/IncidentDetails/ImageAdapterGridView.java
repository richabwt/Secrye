package com.example.bosswebtech.secrye.IncidentDetails;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bosswebtech.secrye.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapterGridView extends ArrayAdapter<Object>
{
    String TAG = "ImageAdapter";

    private int resource;
    private List<Object> photos;
    Context context;
    public ImageAdapterGridView(Context context, int resorce,List<Object> photos)
    {
        super(context, resorce, photos);
        Log.i(TAG," in set adapter lstRecipes "+ photos.size() );
        this.context = context;
        this.resource = resorce;
        this.photos = photos;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView  = layoutInflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.imgGrid = (ImageView)convertView.findViewById(R.id.imgGrid);

            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        try
        {
            Picasso.with(context)
                    .load(String.valueOf(photos.get(position)))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imgGrid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }

    public static class ViewHolder
    {
        private TextView txtTitle;
        private ImageView imgGrid;
    }
}