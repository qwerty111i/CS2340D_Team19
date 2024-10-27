package com.example.sprint1.view;

import com.example.sprint1.databinding.ActivityLogisticsChartBinding;
import com.example.sprint1.viewmodel.LogisticsViewModel;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprint1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class LogisticsChart extends DialogFragment {
    private ActivityLogisticsChartBinding binding;
    private LogisticsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        binding = ActivityLogisticsChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
        Dialog dialog = getDialog();
        viewModel.getAllottedTime().observe(
                getViewLifecycleOwner(), allottedTime -> drawGraph(viewModel));
        viewModel.getPlannedTime().observe(
                getViewLifecycleOwner(), plannedTime -> drawGraph(viewModel));
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

    public void drawGraph(LogisticsViewModel viewModel) {
        PieChart pieChart = binding.pieChart;
        List<PieEntry> entries = new ArrayList<>();

        Integer allottedTimeValue = viewModel.getAllottedTime().getValue();
        Integer plannedTimeValue = viewModel.getPlannedTime().getValue();

        // Log the retrieved values
        Log.d("LogisticsChart", "Allotted Time: " + allottedTimeValue);
        Log.d("LogisticsChart", "Planned Time: " + plannedTimeValue);


        // Use the ViewModel to get data
        entries.add(new PieEntry(allottedTimeValue, "Allotted Time"));
        entries.add(new PieEntry(plannedTimeValue, "Planned Time"));

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        data.setValueTextColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.getLegend().setTextColor(Color.WHITE);

        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pieChart.getLegend().setDrawInside(false);
        pieChart.getLegend().setTypeface(ResourcesCompat.getFont(
                getContext(), R.font.poppins_regular));
        pieChart.getLegend().setTextSize(30f);
        pieChart.getLegend().setWordWrapEnabled(true);

        pieChart.setEntryLabelTextSize(20f);
        dataSet.setValueTextSize(15f);
        pieChart.setEntryLabelColor(Color.parseColor("#6A0DAD"));
        dataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(
                ResourcesCompat.getFont(getContext(), R.font.poppins_regular));


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setHoleColor(Color.parseColor("#FF6F61"));

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate(); // Refresh the chart
    }
}
