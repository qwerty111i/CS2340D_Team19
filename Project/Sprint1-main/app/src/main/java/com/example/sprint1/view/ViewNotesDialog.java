package com.example.sprint1.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprint1.databinding.DialogViewNotesBinding;
import com.example.sprint1.viewmodel.LogisticsViewModel;

public class ViewNotesDialog extends DialogFragment {
    private LogisticsViewModel viewModel;
    private DialogViewNotesBinding binding;
    private TextView noteText;
    private String selectedTrip;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the binding for the dialog layout
        binding = DialogViewNotesBinding.inflate(inflater, container, false);

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
            int height = (int) (metrics.heightPixels * 0.6);

            // Sets the dialog size
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void startDialog() {
        noteText = binding.noteTextView;

        viewModel.fetchNotesForTrip(selectedTrip);

        viewModel.getNotesLiveData().observe(getViewLifecycleOwner(),
            notes -> {
                StringBuilder notesString = new StringBuilder();
                for (String note : notes) {
                    String dash = "- ";
                    note = dash + note;
                    notesString.append(note).append("\n\n");
                }

                noteText.setText(notesString.toString());
            });
    }
}