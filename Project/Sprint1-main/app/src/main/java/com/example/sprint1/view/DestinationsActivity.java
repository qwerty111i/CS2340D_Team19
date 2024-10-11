package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityDestinationsBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;

public class DestinationsActivity extends AppCompatActivity {

    private DestinationsViewModel viewModel;
    private Button logTravelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityDestinationsBinding binding = ActivityDestinationsBinding.inflate(getLayoutInflater());
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
    }

    public void logTravel(ActivityDestinationsBinding binding) {
        logTravelBtn = binding.logTravel;

        logTravelBtn.setOnClickListener(v -> {
            LogTravelDialog dialog = new LogTravelDialog();
            dialog.show(getSupportFragmentManager(), "LogTravelDialog");
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