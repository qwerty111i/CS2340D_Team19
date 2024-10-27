package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprint1.viewmodel.LogisticsViewModel;
import java.util.List;


public class LogisticsActivity extends AppCompatActivity {

    //private DatabaseReference databaseReference;
    //private TextView tvInvitedUsers;
    private LogisticsViewModel logisticsViewModel;
    //private TextView notesTextView;
    //private List<String> startDates = new ArrayList<>();
    //private List<String> endDates = new ArrayList<>();
    //private List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        // Initialize UI elements
        ImageButton btnLogistics = findViewById(R.id.btn_logistics);
        ImageButton btnAccom = findViewById(R.id.btn_accom);
        ImageButton btnDest = findViewById(R.id.btn_dest);
        ImageButton btnDining = findViewById(R.id.btn_dining);
        ImageButton btnTransport = findViewById(R.id.btn_transport);
        ImageButton btnTravel = findViewById(R.id.btn_travel);
        Button btnAlloted = findViewById(R.id.alloted_vs_planned);
        ImageButton notesButton = findViewById(R.id.button_notes);
        notesButton.setOnClickListener(v -> showNotesDialog());
        //loadInvitedUsers();
        //retrieveNotesFromDatabase();
        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Find the TextView in your layout
        //notesTextView = findViewById(R.id.tv_notes);

        // Observe the notesLiveData from the ViewModel

        logisticsViewModel.retrieveNotes();

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe invited users LiveData
        logisticsViewModel.getInvitedUsersLiveData().observe(this, invitedUsers -> {
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
            Intent intent = new Intent(LogisticsActivity.this, LogisticsChart.class);
            startActivity(intent);
        });

        btnLogistics.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        btnDest.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, DestinationsActivity.class);
            startActivity(intent);
        });

        btnDining.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        btnAccom.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        btnTravel.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, TravelActivity.class);
            startActivity(intent);
        });

        btnTransport.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, TransportationActivity.class);
            startActivity(intent);
        });


        ImageButton inviteButton = findViewById(R.id.button_invite);
        inviteButton.setOnClickListener(this::onInviteButtonClick);

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe changes in the users list
        logisticsViewModel.getUsersLiveData().observe(this, new Observer<List<String>>() {
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
        logisticsViewModel.getUsersLiveData().observe(this, users -> {
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
            logisticsViewModel.inviteUsers(selectedUsers, LogisticsActivity.this);
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


        logisticsViewModel.getNotesLiveData().observe(this, notes -> {
            notesContainer.removeAllViews();
            for (String note : notes) {
                appendNoteToContainer(notesContainer, note);
            }
        });

        // Retrieve notes from Firebase
        logisticsViewModel.retrieveNotes();

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            if (!note.isEmpty()) {
                logisticsViewModel.addNote(note);
                //logisticsViewModel.saveNoteToFirebase(note);
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
                        logisticsViewModel.removeNote(note); // Remove note from ViewModel
                        logisticsViewModel.removeNoteFromFirebase(note); // Remove from Firebase
                        notesContainer.removeView(noteTextView); // Remove from UI
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        notesContainer.addView(noteTextView);
    }
}



