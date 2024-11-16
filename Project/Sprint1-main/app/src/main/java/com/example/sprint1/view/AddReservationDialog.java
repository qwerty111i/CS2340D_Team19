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

import com.example.sprint1.databinding.DialogAddReservationBinding;
import com.example.sprint1.viewmodel.DiningViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddReservationDialog extends DialogFragment {

    private DiningViewModel viewModel;
    private DialogAddReservationBinding binding;
    private TextInputLayout name;
    private TextInputLayout location;
    private TextInputLayout website;
    private TextInputLayout date;
    private TextInputLayout time;
    private TextInputEditText nameText;
    private TextInputEditText locationText;
    private TextInputEditText websiteText;
    private TextInputEditText dateText;
    private TextInputEditText timeText;
    private AutoCompleteTextView tripDropDown;
    private Button submitButton;
    private String selectedTrip;
    private ArrayList<String> updatedTripList;

    @Override
    public View
        onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = DialogAddReservationBinding.inflate(inflater, container, false);

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
        name = binding.nameView;
        nameText = binding.nameText;

        location = binding.locationView;
        locationText = binding.locationText;

        website = binding.websiteView;
        websiteText = binding.websiteText;

        date = binding.dateView;
        dateText = binding.dateText;

        time = binding.timeView;
        timeText = binding.timeText;

        tripDropDown = binding.dropdown;
        submitButton = binding.submit;

        // Calls the date picker dialog when clicked
        dateText.setOnClickListener(v -> showDatePickerDialog(dateText));

        // Calls the time picker dialog when clicked
        timeText.setOnClickListener(v -> showTimePickerDialog(timeText));

        // Sets the list of trips
        viewModel.setDropdownItems();

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String nameText = this.nameText.getText().toString();
            String locationText = this.locationText.getText().toString();
            String websiteText = this.websiteText.getText().toString();
            String dateText = this.dateText.getText().toString();
            String timeText = this.timeText.getText().toString();
            String currentTripText = selectedTrip;

            // Updates the MutableLiveData in the View Model
            viewModel.setReservationDetails(nameText, locationText,
                    websiteText, dateText, timeText, currentTripText);

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
        // Obtains name error using getNameError in viewModel
        // Updates new variable errorMessage to match the name error
        viewModel.getNameError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                name.setError(errorMessage);
            } else {
                name.setError(null);
            }
        });

        // Obtains location error using getLocationError in viewModel
        // Updates new variable errorMessage to match the location error
        viewModel.getLocationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                location.setError(errorMessage);
            } else {
                location.setError(null);
            }
        });

        // Obtains website error using getWebsiteError in viewModel
        // Updates new variable errorMessage to match the website error
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
                date.setError(errorMessage);
            } else {
                date.setError(null);
            }
        });

        // Obtains time error using getTimeError in viewModel
        // Updates new variable errorMessage to match the time error
        viewModel.getTimeError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                time.setError(errorMessage);
            } else {
                time.setError(null);
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

    private void textWatchers() {
        // Checks if name text field is edited after error is shown
        nameText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                nameText.setError(null);
            }
        });

        // Checks if location text field is edited after error is shown
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