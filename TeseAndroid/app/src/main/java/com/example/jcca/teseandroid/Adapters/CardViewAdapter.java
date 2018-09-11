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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.Misc.speciesDetails_activity;
import com.example.jcca.teseandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JCCA on 11/12/17.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {


    List<ImageInfo> MainImageUploadInfoList;
    Context context;
    List<ImageInfo> filteredItems;
    boolean isFilterOn=false;

    public CardViewAdapter(Context context, List<ImageInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
        filteredItems= new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if(isFilterOn==true){
            if(filteredItems.get(position)!=null){

                //holder.speciesDesc.setText(UploadInfo.getDescription());
                holder.vulgarName.setText(filteredItems.get(position).getVulgar());
                holder.speciesName.setText(filteredItems.get(position).getSpecies());
                holder.itemView.setLongClickable(true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle toSend = new Bundle();
                        toSend.putString("URL", filteredItems.get(position).getUrl());
                        toSend.putString("Species", filteredItems.get(position).getSpecies());
                        toSend.putString("Eco", filteredItems.get(position).getEco());
                        toSend.putString("Vulgar", filteredItems.get(position).getVulgar());
                        Intent goTo = new Intent(view.getContext(), speciesDetails_activity.class);
                        goTo.putExtras(toSend);
                        goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(goTo);

                    }
                });

                //Loading image from Glide library.
                GlideApp.with(context).load(filteredItems.get(position).getUrl()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).into(holder.speciesPhoto);
            }
        }else{
            if(MainImageUploadInfoList.get(position)!=null){

                //holder.speciesDesc.setText(UploadInfo.getDescription());
                holder.vulgarName.setText(MainImageUploadInfoList.get(position).getVulgar());
                holder.speciesName.setText(MainImageUploadInfoList.get(position).getSpecies());
                holder.itemView.setLongClickable(true);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle toSend = new Bundle();
                        toSend.putString("URL", MainImageUploadInfoList.get(position).getUrl());
                        toSend.putString("Species", MainImageUploadInfoList.get(position).getSpecies());
                        toSend.putString("Eco", MainImageUploadInfoList.get(position).getEco());
                        toSend.putString("Vulgar", MainImageUploadInfoList.get(position).getVulgar());
                        Intent goTo = new Intent(view.getContext(), speciesDetails_activity.class);
                        goTo.putExtras(toSend);
                        goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(goTo);

                    }
                });

                //Loading image from Glide library.
                GlideApp.with(context).load(MainImageUploadInfoList.get(position).getUrl()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).into(holder.speciesPhoto);
            }
        }

    }

    @Override
    public int getItemCount() {
        if(isFilterOn==true)
            return filteredItems.size();
        else
            return MainImageUploadInfoList.size();
    }

    public void setFilter(String filter){
        for(int i=0; i< MainImageUploadInfoList.size();i++ ){
            if(MainImageUploadInfoList.get(i).getSpecies().toLowerCase().contains(filter.toLowerCase())){
                if(!filteredItems.contains(MainImageUploadInfoList.get(i)))
                    filteredItems.add(MainImageUploadInfoList.get(i));
            }
            if(MainImageUploadInfoList.get(i).getVulgar().toLowerCase().contains(filter.toLowerCase())){
                if(!filteredItems.contains(MainImageUploadInfoList.get(i)))
                    filteredItems.add(MainImageUploadInfoList.get(i));
            }

        }
    }

    public void setFilterOn(String searchCriteria){
        filteredItems.clear();
        setFilter(searchCriteria);
        isFilterOn=true;
        notifyDataSetChanged();

    }

    public void setFilterOff(){
        isFilterOn=false;
        filteredItems.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView species;
        public TextView speciesName;
        public TextView vulgarName;
        public ImageView speciesPhoto;


        public ViewHolder(View itemView) {
            super(itemView);

            species = (CardView)itemView.findViewById(R.id.guideList);
            vulgarName = (TextView)itemView.findViewById(R.id.vulgar_name);
            speciesName= (TextView)itemView.findViewById(R.id.species_name);
            speciesPhoto = (ImageView)itemView.findViewById(R.id.species_photo);


        }


    }

}



