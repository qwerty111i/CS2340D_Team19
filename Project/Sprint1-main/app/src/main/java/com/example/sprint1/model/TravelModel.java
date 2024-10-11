package com.example.sprint1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TravelModel {
    // Creates an instance of the DatabaseReference
    private DatabaseReference database;

    // Creates a static instance
    private static TravelModel instance;

    // Private constructor
    private TravelModel() {
        // Uses the DatabaseReference to point to the "travel_details" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("travel_details");
    }

    // Uses the Singleton design pattern to get instance
    public static synchronized TravelModel getInstance() {
        if (instance == null) {
            instance = new TravelModel();
        }
        return instance;
    }

    public void storeTravelDetails(TravelDetails travelDetails) {
        // TO-DO: LINK WITH USER
        // Stores the travel data in the database under the node "travel_details"
        database.push().setValue(travelDetails);
    }
}
