package com.example.sprint1.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.databinding.DialogAddTransportationBinding;
import com.example.sprint1.viewmodel.TransportationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddTransportationDialog extends DialogFragment {

    private DialogAddTransportationBinding binding;
    private TransportationViewModel viewModel;

    private TextInputLayout type;
    private AutoCompleteTextView typeText;

    private TextInputLayout startLocation;
    private TextInputEditText startLocationText;

    private TextInputLayout endLocation;
    private TextInputEditText endLocationText;

    private TextInputLayout startDate;
    private TextInputEditText startDateText;

    private TextInputLayout startTime;
    private TextInputEditText startTimeText;

    private AutoCompleteTextView tripDropDown;
    private Button submitButton;

    private String selectedTrip;
    private ArrayList<String> updatedTripList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        binding = DialogAddTransportationBinding .inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(TransportationViewModel.class);

        binding.setViewModel(viewModel);

        binding.setLifecycleOwner(this);

        updatedTripList = new ArrayList<>();

        startDialog();

        observers();

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
        type = binding.typeView;
        typeText = binding.typeText;

        startLocation = binding.startLocationView;
        startLocationText = binding.startLocationText;

        endLocation = binding.endLocationView;
        endLocationText = binding.endLocationText;

        startDate = binding.startDateView;
        startDateText = binding.startDateText;

        startTime = binding.startTimeView;
        startTimeText = binding.startTimeText;

        tripDropDown = binding.dropdown;
        submitButton = binding.submit;

        String[] types = {"Car", "Bus", "Train", "Boat", "Flight"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, types);
        typeText.setAdapter(adapter1);

        // Calls the date picker dialog when clicked
        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        startTimeText.setOnClickListener(v -> showTimePickerDialog(startTimeText));

        // Sets the list of trips
        viewModel.setDropdownItems();

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String currentTypeText = this.typeText.getText().toString();
            String currentStartLocationText = this.startLocationText.getText().toString();
            String currentEndLocationText = this.endLocationText.getText().toString();
            String currentStartDateText = this.startDateText.getText().toString();
            String currentStartTimeText = this.startTimeText.getText().toString();
            String currentTripText = selectedTrip;

            viewModel.setTransportationDetails(currentTypeText, currentStartLocationText,
                    currentEndLocationText, currentStartDateText, currentStartTimeText,
                    currentTripText);

            if (viewModel.areInputsValid().getValue()) {
                viewModel.saveTransportationDetails();
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

    private void observers() {
        viewModel.getTypeError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                type.setError(errorMessage);
            } else {
                type.setError(null);
            }
        });

        viewModel.getStartLocationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startLocation.setError(errorMessage);
            } else {
                startLocation.setError(null);
            }
        });

        viewModel.getEndLocationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                endLocation.setError(errorMessage);
            } else {
                endLocation.setError(null);
            }
        });

        viewModel.getStartDateError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startDate.setError(errorMessage);
            } else {
                startDate.setError(null);
            }
        });

        viewModel.getStartTimeError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startTime.setError(errorMessage);
            } else {
                startTime.setError(null);
            }
        });

        // Obtains trip error using getTripError in viewModel
        // Updates new variable errorMessage to match the trip error
        viewModel.getTripError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                tripDropDown.setError(errorMessage);
            } else {
                tripDropDown.setError(null);
            }
        });

        // Observes new trips using getTripList in viewModel
        // Updates the dropdown to add the new trips
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

    private void setSelectedTrip() {
        tripDropDown.setOnItemClickListener((parentView, view, position, id) -> {
            selectedTrip = parentView.getItemAtPosition(position).toString();
        });
    }

    private void textWatchers() {
        startLocationText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                startLocationText.setError(null);
            }
        });

        endLocationText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                endLocationText.setError(null);
            }
        });
    }
}
