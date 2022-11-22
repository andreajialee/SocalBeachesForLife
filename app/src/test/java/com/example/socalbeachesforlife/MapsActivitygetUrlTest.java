package com.example.socalbeachesforlife;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapsActivitygetUrlTest {
    private String getUrl(double latitude, double longitude, String nearbyPlace, int radius, boolean feature)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+radius);
        googlePlaceUrl.append("&name="+nearbyPlace);
        if(feature) {
            googlePlaceUrl.append("&type=natural_feature");
        }
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+MAPS_API_KEY);

        return googlePlaceUrl.toString();
    }

    @Test
    public void getUrlReturnsCorrectString() {
        assertEquals("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=0.0,0.0&radius=0&name=beach&type=natural_feature&sensor=true&key=AIzaSyCZ-sMYcQNBMAYHkYdIuA6syOfEEvaMR0I",
                getUrl(0, 0, "beach", 0, true));
    }

}