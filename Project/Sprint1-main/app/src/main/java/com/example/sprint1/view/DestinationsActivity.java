package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityDestinationsBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.TravelAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private DestinationsViewModel viewModel;
    private Button logTravelBtn;
    private Button vacationBtn;
    private Button createTrip;



    //Create local lists for data pulled from Firebase
    private List<String> startDates = new ArrayList<>();
    private List<String> endDates = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    private List<String> days = new ArrayList<>();

    private String currentEmail;

    //Initialize Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    private TravelAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityDestinationsBinding binding =
                ActivityDestinationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        //Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentEmail = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentEmail);
        }

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();

        // Log Travel Feature
        logTravel(binding);

        // Calculate Vacation Time Feature
        calculateVacation(binding);

        // ...
        createNewTrip(binding);

        //connect adapter to Recycler View
        RecyclerView recyclerView = binding.logRecycler;
        adapter = new TravelAdapter(locations, days);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Pulling data for log entries from Firebase
        getTravelDetails(currentEmail);

    }

    public void logTravel(ActivityDestinationsBinding binding) {
        logTravelBtn = binding.logTravel;

        logTravelBtn.setOnClickListener(v -> {
            LogTravelDialog dialog = new LogTravelDialog();
            dialog.show(getSupportFragmentManager(), "LogTravelDialog");
        });
    }

    public void calculateVacation(ActivityDestinationsBinding binding) {
        vacationBtn = binding.buttonVacation;

        vacationBtn.setOnClickListener(v -> {
            CalculateVacationDialog dialog = new CalculateVacationDialog();
            dialog.show(getSupportFragmentManager(), "CalculateVacationDialog");
        });
    }

    public void createNewTrip(ActivityDestinationsBinding binding) {
        createTrip = binding.addTrip;

        createTrip.setOnClickListener(v -> {
            TripDialog dialog = new TripDialog();
            dialog.show(getSupportFragmentManager(), "CalculateVacationDialog");
        });
    }

    private void getTravelDetails(String email) {
        DatabaseReference travelDetailsRef = databaseRef.child("users");

        travelDetailsRef.orderByChild("email").equalTo(email).addValueEventListener(
                new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //clear lists to avoid duplication
                    locations.clear();
                    startDates.clear();
                    endDates.clear();
                    days.clear();

                    for (DataSnapshot travelSnapshot : snapshot.getChildren()) {
                        addTravelToLists(travelSnapshot.child("travelDetails"));
                    }

                    //verify data retrieval
                    Log.d("Firebase", "Start Dates: " + startDates);
                    Log.d("Firebase", "End Dates: " + endDates);
                    Log.d("Firebase", "Locations:" + locations);

                    //Add days of travel to each location to days list
                    getAllDuration(startDates, endDates);

                    //Notify adapter when data has changed
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Firebase", "Error retrieving data");

                }
            });
    }

    private void addTravelToLists(DataSnapshot travelDetailsSnapshot) {
        //Loop through each travel detail
        for (DataSnapshot snapshot : travelDetailsSnapshot.getChildren()) {

            String startDate = snapshot.child("startDate").getValue(String.class);
            String endDate = snapshot.child("endDate").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);

            if (startDate != null) {
                startDates.add(startDate);
            }

            if (endDate != null) {
                endDates.add(endDate);
            }

            if (location != null) {
                locations.add(location);
            }

        }
    }

    public void getAllDuration(List<String> startDates, List<String> endDates) {
        for (int i = 0; i < startDates.size(); i++) {
            String start = startDates.get(i);
            String end = endDates.get(i);

            //Define date format
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

            try {
                //Parse strings to Date objects
                Date startDate = formatter.parse(start);
                Date endDate = formatter.parse(end);

                long durationInMillis = Math.abs(endDate.getTime() - startDate.getTime());

                int durationInDays = (int)
                        TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);

                days.add(durationInDays + " days");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Log.d("Duration", "getAllDuration: " + days);

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

            checkSelected = i == 1;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();

                if (id == 0) {
                    intent = new Intent(DestinationsActivity.this, LogisticsActivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    intent = new Intent(DestinationsActivity.this, DiningActivity.class);
                    startActivity(intent);
                } else if (id == 3) {
                    intent = new Intent(DestinationsActivity.this, AccommodationsActivity.class);
                    startActivity(intent);
                } else if (id == 4) {
                    intent = new Intent(DestinationsActivity.this, TransportationActivity.class);
                    startActivity(intent);
                } else if (id == 5) {
                    intent = new Intent(DestinationsActivity.this, TravelActivity.class);
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