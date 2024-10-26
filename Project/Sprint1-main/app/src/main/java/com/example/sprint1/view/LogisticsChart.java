package com.example.sprint1.view;

import com.example.sprint1.databinding.ActivityLogisticsChartBinding;
import com.example.sprint1.viewmodel.LogisticsViewModel;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprint1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogisticsChart extends DialogFragment {
    private ActivityLogisticsChartBinding binding;
    private LogisticsViewModel viewModel;


    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityLogisticsChartBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Binding the ViewModel
        binding.setVariable();
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Gets the dialog component
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Sets the background color of the dialog as transparent
            // Necessary in order to achieve rounded corners
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Gets the metrics of the current display
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // Sets the values of width and height based on the device's screen
            int width = (int) (metrics.widthPixels * 0.9);
            int height = (int) (metrics.heightPixels * 0.6);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height); // Set desired size here
        }
    }

    public void drawGraph(View view) {
        PieChart pieChart = view.findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();

        // Use the ViewModel to get data
        entries.add(new PieEntry(Objects.requireNonNullElse(viewModel.getAllottedTime().getValue(), 0), "Allotted Time"));
        entries.add(new PieEntry(Objects.requireNonNullElse(viewModel.getPlannedTime().getValue(), 0), "Planned Time"));

        PieDataSet dataSet = new PieDataSet(entries, "Allotted vs. Planned");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate(); // Refresh the chart
    }
}
