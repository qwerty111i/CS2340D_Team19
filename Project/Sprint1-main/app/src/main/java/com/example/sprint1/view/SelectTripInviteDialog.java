package com.example.sprint1.view;

import android.app.Dialog;
import android.content.Context;
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
import com.example.sprint1.databinding.DialogTripSelectorBinding;
import com.example.sprint1.viewmodel.LogisticsViewModel;
import java.util.ArrayList;

public class SelectTripInviteDialog extends DialogFragment {

    private LogisticsViewModel viewModel;
    private DialogTripSelectorBinding binding;
    private AutoCompleteTextView tripDropDown;
    private Button submitButton;
    private String selectedTrip;
    private ArrayList<String> updatedTripList;
    private TripSelectionListener listener;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = DialogTripSelectorBinding.inflate(inflater, container, false);

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        updatedTripList = new ArrayList<>();

        // Binds components and validates submit button
        startDialog();

        // Observes changes in the Live Data
        observers();

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
            int height = (int) (metrics.heightPixels * 0.35);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripSelectionListener) {
            listener = (TripSelectionListener) context;
        }
    }

    private void startDialog() {
        // Binds the variables to the proper xml components
        tripDropDown = binding.dropdown;
        submitButton = binding.submit;

        // Sets the list of trips
        viewModel.setDropdownItems();

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripSelected(selectedTrip); // Pass selectedTrip to the listener
            }
            dismiss();
        });

    }

    private void setSelectedTrip() {
        tripDropDown.setOnItemClickListener((parentView, view, position, id) -> {
            selectedTrip = parentView.getItemAtPosition(position).toString();
        });
    }

    private void observers() {
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

    public interface TripSelectionListener {
        void onTripSelected(String selectedTrip);
    }


}