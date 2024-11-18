package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TravelFormEntry;
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

public class TravelViewModel extends ViewModel {
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> startDateError = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> endDateError = new MutableLiveData<>();
    private MutableLiveData<String> destination = new MutableLiveData<>();
    private MutableLiveData<String> destinationError = new MutableLiveData<>();
    private MutableLiveData<String> accommodation = new MutableLiveData<>();
    private MutableLiveData<String> accommodationError = new MutableLiveData<>();
    private MutableLiveData<String> dining = new MutableLiveData<>();
    private MutableLiveData<String> diningError = new MutableLiveData<>();
    private MutableLiveData<String> rating = new MutableLiveData<>();
    private MutableLiveData<String> ratingError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();

    public void setTravelDetails(String startDate, String endDate, String destination,
                                      String accommodation, String dining, String rating) {
        // Sets the values of the MutableLiveData
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.destination.setValue(destination);
        this.accommodation.setValue(accommodation);
        this.dining.setValue(dining);
        this.rating.setValue(rating);


        // Checks whether inputs are valid
        boolean validStartDate = checkInput(startDate);
        boolean validEndDate = checkInput(endDate);
        boolean validDestination = checkInput(destination);
        boolean validAccommodation = checkInput(accommodation);
        boolean validDining = checkInput(dining);
        boolean validRating = checkInput(rating);

        // Sets the Start Date error message
        if (!validStartDate) {
            startDateError.setValue("Invalid Start Date!");
        } else {
            startDateError.setValue(null);
        }

        // Sets the End Date error message
        if (!validEndDate) {
            endDateError.setValue("Invalid End Date!");
        } else {
            endDateError.setValue(null);
        }

        // Sets the Destination error message
        if (!validDestination) {
            destinationError.setValue("Invalid Destination!");
        } else {
            destinationError.setValue(null);
        }

        // Sets the Accommodation error message
        if (!validAccommodation) {
            accommodationError.setValue("Invalid Accommodations!");
        } else {
            accommodationError.setValue(null);
        }

        // Sets the Dining error message
        if (!validDining) {
            diningError.setValue("Invalid Dining Input!");
        } else {
            diningError.setValue(null);
        }

        // Sets the Rating error message
        if (!validRating) {
            ratingError.setValue("Invalid Rating!");
        } else {

            ratingError.setValue(null);
        }


        // Sets the value of validInputs (true/false)
        validInputs.setValue(validStartDate && validEndDate && validDestination
                && validAccommodation && validDining && validRating);
    }

//    public void setDropdownItems() {
//        // Empty starting list
//        ArrayList<String> newTripList = new ArrayList<>();
//
//        // Gets the current user ID in Firebase
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = "";
//        if (user != null) {
//            userId = user.getUid();
//        }
//
//        // Gets the trips from Firebase
//        DatabaseReference database = FirebaseDatabase
//                .getInstance()
//                .getReference("users")
//                .child(userId)
//                .child("Trips");
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Iterates through the Trip node and adds each child to the list
//                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
//                    Trip newTrip = tripSnapshot.getValue(Trip.class);
//                    newTripList.add(newTrip.getTripName());
//                }
//
//                // Sets the tripList equal to the new list of trips
//                tripList.setValue(newTripList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) { }
//        });
//    }

    // Base check for inputs (empty or not)
    public boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    // Saves the details in the database
    public void saveTravelDetails() {
        // Creates a new TravelFormEntry object, storing all the data
        TravelFormEntry tfe = new TravelFormEntry(
                startDate.getValue(),
                endDate.getValue(),
                destination.getValue(),
                accommodation.getValue(),
                dining.getValue(),
                rating.getValue());

        // Uses the Singleton implemented Database to store information
        UserModel.getInstance().storeTravelFormEntry(tfe);

//        String inviterEmail = null;
//        String baseTripName = tripName.getValue();
//        if (tripName.getValue() != null && tripName.getValue().contains("(Shared by ")) {
//            int startIndex = tripName.getValue().indexOf("(Shared by ") + "(Shared by ".length();
//            int somestart = tripName.getValue().indexOf("(Shared by ");
//            int endIndex = tripName.getValue().indexOf(")", startIndex);
//            inviterEmail = tripName.getValue().substring(startIndex, endIndex);
//            baseTripName = tripName.getValue().substring(0, somestart - 1);
//        }
//
//        if (inviterEmail != null) {
//            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
//
//            String finalInviterEmail = inviterEmail;
//            String finalBaseTripName = baseTripName;
//            String finalBaseTripName1 = baseTripName;
//            String finalBaseTripName2 = baseTripName;
//            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String inviterId = null;
//
//                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                        String email = userSnapshot.child("email").getValue(String.class);
//                        if (finalInviterEmail.equals(email)) {
//                            inviterId = userSnapshot.getKey();
//                            break;
//                        }
//                    }
//                    if (inviterId != null) {
//                        DatabaseReference tripsReference = FirebaseDatabase.getInstance()
//                                .getReference("users")
//                                .child(inviterId)
//                                .child("Trips");
//                        String finalInviterId = inviterId;
//                        String finalInviterId1 = inviterId;
//                        tripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot tripsSnapshot) {
//                                if (tripsSnapshot.exists()) {
//                                    for (DataSnapshot tripSnapshot : tripsSnapshot.getChildren()) {
//                                        String tripNameDb = tripSnapshot.child("tripName").getValue(String.class);
//                                        if (tripNameDb.equals(finalBaseTripName)) {
//                                            String reservationDetailsId = tripSnapshot.child("Reservation Details").getKey();
//                                            ReservationDetails reservationDetails = new ReservationDetails(
//                                                    name.getValue(),
//                                                    location.getValue(),
//                                                    website.getValue(),
//                                                    date.getValue(),
//                                                    time.getValue(),
//                                                    finalBaseTripName1);
//
//                                            DatabaseReference inviterTripsRef = FirebaseDatabase.getInstance()
//                                                    .getReference("users")
//                                                    .child(finalInviterId1)
//                                                    .child("Trips");
//                                            inviterTripsRef.orderByChild("tripName").equalTo(finalBaseTripName).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
//                                                        String tripId = tripSnapshot.getKey();
//                                                        Log.d("id?", tripId.toString());
//                                                        DatabaseReference reservationDetailsRef = inviterTripsRef
//                                                                .child(tripId)
//                                                                .child("Reservation Details");
//
//                                                        DataSnapshot reservationDetailsSnapshot = tripSnapshot.child("Reservation Details");
//                                                        String newReservationDetailsId = reservationDetailsRef.push().getKey();
//
//                                                        if (newReservationDetailsId != null) {
//                                                            reservationDetailsRef.child(newReservationDetailsId)
//                                                                    .setValue(reservationDetails);
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) { }
//                                            });
//
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e("Firebase", "Error retrieving inviter data", error.toException());
//                }
//            });
//        }
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    // LiveData getters
    public LiveData<String> getStartDate() {
        return startDate;
    }

    public LiveData<String> getStartDateError() {
        return startDateError;
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getEndDateError() {
        return endDateError;
    }

    public LiveData<String> getDestination() {
        return destination;
    }

    public LiveData<String> getDestinationError() {
        return destinationError;
    }

    public LiveData<String> getAccommodation() {
        return accommodation;
    }

    public LiveData<String> getAccommodationError() {
        return accommodationError;
    }

    public LiveData<String> getDining() {
        return dining;
    }

    public LiveData<String> getDiningError() {
        return diningError;
    }

    public LiveData<String> getRating() {
        return rating;
    }

    public LiveData<String> getRatingError() {
        return ratingError;
    }
}
