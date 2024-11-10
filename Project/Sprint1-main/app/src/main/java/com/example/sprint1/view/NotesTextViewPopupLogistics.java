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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityActualNotesPopupBinding;
import com.example.sprint1.databinding.ActivityNotesTextViewPopupLogisticsBinding;
import com.example.sprint1.viewmodel.DestinationsViewModel;
import com.example.sprint1.viewmodel.LogisticsViewModel;

import java.util.List;

public class NotesTextViewPopupLogistics extends DialogFragment {

    private DestinationsViewModel viewModel;
    private ActivityNotesTextViewPopupLogisticsBinding binding;
    private TextView noteText;
    private String selectedTrip;
    private LogisticsViewModel logisticsViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding for the dialog layout
        binding = ActivityNotesTextViewPopupLogisticsBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            selectedTrip = getArguments().getString("selectedTrip");
        }
        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);
        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
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
            int height = (int) (metrics.heightPixels * 0.6);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height); // Set desired size here
        }
    }
    private void startDialog() {
        // Bind the TextView for displaying the notes
        noteText = binding.noteTextView;

        // Fetch notes for the selected trip
        logisticsViewModel.fetchNotesForTrip(selectedTrip);

        // Observe the LiveData for notes and update the TextView when data changes
        logisticsViewModel.getNotesLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> notes) {
                // Concatenate all notes into a single string
                StringBuilder notesString = new StringBuilder();
                for (String note : notes) {
                    notesString.append(note).append("\n\n");
                }

                // Set the concatenated notes to the TextView
                noteText.setText(notesString.toString());
            }
        });
    }
}