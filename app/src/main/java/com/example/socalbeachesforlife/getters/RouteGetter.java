package com.example.socalbeachesforlife.getters;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.socalbeachesforlife.models.Url;
import com.example.socalbeachesforlife.parsers.RouteParser;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RouteGetter extends AsyncTask<Object, String, String> {
    private String googleRoutesData;
    private GoogleMap mMap;
    String url;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        Url directionsURL = new Url();
        try {
            googleRoutesData = directionsURL.readUrl(url);
        } catch (IOException e){
            e.printStackTrace();
        }

        return googleRoutesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> routeList;
        RouteParser parser = new RouteParser();
        routeList = parser.parse(s);
        showRoutes(routeList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showRoutes(List<HashMap<String, String>> routeList)
    {
        HashMap<String, String> directionsGooglePlace = routeList.get(0);
        double ETA = Double.parseDouble(directionsGooglePlace.get("duration"));

        NearbyBeaches.ETA = ETA;
        ParkingLots.ETA = ETA;
    }
}