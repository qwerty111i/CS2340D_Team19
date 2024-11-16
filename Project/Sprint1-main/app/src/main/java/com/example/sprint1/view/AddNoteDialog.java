package com.example.sprint1.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.R;
import com.example.sprint1.databinding.DialogAddNoteBinding;
import com.example.sprint1.viewmodel.LogisticsViewModel;

public class AddNoteDialog extends DialogFragment {
    private LogisticsViewModel viewModel;
    private DialogAddNoteBinding binding;
    private Button submitButton;
    private EditText noteEditText;
    private String selectedTrip;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the binding for the dialog layout
        binding = DialogAddNoteBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            selectedTrip = getArguments().getString("selectedTrip");
        }

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Binding the ViewModel
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Gets the metrics of the current display
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // Sets the values of width and height based on the device's screen
            int width = (int) (metrics.widthPixels * 0.9);
            int height = (int) (metrics.heightPixels * 0.35);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height); // Set desired size here
        }
    }

    private void startDialog() {
        noteEditText = binding.noteEditText;
        submitButton = binding.submitButton;

        // Called when the Submit button is pressed
        submitButton.setOnClickListener(v -> {
            String noteText = noteEditText.getText().toString().trim();

            // Make sure trip is selected and note is not empty
            if (selectedTrip != null && !noteText.isEmpty()) {
                // Save the note under the selected trip
                viewModel.saveNoteForTrip(selectedTrip, noteText);



                // Dismiss the dialog after saving
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please select a trip and enter a note",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNoteUnderSelectedTrip() {
        // Get the note text from an EditText or other input field
        String noteText = ((EditText) getView().findViewById(R.id.noteEditText)).getText().
                toString();  // Replace with your EditText ID

        if (!noteText.isEmpty() && selectedTrip != null) {
            // Call ViewModel to add the note under the selected trip in the database
            viewModel.addNote(selectedTrip, noteText);

            // Dismiss the dialog after saving the note
            dismiss();
        } else {
            // Show a message if no note text was entered
            Toast.makeText(getContext(), "Please enter a note.", Toast.LENGTH_SHORT).show();
        }
    }
}
