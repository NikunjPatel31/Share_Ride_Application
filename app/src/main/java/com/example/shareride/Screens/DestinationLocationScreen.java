package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shareride.R;
import com.example.shareride.databinding.ActivityDestinationLocationScreenBinding;
import com.example.shareride.databinding.ActivitySourceLocationScreenBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class DestinationLocationScreen extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private @NonNull ActivityDestinationLocationScreenBinding binding;
    private Location lastKnowLocation;
    private GoogleApiClient mGoogleClient;
    private LocationRequest mLocationRequest;

    // local instance variable
    private boolean location_permission = false;
    private final float DEFAULT_ZOOM = 15f;
    private final int ACCESS_FINE_LOCATION = 1;

    private FusedLocationProviderClient mfusedLocationProviderClient;
    private LatLng centerScreenLatlng;

    private EditText etSearch;

    public void centerOnMyLocation(View view) {
        getDeviceLocation();
    }

    public void next(View view) {
        centerScreenLatlng = mMap.getCameraPosition().target;
        Log.d(TAG, "next: Destination location: "+centerScreenLatlng.latitude);
        Intent intent = new Intent(getApplicationContext(), OfferRideOne.class);
        intent.putExtra("DestinationLocation", centerScreenLatlng);
        LatLng sourceLocation = getIntent().getExtras().getParcelable("SourceLocation");
        intent.putExtra("SourceLocation", sourceLocation);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDestinationLocationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();
        getDeviceLocation();
        initializeComponents();
        searchLocation();
    }

    private void initializeComponents() {
        // this method will initialize all components
        etSearch = findViewById(R.id.search_edit_text);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleClient.connect();
    }

    private void getDeviceLocation() {
        if (location_permission) {
            Log.d(TAG, "getDeviceLocation: getting device location.");
            mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> task = mfusedLocationProviderClient.getLastLocation();
            task.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Log.d(TAG, "getDeviceLocation: location: " + location.getLongitude() + " Lat: " + location.getLatitude());
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        // mMap.setMyLocationEnabled(true);
                        Log.d(TAG, "onComplete: We are inside the onComplete method");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),DEFAULT_ZOOM));
                        //addMarker(new LatLng(location.getLatitude(), location.getLongitude()),DEFAULT_ZOOM);
                    }
                    else {
                        Log.d(TAG, "onComplete: unable to access location.");
                    }
                }
            });
        } else {
            Log.d(TAG, "getDeviceLocation: device location not provided");
        }
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission.");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "getLocationPermission: permissions are not given.");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION);
        }
        else
        {
            Log.d(TAG, "getLocationPermission: permissions are given.");
            location_permission = true;
        }
    }

    private void addMarker(LatLng latLng,float zoom) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
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

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged: current location: "+location.getLatitude());
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void searchLocation()
    {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating to the specified location.");

        String searchString =  "Mumbai";//searchSourceET.getText().toString();

        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> list = new ArrayList<>();

        try
        {
            list = geocoder.getFromLocationName(searchString,1);
        }
        catch (Exception e)
        {
            Log.d(TAG, "geoLocate: Exception: "+e.getLocalizedMessage());
        }
        Address address = list.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        addMarker(latLng, DEFAULT_ZOOM);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                location_permission = true;
                getDeviceLocation();
            }
        }
    }
}