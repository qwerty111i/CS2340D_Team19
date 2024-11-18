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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.R;
import com.example.sprint1.databinding.DialogAddTravelFormEntryBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.TravelViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class TravelFormEntryDialog extends DialogFragment {

    private TravelViewModel viewModel;
    private DialogAddTravelFormEntryBinding binding;
    private Button submitButton;
    private TextInputLayout startDate;
    private TextInputLayout endDate;
    private TextInputLayout destination;
    private TextInputLayout accommodation;
    private TextInputLayout dining;
    private TextInputLayout rating;
    private TextInputEditText startDateText;
    private TextInputEditText endDateText;
    private TextInputEditText destinationText;
    private TextInputEditText accommodationText;
    private TextInputEditText diningText;
    private TextInputEditText ratingText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = DialogAddTravelFormEntryBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(TravelViewModel.class);

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
            int width = (int) (metrics.widthPixels * 0.8);
            int height = (int) (metrics.heightPixels * 0.8);

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
        startDate = binding.startDateView;
        startDateText = binding.startDateText;

        endDate = binding.endDateView;
        endDateText = binding.endDateText;

        destination = binding.destinationView;
        destinationText = binding.destinationText;

        accommodation = binding.accommodationView;
        accommodationText = binding.accommodationText;

        dining = binding.diningView;
        diningText = binding.diningText;

        rating = binding.ratingView;
        ratingText = binding.ratingText;

        submitButton = binding.submit;

        // Calls the date picker dialog when clicked
        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String startDateText = this.startDateText.getText().toString();
            String endDateText = this.endDateText.getText().toString();
            String destinationText = this.destinationText.getText().toString();
            String accommodationText = this.accommodationText.getText().toString();
            String diningText = this.diningText.getText().toString();
            String ratingText = this.ratingText.getText().toString();

//            // Does calculations in the View Model
//            viewModel.calculateVacationTime(durationText, startDateText, endDateText);

            if (viewModel.areInputsValid().getValue()) {
                // Pop up message
//                viewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
//                    if (message != null) {
//                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
//                    }
//                });

                // Saves details in the database
                viewModel.saveTravelDetails();

                // Closes the dialog
                dismiss();
            }
        });
    }

    private void observers() {
        viewModel.getStartDate().observe(this, d -> {
            if (d != null) {
                startDateText.setText(d);
            }
        });

        viewModel.getEndDate().observe(this, d -> {
            if (d != null) {
                endDateText.setText(d);
            }
        });
        viewModel.getAccommodation().observe(this, d -> {
            if (d != null) {
                accommodationText.setText(d);
            }
        });

        viewModel.getDestination().observe(this, d -> {
            if (d != null) {
                destinationText.setText(d);
            }
        });
        viewModel.getDining().observe(this, d -> {
            if (d != null) {
                diningText.setText(d);
            }
        });

        viewModel.getRating().observe(this, d -> {
            if (d != null) {
                ratingText.setText(d);
            }
        });

        // Observing Start Date Error
        viewModel.getStartDateError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                startDate.setError(errorMessage);
            } else {
                startDate.setError(null);
            }
        });

        // Observing End Date Error
        viewModel.getEndDateError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                endDate.setError(errorMessage);
            } else {
                endDate.setError(null);
            }
        });

        // Observing Destination Error
        viewModel.getDestinationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                destination.setError(errorMessage);
            } else {
                destination.setError(null);
            }
        });

        // Observing Accommodation Error
        viewModel.getAccommodationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                accommodation.setError(errorMessage);
            } else {
                accommodation.setError(null);
            }
        });

        // Observing Dining Error
        viewModel.getDiningError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                dining.setError(errorMessage);
            } else {
                dining.setError(null);
            }
        });

        // Observing Rating Error
        viewModel.getRatingError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                rating.setError(errorMessage);
            } else {
                rating.setError(null);
            }
        });

    }

    private void textWatchers() {
        // Checks if duration text field is edited after error is shown
        durationText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                durationText.setError(null);
            }
        });
    }
}