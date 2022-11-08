package com.example.socalbeachesforlife.getters;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import android.os.AsyncTask;

import com.example.socalbeachesforlife.models.Url;
import com.example.socalbeachesforlife.parsers.DataParser;
import com.example.socalbeachesforlife.activities.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ParkingLots extends AsyncTask<Object, String, String> {
    public static double ETA;
    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    private String[] likelyPlaceNames = new String[2];
    private LatLng[] likelyPlaceLatLngs = new LatLng[2];
    private double[] likelyPlaceETA = new double[2];

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
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        int count = 0;
        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            LatLng oLocation = MapsActivity.getCurrBeachLoc();
            double olatitude = oLocation.latitude;
            double olongitude = oLocation.longitude;

            List<HashMap<String, String>> directionsNearbyPlaceList;
            String rurl = getUrl(olatitude, olongitude, lat, lng);
            Object dataTransfer[] = new Object[2];
            dataTransfer[0] = mMap;
            dataTransfer[1] = rurl;
            RouteGetter routeGetter = new RouteGetter();
            routeGetter.execute(dataTransfer);

            if(count < 2) {
                likelyPlaceLatLngs[count] = latLng;
                likelyPlaceNames[count] = placeName;
                likelyPlaceETA[count] = ETA;
                count++;
            }
            else {
                double temp = 0;
                int k = 0;
                for(int j = 0; j < 2; j++) {
                    if(likelyPlaceETA[j] < temp) {
                        temp = likelyPlaceETA[j];
                        k = j;
                    }
                }
                if(likelyPlaceETA[k] > ETA) {
                    likelyPlaceLatLngs[k] = latLng;
                    likelyPlaceNames[k] = placeName;
                    likelyPlaceETA[k] = ETA;
                }
            }

        }
        for(int i = 0; i < 2; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(likelyPlaceLatLngs[i]);
            markerOptions.title(likelyPlaceNames[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
        }
    }

    private String getUrl(double olatitude, double olongitude, double lat, double lng)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googlePlaceUrl.append("origin="+olatitude+","+olongitude);
        googlePlaceUrl.append("&destination="+lat+","+lng);
        googlePlaceUrl.append("&key="+MAPS_API_KEY);

        return googlePlaceUrl.toString();
    }
}
