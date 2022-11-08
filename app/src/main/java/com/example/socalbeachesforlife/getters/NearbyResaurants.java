package com.example.socalbeachesforlife.getters;

import android.os.AsyncTask;

import com.example.socalbeachesforlife.models.Url;
import com.example.socalbeachesforlife.parsers.RestaurantParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NearbyResaurants extends AsyncTask<Object, String, String> {
    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    private String[] likelyPlaceNames = new String[5];
    private LatLng[] likelyPlaceLatLngs = new LatLng[5];
    private String[] likelyPlaceInfo = new String[5];

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        Url downloadURL = new Url();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e){
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        RestaurantParser parser = new RestaurantParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        int count = 0;
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("name");
            String open = googlePlace.get("open");
            String rating = googlePlace.get("rating");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);

            if (count < 5) {
                likelyPlaceLatLngs[count] = latLng;
                likelyPlaceNames[count] = placeName;
                if(open == "true") {
                    likelyPlaceInfo[count] = "Open, " + rating + " stars";
                }
                else {
                    likelyPlaceInfo[count] = "Closed, " + rating + " stars";
                }
                count++;
            }
        }
        for (int i = 0; i < nearbyPlaceList.size() && i < 5; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(likelyPlaceLatLngs[i]);
            markerOptions.title(likelyPlaceNames[i] + " : " + likelyPlaceInfo[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mMap.addMarker(markerOptions);
        }
    }
}
