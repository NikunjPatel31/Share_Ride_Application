package com.example.shareride.Screens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class SignUp extends AppCompatActivity {


    // components
    private TextView tvGender;
    private Spinner spinnerGender;
    private CircleImageView cirImgViewProfilePic;
    private EditText etFirstName, etLastName, etYearOfBirth, etContact, etCity, etPincode, etUPIID;
    private AlertDialog.Builder emailNotVerifiedBuilder;
    private AlertDialog emailVerifyAlertDialog;

    // local instance variable
    private Uri imageUri, uploadUri;
    String[] gender = {"Male", "Female", "Other"};
    Boolean storagePermission;
    private static final String TAG = "SignUp";
    private int GALLERY_REQUEST_CODE = 1, READ_EXTERNAL_STORAGE_REQUSET_CODE = 2;
    ActivityResultLauncher<String> mGetContent;
    String firstName, lastName, genderVal, yearOfBirth, contact, city, pincode, upiId;
    private Boolean userCreated = false;

    // Firebase instance
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    public void apply(View view) {
        // this method will handle onclick of button
        if (validateFields()) {
            if (isEmailVerified()) {
                sendUserDetails();
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                userCreated = true;
            } else {
                // show email is not verified dialog
                emailNotVerifiedDialog();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        initializeComponents();
        initializeFirebaseInstance();
        populateSipper();

        getStoragePermission();

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(getApplicationContext(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUSET_CODE);
            }
        });

        if (storagePermission) {
            Toast.makeText(this, "Now user can access storage", Toast.LENGTH_SHORT).show();
            imageSelect();
        }

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
    }

    private void initializeComponents() {
        // this method will initialize all components
        tvGender = findViewById(R.id.gender_textview);
        spinnerGender = findViewById(R.id.gender_spinner);
        cirImgViewProfilePic = findViewById(R.id.profile_picture_image_view);
        etFirstName = findViewById(R.id.first_name_edit_text);
        etLastName = findViewById(R.id.last_name_edit_text);
        etYearOfBirth = findViewById(R.id.year_of_birth_edittext);
        etContact = findViewById(R.id.contact_edittext);
        etCity = findViewById(R.id.city_edittext);
        etPincode = findViewById(R.id.pincode_edittext);
        etUPIID = findViewById(R.id.upi_id_edit_text);
    }

    private void initializeFirebaseInstance() {
        // this method will initialize all the firebase instance.
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void populateSipper() {
        // this method will populate Gender spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, gender);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void imageSelect() {
        // this method will handle onclick of profile image view
        cirImgViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: profile image clicked.");
//                Toast.makeText(SignUp.this, "circular image view selected", Toast.LENGTH_SHORT).show();
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
            cirImgViewProfilePic.setImageURI(resultUri);
        }
     }

     private boolean validateFields() {
        // this method will validate all the fields
         firstName = etFirstName.getText().toString();
         lastName = etLastName.getText().toString();
         yearOfBirth = etYearOfBirth.getText().toString();
         contact = etContact.getText().toString();
         city = etCity.getText().toString();
         pincode = etPincode.getText().toString();
         upiId = etUPIID.getText().toString();

         if (!(TextUtils.isEmpty(firstName) &&
                 TextUtils.isEmpty(lastName) &&
                 TextUtils.isEmpty(yearOfBirth) &&
                 TextUtils.isEmpty(contact) &&
                 TextUtils.isEmpty(city) &&
                 TextUtils.isEmpty(pincode) &&
                 TextUtils.isEmpty(upiId))) {
             // all the required fields are not empty
             return true;
         } else {
             // All or some of the required field is empty.
             Toast.makeText(this, "Please fill the required field", Toast.LENGTH_SHORT).show();
             return false;
         }
     }

    private void sendUserDetails() {
        // this method will send all user details
        String UID = mAuth.getUid();
        if (UID == null) {
            // UID is null
            Toast.makeText(this, "UID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = firebaseDatabase.getReference().
                child("Users")
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
        databaseReference.child("Gender").setValue(spinnerGender.getSelectedItem().toString());
        databaseReference.child("DOB").setValue(yearOfBirth);
        databaseReference.child("Contact").setValue(contact);
        databaseReference.child("City").setValue(city);
        databaseReference.child("Pincode").setValue(pincode);
        databaseReference.child("UPI_ID").setValue(upiId);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("First Name", firstName);
        myEdit.putString("Last Name", lastName);
        myEdit.putString("Gender", spinnerGender.getSelectedItem().toString());
        myEdit.putString("DOB", yearOfBirth);
        myEdit.putString("Contact", contact);
        myEdit.putString("City", city);
        myEdit.putString("Pincode", pincode);
        myEdit.putString("UPI ID", upiId);

        myEdit.commit();
    }

    private boolean isEmailVerified() {
        // this method will check if the user has validated the email or not
        mAuth.getCurrentUser().reload();
        if (mAuth.getCurrentUser().isEmailVerified()) {
            return true;
        } else {
            mAuth.getCurrentUser().reload();
            if (mAuth.getCurrentUser().isEmailVerified()) {
                return true;
            } else {
                Toast.makeText(this, "Email is not verified", Toast.LENGTH_SHORT).show();
                return  false;
            }
        }
    }

    private void emailNotVerifiedDialog() {
        // this method will show alert dialog
        emailNotVerifiedBuilder = new AlertDialog.Builder(this);
        emailNotVerifiedBuilder.setTitle("Email Verification")
                .setMessage("Email is not verified. Please verify it")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        emailVerifyAlertDialog = emailNotVerifiedBuilder.create();
        emailVerifyAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                emailVerifyAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        emailVerifyAlertDialog.show();
    }

    private void deleteUser()
    {
        if(mAuth.getCurrentUser() != null)
        {
            Log.d(TAG, "deleteUser: deleting the user.");
            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "onComplete: user deleted successfully.");
                    }
                    else
                    {
                        Log.d(TAG, "onComplete: unsuccessful to delete user.");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: user deletion failure Exception: "+e.getLocalizedMessage());
                }
            });
        }
        else
        {
            Log.d(TAG, "deleteUser: there is not user.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!userCreated) {
            deleteUser();
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK)
//        {
//            Log.d(TAG, "onActivityResult: image from the device is selected.");
//            imageUri = data.getData();
//
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
//        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                cirImgViewProfilePic.setImageURI(resultUri);
//                Log.d(TAG, "onActivityResult: sending selected image to database.");
//
//
//                uploadUri = resultUri;
//                Log.d(TAG, "onActivityResult: URI: "+uploadUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }

    private void getStoragePermission() {
        // this method will ask user for storage permission if it is not given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // requesting user for storage permission
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUSET_CODE);
        }
        storagePermission = true;
    }
}