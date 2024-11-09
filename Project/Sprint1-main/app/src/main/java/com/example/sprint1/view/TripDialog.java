package com.example.sprint1.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.databinding.ActivityLogTravelDialogBinding;
import com.example.sprint1.databinding.ActivityTripDialogBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class TripDialog extends DialogFragment {

    private DestinationsViewModel viewModel;
    private ActivityTripDialogBinding binding;
    private Button submitButton;
    private TextInputLayout trip;
    private TextInputEditText tripText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityTripDialogBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // Binds components and validates submit button
        startDialog();

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

    private void startDialog() {
        // Binds the variables to the proper xml components
        trip = binding.tripInput;
        tripText = binding.tripInputText;

        submitButton = binding.submit;

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String tripText = this.tripText.getText().toString();

            // Updates the MutableLiveData in the View Model
            viewModel.saveTrip(tripText);
            dismiss();
        });
    }
}