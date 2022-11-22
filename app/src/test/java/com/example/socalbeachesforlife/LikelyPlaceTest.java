package com.example.socalbeachesforlife;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

public class LikelyPlaceTest {
    private String[] likelyPlaceNames = new String[5];
    private LatLng[] likelyPlaceLatLngs = new LatLng[5];
    private double[] likelyPlaceETA = new double[5];
    private int count = 0;
    private LatLng latLng;
    private String placeName = "test";
    private int ETA = 0;
    private String[] PlaceNames() {
        for(int i = 0; i < 5; i++) {
            if (count < 5) {
                likelyPlaceLatLngs[count] = latLng;
                likelyPlaceNames[count] = placeName;
                likelyPlaceETA[count] = ETA;
                count++;
            }
        }
        return likelyPlaceNames;
    }
    String[] temp = {"test", "test", "test", "test", "test"};

    @Test
    public void getUrlReturnsCorrectString() {
        assertArrayEquals(temp,
                PlaceNames());
    }

}