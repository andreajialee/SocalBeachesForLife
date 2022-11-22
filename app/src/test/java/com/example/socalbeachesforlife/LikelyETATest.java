package com.example.socalbeachesforlife;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;

public class LikelyETATest {
    private String[] likelyPlaceNames = {"test1", "test2", "test3", "test4", "test5"};
    private LatLng[] likelyPlaceLatLngs = new LatLng[5];
    private double[] likelyPlaceETA = {20, 21, 3, 81, 19};
    private int count = 0;
    private LatLng latLng;
    private String placeName = "test";
    private int ETA = 19;
    private double[] PlaceETAs() {
        for(int i = 0; i < 5; i++) {
            double temp = 0;
            int k = 0;
            for (int j = 0; j < 5; j++) {
                if (likelyPlaceETA[j] > temp) {
                    temp = likelyPlaceETA[j];
                    k = j;
                }
            }
            if (likelyPlaceETA[k] > ETA) {
                likelyPlaceLatLngs[k] = latLng;
                likelyPlaceNames[k] = placeName;
                likelyPlaceETA[k] = ETA;
            }
        }
        return likelyPlaceETA;
    }
    double[] temp = {19, 19, 3, 19, 19};

    @Test
    public void getUrlReturnsCorrectString() {
        assertEquals(temp[1], PlaceETAs()[1], 0);
        assertEquals(temp[2], PlaceETAs()[2], 0);
        assertEquals(temp[3], PlaceETAs()[3], 0);
        assertEquals(temp[4], PlaceETAs()[4], 0);
        assertEquals(temp[0], PlaceETAs()[0], 0);
    }

}