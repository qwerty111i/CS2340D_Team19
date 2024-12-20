package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.Trip;
import com.example.sprint1.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DiningViewModel extends ViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> nameError = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> locationError = new MutableLiveData<>();
    private MutableLiveData<String> website = new MutableLiveData<>();
    private MutableLiveData<String> websiteError = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();
    private MutableLiveData<String> time = new MutableLiveData<>();
    private MutableLiveData<String> timeError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<String> tripName = new MutableLiveData<>();
    private MutableLiveData<String> tripError = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> tripList = new MutableLiveData<>();

    public void setReservationDetails(String name, String location, String website,
                                      String date, String time, String tripName) {
        // Sets the values of the MutableLiveData
        this.name.setValue(name);
        this.location.setValue(location);
        this.website.setValue(website);
        this.date.setValue(date);
        this.time.setValue(time);
        this.tripName.setValue(tripName);


        // Checks whether inputs are valid
        boolean validName = checkInput(name);
        boolean validLocation = checkInput(location);
        boolean validWebsite = checkInput(website);
        boolean validDates = checkInput(date);
        boolean validTime = checkInput(time);
        boolean validTripName = checkInput(tripName);

        // Sets the Name error message
        if (!validName) {
            nameError.setValue("Invalid Name Input!");
        } else {
            nameError.setValue(null);
        }

        // Sets the Location error message
        if (!validLocation) {
            locationError.setValue("Invalid Location Input!");
        } else {
            locationError.setValue(null);
        }

        // Sets the Website error message
        if (!validWebsite) {
            websiteError.setValue("Invalid Website Input!");
        } else {
            websiteError.setValue(null);
        }

        // Sets the Date error message
        if (!validDates) {
            dateError.setValue("Invalid Date!");
        } else {
            dateError.setValue(null);
        }

        // Sets the Time error message
        if (!validTime) {
            timeError.setValue("Invalid Time!");
        } else {
            timeError.setValue(null);
        }

        // Sets the Trip error message
        if (!validTripName) {
            tripError.setValue("No Trip Selected!");
        } else {
            tripError.setValue(null);
        }

        // Sets the value of validInputs (true/false)
        validInputs.setValue(validName && validLocation && validWebsite
                && validDates && validTime && validTripName);
    }

    public void setDropdownItems() {
        // Empty starting list
        ArrayList<String> newTripList = new ArrayList<>();

        // Gets the current user ID in Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = "";
        if (user != null) {
            userId = user.getUid();
        }

        // Gets the trips from Firebase
        DatabaseReference database = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(userId)
                .child("Trips");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterates through the Trip node and adds each child to the list
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    Trip newTrip = tripSnapshot.getValue(Trip.class);
                    newTripList.add(newTrip.getTripName());
                }

                // Sets the tripList equal to the new list of trips
                tripList.setValue(newTripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // Base check for inputs (empty or not)
    public boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    // Saves the details in the database
    public void saveReservationsDetails() {
        // Creates a new ReservationDetails object, storing all the data
        ReservationDetails reservationDetails = new ReservationDetails(
                name.getValue(),
                location.getValue(),
                website.getValue(),
                date.getValue(),
                time.getValue(),
                tripName.getValue());

        // Uses the Singleton implemented Database to store information
        UserModel.getInstance().storeReservationDetails(reservationDetails);

        String inviterEmail = null;
        String baseTripName = tripName.getValue();
        if (tripName.getValue() != null && tripName.getValue().contains("(Shared by ")) {
            int startIndex = tripName.getValue().indexOf("(Shared by ") + "(Shared by ".length();
            int somestart = tripName.getValue().indexOf("(Shared by ");
            int endIndex = tripName.getValue().indexOf(")", startIndex);
            inviterEmail = tripName.getValue().substring(startIndex, endIndex);
            baseTripName = tripName.getValue().substring(0, somestart - 1);
        }

        if (inviterEmail != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

            String finalInviterEmail = inviterEmail;
            String finalBaseTripName = baseTripName;
            String finalBaseTripName1 = baseTripName;
            String finalBaseTripName2 = baseTripName;
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String inviterId = null;

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        if (finalInviterEmail.equals(email)) {
                            inviterId = userSnapshot.getKey();
                            break;
                        }
                    }
                    if (inviterId != null) {
                        DatabaseReference tripsReference = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(inviterId)
                                .child("Trips");
                        String finalInviterId = inviterId;
                        String finalInviterId1 = inviterId;
                        tripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
                                if (tripsSnapshot.exists()) {
                                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
                                        String tripNameDb = tripSnapshot.child("tripName").getValue(String.class);
                                        if (tripNameDb.equals(finalBaseTripName)) {
                                            String reservationDetailsId = tripSnapshot.child("Reservation Details").getKey();
                                            ReservationDetails reservationDetails = new ReservationDetails(
                                                    name.getValue(),
                                                    location.getValue(),
                                                    website.getValue(),
                                                    date.getValue(),
                                                    time.getValue(),
                                                    finalBaseTripName1);

                                            DatabaseReference inviterTripsRef = FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(finalInviterId1)
                                                    .child("Trips");
                                            inviterTripsRef.orderByChild("tripName").equalTo(finalBaseTripName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                                                        String tripId = tripSnapshot.getKey();
                                                        Log.d("id?", tripId.toString());
                                                        DatabaseReference reservationDetailsRef = inviterTripsRef
                                                                .child(tripId)
                                                                .child("Reservation Details");

                                                        DataSnapshot reservationDetailsSnapshot = tripSnapshot.child("Reservation Details");
                                                        String newReservationDetailsId = reservationDetailsRef.push().getKey();

                                                        if (newReservationDetailsId != null) {
                                                            reservationDetailsRef.child(newReservationDetailsId)
                                                                    .setValue(reservationDetails);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) { }
                                            });


                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error retrieving inviter data", error.toException());
                }
            });
        }
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<String> getNameError() {
        return nameError;
    }

    public LiveData<String> getLocationError() {
        return locationError;
    }

    public LiveData<String> getWebsiteError() {
        return websiteError;
    }

    public LiveData<String> getDateError() {
        return dateError;
    }

    public LiveData<String> getDate() {
        return date;
    }

    public LiveData<String> getTimeError() {
        return timeError;
    }

    public LiveData<ArrayList<String>> getTripList() {
        return tripList;
    }

    public LiveData<String> getTripError() {
        return tripError;
    }
}
