package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shareride.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PreferenceActivity extends AppCompatActivity {

    // component
    FloatingActionButton btnnsmoker,btnnbabies,btnadult,btnomen,btnowomen,btnnpet;
    RecyclerView recyclerView;

    Boolean isSmoker=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initializeComponents();
        btnnsmoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSmoker) {
                    isSmoker = true;
                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_horizontal_rule_24));
                } else {
                    isSmoker = false;
                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_24));
                }
            }
        });
    }

    private void initializeComponents() {
        btnnsmoker = findViewById(R.id.btnnsmoker);
        btnnbabies = findViewById(R.id.btnnbabies);
        btnadult = findViewById(R.id.btnadult);
        btnomen = findViewById(R.id.btnomen);
        btnowomen = findViewById(R.id.btnowomen);
        btnnpet = findViewById(R.id.btnnpet);
        recyclerView = findViewById(R.id.preferences_recycler_view);
    }

}