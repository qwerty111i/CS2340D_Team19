package com.example.sprint1.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import android.widget.ArrayAdapter;

import com.example.sprint1.databinding.ActivityNotesPopupDialogCommonBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.LogisticsViewModel;

import java.util.ArrayList;

public class NotesPopupDialogCommon extends DialogFragment {

    private DestinationsViewModel viewModel;
    private ActivityNotesPopupDialogCommonBinding binding;
    private Button submitButton;
    private Spinner tripNameSpinner;
    private String selectedTrip;
    private LogisticsViewModel logisticsViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityNotesPopupDialogCommonBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);



        // Binds components and validates submit button
        startDialog();

        // Populate the Spinner with trips
        populateTripDropdown();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Gets the dialog component
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Sets the background color of the dialog as transparent
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

    private void startDialog() {
        // Binds the variables to the proper xml components
        tripNameSpinner = binding.tripNameSpinner;
        submitButton = binding.submit;

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            // Create the ActualNotesPopup dialog
            ActualNotesPopup dialog = new ActualNotesPopup();

            // Pass the selectedTrip to the next page (ActualNotesPopup)
            Bundle args = new Bundle();
            args.putString("selectedTrip", selectedTrip);  // Set the selected trip as an argument
            dialog.setArguments(args);

            // Show the dialog
            dialog.show(getParentFragmentManager(), "Create New Trip");
            dismiss();
        });

    }

    private void populateTripDropdown() {
        // Get the list of trips from the ViewModel and set it in the Spinner
        ArrayList<String> updatedTripList = new ArrayList<>();

        // Set the trips list in the ViewModel
        viewModel.setDropdownItems();
        viewModel.getTripList().observe(getViewLifecycleOwner(), trips -> {
            updatedTripList.clear();
            updatedTripList.addAll(trips);

            // Populate the Spinner with the trip list
            if (getActivity() != null && getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, updatedTripList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tripNameSpinner.setAdapter(adapter);
                tripNameSpinner.setSelection(0);
            }
        });

        // Set the selected trip when the user selects an item from the Spinner
        tripNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View view, int position, long id) {
                selectedTrip = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });
    }
}
