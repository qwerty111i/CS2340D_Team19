package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.AccommodationDetails;
import com.example.sprint1.model.TransportationDetails;
import com.example.sprint1.model.Trip;
import com.example.sprint1.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransportationViewModel extends ViewModel {
    private final MutableLiveData<String> type = new MutableLiveData<>();
    private final MutableLiveData<String> typeError = new MutableLiveData<>();

    private final MutableLiveData<String> startLocation = new MutableLiveData<>();
    private final MutableLiveData<String> startLocationError = new MutableLiveData<>();

    private final MutableLiveData<String> endLocation = new MutableLiveData<>();
    private final MutableLiveData<String> endLocationError = new MutableLiveData<>();

    private final MutableLiveData<String> startDate = new MutableLiveData<>();
    private final MutableLiveData<String> startDateError = new MutableLiveData<>();

    private final MutableLiveData<String> startTime = new MutableLiveData<>();
    private final MutableLiveData<String> startTimeError = new MutableLiveData<>();

    private final MutableLiveData<String> tripName = new MutableLiveData<>();
    private final MutableLiveData<String> tripError = new MutableLiveData<>();

    private final MutableLiveData<Boolean> validInputs = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<String>> tripList = new MutableLiveData<>();

    public void setTransportationDetails(String type, String startLocation, String endLocation,
                                        String startDate, String startTime, String tripName) {
        this.type.setValue(type);
        this.startLocation.setValue(startLocation);
        this.endLocation.setValue(endLocation);
        this.startDate.setValue(startDate);
        this.startTime.setValue(startTime);
        this.tripName.setValue(tripName);

        // Checks whether inputs are valid
        boolean validType = checkInput(type);
        boolean validStartLocation = checkInput(startLocation);
        boolean validEndLocation = checkInput(endLocation);
        boolean validStartDate = checkInput(startDate);
        boolean validStartTime = checkInput(startTime);
        boolean validTripName = checkInput(tripName);

        // Set input errors based on validation
        if (!validType) {
            typeError.setValue("Transportation type is required!");
        } else {
            typeError.setValue(null);
        }

        if (!validStartLocation) {
            startLocationError.setValue("Starting location is required!");
        } else {
            startLocationError.setValue(null);
        }

        if (!validEndLocation) {
            endLocationError.setValue("Ending Location is required!");
        } else {
            endLocationError.setValue(null);
        }

        if (!validStartDate) {
            startDateError.setValue("Invalid Date!");
        } else {
            startDateError.setValue(null);
        }

        if (!validStartTime) {
            startTimeError.setValue("Invalid Time!");
        } else {
            startTimeError.setValue(null);
        }

        if (!validTripName) {
            tripError.setValue("Invalid Trip Name!");
        } else {
            tripError.setValue(null);
        }

        // Sets the value of validInputs (true/false)
        validInputs.setValue(validType && validStartLocation && validEndLocation
                && validStartDate && validStartTime && validTripName);
    }

    public void saveTransportationDetails() {
        TransportationDetails transportationDetails = new TransportationDetails(
                type.getValue(),
                startLocation.getValue(),
                endLocation.getValue(),
                startDate.getValue(),
                startTime.getValue(),
                tripName.getValue());
        UserModel.getInstance().storeTransportationDetails(transportationDetails);

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
                        Log.d("d", email);
                        if (finalInviterEmail.equals(email)) {
                            inviterId = userSnapshot.getKey();
                            Log.d("ID?", inviterId);
                            Log.d("email?", email);
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
                                        String tripNameDb = tripSnapshot.child("tripName")
                                                .getValue(String.class);
                                        if (tripNameDb.equals(finalBaseTripName)) {
                                            TransportationDetails transportationDetails =
                                                    new TransportationDetails(
                                                            type.getValue(),
                                                            startLocation.getValue(),
                                                            endLocation.getValue(),
                                                            startDate.getValue(),
                                                            startTime.getValue(),
                                                            finalBaseTripName1);

                                            DatabaseReference inviterTripsRef =
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("users")
                                                            .child(finalInviterId1)
                                                            .child("Trips");
                                            inviterTripsRef.orderByChild("tripName")
                                                    .equalTo(finalBaseTripName)
                                                    .addListenerForSingleValueEvent(
                                                            new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                                                                        String tripId = tripSnapshot.getKey();
                                                                        Log.d("id?", tripId.toString());
                                                                        DatabaseReference transportationDetailsRef = inviterTripsRef
                                                                                .child(tripId)
                                                                                .child("Transportation Details");

                                                                        DataSnapshot transportationDetailsSnapshot = tripSnapshot.child("Transportation Details");
                                                                        String newTransportationDetailsId = transportationDetailsRef.push().getKey();

                                                                        if (newTransportationDetailsId != null) {
                                                                            transportationDetailsRef.child(newTransportationDetailsId).setValue(transportationDetails);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
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

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }
    public LiveData<String> getTypeError() {
        return typeError;
    }
    public LiveData<String> getStartLocationError() {
        return startLocationError;
    }
    public LiveData<String> getEndLocationError() {
        return endLocationError;
    }
    public LiveData<String> getStartDateError() {
        return startDateError;
    }
    public LiveData<String> getStartTimeError() {
        return startTimeError;
    }
    public LiveData<String> getTripError() {
        return tripError;
    }
    public LiveData<ArrayList<String>> getTripList() {
        return tripList;
    }
}
