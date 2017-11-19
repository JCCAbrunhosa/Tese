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
    private String location;
    private String species;
    private String description;
    private String eco;


    public ImageInfo(){};

    public ImageInfo(String date, String url, String author, String location, String species, String description, String eco){

        this.date=date;
        this.url=url;
        this.author=author;
        this.location=location;
        this.species=species;
        this.description=description;
        this.eco=eco;
    }

    //Getters
    public String getDate(){
        return date;
    }

    public String getUrl(){
        return url;
    }

    public String getAuthor(){  return author;  }

    public String getLocation(){return location;}

    public String getSpecies(){return species;}

    public String getDescription(){return description;}

    public String getEco(){ return eco;}

    public void setDate(String date){
        this.date=date;
    }

    public void setUrl(String url){
        this.url=url;
    }
}
