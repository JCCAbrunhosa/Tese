package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.Misc.speciesDetails_activity;
import com.example.jcca.teseandroid.R;

import java.util.List;

/**
 * Created by JCCA on 11/12/17.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {


    List<ImageInfo> MainImageUploadInfoList;
    Context context;

    public CardViewAdapter(Context context, List<ImageInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ImageInfo UploadInfo = MainImageUploadInfoList.get(position);
        if(UploadInfo!=null){

            //holder.speciesDesc.setText(UploadInfo.getDescription());
            holder.speciesName.setText(UploadInfo.getSpecies());
            holder.itemView.setLongClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle toSend = new Bundle();
                    toSend.putString("URL", UploadInfo.getUrl());
                    toSend.putString("Desc", UploadInfo.getDescription());
                    toSend.putString("Species", UploadInfo.getSpecies());
                    toSend.putString("Eco", UploadInfo.getEco());
                    Intent goTo = new Intent(view.getContext(), speciesDetails_activity.class);
                    goTo.putExtras(toSend);
                    goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(goTo);

                }
            });

            //Loading image from Glide library.
            GlideApp.with(context).load(UploadInfo.getUrl()).into(holder.speciesPhoto);
        }


    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView species;
        public TextView speciesName;
        public TextView speciesDesc;
        public ImageView speciesPhoto;


        public ViewHolder(View itemView) {
            super(itemView);

            species = (CardView)itemView.findViewById(R.id.guideList);
            speciesName = (TextView)itemView.findViewById(R.id.species_name);
            //speciesDesc= (TextView)itemView.findViewById(R.id.species_desc);
            speciesPhoto = (ImageView)itemView.findViewById(R.id.species_photo);


        }


    }
}



