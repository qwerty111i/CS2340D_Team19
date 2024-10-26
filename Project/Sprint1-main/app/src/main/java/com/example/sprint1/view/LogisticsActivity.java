package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprint1.viewmodel.LogisticsViewModel;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LogisticsViewModel logisticsViewModel;
    private TextView tvInvitedUsers;

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
        Button notesButton = findViewById(R.id.button_notes);
        notesButton.setOnClickListener(v -> showNotesDialog());
        loadInvitedUsers();



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


        tvInvitedUsers = findViewById(R.id.tv_invited_users);
        Button inviteButton = findViewById(R.id.button_invite);
        inviteButton.setOnClickListener(this::onInviteButtonClick);

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe changes in the users list
        logisticsViewModel.getUsersLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> users) {

            }
        });
    }

    private void displayUsers(List<String> users) {
        StringBuilder emailsDisplay = new StringBuilder("All Users:\n");
        for (String email : users) {
            emailsDisplay.append(email).append("\n");
        }
        tvInvitedUsers.setText(emailsDisplay.toString());
    }
    public void onInviteButtonClick(View view) {
        // Fetch users through ViewModel
        //logisticsViewModel.fetchUsers();

        // Observe the LiveData for users
        logisticsViewModel.getUsersLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> users) {
                // Show the dialog if users are fetched successfully
                if (users != null && !users.isEmpty()) {
                    showInviteDialog(users);
                } else {
                    Toast.makeText(LogisticsActivity.this, "No users found to invite", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showInviteDialog(List<String> userList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select users to invite");

        String[] emails = userList.toArray(new String[0]);
        boolean[] checkedItems = new boolean[userList.size()];

        builder.setMultiChoiceItems(emails, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Invite", (dialog, id) -> {
            ArrayList<String> selectedUsers = new ArrayList<>();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selectedUsers.add(userList.get(i));
                }
            }
            inviteUsers(selectedUsers);
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Handle inviting selected users
    private void inviteUsers(ArrayList<String> selectedUsers) {
        StringBuilder invitedEmails = new StringBuilder(tvInvitedUsers.getText().toString()); // Get existing text

        // Check if there are already invited users
        if (invitedEmails.toString().trim().isEmpty()) {
            invitedEmails.append("Invited Users:\n");
            invitedEmails.append("\n");
        }

        for (String user : selectedUsers) {
            if (!invitedEmails.toString().contains(user)) { // Check to avoid duplicates
                invitedEmails.append(user).append("\n");
            }
        }
        // Update the TextView with the invited users' emails
        tvInvitedUsers.setText(invitedEmails.toString());

        Toast.makeText(LogisticsActivity.this, "Users invited (locally)!", Toast.LENGTH_SHORT).show();

        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        for (String invitedUserEmail : selectedUsers) {
            //directly use the email to store the invitation
            userReference.orderByChild("email").equalTo(invitedUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String invitedUserId = userSnapshot.getKey();


                            String invitedUserEmail = userSnapshot.child("email").getValue(String.class); // Get the email of the invited user
                            if (invitedUserEmail != null) {

                                userReference.child(inviterId).child("invitedUsers").child(invitedUserEmail.replace(".", ",")).setValue(true);
                            }
                            // Copy travel details to the invited user
                            userReference.child(inviterId).child("travelDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot travelDetailsSnapshot) {
                                    for (DataSnapshot travelDetail : travelDetailsSnapshot.getChildren()) {
                                        // Copy travel detail to the invited user using email
                                        userReference.child(invitedUserId).child("travelDetails").push().setValue(travelDetail.getValue());
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("Firebase", "Error retrieving travel details");
                                }
                            });
                        }
                    } else {
                        Log.d("Firebase", "User with email " + invitedUserEmail + " not found.");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Firebase", "Error retrieving user ID from email");
                }
            });
        }
    }

    private void loadInvitedUsers() {
        String inviterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        userReference.child(inviterId).child("invitedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder invitedUsers = new StringBuilder("Invited Users:\n");


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String invitedUserKey = userSnapshot.getKey();
                    if (invitedUserKey != null) {

                        String invitedUserEmail = invitedUserKey.replace(",", ".");
                        invitedUsers.append(invitedUserEmail).append("\n");
                    }
                }
                // Update the TextView with the invited users' emails
                tvInvitedUsers.setText(invitedUsers.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LogisticsActivity", "Error retrieving invited users: " + databaseError.getMessage());
            }
        });
    }


    private void displayInvitedUsers(ArrayList<String> invitedEmails) {
        StringBuilder emailsDisplay = new StringBuilder("Invited Users:\n");
        for (String email : invitedEmails) {
            emailsDisplay.append(email).append("\n");
        }
        tvInvitedUsers.setText(emailsDisplay.toString());
    }



    //NOTES PART
    private void showNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            if (!note.isEmpty()) {
                // Append the note to the new TextView
                appendNoteToTextView(note);
            } else {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Method to append note to the new TextView
    private void appendNoteToTextView(String note) {
        TextView notesTextView = findViewById(R.id.tv_notes); // Reference the new TextView for notes
        String existingNotes = notesTextView.getText().toString();
        notesTextView.setText(existingNotes + "\n" + note);
    }
}



