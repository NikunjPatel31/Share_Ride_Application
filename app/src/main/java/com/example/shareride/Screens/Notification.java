package com.example.shareride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.shareride.PageAdapter.MyPageAdapter;
import com.example.shareride.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Notification extends AppCompatActivity {

    // components
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabItem riderTI, passengerTI;
    private CoordinatorLayout rootLayout;

    // local instances
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initializeWidgets();
        setViewPagerAdapter();
        tablayoutOnTabSeleteListener();
    }

    private void initializeWidgets() {
        Log.d(TAG, "initializeWidgets: initializing Widgets.");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        riderTI = (TabItem) findViewById(R.id.rider_tabItem);
        passengerTI = (TabItem) findViewById(R.id.passenger_tabItem);
        rootLayout = findViewById(R.id.root_layout);
    }

    private void setViewPagerAdapter() {
        Log.d(TAG, "setViewPagerAdapter: setting viewpager adapter.");
        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),"Notification");
        viewPager.setAdapter(pageAdapter);
    }

    private void tablayoutOnTabSeleteListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}