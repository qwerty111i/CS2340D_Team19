package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.sprint1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class AccommodationsActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_accommodations);

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

            checkSelected = i == 3;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();

                if (id == 0) {
                    intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);
                    startActivity(intent);
                } else if (id == 1) {
                    intent = new Intent(AccommodationsActivity.this, DestinationsActivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    intent = new Intent(AccommodationsActivity.this, DiningActivity.class);
                    startActivity(intent);
                } else if (id == 4) {
                    intent = new Intent(AccommodationsActivity.this, TransportationActivity.class);
                    startActivity(intent);
                } else if (id == 5) {
                    intent = new Intent(AccommodationsActivity.this, TravelActivity.class);
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