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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.shareride.CropperActivity;
import com.example.shareride.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    // components
    private EditText etFirstName, etLastName, etYearOfBirth, etContact, etCity, etPincode;
    private Spinner spinnerGender;
    private CircleImageView cirImgProfilePic;
    private TextView tvGender;

    // firebase instances
    private FirebaseAuth mAuth;

    // local instance variable
    String[] gender = {"Male", "Female", "Other"};
    ActivityResultLauncher<String> mGetContent;
    private int GALLERY_REQUEST_CODE = 1, READ_EXTERNAL_STORAGE_REQUSET_CODE = 2;
    private Uri uploadUri;
    private String firstName, lastName, genderVal, yearOfBirth, contact, city, pincode, imageString;

    public void apply(View view) {
        if (validateFields()) {
            sendUserDetails();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        initializeComponents();
        initializeFirebaseInstance();
        populateSipper();

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(getApplicationContext(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUSET_CODE);
            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvGender.setText(gender[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvGender.setText("Select gender");
            }
        });

        cirImgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        gettingUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initializeComponents() {
        // this method will be used to initialize all the components
        etFirstName = findViewById(R.id.first_name_edit_text);
        etLastName = findViewById(R.id.last_name_edit_text);
        etYearOfBirth = findViewById(R.id.year_of_birth_edittext);
        etContact = findViewById(R.id.contact_edittext);
        etCity = findViewById(R.id.city_edittext);
        etPincode = findViewById(R.id.pincode_edittext);
        spinnerGender = findViewById(R.id.gender_spinner);
        cirImgProfilePic = findViewById(R.id.profile_picture_image_view);
        tvGender = findViewById(R.id.gender_textview);
    }

    private void initializeFirebaseInstance() {
        // this method will initialize all firebase instance
        mAuth = FirebaseAuth.getInstance();
    }

    private void populateSipper() {
        // this method will populate Gender spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, gender);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
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
            cirImgProfilePic.setImageURI(resultUri);
        }
    }

    private void gettingUserData() {
        // this method will be used to get user details
        String UID = mAuth.getCurrentUser().getUid();
        DatabaseReference childDB = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(UID);

        childDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.d(TAG, "onDataChange: Snapshot: "+snapshot);
                    firstName = snapshot.child("First_Name").getValue().toString();
                    lastName = snapshot.child("Last_Name").getValue().toString();
                    genderVal = snapshot.child("Gender").getValue().toString();
                    yearOfBirth = snapshot.child("DOB").getValue().toString();
                    contact = snapshot.child("Contact").getValue().toString();
                    city = snapshot.child("City").getValue().toString();
                    pincode = snapshot.child("Pincode").getValue().toString();
                    imageString = snapshot.child("Profile_picture").getValue().toString();

                    populateFields();
                } catch (Exception e) {
                    Log.d(TAG, "onDataChange: Exception: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateFields() {
        // this method will be used to populate fields base on the value fetched from database.
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        tvGender.setText(genderVal);
        //
        etYearOfBirth.setText(yearOfBirth);
        etContact.setText(contact);
        etCity.setText(city);
        etPincode.setText(pincode);

        if (!imageString.equals("null")) {
            Log.d(TAG, "populateFields: we are now set the image fetched from firebase.");
            Uri uri = Uri.parse(imageString);
            Picasso.get().load(uri).into(cirImgProfilePic);
        }
    }

    private boolean validateFields() {
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        genderVal = spinnerGender.getSelectedItem().toString();
        yearOfBirth = etYearOfBirth.getText().toString();
        contact = etContact.getText().toString();
        city = etCity.getText().toString();
        pincode = etPincode.getText().toString();

        if (!(TextUtils.isEmpty(firstName) &&
                TextUtils.isEmpty(lastName) &&
                TextUtils.isEmpty(genderVal) &&
                TextUtils.isEmpty(yearOfBirth) &&
                TextUtils.isEmpty(contact) &&
                TextUtils.isEmpty(city) &&
                TextUtils.isEmpty(pincode))) {
            return true;
        } else {
            Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void sendUserDetails() {
        String UID = mAuth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(UID);

        if (uploadUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Profile_picture").
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

                            databaseReference.child("Profile_picture").setValue(path);
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
        } else {
            databaseReference.child("Profile_picture").setValue("null");
        }

        databaseReference.child("First_Name").setValue(firstName);
        databaseReference.child("Last_Name").setValue(lastName);
        databaseReference.child("Gender").setValue(genderVal);
        databaseReference.child("DOB").setValue(yearOfBirth);
        databaseReference.child("Contact").setValue(contact);
        databaseReference.child("City").setValue(city);
        databaseReference.child("Pincode").setValue(pincode);
    }
}