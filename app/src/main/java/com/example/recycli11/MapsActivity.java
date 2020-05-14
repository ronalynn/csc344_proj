package com.example.recycli11;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * @author ronalyn nanong
 * Locates the users nearest recycling centre
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageButton homeBtn;
    String homePrompt = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        homeBtn = findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, homePrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });
    }

    /*when the map is loaded, the markers are placed*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.606048, -3.982484))
                .title("Sketty Lane car park recycling site"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.622452, -3.936414))
                .title("Sainsbury's Swansea Recycling Site"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.668365, -3.921019))
                .title("Llansamlet Household Waste Recycling Centre"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.606569, -4.000233))
                .title("Clyne Household Waste Recycling Centre"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.665459, -4.017772))
                .title("Garngoch Household Waste Recycling Centre"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.648564, -3.962155))
                .title("Penlan Recycling Centre"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.625114, -3.898088))
                .title("Tir John Household Waste Recycling Centre"));

        /*checks that user has given location permissions*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            enableUserLocation();
            zoomToUserLocation();
        } else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    /*gets user location*/
    private void enableUserLocation(){
        mMap.setMyLocationEnabled(true);
    }

    /*displays user location on map*/
    private void zoomToUserLocation(){
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            }
        });
    }

    /*asks the user for location permissions*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableUserLocation();
                zoomToUserLocation();
            } else {

            }
        }
    }
}
