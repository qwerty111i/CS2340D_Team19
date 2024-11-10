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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityAccommodationsBinding;
import com.example.sprint1.model.Accommodation;
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

import java.util.ArrayList;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AccommodationViewModel viewModel;
    private AccommodationAdapter adapter;
    private List<Accommodation> accommodations = new ArrayList<>();
    private String currentEmail;

    //Initialize Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityAccommodationsBinding binding =
                ActivityAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        fetchAccommodations();

        //Create adapter (AFTER pulling data from firebase)
        adapter = new AccommodationAdapter(this, accommodations);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void fetchAccommodations() {
        DatabaseReference accommodationDatabaseRef = databaseRef.child("users");

        accommodationDatabaseRef.orderByChild("email").equalTo(currentEmail).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list of accommodations to avoid duplication
                        accommodations.clear();

                        for (DataSnapshot accommodationsSnapshot : snapshot.getChildren()) {
                            addAccommodationsToList(accommodationsSnapshot.child("accommodations"));
                        }

                        //verify data retrieval
                        for(int i = 0; i < accommodations.size(); i++){
                            Log.d("Firebase", "Check in: " + accommodations.get(i).getCheckIn());
                            Log.d("Firebase", "Check out: " + accommodations.get(i).getCheckOut());
                            Log.d("Firebase", "RoomType: " + accommodations.get(i).getRoomType());
                            Log.d("Firebase", "Location: " + accommodations.get(i).getLocation());
                            Log.d("Firebase", "Num Rooms: " + accommodations.get(i).getNumRooms());
                            //check in, checkout, location, num of rooms, room type
                        }
                        // Notify adapter
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Firebase", "Error retrieving data");

                    }
                });
    }

    public void addAccommodationsToList(DataSnapshot accommodationsSnapshot) {
        //Loop through each accommodation
        for (DataSnapshot snapshot : accommodationsSnapshot.getChildren()) {

            String checkIn = snapshot.child("checkIn").getValue(String.class);
            String checkOut = snapshot.child("checkOut").getValue(String.class);
            int numRooms = snapshot.child("numRooms").getValue(int.class);
            String roomType = snapshot.child("roomType").getValue(String.class);
            String location = snapshot.child("location").getValue(String.class);

            Accommodation accommodation = new Accommodation(checkIn, checkOut, location, numRooms, roomType);
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



}