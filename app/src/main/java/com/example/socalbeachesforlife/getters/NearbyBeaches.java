package com.example.socalbeachesforlife.getters;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.socalbeachesforlife.models.Url;
import com.example.socalbeachesforlife.parsers.DataParser;
import com.example.socalbeachesforlife.activities.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NearbyBeaches extends AsyncTask<Object, String, String> {
    public static double ETA;
    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    private String[] likelyPlaceNames = new String[5];
    private LatLng[] likelyPlaceLatLngs = new LatLng[5];
    private double[] likelyPlaceETA = new double[5];
    private int radius;
    private boolean circleDrawn = false;
    private Circle circle;

    public NearbyBeaches(int radius) {
        this.radius = radius;
    }
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

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        int count = 0;
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("place_name");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            Location oLocation = MapsActivity.getCurrLoc();
            double olatitude = oLocation.getLatitude();
            double olongitude = oLocation.getLongitude();

            List<HashMap<String, String>> directionsNearbyPlaceList;
            String rurl = getUrl(olatitude, olongitude, lat, lng);
            Object dataTransfer[] = new Object[2];
            dataTransfer[0] = mMap;
            dataTransfer[1] = rurl;
            RouteGetter routeGetter = new RouteGetter();
            routeGetter.execute(dataTransfer);

            if (count < 5) {
                likelyPlaceLatLngs[count] = latLng;
                likelyPlaceNames[count] = placeName;
                likelyPlaceETA[count] = ETA;
                count++;
            } else {
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
        }
        MapsActivity.likelyPlaceNames = likelyPlaceNames;
        MapsActivity.likelyPlaceLatLngs = likelyPlaceLatLngs;
        for (int i = 0; i < 5; i++) {
            if(likelyPlaceLatLngs[i] == null) {
                continue;
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(likelyPlaceLatLngs[i]);
            markerOptions.title(likelyPlaceNames[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(likelyPlaceLatLngs[i]));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                circle = mMap.addCircle(new CircleOptions()
                        .center(likelyPlaceLatLngs[i])
                        .radius(radius)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(.25f, 0f, 0f, 1f)));
                circleDrawn = true;

            }
            Object dataTransfer[] = new Object[2];
            NearbyResaurants nearbyResaurants = new NearbyResaurants();
            String reurl = getRestaurantUrl(likelyPlaceLatLngs[i].latitude, likelyPlaceLatLngs[i].longitude, radius);
            dataTransfer[0] = mMap;
            dataTransfer[1] = reurl;

            nearbyResaurants.execute(dataTransfer);
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

    private String getRestaurantUrl(double latitude, double longitude, int radius)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+radius);
        googlePlaceUrl.append("&type=restaurant");
        googlePlaceUrl.append("&key="+MAPS_API_KEY);
        System.out.println(googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }
}
