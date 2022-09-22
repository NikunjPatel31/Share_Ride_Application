package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PreferencesOptionRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.PreferencesRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PreferenceActivity extends AppCompatActivity {

    // component
    FloatingActionButton btnnsmoker,btnnbabies,btnadult,btnomen,btnowomen,btnnpet;
    RecyclerView recyclerView, preferencesOptionRecyclerView;

    // local instances
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> preferencesOptionList = new ArrayList<>();
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

        preferencesOptionList.add("Non Somker");
        preferencesOptionList.add("No Babies");
        preferencesOptionList.add("Adult");
        preferencesOptionList.add("Only men");
        preferencesOptionList.add("Only women");
        preferencesOptionList.add("No pet");

//        btnnsmoker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isSmoker) {
//                    isSmoker = true;
//                    list.add("No smoking");
//                    adapter = new PreferencesRecyclerViewAdapter(list,PreferenceActivity.this);
//                    recyclerView.setAdapter(adapter);
//
////                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_horizontal_rule_24));
//                } else {
//                    isSmoker = false;
//                    btnnsmoker.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_24));
//                }
//            }
//        });

        PreferencesOptionRecyclerViewAdapter preferencesOptionRecyclerViewAdapter
                = new PreferencesOptionRecyclerViewAdapter(preferencesOptionList, getApplicationContext());
        preferencesOptionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        preferencesOptionRecyclerView.setAdapter(preferencesOptionRecyclerViewAdapter);

        preferencesOptionRecyclerViewAdapter.setPreferenceListener(new PreferencesOptionRecyclerViewAdapter.PreferenceOptionInterface() {
            @Override
            public void onPreferenceSelected(int position) {
                list.add(preferencesOptionList.get(position));
                adapter.notifyItemChanged(position);
                preferencesOptionList.remove(position);
                preferencesOptionRecyclerViewAdapter.notifyItemRemoved(position);
            }
        });

        adapter = new PreferencesRecyclerViewAdapter(list,PreferenceActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setPreferencesViewListener(new PreferencesRecyclerViewAdapter.PreferencesViewListener() {
            @Override
            public void onPreferenceRemoveListener(int position) {
                preferencesOptionList.add(list.remove(position));
                adapter.notifyItemRemoved(position);
                preferencesOptionRecyclerViewAdapter.notifyItemChanged(position);
            }
        });
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.preferences_recycler_view);
        preferencesOptionRecyclerView = findViewById(R.id.preferences_option_recycler_view);
    }

}