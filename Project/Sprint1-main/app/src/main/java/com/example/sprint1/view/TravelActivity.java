package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityTravelBinding;
import com.example.sprint1.model.TFEUser;
import com.example.sprint1.viewmodel.TFEAdapter;
import com.example.sprint1.viewmodel.TravelAdapter;
import com.example.sprint1.viewmodel.TravelViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TravelViewModel viewModel;
    private Button logTravelFormEntryBtn;
    private RecyclerView recyclerView;
    private TFEAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflate the layout using View Binding
        ActivityTravelBinding binding = ActivityTravelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TravelViewModel.class);

        // Bind the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        // Initialize RecyclerView
        recyclerView = binding.recyclerViewTravelEntries;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        adapter = new TFEAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Observe LiveData from ViewModel
        observeViewModel();

        logTravelFormEntry(binding);

        // Initialize TabLayout
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();
    }

    private void observeViewModel() {
        viewModel.getTravelEntriesLiveData().observe(this, new Observer<List<TFEUser>>() {
            @Override
            public void onChanged(List<TFEUser> tfeUsers) {
                adapter.updateData(tfeUsers);
            }
        });
    }

    public void logTravelFormEntry(ActivityTravelBinding binding) {
        logTravelFormEntryBtn = binding.logTravelFormEntryDialog;

        logTravelFormEntryBtn.setOnClickListener(v -> {
            AddSharedTravelDialog dialog = new AddSharedTravelDialog();
            dialog.show(getSupportFragmentManager(), "AddSharedTravelDialog");
        });
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

            checkSelected = i == 5;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();
                Log.d("TravelActivity", "Tab selected: " + id); // Log the selected tab index
                if (id == 0) {
                    intent = new Intent(TravelActivity.this, LogisticsActivity.class);
                    Log.d("TravelActivity", "Tab selected: " + id); // Log the selected tab index
                    startActivity(intent);
                } else if (id == 1) {
                    intent = new Intent(TravelActivity.this, DestinationsActivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    intent = new Intent(TravelActivity.this, DiningActivity.class);
                    startActivity(intent);
                } else if (id == 3) {
                    intent = new Intent(TravelActivity.this, AccommodationsActivity.class);
                    startActivity(intent);
                } else if (id == 4) {
                    intent = new Intent(TravelActivity.this, TransportationActivity.class);
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
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    };
}