package com.example.shareride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.shareride.Model.MyAvailableRideData;
import com.example.shareride.Model.OfferedRide;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.OfferedRidesRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOfferedRides extends AppCompatActivity {

    //  components
    private RecyclerView recyclerView;

    // firebase instances
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // local instances
    private final String TAG = "MyOfferedRides";
    private ArrayList<OfferedRide> offeredRideList = new ArrayList<>();
    private OfferedRidesRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offered_rides);
        initializeComponents();
        recyclerView.setLayoutManager(new LinearLayoutManager(MyOfferedRides.this));

        OfferedRidesRecyclerViewAdapter.setRideSelectListener(new OfferedRidesRecyclerViewAdapter.RideSelectListener() {
            @Override
            public void onRideSelectListener(OfferedRide ride) {
                // Write code to navigate from MyOfferedRide to RideInfo Activity
                Intent intent = new Intent(getApplicationContext(), RideDetail.class);
                intent.putExtra("Screen", "Rider");
                MyAvailableRideData availableRideData = new MyAvailableRideData();
                availableRideData.setOfferedRide(ride);
                intent.putExtra("Ride", availableRideData);
                Log.d(TAG, "onRideSelectListener: ride: "+ride.getRideID());
                startActivity(intent);
            }
        });

        OfferedRidesRecyclerViewAdapter.setRideDeleteListener(new OfferedRidesRecyclerViewAdapter.RideDeleteListener() {
            @Override
            public void onRideDeleteListener(int position, OfferedRide ride) {
                db.collection("Offer Ride")
                        .document(ride.getRideID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    offeredRideList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                } else {
                                    Log.d(TAG, "onComplete: Exception: "+task.getException().getLocalizedMessage());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    private void initializeComponents() {
        // this method will initialize all components
        recyclerView = findViewById(R.id.recyclerView_offered_ride);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRides();
    }

    private void getRides() {
        // this method will fetch all the rides belonging to current user
        db.collection("Offer Ride")
                .whereEqualTo("RiderID", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                //offeredRide.setSourceLocation(documentSnapshot.get("Source Location"));

                                HashMap<String, Double> map = (HashMap<String, Double>) documentSnapshot.get("Source Location");
                                LatLng sourceLocation = new LatLng(map.get("latitude"), map.get("longitude"));

                                map = (HashMap<String, Double>) documentSnapshot.get("Destination Location");
                                LatLng destinationLocation = new LatLng(map.get("latitude"), map.get("longitude"));

                                OfferedRide offeredRide = new OfferedRide();
                                offeredRide.setRideID(documentSnapshot.getId());
                                offeredRide.setSourceLocation(sourceLocation);
                                offeredRide.setDestinationLocation(destinationLocation);
                                offeredRide.setDate(documentSnapshot.get("Date").toString());
                                offeredRide.setCostPerSeats(documentSnapshot.get("Cost Per Seats").toString());
                                offeredRide.setSeats(Integer.parseInt(documentSnapshot.get("Seats").toString()));
                                offeredRide.setTime(documentSnapshot.get("Time").toString());
                                offeredRide.setRiderID(documentSnapshot.get("RiderID").toString());
                                offeredRide.setPassengersIDList((ArrayList<String>) documentSnapshot.get("PassengerList"));
                                offeredRide.setCarID(documentSnapshot.get("CarID").toString());
                                offeredRide.setPreferencesList((ArrayList<String>) documentSnapshot.get("Preferences"));
                                offeredRide.setStatus(documentSnapshot.get("Status").toString());
                                offeredRideList.add(offeredRide);
                            }
                            adapter = new OfferedRidesRecyclerViewAdapter(offeredRideList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "onComplete: Exception: "+task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
}