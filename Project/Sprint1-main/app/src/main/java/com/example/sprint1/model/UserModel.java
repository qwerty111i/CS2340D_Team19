package com.example.sprint1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModel {
    // Creates an instance of the DatabaseReference
    private DatabaseReference database;

    public UserModel() {
        // Uses the DatabaseReference to point to the "users" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("users");
    }

    public void storeUser(String userId, User user) {
        // Stores the user data in the database under the node "users"
        database.child(userId).setValue(user);
    }
}
