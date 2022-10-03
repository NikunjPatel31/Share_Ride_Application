package com.example.shareride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PreferencesOptionRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.PreferencesRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PreferenceActivity extends AppCompatActivity {

    // component
    RecyclerView recyclerView, preferencesOptionRecyclerView;

    // local instances
    private static final String TAG = "PreferenceActivity";
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> preferencesOptionList = new ArrayList<>();
    PreferencesRecyclerViewAdapter adapter;
    LatLng sourceLocation;
    LatLng destinationLocation;
    int numberOfSeats = 0;
    String costPerSeat = "";
    String date = "", time = "";
    String sourceLocationName = "";
    String destinationLocationName = "";

    // firebase instances
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void offerRide(View view) {
        sourceLocation = getIntent().getExtras().getParcelable("Source Location");
        destinationLocation = getIntent().getExtras().getParcelable("Destination Location");
        numberOfSeats = getIntent().getIntExtra("Seats",0);
        costPerSeat = getIntent().getStringExtra("Cost_per_seats");
        date = getIntent().getStringExtra("Date");
        time = getIntent().getStringExtra("Time");
        Car car = getIntent().getExtras().getParcelable("Car");

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(sourceLocation.latitude, sourceLocation.longitude, 1);
            sourceLocationName = addresses.get(0).getLocality();

            addresses = geocoder.getFromLocation(destinationLocation.latitude, destinationLocation.longitude, 1);
            destinationLocationName = addresses.get(0).getLocality();
        } catch (Exception e) {
            Log.d(TAG, "offerRide: Exception: "+e.getLocalizedMessage());
        }

        Log.d(TAG, "offerRide: source location: "+sourceLocation.latitude);
        Log.d(TAG, "offerRide: destination location: "+destinationLocation.latitude);
        Log.d(TAG, "offerRide: number of seats: "+numberOfSeats);
        Log.d(TAG, "offerRide: cost per seats: "+costPerSeat);
        Log.d(TAG, "offerRide: date: "+date);
        Log.d(TAG, "offerRide: Time: "+time);
        Log.d(TAG, "offerRide: UID: "+mAuth.getCurrentUser().getUid());
        Log.d(TAG, "offerRide: SourceLocationName: "+sourceLocationName);
        Log.d(TAG, "offerRide: destinationLocationName: "+destinationLocationName);
        Log.d(TAG, "offerRide: CarId: "+car.getCarId());

        HashMap<String, Object> map = new HashMap<>();
        map.put("Source Location", sourceLocation);
        map.put("Destination Location", destinationLocation);
        map.put("Seats", numberOfSeats);
        map.put("Cost Per Seats", costPerSeat);
        map.put("Date", date);
        map.put("Time", time);
        map.put("Preferences", list);
        map.put("RiderID", mAuth.getCurrentUser().getUid());
        map.put("Source Location Name", sourceLocationName);
        map.put("Destination Location Name", destinationLocationName);
        map.put("CarID",car.getCarId());

        db.collection("Offer Ride")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: offer ride uploaded successfully.");
                        }  else {
                            Log.d(TAG, "onComplete: Exception: "+task.getException().getLocalizedMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                    }
                });

        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initializeComponents();

        preferencesOptionList.add("Non Somker");
        preferencesOptionList.add("No Babies");
        preferencesOptionList.add("Adult");
        preferencesOptionList.add("Only men");
        preferencesOptionList.add("Only women");
        preferencesOptionList.add("No pet");

        PreferencesOptionRecyclerViewAdapter preferencesOptionRecyclerViewAdapter
                = new PreferencesOptionRecyclerViewAdapter(preferencesOptionList, getApplicationContext());
        preferencesOptionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        preferencesOptionRecyclerView.setAdapter(preferencesOptionRecyclerViewAdapter);

        preferencesOptionRecyclerViewAdapter.setPreferenceListener(new PreferencesOptionRecyclerViewAdapter.PreferenceOptionInterface() {
            @Override
            public void onPreferenceSelected(int position) {
                list.add(preferencesOptionList.get(position));
                adapter.notifyItemChanged(position);
                preferencesOptionList.remove(position);
                preferencesOptionRecyclerViewAdapter.notifyItemRemoved(position);
            }
        });

        adapter = new PreferencesRecyclerViewAdapter(list,PreferenceActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setPreferencesViewListener(new PreferencesRecyclerViewAdapter.PreferencesViewListener() {
            @Override
            public void onPreferenceRemoveListener(int position) {
                preferencesOptionList.add(list.remove(position));
                adapter.notifyItemRemoved(position);
                preferencesOptionRecyclerViewAdapter.notifyItemChanged(position);
            }
        });
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.preferences_recycler_view);
        preferencesOptionRecyclerView = findViewById(R.id.preferences_option_recycler_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        list.clear();
    }
}