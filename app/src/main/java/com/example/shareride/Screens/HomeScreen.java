package com.example.shareride.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.example.shareride.R;

public class HomeScreen extends AppCompatActivity {

    // components
    private BottomAppBar bottomAppBar;

    public void add(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeComponents();
        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddCar.class));
            }
        });
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
    }

    private void initializeComponents() {
        // this method will be used to initialze all the components
        bottomAppBar = findViewById(R.id.bottom_action_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
                break;
            case R.id.app_bar_notification:
                startActivity(new Intent(getApplicationContext(), ViewMyCars.class));
                break;
        }

        return true;




    }
}