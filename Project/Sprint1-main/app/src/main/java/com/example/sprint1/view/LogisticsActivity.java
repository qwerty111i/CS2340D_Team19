package com.example.sprint1.view;

import static com.example.sprint1.BR.viewModel;

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

import com.example.sprint1.model.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprint1.viewmodel.LogisticsViewModel;
import java.util.List;
import java.util.Objects;


public class LogisticsActivity extends AppCompatActivity {
    private TabLayout tabLayout;


    private LogisticsViewModel viewModel;
    private TextView notesTextView;
    //private DatabaseReference databaseReference;
    //private TextView tvInvitedUsers;
    //private TextView notesTextView;
    //private List<String> startDates = new ArrayList<>();
    //private List<String> endDates = new ArrayList<>();
    //private List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityLogisticsBinding binding =
                ActivityLogisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        // Initialize UI elements
        Button btnAlloted = findViewById(R.id.alloted_vs_planned);
        Button notesButton = findViewById(R.id.button_notes);
        notesButton.setOnClickListener(v -> showNotesDialog());

        //loadInvitedUsers();
        //retrieveNotesFromDatabase();
        viewModel.retrieveNotes();

        // Find the TextView in your layout
        //notesTextView = findViewById(R.id.tv_notes);

        // Observe the notesLiveData from the ViewModel


        viewModel.retrieveNotes();

        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe invited users LiveData
        viewModel.getInvitedUsersLiveData().observe(this, invitedUsers -> {
            loadInvitedUsers(invitedUsers);
            LinearLayout invitedUsersContainer = findViewById(R.id.invited_users_container);
            invitedUsersContainer.removeAllViews(); // Clear previous views
            for (String email : invitedUsers) {
                View invitedUserCard = createInvitedUserCard(email);
                invitedUsersContainer.addView(invitedUserCard);
            }
        });

        // Button click listeners
        btnAlloted.setOnClickListener(v -> {
            LogisticsChart logisticsChart = new LogisticsChart();
            logisticsChart.show(getSupportFragmentManager(), "LogisticsChart");
        });

        // navigation
        tabLayout = binding.tabNavigation;
        navigation();


        //tvInvitedUsers = findViewById(R.id.tv_invited_users);
        Button inviteButton = binding.buttonInvite;

        inviteButton.setOnClickListener(this::onInviteButtonClick);
        
        // Observe changes in the users list
        viewModel.getUsersLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> users) {

            }
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
        emailTextView.setTextColor(getResources().getColor(R.color.white));
        emailTextView.setPadding(30, 25, 0, 0);

        // Add the email TextView to the card layout
        cardLayout.addView(emailTextView);

        return cardLayout;
    }


    public void onInviteButtonClick(View view) {
        // Observe the LiveData
        viewModel.getUsersLiveData().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                showInviteDialog(users);
            } else {
                Toast.makeText(LogisticsActivity.this, "No users found to invite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInviteDialog(List<String> userList) {
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
            viewModel.inviteUsers(selectedUsers, LogisticsActivity.this);
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void loadInvitedUsers(List<String> invitedUsers) {
        LinearLayout invitedUsersContainer = findViewById(R.id.invited_users_container);
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
        viewModel.retrieveNotes();

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            if (!note.isEmpty()) {
                viewModel.addNote(note);
                //logisticsViewModel.saveNoteToFirebase(note); // Call this to save to Firebase

            } else {
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
        noteTextView.setTextColor(getResources().getColor(R.color.black));

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
        int[] navIcons = {
                R.drawable.logistics,
                R.drawable.destination,
                R.drawable.dining,
                R.drawable.accommodation,
                R.drawable.transport,
                R.drawable.travel };

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
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    };
}



