package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityAccommodationsBinding;
import com.example.sprint1.databinding.ActivityDiningBinding;
import com.example.sprint1.model.AccommodationDetails;
import com.example.sprint1.viewmodel.AccommodationAdapter;
import com.example.sprint1.viewmodel.AccommodationViewModel;
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


public class AccommodationsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AccommodationViewModel viewModel;
    private AccommodationAdapter adapter;
    private List<AccommodationDetails> accommodations = new ArrayList<>();
    private String currentEmail;
    private Button createTrip;

    //Initialize Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityAccommodationsBinding binding =
                ActivityAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sort");

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);



        //Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentEmail = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentEmail);
        }

        Button buttonLog = binding.buttonLog;
        binding.buttonLog.setOnClickListener(v -> {
            LogAccommodationDialog accommodationLog = new LogAccommodationDialog();
            accommodationLog.show(getSupportFragmentManager(), "LogAccommodationDialog");
        });

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();

        //Connect recycler view
        RecyclerView recyclerView = findViewById(R.id.accommodationRecycler);

        //Fetch Accommodation entries from firebase
        getAccommodationDetails(currentEmail);
        chooseTrip(binding);

        //Create adapter (AFTER pulling data from firebase)
        adapter = new AccommodationAdapter(this, accommodations);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void chooseTrip(ActivityAccommodationsBinding binding) {
        createTrip = binding.buttonNotes;

        createTrip.setOnClickListener(v -> {
            NotesPopupDialogCommon dialog = new NotesPopupDialogCommon();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });
    }

    private void getAccommodationDetails(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Finds the user using their email
        usersRef.orderByChild("email").equalTo(email).addValueEventListener(
                new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    // Clears the lists to avoid duplication
                    accommodations.clear();

                    // Snapshot of the user with the associated email ID
                    DataSnapshot userData = userSnapshot.getChildren().iterator().next();
                    String userId = userData.getKey();

                    // Reference at user/userId/"Trips"
                    DatabaseReference tripsRef = usersRef.child(userId).child("Trips");

                    // Retrieves all trips made by the user
                    tripsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                            accommodations.clear();

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
                                        child("Accommodation Details");

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

    private void addAccommodationToList(DataSnapshot accommodationDetailsSnapshot) {
        // Loop through each travel detail
        for (DataSnapshot snapshot : accommodationDetailsSnapshot.getChildren()) {
            String checkIn = snapshot.child("checkIn").getValue(String.class);
            String checkOut = snapshot.child("checkOut").getValue(String.class);
            int numRooms = snapshot.child("numRooms").getValue(int.class);
            String roomType = snapshot.child("roomType").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);
            String trip = snapshot.child("tripName").getValue(String.class);

            Log.d("Firebase", "Check in: " + checkIn);
            Log.d("Firebase", "Check out: " + checkOut);
            Log.d("Firebase", "RoomType: " + roomType);
            Log.d("Firebase", "Location: " + location);
            Log.d("Firebase", "Num Rooms: " + numRooms);
            Log.d("Firebase", "Trip Name: " + trip);
            //check in, checkout, location, num of rooms, room type

            AccommodationDetails accommodation = new AccommodationDetails(checkIn, checkOut,
                    location, numRooms, roomType, trip);
            accommodations.add(accommodation);
        }
    }

    private void navigation() {
        boolean checkSelected = false;
        int[] navIcons = {
            R.drawable.logistics, R.drawable.destination, R.drawable.dining,
            R.drawable.accommodation, R.drawable.transport, R.drawable.travel };

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
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_accommodations, menu); //inflate menu xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.accom_sort_CheckIn) {
            sortAccommodationsByCheckIn();
            return true;
        } else if (item.getItemId() == R.id.accom_sort_CheckOut) {
            sortAccommodationsByCheckOut();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sortAccommodationsByCheckIn() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

        Collections.sort(accommodations, (d1, d2) -> {
            try {
                Date date1 = dateFormat.parse(d1.getCheckIn());
                Date date2 = dateFormat.parse(d2.getCheckIn());

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

    private void sortAccommodationsByCheckOut() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

        Collections.sort(accommodations, (d1, d2) -> {
            try {
                Date date1 = dateFormat.parse(d1.getCheckOut());
                Date date2 = dateFormat.parse(d2.getCheckOut());

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