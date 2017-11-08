package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jcca.teseandroid.DataObjects.ImageInfo;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageInfo UploadInfo = MainImageUploadInfoList.get(position);

        if(UploadInfo!=null){
            holder.imageNameTextView.setText(UploadInfo.getDate());


            //Loading image from Glide library.
            GlideApp.with(context).load(UploadInfo.getUrl()+".jpg").override(100,100).centerCrop().dontTransform().into(holder.imageView);


        }


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);

            imageNameTextView = (TextView) itemView.findViewById(R.id.data);
        }
    }
}
