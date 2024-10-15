package com.example.sprint1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModel {
    // Creates an instance of the DatabaseReference
    private DatabaseReference database;

    // Creates a static instance
    private static UserModel instance;

    // Private constructor
    private UserModel() {
        // Uses the DatabaseReference to point to the "users" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("users");
    }

    // Uses the Singleton design pattern to get instance
    public static synchronized UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    public void storeUser(String userId, User user) {
        // Stores the user data in the database under the node "users"
        database.child(userId).setValue(user);
    }

    public void storeVacation(String vacaID, VacationTime vacationTime) {
        // Stores the vacation information under user database
        database.child(vacaID).setValue(vacationTime);
    }
}
