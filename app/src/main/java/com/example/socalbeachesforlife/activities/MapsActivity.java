package com.example.socalbeachesforlife.activities;

import static com.example.socalbeachesforlife.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.socalbeachesforlife.getters.NearbyBeaches;
import com.example.socalbeachesforlife.getters.ParkingLots;
import com.example.socalbeachesforlife.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Set;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private static double latitude, longitude;
    private static double blatitude, blongitude;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private static Location lastKnownLocation = new Location("");
    private static Location chosenBeachLocation = new Location("");
    private static LatLng defaultLocation = new LatLng(34.024805, -118.285404);
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    public static String[] likelyPlaceNames;
    public static LatLng[] likelyPlaceLatLngs;

    private int REST_RADIUS = 1000;
    private Button mRadius;
    private Button mProfile;
    private final String[] radi = new String[]{"1000", "2000", "3000"};

    private ImageButton mDirection;
    private int markerType = -1;

    public static Location getCurrLoc() {
        return lastKnownLocation;
    }

    public static Location getCurrBeach() { return chosenBeachLocation; }

    public static LatLng getCurrBeachLoc() {
        LatLng latLng = new LatLng(blatitude, blongitude);
        return latLng;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mRadius = (Button) findViewById(R.id.radius_button);
        mRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadi();
            }
        });

        mProfile = (Button) findViewById(R.id.profile_button);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent(MapsActivity.this, Profile.class));
            }
        });

        mDirection = (ImageButton) findViewById(R.id.direction_button);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set Default Location
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(34.024805);
        location.setLongitude(-118.285404);
        lastKnownLocation = location;
    }

    private void showRadi() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Stores the user's picked beach
                Location beachLoc = new Location(LocationManager.GPS_PROVIDER);
                LatLng beachLatLng = likelyPlaceLatLngs[which];
                beachLoc.setLatitude(beachLatLng.latitude);
                beachLoc.setLongitude(beachLatLng.longitude);
                chosenBeachLocation = beachLoc;
                // The "which" argument contains the position of the selected item.
                String radius = radi[which];
                // Update Radius
                REST_RADIUS = Integer.valueOf(radius);
                map.clear();
                getDeviceLocation();
                Object dataTransfer[] = new Object[2];
                ParkingLots parkingLots = new ParkingLots();
                String parking = "parking";
                String url = getUrl(blatitude, blongitude, parking, REST_RADIUS, false);
                dataTransfer[0] = map;
                dataTransfer[1] = url;
                parkingLots.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Updated Radius", Toast.LENGTH_LONG).show();
            }
        };
        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Choose your radius")
                .setItems(radi, listener)
                .show();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    /**
     * This function creates a direction uri for us to launch a new map activity
     * @param tag
     * @param lat
     * @param lon
     * @return String
     */
    public String getDirectionUri(int tag, double lat, double lon) {
        double curLat = lastKnownLocation.getLatitude();
        double curLon = lastKnownLocation.getLongitude();
        double beachLat = getCurrBeachLoc().latitude;
        double beachLon = getCurrBeachLoc().longitude;
        String uri = "";
        // If tag is 1, then we know the marker is a parking lot
        // We create a URI to map from current location to beach
        if(tag == 1) {
            uri = "https://www.google.com/maps/dir/?api=1&origin=" + curLat + ","+ curLon +
                    "&destination=" + lat + "," + lon +
                    "&travelmode=driving&dir_action=navigate";
        }
        // If tag is 2, then we know the marker is a restaurant
        // We create a URI to map from beach to restaurant
        else if(tag == 2) {
            uri = "https://www.google.com/maps/dir/?api=1&origin=" + beachLat + ","+ beachLon +
                    "&destination=" + lat + "," + lon +
                    "&travelmode=walking";
        }
        // If tag is 0, we know the marker is a beach
        // We create a URI to map from beach to restaurant
        else if(tag == 0) {
            uri = "https://www.google.com/maps/dir/?api=1&origin=" + curLat + ","+ curLon +
                    "&destination=" + beachLat + "," + beachLon +
                    "&travelmode=driving&dir_action=navigate";
        }
        return uri;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Hides the button when you click on the map
                mDirection.setVisibility(View.INVISIBLE);
            }
        });
        // Disables the auto toolbar
        map.getUiSettings().setMapToolbarEnabled(false);

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                // When the user clicks a marker, we show the button to allow them to route
                mDirection.setVisibility(View.VISIBLE);
                mDirection.setVisibility(View.VISIBLE);
                mDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int tag = (int) marker.getTag();
                        double lat = marker.getPosition().latitude;
                        double lon = marker.getPosition().longitude;
                        String uri = getDirectionUri(tag, lat, lon);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                return infoWindow;
            }
        });
        // Prompt the user for permission.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location tempLastKnownLocation = task.getResult();
                            // If it is not null, we set lastKnownLocation to current location
                            // Else, we leave it at our current default location
                            if(tempLastKnownLocation != null) {
                                lastKnownLocation = tempLastKnownLocation;
                            }
                            latitude = lastKnownLocation.getLatitude();
                            longitude = lastKnownLocation.getLongitude();
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(latitude,longitude), DEFAULT_ZOOM));
                            Object dataTransfer[] = new Object[2];
                            NearbyBeaches nearbyBeaches = new NearbyBeaches(REST_RADIUS);
                            String beach = "beach";
                            String url = getUrl(latitude, longitude, beach, 100000, true);
                            dataTransfer[0] = map;
                            dataTransfer[1] = url;
                            nearbyBeaches.execute(dataTransfer);
                            Toast.makeText(MapsActivity.this, "Showing Nearby Beaches", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }

        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    public static String getUrl(double latitude, double longitude, String nearbyPlace, int radius, boolean feature)
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

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }
        if (locationPermissionGranted) {
            MapsActivity.this.openPlacesDialog();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");
            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));
            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));

                blatitude = likelyPlaceLatLngs[which].latitude;
                blongitude = likelyPlaceLatLngs[which].longitude;
                Object dataTransfer[] = new Object[2];
                ParkingLots parkingLots = new ParkingLots();
                String parking = "parking";
                String url = getUrl(blatitude, blongitude, parking, REST_RADIUS, false);
                dataTransfer[0] = map;
                dataTransfer[1] = url;

                parkingLots.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Beaches", Toast.LENGTH_LONG).show();
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}

