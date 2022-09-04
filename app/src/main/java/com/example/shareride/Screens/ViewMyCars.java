package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.CarsRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMyCars extends AppCompatActivity {

    // local instance variable
    private CarsRecyclerViewAdapter carAdapter;
    private ArrayList<Car> carList = new ArrayList<>();

    // components
    private RecyclerView carsRecyclerView;

    // Firebase instance
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_cars);

        initializeComponents();
        initializeFirebaseInstance();

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
                        carAdapter = new CarsRecyclerViewAdapter(carList);
                        carsRecyclerView.setAdapter(carAdapter);
                    }
                });

    }
}