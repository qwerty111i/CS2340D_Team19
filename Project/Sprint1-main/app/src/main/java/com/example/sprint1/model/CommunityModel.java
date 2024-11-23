package com.example.sprint1.model;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityModel {
    private static CommunityModel instance;
    private DatabaseReference communityRef;
    private DatabaseReference userRef;
    private static int size;
    public String currentDisplayName;

    private CommunityModel() {
        communityRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");
        userRef = FirebaseDatabase.getInstance().getReference("users");


//        initializeSizeListener();
    }

    public static synchronized CommunityModel getInstance() {
        if (instance == null) {
            instance = new CommunityModel();
        }
        return instance;
    }

//    private void initializeSizeListener() {
//        // Attach a listener to count the number of entries in the "CommunityPosts" node
//        communityRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Update the size based on the number of children in the "CommunityPosts" node
//                size = (int) dataSnapshot.getChildrenCount();
//                Log.d("CommunityModel", "Size updated: " + size);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle database errors
//                Log.e("CommunityModel", "Failed to update size: " + databaseError.getMessage());
//            }
//        });
//    }

    public void addSampleIfEmpty() {
        communityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                Log.d("CommunityModel", "Size checked: " + size);

                if (size == 0) {
                    // Create a sample TravelFormEntry object
                    TravelFormEntry tfe = new TravelFormEntry("10/01/24", "10/31/24", "Ohio",
                            "Hyatt", "McDonalds", "6");
                    TFEUser sampleEntry = new TFEUser(
                            tfe, // destination
                            "default@gmail.com" // estimated cost
                    );

                    // Store the sample entry in the database
                    communityRef.push().setValue(sampleEntry, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("CommunityModel", "Failed to store TravelFormEntry: " + databaseError.getMessage());
                            } else {
                                size++;
                                Log.d("CommunityModel", "Sample TravelFormEntry successfully stored.");
                            }
                        }
                    });
                    Log.d("CommunityModel", "Sample TravelFormEntry added to the database.");
                } else {
                    Log.d("CommunityModel", "Size is not 0. No sample entry added.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("CommunityModel", "Failed to check size: " + databaseError.getMessage());
            }
        });
    }


    public void storeTravelFormEntry(TravelFormEntry tfe) {
        //Get currently logged in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentDisplayName = currentUser.getEmail();
            Log.d("UserEmail", "User email: " + currentDisplayName);
        }

        // Get the userId from the UserModel
        String userId = UserModel.getInstance().getUserId();

        // Check if the userId is valid
        if (userId == null || userId.isEmpty()) {
            Log.e("CommunityModel", "User ID is not set. Cannot store travel form entry.");
            return; // Exit early if userId is not valid
        }

        // Create a TFEUser object with the TravelFormEntry and userId
        TFEUser tfeUser = new TFEUser(tfe, currentDisplayName); //can change back to user Id

        // Store the TravelFormEntry data in the community database
        communityRef.push().setValue(tfeUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    // Log error if the database operation fails
                    Log.e("CommunityModel", "Failed to store TravelFormEntry: " + databaseError.getMessage());
                } else {
                    // Log success message
                    size++;
                    Log.d("CommunityModel", "TravelFormEntry successfully stored.");
                }
            }
        });
    }

    public int getSize() {
        return size;
    }
}
