package com.example.socalbeachesforlife;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import org.junit.Test;

import static org.junit.Assert.*;

public class getUrlTest {
    private String getUrl(double olatitude, double olongitude, double lat, double lng)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googlePlaceUrl.append("origin="+olatitude+","+olongitude);
        googlePlaceUrl.append("&destination="+lat+","+lng);
        googlePlaceUrl.append("&key="+MAPS_API_KEY);

        return googlePlaceUrl.toString();
    }

    @Test
    public void getUrlReturnsCorrectString() {
        assertEquals("https://maps.googleapis.com/maps/api/directions/json?origin=0.0,0.0&destination=0.0,0.0&key=AIzaSyCZ-sMYcQNBMAYHkYdIuA6syOfEEvaMR0I",
                getUrl(0, 0, 0, 0));
    }

}