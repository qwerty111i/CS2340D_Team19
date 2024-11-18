package com.example.sprint1.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    // Creates an instance of the DatabaseReference
    private DatabaseReference database;

    // Creates a static instance
    private static UserModel instance;
    private String userId;
    private Map<String, String> userMap;
    private ArrayList<String> tripIds;

    // Private constructor
    private UserModel() {
        // Uses the DatabaseReference to point to the "users" node in Firebase
        database = FirebaseDatabase.getInstance().getReference("users");
        userMap = new HashMap<>();
        tripIds = new ArrayList<>();
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
        String currentTripName = travelDetails.getTripName();
        if (userId != null) {
            // Gets all the nodes under Trips
            DatabaseReference tripsRef = database.child(userId).child("Trips");
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                    // Returns if no trips exist
                    if (!tripsSnapshot.exists()) {
                        Log.e("StoreTravelDetails", "No trips found for the user");
                        return;
                    }

                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        // Gets the trip key
                        String tripId = tripSnapshot.getKey();

                        // Gets the trip name from the trip key
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // Adds the travel details if the trip names are equal
                        if (currentTripName.equals(tripName)) {
                            database.child(userId)
                                    .child("Trips")
                                    .child(tripId)
                                    .child("Travel Details")
                                    .push()
                                    .setValue(travelDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("StoreTravelDetails", "Unable to store: " + error.getMessage());
                }
            });
        } else {
            Log.e("UserModel", "UserId is not set, cannot store travel details.");
        }
    }

    // Method to store trips under the specific user's node
    public void storeTrip(Trip trip) {
        if (userId != null) {
            DatabaseReference tripRef = database.child(userId)
                    .child("Trips").push();
            tripIds.add(tripRef.getKey());
            tripRef.setValue(trip);
        } else {
            Log.e("UserModel", "UserId is not set, cannot store trip.");
        }
    }

    public void storeVacationTime(VacationTime vacationTime) {
        // Stores the vacation data in the database under the node "user"
        if (userId != null) {
            database.child(userId).child("vacations").push().setValue(vacationTime);
        } else {
            Log.e("UserModel", "UserId is not set, cannot store vacation.");
        }
    }

    // Method to store transportation details under the specific user's node
    public void storeTransportationDetails(TransportationDetails transportationDetails) {
        String currentTripName = transportationDetails.getTripName();
        if (userId != null) {
            // Gets all the nodes under Trips
            DatabaseReference tripsRef = database.child(userId).child("Trips");
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                    // Returns if no trips exist
                    if (!tripsSnapshot.exists()) {
                        Log.e("StoreTransportationDetails", "No trips found for the user");
                        return;
                    }

                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        // Gets the trip key
                        String tripId = tripSnapshot.getKey();

                        // Gets the trip name from the trip key
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // Adds the travel details if the trip names are equal
                        if (currentTripName.equals(tripName)) {
                            database.child(userId)
                                    .child("Trips")
                                    .child(tripId)
                                    .child("Transportation Details")
                                    .push()
                                    .setValue(transportationDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("StoreTransportationDetails", "Unable to store: " + error.getMessage());
                }
            });
        } else {
            Log.e("UserModel", "UserId is not set, cannot store transportation details.");
        }
    }

    // Method to store accommodation details under the specific user's node
    public void storeAccommodationDetails(AccommodationDetails accommodationDetails) {
        String currentTripName = accommodationDetails.getTripName();
        if (userId != null) {
            // Gets all the nodes under Trips
            DatabaseReference tripsRef = database.child(userId).child("Trips");
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                    // Returns if no trips exist
                    if (!tripsSnapshot.exists()) {
                        Log.e("StoreTravelDetails", "No trips found for the user");
                        return;
                    }

                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        // Gets the trip key
                        String tripId = tripSnapshot.getKey();

                        // Gets the trip name from the trip key
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // Adds the travel details if the trip names are equal
                        if (currentTripName.equals(tripName)) {
                            database.child(userId)
                                    .child("Trips")
                                    .child(tripId)
                                    .child("Accommodation Details")
                                    .push()
                                    .setValue(accommodationDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("StoreAccommodationDetails", "Unable to store: " + error.getMessage());
                }
            });
        } else {
            Log.e("UserModel", "UserId is not set, cannot store accommodation details.");
        }
    }

    // Method to store reservation details under the specific user's node
    public void storeReservationDetails(ReservationDetails reservationDetails) {
        String currentTripName = reservationDetails.getTripName();
        if (userId != null) {
            // Gets all the nodes under Trips
            DatabaseReference tripsRef = database.child(userId).child("Trips");
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                    // Returns if no trips exist
                    if (!tripsSnapshot.exists()) {
                        Log.e("StoreTravelDetails", "No trips found for the user");
                        return;
                    }

                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        // Gets the trip key
                        String tripId = tripSnapshot.getKey();

                        // Gets the trip name from the trip key
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // Adds the travel details if the trip names are equal
                        if (currentTripName.equals(tripName)) {
                            database.child(userId)
                                    .child("Trips")
                                    .child(tripId)
                                    .child("Reservation Details")
                                    .push()
                                    .setValue(reservationDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("StoreTravelDetails", "Unable to store: " + error.getMessage());
                }
            });
        } else {
            Log.e("UserModel", "UserId is not set, cannot store travel details.");
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

    public void getTripId(String selectedTripName, String userId,
                          DatabaseReference database, final TripIdCallback callback) {
        DatabaseReference tripsRef = database.child(userId).child("Trips");
        tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                if (tripsSnapshot.exists()) {
                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                        String tripName = tripSnapshot.child("tripName").getValue(String.class);

                        // Check if the tripName matches the selected trip
                        if (tripName != null && tripName.equals(selectedTripName)) {
                            // Get the tripId
                            String tripId = tripSnapshot.getKey();
                            // Pass the tripId back through the callback
                            callback.onTripIdRetrieved(tripId);
                            // Exit the loop once we find the tripId
                            return;
                        }
                    }
                    callback.onTripIdRetrieved(null); // No matching trip found
                } else {
                    callback.onTripIdRetrieved(null); // No trips available
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving trips: " + error.getMessage());

                // Handle error case (you might also consider passing the error message)
                callback.onTripIdRetrieved(null);
            }
        });
    }



    public interface TripIdCallback {
        void onTripIdRetrieved(String tripId);
    }


    public boolean isUsernameTaken(String username) {
        return userMap.containsKey(username);
    }

    public String getEmailByUsername(String username) {
        return userMap.get(username);
    }
}
