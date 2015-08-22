package com.example.posturigovro.posturi.utils;

/**
 * Created by section11 on 11/08/14.
 */
public class Judet {
    public String judet;
    public double lat, lng;

    public Judet(String j, double lat, double lng){
        this.judet = j;
        this.lat = lat;
        this.lng = lng;
    }

    public String getJudet(){return judet; }

    public double getLatitude(){return lat; }

    public double getLongitude(){return lng; }

}
