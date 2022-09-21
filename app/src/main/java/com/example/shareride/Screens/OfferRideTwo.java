package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shareride.R;

public class OfferRideTwo extends AppCompatActivity {

    // components
    private EditText etCostPerSeat;
    private TextView tvNumberOfSeats;

    public void offer(View view) {

    }

    public void decreaseNumber(View view) {
        int value = Integer.parseInt(tvNumberOfSeats.getText().toString());
        if (value > 0) {
            tvNumberOfSeats.setText(""+(--value));
        }
    }

    public void increaseNumber(View view) {
        int value = Integer.parseInt(tvNumberOfSeats.getText().toString());
        tvNumberOfSeats.setText(""+(++value));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride_two);
        initializeComponents();
    }

    private void initializeComponents() {
        etCostPerSeat = findViewById(R.id.cost_per_seat_edittext);
        tvNumberOfSeats = findViewById(R.id.number_of_seats_textview);
    }
}