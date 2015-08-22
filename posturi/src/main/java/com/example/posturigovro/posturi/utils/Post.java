package com.example.posturigovro.posturi.utils;

/**
 * Created by section11 on 31/07/14.
 */
public class Post {
    protected String titlu, institutie, data, locatie, continut, tipjob;
    protected Integer id;


    public Post(String t, String i, String d, Integer id, String l, String c, String tj){
        this.titlu = t;
        this.institutie = i;
        this.data = d;
        this.id = id;
        this.locatie = l;
        this.continut = c;
        this.tipjob = tj;
    }

    public String getTitlu() {
        return titlu;
    }

    public Integer getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getInstitutie() {
        return institutie;
    }

    public String getLocatie(){ return locatie; }

    public String getContinut(){ return continut;}

    public String getTipjob(){return tipjob;}
}
