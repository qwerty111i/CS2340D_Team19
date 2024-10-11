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
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class LogTravelDialog extends DialogFragment {

    private DestinationsViewModel viewModel;
    private ActivityLogTravelDialogBinding binding;
    private Button submitButton;
    private TextInputLayout location;
    private TextInputLayout startDate;
    private TextInputLayout endDate;
    private TextInputEditText locationText;
    private TextInputEditText startDateText;
    private TextInputEditText endDateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityLogTravelDialogBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // Binds components and validates submit button
        startDialog();

        // Observes changes in the Live Data
        observers();

        // Live check to see if inputs are edited after errors
        textWatchers();

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

    private void showDatePickerDialog(TextInputEditText dateInput) {
        // Creates an instance of the calendar to get the current date
        // Used to initialize the date picker with the current date
        final Calendar calendar = Calendar.getInstance();

        // Gets the current year, month, and day
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Creates an instance of the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                // Formats the date as MM/DD/YY
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate =
                            (selectedMonth + 1) + "/" +
                                    selectedDay + "/" + (selectedYear % 100);

                    // Sets the input field to the selected date
                    dateInput.setText(selectedDate);
                },
                year, month, day);

        // Shows the date picker dialog
        datePickerDialog.show();
    }

    private void startDialog() {
        // Binds the variables to the proper xml components
        location = binding.locationView;
        locationText = binding.locationText;

        startDate = binding.startDateView;
        startDateText = binding.startDateText;

        endDate = binding.endDateView;
        endDateText = binding.endDateText;

        submitButton = binding.submit;

        // Calls the date picker dialog when clicked
        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));


        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String locationText = this.locationText.getText().toString();
            String startDateText = this.startDateText.getText().toString();
            String endDateText = this.endDateText.getText().toString();

            // Updates the MutableLiveData in the View Model
            viewModel.setTravelDetails(locationText, startDateText, endDateText);

            if (viewModel.areInputsValid().getValue()) {
                // Saves details in the database
                viewModel.saveDetails();

                // Closes the dialog
                dismiss();
            }
        });
    }

    private void observers() {
        // Obtains location error using getLocationError in viewModel
        // Updates new variable errorMessage to match the location error
        viewModel.getLocationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                location.setError(errorMessage);
            } else {
                location.setError(null);
            }
        });

        // Obtains date error using getDateError in viewModel
        // Updates new variable errorMessage to match the date error
        viewModel.getDateError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startDate.setError(errorMessage);
                endDate.setError(errorMessage);
            } else {
                startDate.setError(null);
                endDate.setError(null);
            }
        });
    }

    private void textWatchers() {
        // Checks if username text field is edited after error is shown
        locationText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                locationText.setError(null);
            }
        });
    }
}