package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityDestinationsBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DestinationsActivity extends AppCompatActivity {

    private DestinationsViewModel viewModel;
    private Button logTravelBtn;
    private Button vacationBtn;


    //Initialize Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference travel_details_ref = database.getReference("travel_details");

    //Create local lists for data pulled from Firebase
    List<String> startDates = new ArrayList<>();
    List<String> endDates = new ArrayList<>();
    List<String> locations = new ArrayList<>();


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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.destinations), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add navigation bar
        navigationBar(binding);

        // Log Travel Feature
        logTravel(binding);

        // Calculate Vacation Time Feature
        calculateVacation(binding);

        //Pulling data for log entries from Firebase
        getTravelDetails();

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


    private void getTravelDetails(){
        travel_details_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot travelSnapshot : snapshot.getChildren()){
                    String startDate = travelSnapshot.child("startDate").getValue(String.class);
                    String endDate = travelSnapshot.child("endDate").getValue(String.class);
                    String location = travelSnapshot.child("location").getValue(String.class);

                    if(startDate != null){
                        startDates.add(startDate);
                    }

                    if(endDate != null){
                        endDates.add(endDate);
                    }

                    if(location != null){
                        locations.add(location);
                    }


                }
                //verify data retrieval
                Log.d("Firebase", "Start Dates: " + startDates);
                Log.d("Firebase", "End Dates: " + endDates);
                Log.d("Firebase", "Locations:" + locations);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Error retrieving data");

            }
        });
    }


    public void navigationBar(ActivityDestinationsBinding binding) {
        ImageButton btnLogistics = binding.btnLogistics;
        ImageButton btnAccom = binding.btnAccom;
        ImageButton btnDest = binding.btnDest;
        ImageButton btnDining = binding.btnDining;
        ImageButton btnTransport = binding.btnTransport;
        ImageButton btnTravel = binding.btnTravel;

        btnLogistics.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        btnDest.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, DestinationsActivity.class);
            startActivity(intent);
        });

        btnDining.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        btnAccom.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        btnTravel.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, TravelActivity.class);
            startActivity(intent);
        });

        btnTransport.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationsActivity.this, TransportationActivity.class);
            startActivity(intent);
        });
    }
}