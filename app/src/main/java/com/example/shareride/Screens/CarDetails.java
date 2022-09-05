package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.shareride.Model.Car;
import com.example.shareride.R;

public class CarDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        Intent intent = getIntent();
        Car car = intent.getParcelableExtra("Car");
        Log.d(TAG, "onCreate: Car: "+car.getCarName());
    }
}