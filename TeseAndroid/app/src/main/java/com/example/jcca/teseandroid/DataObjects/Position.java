package com.example.jcca.teseandroid.DataObjects;

/**
 * Created by JCCA on 28/11/17.
 */

public class Position {

    double latitude;
    double longitude;

    public Position (double latitude, double longitude){

        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Position(){}

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }
    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
