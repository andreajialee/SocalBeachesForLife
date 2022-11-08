package com.example.socalbeachesforlife.models;

public class Review {
    public String beachName, comment;
    public Float starCount;
    public Boolean anon;

    public Review(){

    }

    public Review(Float starCount, String beachName, String comment, Boolean anon){
        this.starCount = starCount;
        this.beachName = beachName;
        this.comment = comment;
        this.anon = anon;
    }
}
