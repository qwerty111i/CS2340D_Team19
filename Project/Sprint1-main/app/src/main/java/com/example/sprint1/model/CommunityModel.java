package com.example.sprint1.model;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityModel {
    private static CommunityModel instance;
    private static DatabaseReference communityRef;
    private DatabaseReference userRef;

    private CommunityModel() {
        communityRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public static synchronized CommunityModel getInstance() {
        if (instance == null) {
            instance = new CommunityModel();
        }
        return instance;
    }

    public void storeTravelFormEntry(TravelFormEntry tfe) {
        // Stores the TravelFormEntry data in the community database
        communityRef.push().setValue(new TFEUser(tfe, UserModel.getInstance().getUserId()));
    }
}
