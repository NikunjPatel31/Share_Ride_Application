package com.example.shareride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.shareride.Model.OfferedRide;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.OfferedRidesRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
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

                                offeredRideList.add(offeredRide);
                            }
                            adapter = new OfferedRidesRecyclerViewAdapter(offeredRideList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "onComplete: Exception: "+task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
}