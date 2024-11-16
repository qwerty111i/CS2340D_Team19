package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityLogisticsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprint1.viewmodel.LogisticsViewModel;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity
        implements SelectTripInviteDialog.TripSelectionListener {
    private TabLayout tabLayout;
    private LogisticsViewModel viewModel;
    private Button createTrip;
    private Button inviteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityLogisticsBinding binding = ActivityLogisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        // Initialize UI elements
        Button btnAllotted = binding.allottedVsPlanned;

        // Observe invited users LiveData
        viewModel.getInvitedUsersLiveData().observe(this, invitedUsers -> {
            loadInvitedUsers(invitedUsers, binding);
            LinearLayout invitedUsersContainer = binding.invitedUsersContainer;
            invitedUsersContainer.removeAllViews(); // Clear previous views
            for (String email : invitedUsers) {
                View invitedUserCard = createInvitedUserCard(email);
                invitedUsersContainer.addView(invitedUserCard);
            }
        });

        // New Trip button
        createNewTrip(binding);

        // Button click listeners
        btnAllotted.setOnClickListener(v -> {
            LogisticsChartDialog logisticsChartDialog = new LogisticsChartDialog();
            logisticsChartDialog.show(getSupportFragmentManager(), "LogisticsChart");
        });

        // Navigation bar
        tabLayout = binding.tabNavigation;
        navigation();
        chooseTrip(binding);

    }

    @Override
    public void onTripSelected(String selectedTrip) {
        viewModel.getUsersLiveData().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                showInviteDialog(users, selectedTrip);
            } else {
                Toast.makeText(LogisticsActivity.this,
                        "No users found to invite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void chooseTrip(ActivityLogisticsBinding binding) {
        createTrip = binding.buttonNotes;

        inviteButton = binding.buttonInvite;
        inviteButton.setOnClickListener(v -> {
            SelectTripInviteDialog dialog = new SelectTripInviteDialog();
            dialog.show(getSupportFragmentManager(), "Invite trip");
        });

        createTrip.setOnClickListener(v -> {
            SelectTripViewNoteDialog dialog = new SelectTripViewNoteDialog();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });


    }

    public void createNewTrip(ActivityLogisticsBinding binding) {
        createTrip = binding.addTrip;

        createTrip.setOnClickListener(v -> {
            LogTripDialog dialog = new LogTripDialog();
            dialog.show(getSupportFragmentManager(), "Create New Trip");
        });
    }

    private View createInvitedUserCard(String email) {
        // Create a new LinearLayout as a container
        LinearLayout cardLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 125

        );
        layoutParams.setMargins(5, 15, 5, 15);

        cardLayout.setLayoutParams(layoutParams);
        cardLayout.setBackgroundResource(R.drawable.entry_rectangle_card);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create a TextView for the user's email
        TextView emailTextView = new TextView(this);
        emailTextView.setText(email);
        emailTextView.setTextSize(18);
        emailTextView.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));
        emailTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
        emailTextView.setPadding(30, 25, 0, 0);

        // Add the email TextView to the card layout
        cardLayout.addView(emailTextView);

        return cardLayout;
    }



    private void showInviteDialog(List<String> userList, String selectedUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select users to invite");

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        List<String> filteredUserList = new ArrayList<>(); //Remove current User
        for (String email : userList) {
            if (!email.equals(currentUserEmail)) {
                filteredUserList.add(email);
            }
        }

        String[] emails = filteredUserList.toArray(new String[0]);
        boolean[] checkedItems = new boolean[filteredUserList.size()];

        builder.setMultiChoiceItems(emails, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Invite", (dialog, id) -> {
            ArrayList<String> selectedUsers = new ArrayList<>();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selectedUsers.add(filteredUserList.get(i));
                }
            }
            viewModel.inviteUsers(selectedUsers, LogisticsActivity.this, selectedUser);
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadInvitedUsers(List<String> invitedUsers, ActivityLogisticsBinding binding) {
        LinearLayout invitedUsersContainer = binding.invitedUsersContainer;
        invitedUsersContainer.removeAllViews(); // Clear previous views

        for (String invitedUserEmail : invitedUsers) {
            View invitedUserCard = createInvitedUserCard(invitedUserEmail);
            invitedUsersContainer.addView(invitedUserCard);
        }
    }

    //NOTES PART
    private void showNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Notes");

        // Set up a LinearLayout to hold the notes and input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input field for adding a new note
        final EditText input = new EditText(this);
        input.setHint("Enter a new note");
        layout.addView(input);

        // Create a LinearLayout to hold the list of notes
        final LinearLayout notesContainer = new LinearLayout(this);
        notesContainer.setOrientation(LinearLayout.VERTICAL);
        layout.addView(notesContainer);

        // Observe notes LiveData
        viewModel.getNotesLiveData().observe(this, notes -> {
            notesContainer.removeAllViews(); // Clear existing views

            for (String note : notes) {
                appendNoteToContainer(notesContainer, note);
            }
        });

        // Retrieve notes from Firebase
        viewModel.retrieveNotes(); // Make sure to call this to populate the list initially
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            if (note.isEmpty()) {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void appendNoteToContainer(LinearLayout notesContainer, String note) {
        TextView noteTextView = new TextView(this);
        noteTextView.setText(note);
        noteTextView.setPadding(20, 10, 20, 10);
        noteTextView.setTextSize(16);
        noteTextView.setTextColor(ContextCompat.getColor(this, R.color.black));

        // Add a long-click listener to remove the note
        noteTextView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        viewModel.removeNote(note); // Remove note from ViewModel
                        viewModel.removeNoteFromFirebase(note); // Remove from Firebase
                        notesContainer.removeView(noteTextView); // Remove from UI
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
        notesContainer.addView(noteTextView);
    }

    private void navigation() {
        boolean checkSelected = false;
        int[] navIcons = {R.drawable.logistics, R.drawable.destination, R.drawable.dining,
            R.drawable.accommodation, R.drawable.transport, R.drawable.travel };

        for (int i = 0; i < navIcons.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();

            checkSelected = i == 0;

            tab.setIcon(navIcons[i]);
            tabLayout.addTab(tab, checkSelected);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                int id = tab.getPosition();

                if (id == 1) {
                    intent = new Intent(LogisticsActivity.this, DestinationsActivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    intent = new Intent(LogisticsActivity.this, DiningActivity.class);
                    startActivity(intent);
                } else if (id == 3) {
                    intent = new Intent(LogisticsActivity.this, AccommodationsActivity.class);
                    startActivity(intent);
                } else if (id == 4) {
                    intent = new Intent(LogisticsActivity.this, TransportationActivity.class);
                    startActivity(intent);
                } else if (id == 5) {
                    intent = new Intent(LogisticsActivity.this, TravelActivity.class);
                    startActivity(intent);
                }

                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ImageView tabIcon = tabView.findViewById(R.id.tab_navigation);
                    tabIcon.setColorFilter(getResources().getColor(R.color.light_modern_purple));
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ImageView tabIcon = tabView.findViewById(R.id.tab_navigation);
                    tabIcon.clearColorFilter();
                }
            }
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    };
}
