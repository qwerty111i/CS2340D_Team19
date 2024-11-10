package com.example.sprint1.view;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityNotesPopupDialogCommonBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.LogisticsViewModel;

import java.util.ArrayList;

public class InviteUserSpinnerPopup extends DialogFragment {

    private DestinationsViewModel viewModel;
    private ActivityNotesPopupDialogCommonBinding binding;
    private Button submitButton;
    private Spinner tripNameSpinner;
    private String selectedTrip;
    private LogisticsViewModel logisticsViewModel;
    private TripSelectionListener listener;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripSelectionListener) {
            listener = (TripSelectionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement TripSelectionListener");
        }
    }

    private void startDialog() {
        // Binds the variables to the proper xml components
        tripNameSpinner = binding.tripNameSpinner;
        submitButton = binding.submit;

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripSelected(selectedTrip); // Pass selectedTrip to the listener
            }
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, updatedTripList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tripNameSpinner.setAdapter(adapter);
                tripNameSpinner.setSelection(0);
            }
        });

        // Set the selected trip when the user selects an item from the Spinner
        tripNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                selectedTrip = parentView.getItemAtPosition(position).toString();
                Log.d("SelectedTrip", "Selected Trip: " + selectedTrip);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });
    }
    public interface TripSelectionListener {
        void onTripSelected(String selectedTrip);
    }


}