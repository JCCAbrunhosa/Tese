package com.example.jcca.teseandroid.DataObjects;

import android.net.Uri;

import java.net.URI;

/**
 * Created by JCCA on 28/10/17.
 */

public class ImageUploadData {


    public String name;
    public String author;
    public String location;


    public ImageUploadData(String name, String author, String location){
        this.name=name;
        this.author=author;
        this.location=location;
    }

    //Getters
    public String getName(){
        return name;
    }

    public String getAuthor(){
        return author;
    }

    public String getLocation(){
        return location;
    }



}
