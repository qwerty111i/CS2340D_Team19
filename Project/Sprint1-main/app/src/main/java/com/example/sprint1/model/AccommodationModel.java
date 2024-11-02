package com.example.sprint1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccommodationModel {
    // the only instance of database
    private DatabaseReference database;

    // Static instance
    private static AccommodationModel instance;

    private AccommodationModel() {
        // Uses DatabaseReference to point to "accommodations" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("accommodations");
    }

    // Uses the Singleton design pattern to get instance
    public static synchronized AccommodationModel getInstance() {
        if (instance == null) {
            instance = new AccommodationModel();
        }
        return instance;
    }

    public void storeAccommodation(Accommodation accommodation) {
        // Stores the accommodation in the database under the node "accommodations"
        database.push().setValue(accommodation);
    }
}
