package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.Misc.photoDetails_activity;
import com.example.jcca.teseandroid.Misc.showOnMap;
import com.example.jcca.teseandroid.R;

import java.util.List;

/**
 * Created by JCCA on 11/12/17.
 */

public class galleryFeedAdapter extends RecyclerView.Adapter<galleryFeedAdapter.ViewHolder> {


    List<ImageInfo> MainImageUploadInfoList;
    Context context;

    public galleryFeedAdapter(Context context, List<ImageInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_feed_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ImageInfo UploadInfo = MainImageUploadInfoList.get(position);
        if(UploadInfo!=null){

            if(UploadInfo.getSpecies().matches(""))
                holder.speciesName.setText("Sem descrição!");
            else
                holder.speciesName.setText(UploadInfo.getSpecies());

            //Loading image from Glide library.
            GlideApp.with(context).load(UploadInfo.getUrl()).into(holder.speciesPhoto);

            holder.author.setText(UploadInfo.getAuthor());
            holder.itemView.setLongClickable(true);
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle info= new Bundle();
                    Log.d("URL", UploadInfo.getUrl());
                    info.putString("Date", UploadInfo.getDate());
                    info.putString("Species", UploadInfo.getSpecies());
                    info.putString("Eco", UploadInfo.getEco());
                    info.putString("Vulgar", UploadInfo.getVulgar());
                    info.putString("Author", UploadInfo.getAuthor());
                    info.putString("URL", UploadInfo.getUrl());
                    info.putString("Lat", String.valueOf(UploadInfo.getLocation().getLatitude()));
                    info.putString("Long", String.valueOf(UploadInfo.getLocation().getLongitude()));
                    info.putString("UID", UploadInfo.getUid());
                    Intent goTo = new Intent(view.getContext(), photoDetails_activity.class);
                    goTo.putExtras(info);
                    goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(goTo);

                }
            });

            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle pos = new Bundle();
                    pos.putString("Lat", String.valueOf(UploadInfo.getLocation().getLatitude()));
                    pos.putString("Long", String.valueOf(UploadInfo.getLocation().getLongitude()));
                    Intent goTo = new Intent(view.getContext(), showOnMap.class);
                    goTo.putExtras(pos);
                    goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(goTo);
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView species;
        public TextView speciesName;
        public Button info;
        public Button map;
        public TextView author;
        public ImageView speciesPhoto;


        public ViewHolder(View itemView) {
            super(itemView);

            species = (CardView)itemView.findViewById(R.id.guideList);
            speciesName = (TextView)itemView.findViewById(R.id.species_name);
            author=(TextView)itemView.findViewById(R.id.author);
            map = (Button)itemView.findViewById(R.id.map);
            info = (Button) itemView.findViewById(R.id.info);
            speciesPhoto = (ImageView)itemView.findViewById(R.id.species_photo);


        }


    }
}



