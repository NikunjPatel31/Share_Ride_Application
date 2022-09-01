package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.shareride.CropperActivity;
import com.example.shareride.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCar extends AppCompatActivity {

    // component
    private EditText etCarName, etMdodelYear, etVehicleNumber;
    private Spinner spinnerAirConditioner, spinnerFuel;
    private TextView tvAirConditioner, tvFuel;
    private CircleImageView cirImgCarProfile;

    // firebase instance
    private FirebaseAuth mAuth;

    // local instance variable
    Uri uploadUri;
    String carName, carModel, airConditionerVal, fuelVal, vehicleNumber;
    String[] airConditionerList = {"AC", "Non AC"};
    String[] fuelList = {"Petrol", "Diesel", "CNG"};
    ActivityResultLauncher<String> mGetContent;
    private int GALLERY_REQUEST_CODE = 1, READ_EXTERNAL_STORAGE_REQUSET_CODE = 2;

    public void addCar(View view) {
        // this method will handle button click event
        if (validateFields()) {
            uploadCarDetails();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initializeComponent();
        initializeFirebaseInitialize();
        populateSpinner();

        spinnerAirConditioner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvAirConditioner.setText(airConditionerList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvFuel.setText(fuelList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(getApplicationContext(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUSET_CODE);
            }
        });

        cirImgCarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void initializeComponent() {
        // this method will be used to initialize all component
        etCarName = findViewById(R.id.car_name_edit_text);
        etMdodelYear = findViewById(R.id.model_year_edit_text);
        etVehicleNumber = findViewById(R.id.vehicle_edittext);
        spinnerAirConditioner = findViewById(R.id.air_conditioner_spinner);
        spinnerFuel = findViewById(R.id.fuel_spinner);
        tvAirConditioner = findViewById(R.id.air_conditioner_textview);
        tvFuel = findViewById(R.id.fuel_text_view);
        cirImgCarProfile = findViewById(R.id.car_image_view);
    }

    private void initializeFirebaseInitialize() {
        // this method will initialize all firebase object
        mAuth = FirebaseAuth.getInstance();
    }

    private void populateSpinner() {
        // this method will populate both spinner
        ArrayAdapter<String> airConditionerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, airConditionerList);
        spinnerAirConditioner.setAdapter(airConditionerAdapter);
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, fuelList);
        spinnerFuel.setAdapter(fuelAdapter);
    }

    private boolean validateFields() {
        // this method will validate all fields
        carName = etCarName.getText().toString();
        carModel = etMdodelYear.getText().toString();
        airConditionerVal = tvAirConditioner.getText().toString();
        fuelVal = tvFuel.getText().toString();
        vehicleNumber = etVehicleNumber.getText().toString();

        if (!(TextUtils.isEmpty(carName) &&
                TextUtils.isEmpty(carModel) &&
                TextUtils.isEmpty(airConditionerVal) &&
                TextUtils.isEmpty(fuelVal) &&
                TextUtils.isEmpty(vehicleNumber))) {
            return true;
        } else {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void uploadCarDetails() {
        // this method will upload car details to firebase.
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference childDB = FirebaseDatabase.getInstance().
                getReference()
                .child("Cars")
                .child(UID)
                .push();

        String car_id = "";
        try {
            car_id = childDB.getKey();
        } catch(Exception e) {
            Log.d(TAG, "uploadCarDetails: Exception: "+e.getLocalizedMessage());
        }
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference()
                .child("Car_Images")
                .child(UID)
                .child(car_id);

        storageReference.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String path = uri.toString();

                        Log.d(TAG, "sendUserData: sending car image uri URI: "+path);
                        childDB.child("Car_Image").setValue(path);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
            }
        });

        childDB.child("Car_name").setValue(carName);
        childDB.child("Model_year").setValue(carModel);
        childDB.child("Air_conditioner").setValue(airConditionerVal);
        childDB.child("Fuel_type").setValue(fuelVal);
        childDB.child("Vehicle_number").setValue(vehicleNumber);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1 && requestCode == READ_EXTERNAL_STORAGE_REQUSET_CODE) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
                uploadUri = resultUri;
            }
            Log.d(TAG, "onActivityResult: We are getting changed image here in side the onActivity");
            cirImgCarProfile.setImageURI(resultUri);
        }
    }
}