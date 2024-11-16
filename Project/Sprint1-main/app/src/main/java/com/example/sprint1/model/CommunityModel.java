package com.example.sprint1.model;


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
    public void addPost(String userId, Trip trip, List<String> transportation, String notes) {
        userRef.child(userId).child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    if (tripSnapshot.child("tripName").getValue(String.class).equals(trip.getTripName())) {
                        List<AccommodationDetails> accommodations = new ArrayList<>();
                        List<ReservationDetails> reservations = new ArrayList<>();
                        for (DataSnapshot accommodationSnapshot : tripSnapshot.child("Accommodation Details").getChildren()) {
                            AccommodationDetails accommodation = accommodationSnapshot.getValue(AccommodationDetails.class);
                            accommodations.add(accommodation);
                        }

                        for (DataSnapshot reservationSnapshot : tripSnapshot.child("Reservation Details").getChildren()) {
                            ReservationDetails reservation = reservationSnapshot.getValue(ReservationDetails.class);
                            reservations.add(reservation);
                        }
                        PostDetails postDetails = new PostDetails(
                                trip, transportation, notes, accommodations, reservations
                        );

                        // Set the userId for the post
                        postDetails.setUserId(userId);

                        // Store the post in the community database
                        String postId = communityRef.push().getKey();
                        communityRef.child(postId).setValue(postDetails);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Error
            }
        });
    }
}
