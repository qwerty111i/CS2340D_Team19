package com.example.sprint1.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiningModel {
    private DatabaseReference database;

    private static DiningModel instance;

    private DiningModel() {
        database = FirebaseDatabase.getInstance().getReference("dining_details");
    }

    public static synchronized DiningModel getInstance() {
        if (instance == null) {
            instance = new DiningModel();
        }
        return instance;
    }

    public void storeDining(Dining dining) {
        database.push().setValue(dining);
    }
}
