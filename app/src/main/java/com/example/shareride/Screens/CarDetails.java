package com.example.shareride.Screens;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareride.CropperActivity;
import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class CarDetails extends AppCompatActivity {

    // component
    private EditText etCarName, etMdodelYear, etVehicleNumber;
    private Spinner spinnerAirConditioner, spinnerFuel;
    private TextView tvAirConditioner, tvFuel;
    private CircleImageView cirImgCarProfile;
    private Button apply, cancel;

    // firebase instance
    private FirebaseAuth mAuth;

    // local instance variable
    Uri uploadUri;
    String carName, carModel, airConditionerVal, fuelVal, vehicleNumber;
    String[] airConditionerList = {"AC", "Non AC"};
    String[] fuelList = {"Petrol", "Diesel", "CNG"};
    Car car;
    ActivityResultLauncher<String> mGetContent;
    private int GALLERY_REQUEST_CODE = 1, READ_EXTERNAL_STORAGE_REQUSET_CODE = 2;

    public void applyon(View view){
        if (validateFields()) {
            sendUserDetails();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        Intent intent = getIntent();
        car = intent.getParcelableExtra("Car");
        Log.d(TAG, "onCreate: Car: "+car.getCarName());

        initializeComponents();
        initializeFirebaseInstance();
        populateSipper();


        etCarName.setText(car.getCarName());
        etMdodelYear.setText(car.getModelYear());
        etVehicleNumber.setText(car.getVehicleNumber());
        Picasso.get().load(Uri.parse(car.getCarImage())).into(cirImgCarProfile);
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
            Toast.makeText(this, "We are getting the changed image." +
                    "", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onActivityResult: We are getting changed image here in side the onActivity");
            cirImgCarProfile.setImageURI(resultUri);
        }
    }
    private void populateSipper() {
        // this method will populate Gender spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_layout, airConditionerList);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAirConditioner.setAdapter(adapter1);
        int airConditionerPosition = adapter1.getPosition(car.getAirConditioner());
        spinnerAirConditioner.setSelection(airConditionerPosition);

        // this method will populate Gender spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_layout, fuelList);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuel.setAdapter(adapter2);
        int fuelPosition = adapter2.getPosition(car.getFuelType());
        spinnerFuel.setSelection(fuelPosition);

    }

    private void initializeFirebaseInstance() {
        // this method will initialize all firebase instance
        mAuth = FirebaseAuth.getInstance();
    }

    private void initializeComponents() {
        // this method will be used to initialize all component
        etCarName = findViewById(R.id.car_name_edit_text);
        etMdodelYear = findViewById(R.id.model_year_edit_text);
        etVehicleNumber = findViewById(R.id.vehicle_edittext);
        spinnerAirConditioner = findViewById(R.id.air_conditioner_spinner);
        spinnerFuel = findViewById(R.id.fuel_spinner);
        tvAirConditioner = findViewById(R.id.air_conditioner_textview);
        tvFuel = findViewById(R.id.fuel_text_view);
        cirImgCarProfile = findViewById(R.id.car_image_view);
        apply = findViewById(R.id.apply_button);
        cancel = findViewById(R.id.cancel_button);
        cirImgCarProfile=findViewById(R.id.car_image_view);
    }

    private boolean validateFields() {
        carName = etCarName.getText().toString();
        carModel = etMdodelYear.getText().toString();
        airConditionerVal= spinnerAirConditioner.getSelectedItem().toString();
        fuelVal= spinnerFuel.getSelectedItem().toString();
        vehicleNumber = etVehicleNumber.getText().toString();


        if (!(TextUtils.isEmpty(carName) &&
                TextUtils.isEmpty(carModel) &&
                TextUtils.isEmpty(airConditionerVal) &&
                TextUtils.isEmpty(fuelVal) &&
                TextUtils.isEmpty(vehicleNumber)
               )) {
            return true;
        } else {
            Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void sendUserDetails() {
        String UID = mAuth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cars")
                .child(car.getCarId());

        if (uploadUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Car_Images").
                    child(UID);

        storageReference.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().
                        getReference().
                        getDownloadUrl();

                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String path = uri.toString();
                        Log.d(TAG, "onSuccess: Profile picture uri: "+path);

                        databaseReference.child("Car_Image").setValue(path);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ProfilePictureInnerThreadException: "+e.getLocalizedMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ProfilePictureException: "+e.getLocalizedMessage());
            }
        });
    }
        else{
            databaseReference.child("Car_Image").setValue("null");
        }
        databaseReference.child("User_id").setValue(UID);
        databaseReference.child("Car_name").setValue(carName);
        databaseReference.child("Model_year").setValue(carModel);
        databaseReference.child("Air_conditioner").setValue(airConditionerVal);
        databaseReference.child("Fuel_type").setValue(fuelVal);
        databaseReference.child("Vehicle_number").setValue(vehicleNumber);
    }
}
