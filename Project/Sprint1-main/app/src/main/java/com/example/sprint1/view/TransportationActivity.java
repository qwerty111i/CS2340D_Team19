package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprint1.R;
import com.google.android.material.tabs.TabLayout;

public class TransportationActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transportation);

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();
    }

    private void navigation() {
        boolean checkSelected = false;
        int[] navIcons = {
                R.drawable.logistics,
                R.drawable.destination,
                R.drawable.dining,
                R.drawable.accommodation,
                R.drawable.transport,
                R.drawable.travel };

        for (int i = 0; i < navIcons.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();

            checkSelected = i == 4;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();

                if (id == 0) {
                    intent = new Intent(TransportationActivity.this, LogisticsActivity.class);
                    startActivity(intent);
                } else if (id == 1) {
                    intent = new Intent(TransportationActivity.this, DestinationsActivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    intent = new Intent(TransportationActivity.this, DiningActivity.class);
                    startActivity(intent);
                } else if (id == 3) {
                    intent = new Intent(TransportationActivity.this, AccommodationsActivity.class);
                    startActivity(intent);
                } else if (id == 5) {
                    intent = new Intent(TransportationActivity.this, TravelActivity.class);
                    startActivity(intent);
                }

                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ImageView tabIcon = tabView.findViewById(R.id.tab_navigation);
                    tabIcon.setColorFilter(getResources().getColor(R.color.light_modern_purple));
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ImageView tabIcon = tabView.findViewById(R.id.tab_navigation);
                    tabIcon.clearColorFilter();
                }
            }
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    };
}