package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.AccommodationDetails;
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

public class AccommodationViewModel extends ViewModel {
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> nameError = new MutableLiveData<>();

    private final MutableLiveData<String> location = new MutableLiveData<>();
    private final MutableLiveData<String> locationError = new MutableLiveData<>();

    private final MutableLiveData<String> checkInDate = new MutableLiveData<>();
    private final MutableLiveData<String> checkOutDate = new MutableLiveData<>();
    private final MutableLiveData<String> dateError = new MutableLiveData<>();

    private final MutableLiveData<Integer> numberOfRooms = new MutableLiveData<>();
    private final MutableLiveData<String> numRoomError = new MutableLiveData<>();

    private final MutableLiveData<String> roomType = new MutableLiveData<>();
    private final MutableLiveData<String> roomError = new MutableLiveData<>();

    private final MutableLiveData<String> tripName = new MutableLiveData<>();
    private final MutableLiveData<String> tripError = new MutableLiveData<>();

    private final MutableLiveData<Boolean> validInputs = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<String>> tripList = new MutableLiveData<>();

    public void setAccommodationDetails(String name, String location, String checkIn,
                                        String checkOut, int numRooms, String roomType,
                                        String tripName) {
        this.name.setValue(name);
        this.location.setValue(location);
        this.checkInDate.setValue(checkIn);
        this.checkOutDate.setValue(checkOut);
        this.numberOfRooms.setValue(numRooms);
        this.roomType.setValue(roomType);
        this.tripName.setValue(tripName);

        // Checks whether location and date are valid
        boolean validName = checkInput(name);
        boolean validLocation = checkInput(location);
        boolean validRoomType = checkInput(roomType);
        boolean validNumberOfRooms = checkInput(numRooms);
        boolean validDates = checkDates(checkIn, checkOut);
        boolean validTripName = checkInput(tripName);

        // Set input errors based on validation
        if (!validName) {
            nameError.setValue("Name is required!");
        } else {
            nameError.setValue(null);
        }

        if (!validLocation) {
            locationError.setValue("Location is required!");
        } else {
            locationError.setValue(null);
        }

        if (!validRoomType) {
            roomError.setValue("Room type is required!");
        } else {
            roomError.setValue(null);
        }

        if (!validNumberOfRooms) {
            numRoomError.setValue("Invalid Number!");
        } else {
            numRoomError.setValue(null);
        }

        if (!validTripName) {
            tripError.setValue("Invalid Trip Selection!");
        } else {
            tripError.setValue(null);
        }

        // Sets the Date error message
        if (!validDates) {
            dateError.setValue("Invalid Dates!");
        } else {
            dateError.setValue(null);
        }

        // Sets the value of validInputs (true/false)
        validInputs.setValue(validName && validLocation && validRoomType
                && validNumberOfRooms && validDates && validTripName);
    }

    public void saveAccommodationDetails() {
        AccommodationDetails accommodationDetails = new AccommodationDetails(
                checkInDate.getValue(),
                checkOutDate.getValue(),
                name.getValue(),
                location.getValue(),
                numberOfRooms.getValue(),
                roomType.getValue(),
                tripName.getValue());
        UserModel.getInstance().storeAccommodationDetails(accommodationDetails);

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
                                            AccommodationDetails accommodationDetails =
                                                    new AccommodationDetails(
                                                    checkInDate.getValue(),
                                                    checkOutDate.getValue(),
                                                    name.getValue(),
                                                    location.getValue(),
                                                    numberOfRooms.getValue(),
                                                    roomType.getValue(),
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
                                                                        DatabaseReference accommodationDetailsRef = inviterTripsRef
                                                                                .child(tripId)
                                                                                .child("Accommodation Details");

                                                                        DataSnapshot accommodationDetailsSnapshot = tripSnapshot.child("Accommodation Details");
                                                                        String newAccommodationDetailsId = accommodationDetailsRef.push().getKey();

                                                                        if (newAccommodationDetailsId != null) {
                                                                            accommodationDetailsRef.child(newAccommodationDetailsId).setValue(accommodationDetails)
                                                                                    .addOnCompleteListener(task -> {
                                                                                        if (task.isSuccessful()) {
                                                                                            Log.d("Firebase", "Travel details added successfully to inviter's trip.");
                                                                                        } else {
                                                                                            Log.d("Firebase", "Failed to add travel details to inviter's trip.");
                                                                                        }
                                                                                    });
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

    public boolean checkInput(int input) {
        return input > 0;
    }

    // Checks if dates are valid
    public boolean checkDates(String date1, String date2) {
        if (date1 != null && date2 != null) {
            Log.d("checkDates", "Date1: " + date1 + ", Date2: " + date2); // Log the input dates
            try {
                // Creates a SimpleDateFormat instance, formatted as below
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.getDefault());

                // Creates new Dates following the format created above
                Date startDate = sdf.parse(date1);
                Date endDate = sdf.parse(date2);

                // Checks if the start date is less than or equal to the end date
                return !startDate.after(endDate);
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }


    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }
    public LiveData<String> getNameError() {
        return nameError;
    }
    public LiveData<String> getLocation() {
        return location;
    }
    public LiveData<String> getLocationError() {
        return locationError;
    }
    public LiveData<String> getRoomError() {
        return roomError;
    }
    public LiveData<String> getNumRoomError() {
        return numRoomError;
    }
    public LiveData<String> getTripError() {
        return tripError;
    }
    public LiveData<ArrayList<String>> getTripList() {
        return tripList;
    }
    public LiveData<String> getDateError() {
        return dateError;
    }
}
