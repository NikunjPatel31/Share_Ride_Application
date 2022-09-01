package com.example.shareride.Screens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.example.shareride.R;

import java.util.Objects;

public class EmailVerification extends AppCompatActivity {

    // components
    ImageView imgGif;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    EditText etEmail, etPassword, etConfirmPassword;
    ProgressDialog progressDialog;

    // Firebase instances
    FirebaseAuth mAuth;
    private String TAG = "SignUp";

    public void signup(View view) {
        // this will handle signup button click event
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (validateFields(email, password, confirmPassword)) {
            // all fields are validated
            createUserAccount(email, password);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        initializeComponents();
        initializeFirebaseInstance();
        progressBar();

        Glide.with(this).load(R.drawable.mobile_login).into(imgGif);
    }

    private void initializeComponents() {
        // this function will initialize all components
        imgGif = findViewById(R.id.signup_image_view);
        etEmail = findViewById(R.id.email_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text);
    }

    private void initializeFirebaseInstance() {
        mAuth =  FirebaseAuth.getInstance();
    }

    private boolean validateFields(String email, String password, String confirmPassword) {
        // this method will validate all fields
        if (!TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(confirmPassword)) {
            return password.equals(confirmPassword);
        }
        Toast.makeText(this, "Please enter fields", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void createUserAccount(String email, String password) {
        // this method will create a new user and after creating user it will send verification email to user.
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // user create successfully
                    // send user email for verification
                    Toast.makeText(EmailVerification.this, "User create successfully", Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();
                } else {
                    Toast.makeText(EmailVerification.this, "We are inside the else block", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                if (e instanceof FirebaseAuthInvalidUserException) {
                    String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();

                    if(errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE"))
                    {
                        Toast.makeText(getApplicationContext(), "Email is already in use.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d(TAG, "onFailure: error code: "+((FirebaseAuthInvalidUserException) e).getErrorCode());
                    }
                }
                else if (e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    String errorCode = ((FirebaseAuthInvalidCredentialsException) e).getErrorCode();

                    if(errorCode.equals("ERROR_INVALID_EMAIL"))
                    {
                        Toast.makeText(getApplicationContext(), "Incorrect email.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (e instanceof FirebaseAuthUserCollisionException)
                {
                    String errorCode = ((FirebaseAuthUserCollisionException) e).getErrorCode();
                    if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE"))
                    {
                        Toast.makeText(getApplicationContext(), "Email address is already in use.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendVerificationEmail() {
        // this method will send user verification email
        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Email send successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: Email send successfully");
                    dialog();

                } else {
                    Log.d(TAG, "onComplete: task Exception: "+task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
            }
        });
    }

    private void progressBar() {
        progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
        progressDialog.setMessage("Creating account.");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
    }

    private void dialog() {
        progressDialog.dismiss();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Email Verification")
                .setMessage("We sent you a verification email. Please verify email before moving further.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: button clicked on alert dialog.");
                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(EmailVerification.this, "Ok Button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        alertDialog.show();
    }
}