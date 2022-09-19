package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.shareride.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class OfferRideOne extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "OfferRideOne";
    private TextView tvDate, tvTime;

    private String time = "";
    private String date = "";

    private LatLng sourceLocation, destinationLocation;

    public void next(View view) {
        if (validateFields()) {
            sourceLocation = getIntent().getExtras().getParcelable("SourceLocation");
            destinationLocation = getIntent().getExtras().getParcelable("DestinationLocation");
            Intent intent = new Intent(OfferRideOne.this,OfferRideOne.class);
            intent.putExtra("Source Location",sourceLocation);
            intent.putExtra("Destination Location",destinationLocation);
            intent.putExtra("Time",time);
            intent.putExtra("Date",date);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride_one);

        initializeComponents();
        textviewListener();
    }

    private void initializeComponents() {
        // this method will initialize all the components
        tvDate = findViewById(R.id.date_textview);
        tvTime = findViewById(R.id.time_textview);
    }

    private void textviewListener() {
        // this method will handle textview on click listener
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickedDialog();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
    }

    private void showDatePickedDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                //R.style.custom_date_picker_dialog,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "-" + month + "-" + year;
        tvDate.setText(date);
    }

    private void showTimePicker()
    {
        int hours, min;
        Log.d(TAG, "showTimePicker: showing time picker.");
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                this,
                hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                min = Calendar.getInstance().get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(this)
        );
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        if(hourOfDay > 12)
        {
            int tem = hourOfDay - 12;
            time = tem + ":" + minute + " PM";
            Log.d(TAG, "onTimeSet: time: "+tem);
        }
        else
        {
            time = hourOfDay + ":" + minute + " AM";
        }
        tvTime.setText(time);
    }

    private boolean validateFields()
    {
        Log.d(TAG, "validateFields: Validating fields.");
        if(date.equals("") || time.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}