package com.tieutech.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //View variable
    TextView locationTextView;

    //Location variables
    LocationManager locationManager;
    LocationListener locationListener;

    //API variable
    String API_KEY = "AIzaSyAwy94Kj5ZowDOJGcSY61qyKEkMJxW5umc";

    //Listener for when location permission changes are made by the user (i.e. what happens when they allow/deny a location permission)
    //NOTE: This is a one-off call throughout the entire use-life of the application
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //If a permission grant (of any form) has been made by the user
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //If location permission has been granted by the user
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Request for location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = (TextView) findViewById(R.id.locationTextView);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Listener for changes to the location of the device
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
//                locationTextView.setText(location.getLatitude() + ", " + location.getLongitude());
            }
        };

        //If location permission has been granted by the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Request for location permissions
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            //Request for location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        //Configure the application to use the Places SDK to request for location data
        //NOTE: To enable this, a dependency in GRADLE must have been added: "implementation 'com.google.android.libraries.places:places:2.6.0'
        Places.initialize(getApplicationContext(), API_KEY);

        //Obtain the auto-complete fragment (which is a view in activity_main.xml)
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //Set the fields of location data that could be retrieved from the auto-complete fragment and assigned to the "Place" object (below)
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MainActivity.this, "Error with the API", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                //Get Address String
//                locationTextView.setText(place.getAddress());

                //Get latitude and longitude data
                LatLng latLng = place.getLatLng();
                locationTextView.setText(latLng.latitude + ", " + latLng.longitude);
            }
        });
    }
}