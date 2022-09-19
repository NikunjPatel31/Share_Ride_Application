package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PreferencesRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PreferenceActivity extends AppCompatActivity {

    // component
    FloatingActionButton btnnsmoker,btnnbabies,btnadult,btnomen,btnowomen,btnnpet;
    RecyclerView recyclerView;

    // local instances
    ArrayList<String> list = new ArrayList<>();
    Boolean isSmoker=false;
    PreferencesRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initializeComponents();

//        list.add("No Smoking");
//        list.add("No babies");
//        list.add("No krupa");
//        list.add("No jeetu");

        btnnsmoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSmoker) {
                    isSmoker = true;
                    list.add("No smoking");
                    adapter = new PreferencesRecyclerViewAdapter(list,PreferenceActivity.this);
                    recyclerView.setAdapter(adapter);

//                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_horizontal_rule_24));
                } else {
                    isSmoker = false;
                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_24));
                }
            }
        });

        adapter = new PreferencesRecyclerViewAdapter(list,PreferenceActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
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