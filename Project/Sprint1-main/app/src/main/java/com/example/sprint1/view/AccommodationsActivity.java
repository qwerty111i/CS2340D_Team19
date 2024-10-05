package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprint1.R;

public class AccommodationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accommodations), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add navigation bar

        ImageButton btnLogistics = findViewById(R.id.btn_logistics);
        ImageButton btnAccom = findViewById(R.id.btn_accom);
        ImageButton btnDest = findViewById(R.id.btn_dest);
        ImageButton btnDining = findViewById(R.id.btn_dining);
        ImageButton btnTransport = findViewById(R.id.btn_transport);
        ImageButton btnTravel = findViewById(R.id.btn_travel);

        btnLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        btnDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, DestinationsActivity.class);
                startActivity(intent);
            }
        });

        btnDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        btnAccom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
                startActivity(i);
            }
        });

        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, TravelActivity.class);
                startActivity(intent);
            }
        });

        btnTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccommodationsActivity.this, TransportationActivity.class);
                startActivity(i);
            }
        });
    }
}