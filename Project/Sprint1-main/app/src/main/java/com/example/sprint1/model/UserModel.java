package com.example.sprint1.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    // Creates an instance of the DatabaseReference
    private DatabaseReference database;

    // Creates a static instance
    private static UserModel instance;
    private String userId;
    private Map<String, String> userMap;

    // Private constructor
    private UserModel() {
        // Uses the DatabaseReference to point to the "users" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("users");
        userMap = new HashMap<>();
    }

    // Uses the Singleton design pattern to get instance
    public static synchronized UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    // Store the userId when it becomes available (after signup/login)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void storeUser(User user) {
        // Stores the user data in the database under the node "users"
        if (userId != null) {
            database.child(userId).setValue(user);
            userMap.put(user.getUsername(), user.getEmail());
        } else {
            // Handle case where userId is not set
            Log.e("UserModel", "UserId is not set, cannot store user.");
        }
    }

    // Method to store travel details under the specific user's node
    public void storeTravelDetails(TravelDetails travelDetails) {
        if (userId != null) {
            database.child(userId).child("travelDetails").push().setValue(travelDetails);
        } else {
            Log.e("UserModel", "UserId is not set, cannot store travel details.");
        }
    }

    public void storeVacation(VacationTime vacationTime) {
        // Stores the vacation data in the database under the node "user"
        if (userId != null) {
            database.child(userId).child("vacations").push().setValue(vacationTime);
        } else {
            Log.e("UserModel", "UserId is not set, cannot store vacation.");
        }
    }

    // Method to store accommodations under the specific user's nod
    public void storeAccommodation(Accommodation accommodation) {
        if (userId != null) {
            database.child(userId).child("accommodations").push().setValue(accommodation);
        } else {
            Log.e("UserModel", "UserId is not set, cannot store accommodations.");
        }
    }

    public void loadUserMap() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (username != null && email != null) {
                        userMap.put(username, email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
            }
        });
    }
    public boolean isUsernameTaken(String username) {
        return userMap.containsKey(username);
    }
    public String getEmailByUsername(String username) {
        return userMap.get(username);
    }
}
