package com.example.sprint1.view;

import com.example.sprint1.viewmodel.LogisticsViewModel;
import androidx.lifecycle.ViewModelProvider;


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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class LogisticsChart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics_chart);

        LogisticsViewModel viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drawGraph(viewModel);

        ImageButton btnLogistics = findViewById(R.id.btn_logistics);
        ImageButton btnAccom = findViewById(R.id.btn_accom);
        ImageButton btnDest = findViewById(R.id.btn_dest);
        ImageButton btnDining = findViewById(R.id.btn_dining);
        ImageButton btnTransport = findViewById(R.id.btn_transport);
        ImageButton btnTravel = findViewById(R.id.btn_travel);

        btnLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        btnDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, DestinationsActivity.class);
                startActivity(intent);
            }
        });

        btnDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        btnAccom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, TravelActivity.class);
                startActivity(intent);
            }
        });

        btnTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsChart.this, TransportationActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
     * Add Logistics ViewModel?
     * Add allotted entry
     * (Allotted trip time is the total time that the user has to schedule trips in.)
     * Add planned entry

     */
    public void drawGraph(LogisticsViewModel viewModel) {
        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(viewModel.getAllottedTime().getValue(), "Alloted Time"));
        entries.add(new PieEntry(viewModel.getPlannedTime().getValue(), "Planned Time"));
        PieDataSet dataSet = new PieDataSet(entries, "Allotted v. Planned");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
    }
}