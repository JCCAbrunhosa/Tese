package com.example.jcca.teseandroid.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.example.jcca.teseandroid.DataObjects.ImageInfo;
import com.example.jcca.teseandroid.Glide_Module.GlideApp;
import com.example.jcca.teseandroid.R;
import com.example.jcca.teseandroid.Misc.*;

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

            //OnClick
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, UploadInfo.getAuthor(), Toast.LENGTH_SHORT).show();

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

        public ViewHolder(View itemView) {
            super(itemView);


            imageView = (ImageView) itemView.findViewById(R.id.img);

            imageNameTextView = (TextView) itemView.findViewById(R.id.data);

        }

    }


}
