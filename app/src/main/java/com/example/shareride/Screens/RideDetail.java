package com.example.shareride.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareride.Fragments.RideCompleteFragment;
import com.example.shareride.Model.MyAvailableRideData;
import com.example.shareride.Model.User;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.ShowPassengerRecyclerAdapter;
import com.example.shareride.RecyclerViewAdapter.ShowPreferencesRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideDetail extends AppCompatActivity {

    // components
    private TextView tvSourceLocationName,
            tvDestinationLocation,
            tvRideDate,
            tvRideTime,
            tvSeats,
            tvCostPerSeats;

    private TextView tvRiderName,
            tvGender,
            tvAge,
            tvCity;

    private TextView tvCarName,
            tvCarModel,
            tvCarFuelType,
            tvCarAirConditioner,
            tvCarNumber;

    private CircleImageView cirImgRider;
    private ImageView imgViewCar;

    private RecyclerView preferenceRecyclerView;
    private RecyclerView passengerRecyclerView;

    private AppCompatButton btnRideComplete;

    // firebase instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    // local instance
    private static final String TAG = "RideDetails";
    private MyAvailableRideData ride = new MyAvailableRideData();
    private ArrayList<User> userList = new ArrayList<>();
    private User rider = new User();

    // adapter
    private ShowPreferencesRecyclerAdapter preferencesRecyclerAdapter;
    private ShowPassengerRecyclerAdapter passengerRecyclerAdapter;

    public void rideComplete(View view) {
        // set ride status to completed
        db.collection("Offer Ride")
                .document(ride.getOfferedRide().getRideID())
                .update("Status", "Completed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ride completed
                        // we can write a function to write status to completed in ride request
                        Log.d(TAG, "onComplete: Ride completed successfully and status is also update to completed.");
                        RideCompleteFragment.display(getSupportFragmentManager());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RideDetail.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                    }
                });
    }

    public void call(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+rider.getContact()));
        startActivity(callIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
        initializeComponents();
        try {
            if (getIntent().getStringExtra("Screen").equals("Rider")) {
                btnRideComplete.setVisibility(View.VISIBLE);
            }
            ride = getIntent().getExtras().getParcelable("Ride");
            fetchData();
            populateRideDetails();
            populatePreferences();
            passengerRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            passengerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            passengerRecyclerAdapter = new ShowPassengerRecyclerAdapter(userList);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception: "+e.getLocalizedMessage());
        }
    }

    private void initializeComponents() {
        // this method will be used to initialize all components
        tvSourceLocationName = findViewById(R.id.source_location_text_view);
        tvDestinationLocation = findViewById(R.id.destination_location_text_view);
        tvRideDate = findViewById(R.id.date_value_text_view);
        tvRideTime = findViewById(R.id.time_value_text_view);
        tvCostPerSeats = findViewById(R.id.cost_value_textView);
        tvSeats = findViewById(R.id.seats_value_text_view);
        tvRiderName = findViewById(R.id.rider_name_text_view);
        tvAge = findViewById(R.id.age_value_text_view);
        tvCity = findViewById(R.id.city_value_text_view);
        tvGender = findViewById(R.id.gender_value_text_view);
        cirImgRider = findViewById(R.id.rider_image_view);
        imgViewCar = findViewById(R.id.car_image_view);
        tvCarName = findViewById(R.id.car_name_textview);
        tvCarNumber = findViewById(R.id.vehicle_number_text_view);
        tvCarAirConditioner = findViewById(R.id.air_conditioner_text_view);
        tvCarFuelType = findViewById(R.id.fuel_text_view);
        tvCarModel = findViewById(R.id.model_year_text_view);
        preferenceRecyclerView = findViewById(R.id.preferences_recycler_view);
        passengerRecyclerView = findViewById(R.id.passenger_recycler_view);
        btnRideComplete = findViewById(R.id.ride_complete_btn);
    }

    private void fetchData() {
        if (ride != null) {
            String riderID = ride.getOfferedRide().getRiderID();
            databaseReference.child("Users")
                    .child(riderID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: snapshot: "+snapshot.child("First_Name").getValue().toString()+" "+snapshot.child("Last_Name").getValue().toString());
                    rider.setUserID(snapshot.getKey());
                    rider.setFirstName(snapshot.child("First_Name").getValue().toString());
                    rider.setLastName(snapshot.child("Last_Name").getValue().toString());
                    rider.setPincode(snapshot.child("Pincode").getValue().toString());
                    rider.setGender(snapshot.child("Gender").getValue().toString());
                    rider.setDOB(snapshot.child("DOB").getValue().toString());
                    rider.setCity(snapshot.child("City").getValue().toString());
                    rider.setContact(snapshot.child("Contact").getValue().toString());
                    rider.setProfilePic(snapshot.child("Profile_picture").getValue().toString());

                    tvRiderName.setText(snapshot.child("First_Name").getValue().toString()
                            +" "
                            +snapshot.child("Last_Name").getValue().toString());
                    tvAge.setText(snapshot.child("DOB").getValue().toString());
                    tvGender.setText(snapshot.child("Gender").getValue().toString());
                    tvCity.setText(snapshot.child("City").getValue().toString());
                    Picasso.get().load(snapshot.child("Profile_picture").getValue().toString()).into(cirImgRider);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.d(TAG, "fetchData: We are inside the method");
            Log.d(TAG, "fetchData: CarID: "+ride.getOfferedRide().getCarID());
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Cars")
                    .child(ride.getOfferedRide().getCarID())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tvCarAirConditioner.setText(snapshot.child("Air_conditioner").getValue().toString());
                            tvCarName.setText(snapshot.child("Car_name").getValue().toString());
                            tvCarFuelType.setText(snapshot.child("Fuel_type").getValue().toString());
                            tvCarModel.setText(snapshot.child("Model_year").getValue().toString());
                            tvCarNumber.setText(snapshot.child("Vehicle_number").getValue().toString());
                            Log.d(TAG, "onDataChange: data is fetched from the firebase");
                            Picasso.get().load(snapshot.child("Car_Image").getValue().toString()).into(imgViewCar);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            DatabaseReference passengerDB = databaseReference.child("Users");
            for (String id : ride.getOfferedRide().getPassengersIDList()) {
                passengerDB.child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = new User();
                                user.setFirstName(snapshot.child("First_Name").getValue().toString());
                                user.setLastName(snapshot.child("Last_Name").getValue().toString());
                                user.setCity(snapshot.child("City").getValue().toString());
                                user.setContact(snapshot.child("Contact").getValue().toString());
                                user.setDOB(snapshot.child("DOB").getValue().toString());
                                user.setGender(snapshot.child("Gender").getValue().toString());
                                user.setPincode(snapshot.child("Pincode").getValue().toString());
                                user.setProfilePic(snapshot.child("Profile_picture").getValue().toString());
                                user.setUserID(snapshot.getKey());

                                userList.add(user);
                                passengerRecyclerView.setAdapter(passengerRecyclerAdapter);

                                Log.d(TAG, "onDataChange: PassengerID: "+snapshot.getKey());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        } else {
            Toast.makeText(this, "Ride details is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateRideDetails() {
        // this method will populate only ride details
        Log.d(TAG, "populateRideDetails: cost: "+ride.getOfferedRide().getCostPerSeats());
        tvCostPerSeats.setText(ride.getOfferedRide().getCostPerSeats() + " ₹");
        tvRideDate.setText(ride.getOfferedRide().getDate());
        tvRideTime.setText(ride.getOfferedRide().getTime());
        tvSeats.setText(""+ride.getOfferedRide().getSeats());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            if (ride.getOfferedRide().getSourceLocation() == null) {
                Log.d(TAG, "populateRideDetails: It is null");
            }
            List<Address> addresses = geocoder.getFromLocation(ride.getOfferedRide().getSourceLocation().latitude, ride.getOfferedRide().getSourceLocation().longitude,1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            tvSourceLocationName.setText(address+", "+cityName+", "+stateName);

            addresses = geocoder.getFromLocation(ride.getOfferedRide().getDestinationLocation().latitude, ride.getOfferedRide().getDestinationLocation().longitude, 1);

            address = addresses.get(0).getSubLocality();
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAdminArea();
            Log.d(TAG, "populateRideDetails: Address: "+address+", "+cityName+", "+stateName);

            tvDestinationLocation.setText(address+", "+cityName+", "+stateName);
        } catch (Exception e) {
            Log.d(TAG, "populateRideDetails: Exception: "+e.getLocalizedMessage());
        }
    }

    private void populatePreferences() {
        // this method will populate preferences recyclerview
        preferenceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        preferencesRecyclerAdapter = new ShowPreferencesRecyclerAdapter(ride.getOfferedRide().getPreferencesList());
        preferenceRecyclerView.setAdapter(preferencesRecyclerAdapter);
    }
}