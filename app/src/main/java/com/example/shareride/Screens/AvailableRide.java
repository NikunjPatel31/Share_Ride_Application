package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.shareride.R;

public class AvailableRide extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_ride);

        back=findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SearchRide.class);
                startActivity(i);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));


        MyAvailableRideData[] myAvailableRideData=new MyAvailableRideData[]{
                new MyAvailableRideData("Prachi Vasava","5000","Anand","Manali","06:00 AM",5,"06-01-2023","4",R.drawable.ic_baseline_person_24),
                new MyAvailableRideData("Krupa Patel","500","Anand","Ahmedabad","11:00 AM",4,"06-10-2022","3",R.drawable.ic_baseline_person_24),
                new MyAvailableRideData("Nikunj Patel","900","Anand","Rajkot","06:00 AM",5,"06-01-2023","4",R.drawable.ic_baseline_person_24),

        };
        MyAvailableRideAdapter myAvailableRideAdapter = new MyAvailableRideAdapter(myAvailableRideData,AvailableRide.this);
        recyclerView.setAdapter(myAvailableRideAdapter);


    }
}