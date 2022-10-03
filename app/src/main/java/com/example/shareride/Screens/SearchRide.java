package com.example.shareride.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shareride.MainActivity;
import com.example.shareride.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class SearchRide extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    // components
    private EditText etSourceLocation, etDestinationLocation;
    private TextView tvDate, tvTime;

    // firebase instances
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // local instances
    private static final String TAG = "SearchRide";
    private String date = "", time = "";

    public void search(View view) {
        if (validateFields()) {
            String sourceLocation = etSourceLocation.getText().toString();
            String destinationLocation = etDestinationLocation.getText().toString();
            Intent intent = new Intent(getApplicationContext(), AvailableRide.class);
            intent.putExtra("Source Location", sourceLocation);
            intent.putExtra("Destination Location", destinationLocation);
            intent.putExtra("Date", date);
            intent.putExtra("Time", time);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Fill the required fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        initializeComponents();
        textViewListener();
    }

    private void initializeComponents() {
        // this method will be used to initialize all the components
        etSourceLocation = findViewById(R.id.source_location_edit_text);
        etDestinationLocation = findViewById(R.id.destination_location_edit_text);
        tvDate = findViewById(R.id.date_text_view);
        tvTime  = findViewById(R.id.time_text_view);
    }

    private void textViewListener() {
        // this method will handle textview listener
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

    private void showDatePickedDialog() {
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

    private void showTimePicker() {
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
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "-" + month + "-" + year;
        tvDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        if(hourOfDay > 12) {
            int tem = hourOfDay - 12;
            time = tem + ":" + minute + " PM";
            Log.d(TAG, "onTimeSet: time: "+tem);
        } else {
            time = hourOfDay + ":" + minute + " AM";
        }
        tvTime.setText(time);
    }

    private boolean validateFields() {
        // this method will validate all required fields
        String sourceLocation = etSourceLocation.getText().toString();
        String destinationLocation = etDestinationLocation.getText().toString();

        if (TextUtils.isEmpty(sourceLocation) || TextUtils.isEmpty(destinationLocation) || date.equals("") || time.equals("")) {
            return false;
        }
        return true;
    }
}