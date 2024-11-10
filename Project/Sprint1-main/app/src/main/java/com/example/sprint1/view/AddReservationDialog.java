package com.example.sprint1.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.databinding.ActivityAddReservationDialogBinding;
import com.example.sprint1.viewmodel.DiningViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddReservationDialog extends DialogFragment {

    private DiningViewModel viewModel;
    private ActivityAddReservationDialogBinding binding;
    private TextInputLayout location;
    private TextInputLayout website;
    private TextInputLayout startDate;
    private TextInputLayout endDate;
    private TextInputLayout startTime;
    private TextInputEditText locationText;
    private TextInputEditText websiteText;
    private TextInputEditText startDateText;
    private TextInputEditText endDateText;
    private TextInputEditText startTimeText;
    private AutoCompleteTextView tripDropDown;
    private Button submitButton;
    private String selectedTrip;
    private ArrayList<String> updatedTripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityAddReservationDialogBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        updatedTripList = new ArrayList<>();

        // Binds components and validates dialog
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
            int height = (int) (metrics.heightPixels * 0.85);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height); // Set desired size here
        }
    }

    private void startDialog() {
        // Binds the variables to the proper xml components
        location = binding.locationView;
        locationText = binding.locationText;

        website = binding.websiteView;
        websiteText = binding.websiteText;

        startDate = binding.startDateView;
        startDateText = binding.startDateText;

        endDate = binding.endDateView;
        endDateText = binding.endDateText;

        startTime = binding.startTimeView;
        startTimeText = binding.startTimeText;

        tripDropDown = binding.dropdown;
        submitButton = binding.submit;

        // Calls the date picker dialog when clicked
        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

        // Calls the time picker dialog when clicked
        startTimeText.setOnClickListener(v -> showTimePickerDialog(startTimeText));

        // Sets the list of trips
        viewModel.setDropdownItems();

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String locationText = this.locationText.getText().toString();
            String websiteText = this.websiteText.getText().toString();
            String startDateText = this.startDateText.getText().toString();
            String endDateText = this.endDateText.getText().toString();
            String startTimeText = this.startTimeText.getText().toString();
            String currentTripText = selectedTrip;

            // Updates the MutableLiveData in the View Model
            viewModel.setReservationDetails(locationText,
                    websiteText, startDateText, endDateText, startTimeText, currentTripText);

            if (viewModel.areInputsValid().getValue()) {
                // Saves details in the database
                viewModel.saveReservationsDetails();

                // Closes the dialog
                dismiss();
            }
        });
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
                            (selectedMonth + 1) + "/" + selectedDay + "/" + (selectedYear % 100);

                    // Sets the input field to the selected date
                    dateInput.setText(selectedDate);
                },
                year, month, day);

        // Shows the date picker dialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TextInputEditText timeInput) {
        // Setting default time
        int currentHour = 12;
        int currentMinute = 0;

        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hour, minute) -> {
            // Formats the time in a 12-hour format
            String amPm = (hour < 12) ? "AM" : "PM";
            int hour12 = (hour == 0) ? 12 : (hour > 12) ? (hour - 12) : hour;
            String formattedTime = String.format(Locale.US, "%02d:%02d %s", hour12, minute, amPm);

            // Displays the formatted time
            timeInput.setText(formattedTime);
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), timeSetListener, currentHour, currentMinute, false);

        // Shows the time picker dialog
        timePickerDialog.show();
    }

    private void setSelectedTrip() {
        tripDropDown.setOnItemClickListener((parentView, view, position, id) -> {
            selectedTrip = parentView.getItemAtPosition(position).toString();
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

        // Obtains location error using getLocationError in viewModel
        // Updates new variable errorMessage to match the location error
        viewModel.getWebsiteError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                website.setError(errorMessage);
            } else {
                website.setError(null);
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

        // Obtains date error using getDateError in viewModel
        // Updates new variable errorMessage to match the date error
        viewModel.getTimeError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startTime.setError(errorMessage);
            } else {
                startTime.setError(null);
            }
        });

        viewModel.getTripList().observe(this, trips -> {
            updatedTripList.clear();
            updatedTripList.addAll(trips);

            // Sets the dropdown with the list of trips
            if (getActivity() != null && getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_spinner_item, updatedTripList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tripDropDown.setAdapter(adapter);
                tripDropDown.setSelection(0);

                // Sets the dropdown and selectedTrip with the selected item
                setSelectedTrip();
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

        // Checks if website text field is edited after error is shown
        websiteText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                websiteText.setError(null);
            }
        });
    }
}