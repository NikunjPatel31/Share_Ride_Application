package com.example.shareride;

import static android.content.ContentValues.TAG;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.shareride.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    MarkerOptions place1, place2;
    Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location1");

        String url = getUrl(place1.getPosition(), place2.getPosition(), "Driving");
        new FetchURL(MapsActivity.this).execute(url, "driving");
    }

    private String getUrl(LatLng origin, LatLng destination, String directionMode) {
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_destination = "destination="+destination.latitude+","+destination.longitude;

        String mode = "mode="+directionMode;

        String parameters = str_origin+"&"+str_destination+"&"+mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"
                +output+"?"+parameters+"&key"+getString(R.string.google_api_key);

        return url;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            Log.d(TAG, "onTaskDone: currentPolyline is not null ");
            currentPolyline.remove();
        } else {
            Log.d(TAG, "onTaskDone: currentPolyline is null");
            currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        }
    }

    public void click(View view) {
        new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }


}