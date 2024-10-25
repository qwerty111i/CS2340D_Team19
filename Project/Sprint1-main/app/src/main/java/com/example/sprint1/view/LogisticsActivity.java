package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.R;
import com.example.sprint1.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class LogisticsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView tvInvitedUsers; // Declare it here

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

        // Initialize TextView
        tvInvitedUsers = findViewById(R.id.tv_invited_users);

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

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Set click listener for the invite button
        Button inviteButton = findViewById(R.id.button_invite);
        inviteButton.setOnClickListener(v -> onInviteButtonClick(v));
    }

    // This method is called when the invite button is clicked
    public void onInviteButtonClick(View view) {
        fetchUsersAndShowDialog();
    }

    // Fetch users from Firebase Realtime Database
    private void fetchUsersAndShowDialog() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (email != null) {
                        Log.d("LogisticsActivity", "Fetched user: " + email);
                        userList.add(new User(email));
                    } else {
                        Log.w("LogisticsActivity", "User email is null for document: " + userSnapshot.getKey());
                    }
                }
                Log.d("LogisticsActivity", "Total users fetched: " + userList.size());

                if (!userList.isEmpty()) {
                    showInviteDialog(userList);
                } else {
                    Toast.makeText(LogisticsActivity.this, "No users found to invite", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LogisticsActivity", "Error getting users: ", databaseError.toException());
                Toast.makeText(LogisticsActivity.this, "Error getting users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display the users in a multi-select dialog
    private void showInviteDialog(ArrayList<User> userList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select users to invite");

        String[] emails = new String[userList.size()];
        boolean[] checkedItems = new boolean[userList.size()];

        for (int i = 0; i < userList.size(); i++) {
            emails[i] = userList.get(i).getEmail(); // Assuming getEmail() returns the user's email
        }

        builder.setMultiChoiceItems(emails, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Invite", (dialog, id) -> {
            ArrayList<User> selectedUsers = new ArrayList<>();
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
    private void inviteUsers(ArrayList<User> selectedUsers) {
        String itineraryId = FirebaseDatabase.getInstance().getReference("itineraries").push().getKey();

        if (itineraryId != null) {
            ArrayList<String> invitedEmails = new ArrayList<>();
            for (User user : selectedUsers) {
                invitedEmails.add(user.getEmail());
            }

            FirebaseDatabase.getInstance().getReference("itineraries").child(itineraryId)
                    .child("invitedUsers").setValue(invitedEmails)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(LogisticsActivity.this, "Users invited successfully!", Toast.LENGTH_SHORT).show();
                        displayInvitedUsers(invitedEmails);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("LogisticsActivity", "Error inviting users: ", e); // Log the error
                        Toast.makeText(LogisticsActivity.this, "Error inviting users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("LogisticsActivity", "Failed to create itinerary ID");
            Toast.makeText(LogisticsActivity.this, "Failed to create itinerary ID", Toast.LENGTH_SHORT).show();
        }
    }

    // Display the invited user emails on the LogisticsActivity page
    private void displayInvitedUsers(ArrayList<String> invitedEmails) {
        StringBuilder emailsDisplay = new StringBuilder("Invited Users:\n");
        for (String email : invitedEmails) {
            emailsDisplay.append(email).append("\n");
        }
        tvInvitedUsers.setText(emailsDisplay.toString()); // Now accessible here
    }

}
