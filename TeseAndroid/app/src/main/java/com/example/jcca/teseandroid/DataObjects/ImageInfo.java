package com.example.jcca.teseandroid.DataObjects;

import android.media.Image;
import android.net.Uri;

import com.google.android.gms.tasks.Task;

import java.util.Date;

/**
 * Created by JCCA on 29/10/17.
 */

public class ImageInfo {


    private String date;
    private String url;
    private String author;


    public ImageInfo(){};

    public ImageInfo(String date, String url, String author){

        this.date=date;
        this.url=url;
        this.author=author;
    }

    public String getDate(){
        return date;
    }

    public String getUrl(){
        return url;
    }

    public String getAuthor(){  return author;  }

    public void setDate(String date){
        this.date=date;
    }

    public void setUrl(String url){
        this.url=url;
    }
}
