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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.databinding.ActivityLogAccommodationDialogBinding;
import com.example.sprint1.viewmodel.AccommodationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class LogAccommodationDialog extends DialogFragment {

    private ActivityLogAccommodationDialogBinding binding;
    private AccommodationViewModel viewModel;
    private Button submitButton;
    private TextInputLayout location;
    private TextInputLayout checkInDate;
    private TextInputLayout checkOutDate;
    private TextInputLayout numberOfRooms;
    private TextInputLayout roomType;
    private TextInputEditText locationText;
    private TextInputEditText checkInDateText;
    private TextInputEditText checkOutDateText;
    private AutoCompleteTextView numberOfRoomsText;
    private AutoCompleteTextView roomTypeText;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        binding = ActivityLogAccommodationDialogBinding .inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
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
                            (selectedMonth + 1) + "/" + selectedDay + "/" + (selectedYear % 100);

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

        checkInDate = binding.startDateView;
        checkInDateText = binding.startDateText;

        checkOutDate = binding.endDateView;
        checkOutDateText = binding.endDateText;

        numberOfRooms = binding.numberOfRooms;
        numberOfRoomsText = binding.numberOfRoomsText;

        roomType = binding.roomType;
        roomTypeText = binding.roomTypeText;

        submitButton = binding.submit;

        AutoCompleteTextView roomTypeView = binding.roomTypeText;
        String[] roomTypes = {"Single Room", "Double Room", "Suite", "Family Room", "Penthouse"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, roomTypes);
        roomTypeView.setAdapter(adapter1);

        AutoCompleteTextView numberOfRoomsView = binding.numberOfRoomsText;
        String[] roomOptions = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, roomOptions);
        numberOfRoomsView.setAdapter(adapter2);

        // Calls the date picker dialog when clicked
        checkInDateText.setOnClickListener(v -> showDatePickerDialog(checkInDateText));
        checkOutDateText.setOnClickListener(v -> showDatePickerDialog(checkOutDateText));

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String locationText = this.locationText.getText().toString();
            String checkInDateText = this.checkInDateText.getText().toString();
            String checkOutDateText = this.checkOutDateText.getText().toString();
            String numberOfRoomsString = this.numberOfRoomsText.getText().toString();
            int numberOfRooms = 0;
            try {
                numberOfRooms = Integer.parseInt(numberOfRoomsString);
            } catch (NumberFormatException e) {
                this.numberOfRoomsText.setError("Please enter a valid number of rooms");
                return;
            }

            String roomTypeText = this.roomTypeText.getText().toString();

            viewModel.setAccommodationDetails(locationText, checkInDateText,
                    checkOutDateText, numberOfRooms, roomTypeText);

            if (viewModel.areInputsValid().getValue()) {
                viewModel.saveDetails();
                dismiss();
            }
        });
    }
    private void observers() {
        viewModel.getInputError().observe(this, errorMessage -> {
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
                checkInDate.setError(errorMessage);
                checkOutDate.setError(errorMessage);
            } else {
                checkInDate.setError(null);
                checkOutDate.setError(null);
            }
        });
    }

    private void textWatchers() {
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
        numberOfRoomsText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                numberOfRoomsText.setError(null);
            }
        });
        roomTypeText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                roomType.setError(null);
            }
        });
    }
}
