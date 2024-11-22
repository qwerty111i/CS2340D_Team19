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
import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.viewmodel.DiningAdapter;
import com.example.sprint1.viewmodel.DiningViewModel;
import com.example.sprint1.viewmodel.ReservationSorter;
import com.example.sprint1.viewmodel.SortByDateAndTime;
import com.example.sprint1.viewmodel.SortByName;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class DiningActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private DiningViewModel viewModel;
    private TabLayout tabs;
    private Button createTrip;

    private List<ReservationDetails> reservations = new ArrayList<>();
    private ReservationSorter reservationSorter = new ReservationSorter();

    private List<String> shared = new ArrayList<>();
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

        // Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentEmail = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentEmail);
        }

        reservations = new ArrayList<>();

        // Connect adapter to RecyclerView
        RecyclerView recyclerView = binding.logRecycler;
        adapter = new DiningAdapter(shared, tripNames, names, locations, websites, dates, times);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addSortingTabs(binding);

        // Add Reservation Feature
        addNewReservation(binding);

        // Pulling data for log entries from Firebase
        getDiningDetails(currentEmail);
        chooseTrip(binding);

        // Add navigation bar
        tabLayout = binding.tabNavigation;
        navigation();
    }

    public void chooseTrip(ActivityDiningBinding binding) {
        createTrip = binding.buttonNotes;

        createTrip.setOnClickListener(v -> {
            SelectTripAddNoteDialog dialog = new SelectTripAddNoteDialog();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });
    }

    public void addSortingTabs(ActivityDiningBinding binding) {
        tabs = binding.sortOptions;

        tabs.addTab(tabs.newTab().setText("Date"));
        tabs.addTab(tabs.newTab().setText("Name"));

        tabs.selectTab(tabs.getTabAt(0));

        // initial sort by Date
        sortReservationsByDate();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();
                if (tabText.equals("Date")) {
                    sortReservationsByDate();
                } else if (tabText.equals("Name")) {
                    sortReservationsByName();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();
                if (tabText.equals("Date")) {
                    sortReservationsByDate();
                } else if (tabText.equals("Name")) {
                    sortReservationsByName();
                }
            }
        });
    }

    private void sortReservationsByDate() {
        reservationSorter.setStrategy(new SortByDateAndTime());
        reservationSorter.sortReservations(reservations);

        rebuildSeparateLists();

        adapter.notifyDataSetChanged();
    }

    private void sortReservationsByName() {
        reservationSorter.setStrategy(new SortByName());
        reservationSorter.sortReservations(reservations);

        rebuildSeparateLists();

        adapter.notifyDataSetChanged();
    }

    private void rebuildSeparateLists() {
        shared.clear();
        tripNames.clear();
        names.clear();
        locations.clear();
        websites.clear();
        dates.clear();
        times.clear();

        for (ReservationDetails reservation : reservations) {
            shared.add(reservation.getTripName());
            tripNames.add(reservation.getTripName());
            names.add(reservation.getName());
            locations.add(reservation.getLocation());
            websites.add(reservation.getWebsite());
            dates.add(reservation.getDate());
            times.add(reservation.getTime());
        }
    }

    public void addNewReservation(ActivityDiningBinding binding) {
        binding.addReservation.setOnClickListener(v -> {
            AddReservationDialog dialog = new AddReservationDialog();
            dialog.show(getSupportFragmentManager(), "AddReservationDialog");
        });
    }

    private void getDiningDetails(String email) {
        DatabaseReference usersRef = databaseRef.child("users");

        // Finds the user using their email
        usersRef.orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        // Clears the lists to avoid duplication
                        shared.clear();
                        reservations.clear();
                        tripNames.clear();
                        names.clear();
                        locations.clear();
                        websites.clear();
                        dates.clear();
                        times.clear();

                        if (!userSnapshot.exists()) {
                            Log.d("Firebase", "No user found with email: " + email);
                            return;
                        }

                        // Snapshot of the user with the associated email ID
                        DataSnapshot userData = userSnapshot.getChildren().iterator().next();
                        String userId = userData.getKey();

                        DatabaseReference tripsRef = usersRef.child(userId).child("Trips");

                        // Retrieves all trips made by the user
                        tripsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                                reservations.clear();

                                // Returns if no trips exist
                                if (!tripsSnapshot.exists()) {
                                    Log.d("Firebase", "No trips found for: " + userId);
                                    return;
                                }

                                // Iterate through each existing trip
                                for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                                    // Gets the trip ID
                                    String tripId = tripSnapshot.getKey();
                                    String tripName = tripSnapshot.child("tripName")
                                            .getValue(String.class);

                                    // Currently at users/userId/"Trips"/tripId/"Reservation Details"
                                    DatabaseReference reservationDetailsRef = tripsRef
                                            .child(tripId)
                                            .child("Reservation Details");

                                    // Adds the reservation details to the list
                                    reservationDetailsRef.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(
                                                        @NonNull DataSnapshot reservationSnapshot) {
                                                    addReservationToList(reservationSnapshot, tripName);
                                                    adapter.notifyDataSetChanged();
                                                    rebuildSeparateLists();

                                                    TabLayout.Tab selectedTab = tabs.getTabAt(tabs.getSelectedTabPosition());
                                                    if (selectedTab != null) {
                                                        String tabText = selectedTab.getText().toString();
                                                        if (tabText.equals("Date")) {
                                                            sortReservationsByDate();
                                                        } else if (tabText.equals("Name")) {
                                                            sortReservationsByName();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(
                                                        @NonNull DatabaseError error) {
                                                    Log.d("Firebase",
                                                            "Error retrieving reservation "
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

    private void addReservationToList(DataSnapshot reservationDetailsSnapshot, String tripName) {
        for (DataSnapshot snapshot : reservationDetailsSnapshot.getChildren()) {
            String name = snapshot.child("name").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);
            String website = snapshot.child("website").getValue(String.class);
            String date = snapshot.child("date").getValue(String.class);
            String time = snapshot.child("time").getValue(String.class);

            ReservationDetails reservation = new ReservationDetails(
                    name,
                    location,
                    website,
                    date,
                    time,
                    tripName
            );

            reservations.add(reservation);
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
    }
}
