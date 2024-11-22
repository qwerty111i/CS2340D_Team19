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
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityTravelBinding;
import com.example.sprint1.model.TravelCommunitySingleton;
import com.example.sprint1.model.TravelFormEntry;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.TravelViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TravelActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TravelViewModel viewModel;
    private Button logTravelFormEntryBtn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference communityDatabaseReference = database.getReference("CommunityPosts");
    private final DatabaseReference userReference = database.getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_travel);

        ActivityTravelBinding binding = ActivityTravelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(TravelViewModel.class);

        // bind the viewmodel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        logTravelFormEntry(binding);

        // Add navigation bar
        tabLayout = findViewById(R.id.tab_navigation);
        navigation();

        //Read from Firebase
        fetchTravelEntries();
    }

    public void logTravelFormEntry(ActivityTravelBinding binding) {
        logTravelFormEntryBtn = binding.logTravelFormEntryDialog;

        logTravelFormEntryBtn.setOnClickListener(v -> {
            TravelFormEntryDialog dialog = new TravelFormEntryDialog();
            dialog.show(getSupportFragmentManager(), "TravelFormEntryDialog");
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

    public void fetchTravelEntries(){
        TravelCommunitySingleton.getInstance().clearEntries(); //clear existing entries
        communityDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int postInt = 0;
               for (DataSnapshot postSnapshot : snapshot.getChildren()){
                   //Extract userID of the community post
                   String userId = postSnapshot.child("userId").getValue(String.class);

                   if(userId != null){
                       //Fetch display name from users node and add to Singleton
                       fetchUserDisplayName(userId);
                   }

                   DataSnapshot tfeSnapshot = postSnapshot.child("tfe");

                   if(tfeSnapshot.exists()){
                       String startDate = tfeSnapshot.child("startDate").getValue(String.class);
                       String endDate = tfeSnapshot.child("endDate").getValue(String.class);
                       String destination = tfeSnapshot.child("destination").getValue(String.class);
                       String accommodation = tfeSnapshot.child("accommodation").getValue(String.class);
                       String dining = tfeSnapshot.child("dining").getValue(String.class);
                       String rating = tfeSnapshot.child("rating").getValue(String.class);

                       //Checking data fetching
                       System.out.println("Fetched: " + startDate);


                       //Create a new TravelFormEntry
                       TravelFormEntry entry = new TravelFormEntry(
                               startDate,endDate,destination,accommodation,dining,rating
                       );

                       //Add entry to Singleton
                       TravelCommunitySingleton.getInstance().addTravelFormEntry(entry);

                       //Checking data fetching
                       List<TravelFormEntry> posts = TravelCommunitySingleton.getInstance().getTravelFormEntries();
                       System.out.println("Fetched: " + posts.get(postInt).getStartDate());

                       postInt =+ 1;


                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("DatabaseError: " + error.getMessage());

            }
        });
    }

    public void fetchUserDisplayName(String userId){
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String displayName = snapshot.child("displayName").getValue(String.class);
                TravelCommunitySingleton.getInstance().addUser(displayName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("DatabaseError: " + error.getMessage());

            }
        });

    }

}