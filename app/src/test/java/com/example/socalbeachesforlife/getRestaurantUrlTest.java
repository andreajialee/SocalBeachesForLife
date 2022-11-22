package com.example.socalbeachesforlife;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import org.junit.Test;

import static org.junit.Assert.*;

public class getRestaurantUrlTest {
    private String getRestaurantUrl(double latitude, double longitude, int radius)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+radius);
        googlePlaceUrl.append("&type=restaurant");
        googlePlaceUrl.append("&key="+MAPS_API_KEY);

        return googlePlaceUrl.toString();
    }

    @Test
    public void getUrlReturnsCorrectString() {
        assertEquals("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=0.0,0.0&radius=0&type=restaurant&key=AIzaSyCZ-sMYcQNBMAYHkYdIuA6syOfEEvaMR0I",
                getRestaurantUrl(0, 0, 0));
    }

}