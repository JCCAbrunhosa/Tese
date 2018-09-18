package com.example.jcca.teseandroid.DataObjects;

import android.location.Location;
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
    private Position location;
    private String species;
    private String eco;
    private String uid;
    private String vulgar;


    public ImageInfo(){}

    public ImageInfo(String date, String url, String author, Position location, String species,String vulgar, String eco, String uid){

        this.date=date;
        this.url=url;
        this.author=author;
        this.location=location;
        this.species=species;
        this.eco=eco;
        this.uid=uid;
        this.vulgar=vulgar;
    }

    //Getters

    public String getUid(){return uid;}

    public String getDate(){
        return date;
    }

    public String getUrl(){
        return url;
    }

    public String getAuthor(){  return author;  }

    public Position getLocation(){return location;}

    public String getSpecies(){return species;}

    public String getEco(){ return eco;}

    public String getVulgar(){return vulgar;}

    public void setSpecies(String species){ this.species=species;}

    public void setDate(String date){
        this.date=date;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public void setLocation(Position location){
        this.location=location;
    }

    public void setEco(String eco){
        this.eco=eco;
    }

    public void setUid(String uid){ this.uid=uid;}

    public void setVulgar(String vulgar){ this.vulgar=vulgar;}
}
