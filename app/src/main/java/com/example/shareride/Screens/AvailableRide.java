package com.example.shareride.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shareride.Model.MyAvailableRideData;
import com.example.shareride.Model.OfferedRide;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.MyAvailableRideAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AvailableRide extends AppCompatActivity {

    // components
    private RecyclerView recyclerView;

    // firebase instance
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

    // local instances
    private static final String TAG = "Available Ride";
    private String date = "", time = "", sourceLocation = "", destinationLocation = "";
    private ArrayList<MyAvailableRideData> availableRideList = new ArrayList<>();
    private MyAvailableRideAdapter availableRideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_ride);

        initializeComponents();
        fetchIntent();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchResult(sourceLocation, destinationLocation, date, time);

        MyAvailableRideAdapter.setOnRequestRideListener(new MyAvailableRideAdapter.RequestRideListener() {
            @Override
            public void onRequestForRide(MyAvailableRideData rideData) {
                // request for the ride

            }
        });

    }

    private void initializeComponents() {
        // this method will be used to initialize all components
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void fetchIntent() {
        // this method will be used to fetch data from intent
        sourceLocation = getIntent().getStringExtra("Source Location");
        destinationLocation = getIntent().getStringExtra("Destination Location");
        date = getIntent().getStringExtra("Date");
        time = getIntent().getStringExtra("Time");

        Log.d(TAG, "fetchIntent: Source Location: "+sourceLocation);
        Log.d(TAG, "fetchIntent: Destination Location: "+destinationLocation);
        Log.d(TAG, "fetchIntent: Date: "+date);
        Log.d(TAG, "fetchIntent: Time: "+time);
    }

    private void searchResult(String sourceLocation,
                              String destinationLocation,
                              String date,
                              String time) {
        db.collection("Offer Ride")
                .whereEqualTo("Source Location Name", sourceLocation.trim())
                .whereEqualTo("Destination Location Name", destinationLocation.trim())
                .whereEqualTo("Date", date)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: Exception: "+error.getLocalizedMessage());
                            return;
                        }
                        if (value == null) {
                            Log.d(TAG, "onEvent: value is null");
                        } else {
                            Log.d(TAG, "onEvent: value is not null: "+value.getDocuments());

                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Log.d(TAG, "onEvent: snapshot data: "+snapshot);
                                HashMap<String, Double> sourceLocationMap;
                                HashMap<String, Double> destinationLocationMap;
                                String rideSourceLocationName;
                                String rideDestinationLocationName;
                                String rideDate;
                                String rideTime;
                                String rideCostPerSeat;
                                int rideSeat;
                                String rideID;
                                String riderUID;
                                String rideCarID;
                                ArrayList<String> preferencesList = new ArrayList<>();
                                ArrayList<String> passengersIDList = new ArrayList<>();

                                sourceLocationMap = (HashMap<String, Double>) snapshot.get("Source Location");
                                destinationLocationMap = (HashMap<String, Double>) snapshot.get("Destination Location");
                                rideSourceLocationName = snapshot.get("Source Location Name").toString();
                                rideDestinationLocationName = snapshot.get("Destination Location Name").toString();
                                rideDate = snapshot.get("Date").toString();
                                rideTime = snapshot.get("Time").toString();
                                rideCostPerSeat = snapshot.get("Cost Per Seats").toString();
                                rideSeat = Integer.parseInt(snapshot.get("Seats").toString());
                                rideID = snapshot.getId();
                                riderUID = snapshot.get("RiderID").toString();
                                rideCarID = snapshot.get("CarID").toString();
                                preferencesList = (ArrayList<String>) snapshot.get("Preferences");
                                passengersIDList = (ArrayList<String>) snapshot.get("PassengerList");

                                // setting ride details
                                LatLng sourceLatLng = new LatLng(sourceLocationMap.get("latitude").doubleValue(), sourceLocationMap.get("longitude"));
                                LatLng destinationLatLng = new LatLng(destinationLocationMap.get("latitude").doubleValue(), destinationLocationMap.get("longitude"));
                                OfferedRide offeredRide = new OfferedRide();
                                offeredRide.setRideID(rideID);
                                if (sourceLatLng == null) {
                                    Log.d(TAG, "onEvent: sourceLatLng is null");
                                } else {
                                    Log.d(TAG, "onEvent: it is not null");
                                }
                                offeredRide.setSourceLocation(sourceLatLng);
                                Log.d(TAG, "onEvent: SourceLatLan value: "+sourceLatLng);
                                offeredRide.setDestinationLocation(destinationLatLng);
                                offeredRide.setCostPerSeats(rideCostPerSeat);
                                offeredRide.setDate(rideDate);
                                offeredRide.setTime(rideTime);
                                offeredRide.setSeats(rideSeat);
                                offeredRide.setRiderID(riderUID);
                                offeredRide.setPassengersIDList(passengersIDList);
                                offeredRide.setPreferencesList(preferencesList);
                                offeredRide.setCarID(rideCarID);

                                availableRideList.add(new MyAvailableRideData(offeredRide));
                            }
                            availableRideAdapter = new MyAvailableRideAdapter(availableRideList, getApplicationContext());
                            recyclerView.setAdapter(availableRideAdapter);
                        }
//                        for (DocumentSnapshot doc : value) {
//                            Log.d(TAG, "onEvent: "+doc);
//                        }
                    }
                });
    }
}