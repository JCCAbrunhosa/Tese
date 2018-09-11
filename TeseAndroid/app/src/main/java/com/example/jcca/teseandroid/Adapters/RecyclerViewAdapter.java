package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.samespecies_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ImageInfo UploadInfo = MainImageUploadInfoList.get(position);
        if(UploadInfo!=null){

            holder.itemView.setLongClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Bundle info = new Bundle();
                    info.putString("Date", UploadInfo.getDate());
                    info.putString("Species", UploadInfo.getSpecies());
                    info.putString("Eco", UploadInfo.getEco());
                    info.putString("Vulgar", UploadInfo.getVulgar());
                    info.putString("Author", UploadInfo.getAuthor());
                    info.putString("URL", UploadInfo.getUrl());
                    info.putString("Lat", String.valueOf(UploadInfo.getLocation().getLatitude()));
                    info.putString("Long", String.valueOf(UploadInfo.getLocation().getLongitude()));
                    info.putString("UID", UploadInfo.getUid());
                    Log.d("UUID: ", UploadInfo.getUid());
                    Intent goTo = new Intent(view.getContext(), photoDetails_activity.class);
                    goTo.putExtras(info);
                    view.getContext().startActivity(goTo);
                }
            });

            //Loading image from Glide library.
            GlideApp.with(context).load(UploadInfo.getUrl()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).centerCrop().into(holder.imageView);
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

            imageView = (ImageView) itemView.findViewById(R.id.relatedImage);


        }


    }





}
