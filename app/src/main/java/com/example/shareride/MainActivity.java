package com.example.shareride;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.example.shareride.Screens.EmailVerification;
import com.example.shareride.Screens.HomeScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // components
    private ImageView imgGif;
    private TextView tvRegister;
    private EditText etEmail, etPassword;

    // local variable instance
    private String email, password;

    // firebase instance
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void signup(View view) {
        if (valiidateFields()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                databaseReference.child("Users")
                                                .child(mAuth.getUid())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                SharedPreferences sharedPreferences = getSharedPreferences("UserSharedPreferences", MODE_PRIVATE);
                                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                                                myEdit.putString("First Name", snapshot.child("First_Name").getValue().toString());
                                                                myEdit.putString("Last Name", snapshot.child("Last_Name").getValue().toString());
                                                                myEdit.putString("Gender", snapshot.child("Gender").getValue().toString());
                                                                myEdit.putString("DOB", snapshot.child("DOB").getValue().toString());
                                                                myEdit.putString("Contact", snapshot.child("Contact").getValue().toString());
                                                                myEdit.putString("City", snapshot.child("City").getValue().toString());
                                                                myEdit.putString("Pincode", snapshot.child("Pincode").getValue().toString());
                                                                myEdit.putString("UPI ID", snapshot.child("UPI_ID").getValue().toString());

                                                                myEdit.apply();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                Log.d(TAG, "onComplete: user successfully signed in.");
                                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
//                                progressBar.setVisibility(View.INVISIBLE);
//                                loginBtn.setVisibility(View.VISIBLE);

                                Log.d(TAG, "onComplete: error in signing in user. taskException: "+task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: error in signing user. Exception: "+e.getLocalizedMessage());
                            if(e instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(getApplicationContext(), "Entered Password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                            else if (e instanceof FirebaseAuthInvalidUserException)
                            {
                                String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();

                                if (errorCode.equals("ERROR_USER_NOT_FOUND"))
                                {
                                    Toast.makeText(getApplicationContext(), "No Account found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // startActivity(new Intent(getApplicationContext(), SignUp.class));

        initializeComponents();
        initializeFirebaseInstances();

        if (mAuth.getCurrentUser() != null) {
            // user exist
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        Glide.with(this).load(R.drawable.sign_in2).into(imgGif);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EmailVerification.class));
            }
        });
    }

    private void initializeComponents() {
        // this method will initialize all components
        imgGif = findViewById(R.id.image_view_login_screen);
        tvRegister = findViewById(R.id.register_text_view);
        etEmail = findViewById(R.id.email_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
    }

    private void initializeFirebaseInstances() {
        // this method will initialize firebase instances
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean valiidateFields() {
        // this method will validate all the fields
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))) {
            return true;
        } else {
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}