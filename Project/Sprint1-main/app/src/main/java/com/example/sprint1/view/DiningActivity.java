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
import com.example.sprint1.databinding.ActivityDiningBinding;
import com.example.sprint1.viewmodel.DiningAdapter;
import com.example.sprint1.viewmodel.DiningViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiningActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private DiningViewModel viewModel;

    private Button addReservationBtn;
    private Button notesBtn;

    // Create local lists for data pulled from Firebase
    private List<String> tripNames = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    private List<String> websites = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private List<String> times = new ArrayList<>();

    private String currentEmail;

    // Initialize Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    private DiningAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityDiningBinding binding =
                ActivityDiningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        //Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentEmail = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentEmail);
        }

        // Add Reservation Feature
        addNewReservation(binding);

        // Connect adapter to Recycler View
        RecyclerView recyclerView = binding.logRecycler;
        adapter = new DiningAdapter(tripNames, names, locations, websites, dates, times);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pulling data for log entries from Firebase
        getDiningDetails(currentEmail);

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();
    }

    public void addNewReservation(ActivityDiningBinding binding) {
        addReservationBtn = binding.addReservation;

        addReservationBtn.setOnClickListener(v -> {
            AddReservationDialog dialog = new AddReservationDialog();
            dialog.show(getSupportFragmentManager(), "AddReservationDialog");
        });
    }

    private void getDiningDetails(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Finds the user using their email
        usersRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                // Clears the lists to avoid duplication
                names.clear();
                locations.clear();
                websites.clear();
                dates.clear();
                times.clear();
                tripNames.clear();

                // Snapshot of the user with the associated email ID
                DataSnapshot userData = userSnapshot.getChildren().iterator().next();
                String userId = userData.getKey();

                // Reference at user/userId/"Trips"
                DatabaseReference tripsRef = usersRef.child(userId).child("Trips");

                // Retrieves all trips made by the user
                tripsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                        names.clear();
                        locations.clear();
                        websites.clear();
                        dates.clear();
                        times.clear();
                        tripNames.clear();

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
                            DatabaseReference reservationDetailsRef = tripsRef.child(tripId).child("Reservation Details");

                            // Adds the reservation details to the lists
                            reservationDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot reservationSnapshot) {
                                    addReservationToList(reservationSnapshot);

                                    // Log data
                                    Log.d("Firebase", "Names: " + names);
                                    Log.d("Firebase", "Locations: " + locations);
                                    Log.d("Firebase", "Websites: " + websites);
                                    Log.d("Firebase", "Dates: " + dates);
                                    Log.d("Firebase", "Times: " + times);

                                    // Notify adapter
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("Firebase", "Error retrieving reservation details for tripId: " + tripId);
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

    private void addReservationToList(DataSnapshot reservationDetailsSnapshot) {
        // Loop through each travel detail
        for (DataSnapshot snapshot : reservationDetailsSnapshot.getChildren()) {

            String name = snapshot.child("name").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);
            String website = snapshot.child("website").getValue(String.class);
            String date = snapshot.child("date").getValue(String.class);
            String time = snapshot.child("time").getValue(String.class);
            String tripName = snapshot.child("tripName").getValue(String.class);

            if (name != null) {
                names.add(name);
            }

            if (location != null) {
                locations.add(location);
            }

            if (website != null) {
                websites.add(website);
            }

            if (date != null) {
                dates.add(date);
            }

            if (time != null) {
                times.add(time);
            }

            if (tripName != null) {
                tripNames.add(tripName);
            }
        }
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

            checkSelected = i == 2;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();

                if (id == 0) {
                    intent = new Intent(DiningActivity.this, LogisticsActivity.class);
                    startActivity(intent);
                } else if (id == 1) {
                    intent = new Intent(DiningActivity.this, DestinationsActivity.class);
                    startActivity(intent);
                } else if (id == 3) {
                    intent = new Intent(DiningActivity.this, AccommodationsActivity.class);
                    startActivity(intent);
                } else if (id == 4) {
                    intent = new Intent(DiningActivity.this, TransportationActivity.class);
                    startActivity(intent);
                } else if (id == 5) {
                    intent = new Intent(DiningActivity.this, TravelActivity.class);
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