package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.CarsRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.SelectCarRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SelectCarActivity extends AppCompatActivity {

    // local instance variable
    private SelectCarRecyclerViewAdapter carAdapter;
    public static ArrayList<Car> carList = new ArrayList<>();

    // components
    private RecyclerView carsRecyclerView;

    // Firebase instance
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);

        initializeComponents();
        initializeFirebaseInstance();

        SelectCarRecyclerViewAdapter.setSelectCarListener(new SelectCarRecyclerViewAdapter.SelectCarListener() {
            @Override
            public void onCarSelectedListener(Car car) {

                LatLng sourceLocation = getIntent().getExtras().getParcelable("Source Location");
                LatLng destinationLocation = getIntent().getExtras().getParcelable("Destination Location");
                String date = getIntent().getStringExtra("Date");
                String time = getIntent().getStringExtra("Time");
                String costPerSeats = getIntent().getStringExtra("Cost_per_seats");
                int seats = getIntent().getIntExtra("Seats", 1);

                Log.d(TAG, "onCarSelectedListener: source location: "+sourceLocation);
                Intent intent = new Intent(getApplicationContext(), PreferenceActivity.class);
                intent.putExtra("Source Location",sourceLocation);
                intent.putExtra("Destination Location",destinationLocation);
                intent.putExtra("Time",time);
                intent.putExtra("Date",date);
                intent.putExtra("Seats", seats);
                intent.putExtra("Cost_per_seats", costPerSeats);
                intent.putExtra("Car", car);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllCars();
    }

    @Override
    protected void onPause() {
        super.onPause();
        carList.clear();
    }

    private void initializeComponents() {
        // this method will initialize all components
        carsRecyclerView = findViewById(R.id.recyclerView);
    }

    private void initializeFirebaseInstance() {
        // this method will initialize all firebase instances.
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void getAllCars() {
        // this method will fetch all cars details belonging to current user.
        String UID = mAuth.getCurrentUser().getUid();
        databaseReference.child("Cars")
                .orderByChild("User_id")
                .equalTo(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: Key:"+childSnapshot.getKey()+" | Value: "+childSnapshot.getValue()+
                                    " | "+childSnapshot.child("Fuel_type").getValue());
                            Car car = new Car(
                                    childSnapshot.child("Car_name").getValue().toString(),
                                    childSnapshot.child("Fuel_type").getValue().toString(),
                                    childSnapshot.child("Model_year").getValue().toString(),
                                    childSnapshot.child("Vehicle_number").getValue().toString(),
                                    childSnapshot.child("Air_conditioner").getValue().toString(),
                                    childSnapshot.child("Car_Image").getValue().toString(),
                                    childSnapshot.child("User_id").getValue().toString(),
                                    childSnapshot.getKey()
                            );

                            carList.add(car);

                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Log.d(TAG, "onComplete: carList Size: "+carList.size());
                        carAdapter = new SelectCarRecyclerViewAdapter(carList,getApplicationContext());
                        carsRecyclerView.setAdapter(carAdapter);
                    }
                });

    }
}