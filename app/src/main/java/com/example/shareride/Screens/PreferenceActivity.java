package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PreferencesOptionRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.PreferencesRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

    // firebase instances
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public void offerRide(View view) {
        sourceLocation = getIntent().getExtras().getParcelable("Source Location");
        destinationLocation = getIntent().getExtras().getParcelable("Destination Location");
        numberOfSeats = getIntent().getIntExtra("Seats",0);
        costPerSeat = getIntent().getStringExtra("Cost_per_seats");
        date = getIntent().getStringExtra("Date");
        time = getIntent().getStringExtra("Time");

        Log.d(TAG, "offerRide: source location: "+sourceLocation.latitude);
        Log.d(TAG, "offerRide: destination location: "+destinationLocation.latitude);
        Log.d(TAG, "offerRide: number of seats: "+numberOfSeats);
        Log.d(TAG, "offerRide: cost per seats: "+costPerSeat);
        Log.d(TAG, "offerRide: date: "+date);
        Log.d(TAG, "offerRide: Time: "+time);
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

}