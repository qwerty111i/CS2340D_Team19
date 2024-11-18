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
import com.example.sprint1.databinding.ActivityTransportationBinding;
import com.example.sprint1.model.TransportationDetails;
import com.example.sprint1.viewmodel.AccommodationAdapter;
import com.example.sprint1.viewmodel.TransportationAdapter;
import com.example.sprint1.viewmodel.TransportationViewModel;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransportationActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TabLayout tabs;
    private TransportationViewModel viewModel;
    private TransportationAdapter adapter;
    private List<TransportationDetails> transportations = new ArrayList<>();
    private List<String> shared = new ArrayList<>();
    private String currentEmail;
    private Button createTrip;

    // Initialize Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transportation);

        // Inflating the layout
        ActivityTransportationBinding binding =
                ActivityTransportationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(TransportationViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        addSortingTabs(binding);

        //Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentEmail = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentEmail);
        }

        Button buttonLog = binding.buttonLog;
        buttonLog.setOnClickListener(v -> {
            AddTransportationDialog transportationDialog = new AddTransportationDialog();
            transportationDialog.show(getSupportFragmentManager(), "LogTransportationDialog");
        });

        //Connect recycler view
        RecyclerView recyclerView = binding.accommodationRecycler;

        //Fetch Accommodation entries from firebase
        getTransportationDetails(currentEmail);
        chooseTrip(binding);

        //Create adapter (AFTER pulling data from firebase)
        adapter = new TransportationAdapter(this, transportations, shared);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();
    }

    public void chooseTrip(ActivityTransportationBinding binding) {
        createTrip = binding.buttonNotes;

        createTrip.setOnClickListener(v -> {
            SelectTripAddNoteDialog dialog = new SelectTripAddNoteDialog();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });
    }

    private void getTransportationDetails(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Finds the user using their email
        usersRef.orderByChild("email").equalTo(email).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        // Clears the lists to avoid duplication
                        transportations.clear();
                        shared.clear();

                        // Snapshot of the user with the associated email ID
                        DataSnapshot userData = userSnapshot.getChildren().iterator().next();
                        String userId = userData.getKey();

                        // Reference at user/userId/"Trips"
                        DatabaseReference tripsRef = usersRef.child(userId).child("Trips");

                        // Retrieves all trips made by the user
                        tripsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                                transportations.clear();
                                shared.clear();

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
                                    DatabaseReference accommodationDetailsRef = tripsRef.child(tripId).
                                            child("Transportation Details");

                                    // Adds the reservation details to the lists
                                    accommodationDetailsRef.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(
                                                        @NonNull DataSnapshot accommodationSnapshot) {
                                                    addAccommodationToList(accommodationSnapshot);

                                                    // Notify adapter
                                                    adapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("Firebase",
                                                            "Error retrieving transportation "
                                                                    + "details for tripId: "
                                                                    + tripId);
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

    private void addAccommodationToList(DataSnapshot accommodationDetailsSnapshot) {
        // Loop through each travel detail
        for (DataSnapshot snapshot : accommodationDetailsSnapshot.getChildren()) {
            String type = snapshot.child("type").getValue(String.class);
            String startLocation = snapshot.child("startLocation").getValue(String.class);
            String endLocation = snapshot.child("endLocation").getValue(String.class);
            String startDate = snapshot.child("startDate").getValue(String.class);
            String endDate = snapshot.child("endDate").getValue(String.class);
            String trip = snapshot.child("tripName").getValue(String.class);

            Log.d("Firebase", "Type: " + type);
            Log.d("Firebase", "Start Location: " + startLocation);
            Log.d("Firebase", "End Location: " + endLocation);
            Log.d("Firebase", "Start Date: " + startDate);
            Log.d("Firebase", "End Date: " + endDate);
            Log.d("Firebase", "Trip Name: " + trip);
            //check in, checkout, location, num of rooms, room type

            TransportationDetails transportation = new TransportationDetails(type, startLocation,
                    endLocation, startDate, endDate, trip);
            transportations.add(transportation);
            shared.add(trip);
        }
    }

    public void addSortingTabs(ActivityTransportationBinding binding) {
        tabs = binding.sortOptions;

        tabs.addTab(tabs.newTab().setText("Date"));
        tabs.addTab(tabs.newTab().setText("Name"));

        tabs.selectTab(tabs.getTabAt(0));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();
                if (tabText.equals("Check-In")) {
                    sortAccommodationsByDate();
                } else if (tabText.equals("Check-Out")) {
                    sortAccommodationsByDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();
                if (tabText.equals("Date")) {
                    sortAccommodationsByDate();
                } else if (tabText.equals("Date")) {
                    sortAccommodationsByDate();
                }
            }
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
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void sortAccommodationsByDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

        Collections.sort(transportations, (d1, d2) -> {
            try {
                Date date1 = dateFormat.parse(d1.getStartDate());
                Date date2 = dateFormat.parse(d2.getStartDate());

                if (date1 != null && date2 != null) {
                    return date1.compareTo(date2);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return 0;
        });
        adapter.notifyDataSetChanged();
    }
}