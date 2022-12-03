package com.example.socalbeachesforlife.models;

import android.net.Uri;

public class ReviewView {
    private Uri ivNumbersImageId;
    private float rating;
    private String username;
    private String description;

    public ReviewView(Uri ivNumbersImageIds, float ratings, String usernames, String descriptions){
        ivNumbersImageId = ivNumbersImageIds;
        rating = ratings;
        username = usernames;
        description = descriptions;
    }

    public Uri getNumbersImageId() {
        return ivNumbersImageId;
    }

    public float getRatings(){
        return rating;
    }

    public String getUsername(){
        return username;
    }

    public String getDescription(){
        return description;
    }
}
