package com.example.mckennc9.floating_bus_prototype_v1;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private FusedLocationProviderClient mFusedLocationClient;

    private Button requestButton;

    private LatLng userCoords = new LatLng(53.343792, -6.254572);
    private int MY_LOCATION_PERMISSIONS = 0;
    private int MY_INTERNET_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestButton = (Button) findViewById(R.id.submit_req_button);
        requestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, RequestActivity.class));
            }
        });

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, MY_LOCATION_PERMISSIONS);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            String[] permissions = {Manifest.permission.INTERNET};
            ActivityCompat.requestPermissions(this, permissions, MY_INTERNET_PERMISSIONS);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            TextView x = (TextView) findViewById(R.id.intro_message);
                            x.setText("Your location:\n" + location.getLatitude() + ", " + location.getLongitude());
                            userCoords = new LatLng(location.getLatitude(), location.getLongitude());
                        } else {
                            userCoords = new LatLng(53.343792, -6.254572);
                            TextView x = (TextView) findViewById(R.id.intro_message);
                            x.setText("Your location's null, defaulting to:\n" + userCoords.latitude + ", " + userCoords.longitude);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currentLocation = new LatLng(userCoords.latitude, userCoords.longitude);
        googleMap.addMarker(new MarkerOptions().position(currentLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }
}
