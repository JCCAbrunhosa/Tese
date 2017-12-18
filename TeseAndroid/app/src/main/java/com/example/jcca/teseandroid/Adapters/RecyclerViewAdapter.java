package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Misc.photoDetails_activity;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.R;

import java.util.List;

/**
 * Created by JCCA on 29/10/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ImageInfo> MainImageUploadInfoList;



    public RecyclerViewAdapter(Context context, List<ImageInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ImageInfo UploadInfo = MainImageUploadInfoList.get(position);
        if(UploadInfo!=null){
            holder.imageNameTextView.setText(UploadInfo.getAuthor());

            holder.itemView.setLongClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Bundle info = new Bundle();
                    info.putString("Desc", UploadInfo.getDescription());
                    info.putString("Date", UploadInfo.getDate());
                    info.putString("Species", UploadInfo.getSpecies());
                    info.putString("Eco", UploadInfo.getEco());
                    info.putString("Author", UploadInfo.getAuthor());
                    info.putString("URL", UploadInfo.getUrl());
                    info.putString("Lat", String.valueOf(UploadInfo.getLocation().getLatitude()));
                    info.putString("Long", String.valueOf(UploadInfo.getLocation().getLongitude()));
                    Intent goTo = new Intent(view.getContext(), photoDetails_activity.class);
                    goTo.putExtras(info);
                    view.getContext().startActivity(goTo);
                }
            });

            //Loading image from Glide library.
            GlideApp.with(context).load(UploadInfo.getUrl()+".jpg").override(1000,1000).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public TextView species;
        public TextView description;
        public TextView eco;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);

            imageNameTextView = (TextView) itemView.findViewById(R.id.data);

            species = itemView.findViewById(R.id.photoSpecies);
            description=itemView.findViewById(R.id.speciesName);
            eco=itemView.findViewById(R.id.speciesEco);

        }


    }





}
