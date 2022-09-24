package com.example.shareride.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.example.shareride.R;

public class HomeScreen extends AppCompatActivity {

    // components
    private BottomAppBar bottomAppBar;
    private Button search;

    public void add(View view) {
        startActivity(new Intent(getApplicationContext(), SourceLocationScreen.class));
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
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();

        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchRide.class));

            }
        });
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
                break;
            case R.id.app_bar_notification:
                break;
        }

        return true;

    }
}