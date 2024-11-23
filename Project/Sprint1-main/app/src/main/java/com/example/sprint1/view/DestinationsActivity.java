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
import com.example.sprint1.model.TravelDetails;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.LogisticsViewModel;
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
    private LogisticsViewModel logisticsViewModel;

    // Create local lists for data pulled from Firebase
    private List<String> sharedNames = new ArrayList<>();
    private List<String> tripNames = new ArrayList<>();
    private List<String> startDates = new ArrayList<>();
    private List<String> endDates = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    private List<String> days = new ArrayList<>();
    private String currentEmail;

    // Initialize Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    private TravelAdapter adapter;
    private Button createTrip;

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

        // Log Travel Feature
        logTravel(binding);

        // Calculate Vacation Time Feature
        calculateVacation(binding);

        // Connect adapter to Recycler View
        RecyclerView recyclerView = binding.logRecycler;
        adapter = new TravelAdapter(sharedNames, tripNames, locations, days,
                startDates, endDates);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pulling data for log entries from Firebase
        getTravelDetails(currentEmail);
        chooseTrip(binding);

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();
    }

    public void chooseTrip(ActivityDestinationsBinding binding) {
        createTrip = binding.buttonNotes;

        createTrip.setOnClickListener(v -> {
            SelectTripAddNoteDialog dialog = new SelectTripAddNoteDialog();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });
    }

    public void logTravel(ActivityDestinationsBinding binding) {
        logTravelBtn = binding.logTravel;

        logTravelBtn.setOnClickListener(v -> {
            AddTravelDialog dialog = new AddTravelDialog();
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

    private void getTravelDetails(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Finds the user using their email
        usersRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                // Clears the lists to avoid duplication
                tripNames.clear();
                locations.clear();
                startDates.clear();
                endDates.clear();
                days.clear();

                if (!userSnapshot.exists()) {
                    Log.d("Firebase", "No user found with email: " + email);
                    return;
                }

                // Snapshot of the user with the associated email ID
                DataSnapshot userData = userSnapshot.getChildren().iterator().next();
                String userId = userData.getKey();

                // Reference at user/userId/"Trips"
                DatabaseReference tripsRef = usersRef.child(userId).child("Trips");

                // Retrieves all trips made by the user
                tripsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                        sharedNames.clear();
                        tripNames.clear();
                        locations.clear();
                        startDates.clear();
                        endDates.clear();
                        days.clear();

                        // Returns if no trips exist
                        if (!tripsSnapshot.exists()) {
                            Log.d("Firebase", "No trips found for: " + userId);
                            return;
                        }

                        // Iterate through each existing trip
                        for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                            // Gets the trip ID
                            String tripId = tripSnapshot.getKey();

                            // Currently at users/userId/"Trips"/tripId/"Travel Details"
                            DatabaseReference travelDetailsRef = tripsRef.child(tripId).child("Travel Details");

                            // Adds the travel details to the lists
                            travelDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot travelSnapshot) {
                                    addTravelToLists(travelSnapshot);
                                    getAllDuration(startDates, endDates);

                                    // Log data
                                    Log.d("Firebase", "Start Dates: " + startDates);
                                    Log.d("Firebase", "End Dates: " + endDates);
                                    Log.d("Firebase", "Locations: " + locations);

                                    // Notify adapter
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("Firebase", "Error retrieving "
                                            + "travel details for tripId: " + tripId);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Firebase", "Error retrieving trips for userId: " + userId);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Error retrieving user data");
            }
        });
    }

    private void addTravelToLists(DataSnapshot travelDetailsSnapshot) {
        // Loop through each travel detail
        for (DataSnapshot snapshot : travelDetailsSnapshot.getChildren()) {
            Log.d("fore", snapshot.getValue().toString());
            String startDate = snapshot.child("startDate").getValue(String.class);
            //Log.d("foeeeeeeeere", startDate);
            String endDate = snapshot.child("endDate").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);
            String tripName = snapshot.child("tripName").getValue(String.class);

            if (startDate != null) {
                startDates.add(startDate);
            }

            if (endDate != null) {
                endDates.add(endDate);
            }

            if (location != null) {
                locations.add(location);
            }

            if (tripName != null) {
                tripNames.add(tripName);
            }

            if (sharedNames != null) {
                sharedNames.add(tripName);
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