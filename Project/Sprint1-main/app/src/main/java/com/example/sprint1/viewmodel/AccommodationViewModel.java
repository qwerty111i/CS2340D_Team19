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

    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> checkInDate = new MutableLiveData<>();
    private MutableLiveData<String> checkOutDate = new MutableLiveData<>();
    private MutableLiveData<Integer> numberOfRooms = new MutableLiveData<>();
    private MutableLiveData<String> roomType = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();
    private MutableLiveData<String> inputError = new MutableLiveData<>();
    private MutableLiveData<String> trip = new MutableLiveData<>();
    private MutableLiveData<String> tripError = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> tripList = new MutableLiveData<>();

    public void setAccommodationDetails(String location, String checkIn, String checkOut,
                                        int numRooms, String roomType, String trip) {
        this.location.setValue(location);
        this.checkInDate.setValue(checkIn);
        this.checkOutDate.setValue(checkOut);
        this.numberOfRooms.setValue(numRooms);
        this.roomType.setValue(roomType);
        this.trip.setValue(trip);

        // Checks whether location and date are valid
        boolean validLocation = checkInput(location);
        boolean validRoomType = checkInput(roomType);
        boolean validNumberOfRooms = checkInput(numRooms);
        boolean validDates = checkDates(checkIn, checkOut);
        boolean validTrip = checkInput(trip);

        // Set input errors based on validation
        if (!validLocation) {
            inputError.setValue("Location is required!");
        } else if (!validRoomType) {
            inputError.setValue("Room type is required!");
        } else if (!validNumberOfRooms) {
            inputError.setValue("Number of rooms must be greater than 0!");
        } else if (!validTrip) {
            inputError.setValue("Invalid Trip Selection!");
        } else {
            inputError.setValue(null); // Clear error if all inputs are valid
        }

        // Sets the Date error message
        if (!validDates) {
            dateError.setValue("Invalid Dates!");
        } else {
            dateError.setValue(null);
        }

        // Sets the value of validInputs (true/false)
        validInputs.setValue(validLocation && validRoomType && validNumberOfRooms && validDates && validTrip);
    }

    public void saveAccommodationDetails() {
        AccommodationDetails accommodationDetails = new AccommodationDetails(
                checkInDate.getValue(),
                checkOutDate.getValue(),
                location.getValue(),
                numberOfRooms.getValue(),
                roomType.getValue(),
                trip.getValue());
        UserModel.getInstance().storeAccommodationDetails(accommodationDetails);
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


//    public void fetchAccommodations() {
//        String currentEmail = null;
//
//        //Initialize Firebase
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference databaseRef = database.getReference();
//
//        //Get currently logged in user
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            currentEmail = currentUser.getEmail();
//            Log.d("UserEmail", "User email: " + currentEmail);
//        }
//
//
//        DatabaseReference accommodationDatabaseRef = databaseRef.child("users");
//
//        accommodationDatabaseRef.orderByChild("email").equalTo(currentEmail).addValueEventListener(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //clear list of accommodations to avoid duplication
//                        accommodationsList.clear();
//
//                        for (DataSnapshot accommodationsSnapshot : snapshot.getChildren()) {
//                            addAccommodationsToList(accommodationsSnapshot.child("accommodations"));
//                        }
//
//                        //verify data retrieval
//                        for(int i = 0; i < accommodationsList.size(); i++){
//                        Log.d("Firebase", "Check in: " + accommodationsList.get(i).getCheckIn());
//                        Log.d("Firebase", "Check out: " + accommodationsList.get(i).getCheckOut());
//                        Log.d("Firebase", "RoomType: " + accommodationsList.get(i).getRoomType());
//                        Log.d("Firebase", "Location: " + accommodationsList.get(i).getLocation());
//                        Log.d("Firebase", "Num Rooms: " + accommodationsList.get(i).getNumRooms());
//                        //check in, checkout, location, num of rooms, room type
//                        }
//                        // Notify adapter
//                        adapter.notifyDataSetChanged();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d("Firebase", "Error retrieving data");
//
//                    }
//                });
//    }

//    public void addAccommodationsToList(DataSnapshot accommodationsSnapshot) {
//        //Loop through each accommodation
//        for (DataSnapshot snapshot : accommodationsSnapshot.getChildren()) {
//
//            String checkIn = snapshot.child("checkIn").getValue(String.class);
//            String checkOut = snapshot.child("checkOut").getValue(String.class);
//            int numRooms = snapshot.child("numRooms").getValue(int.class);
//            String roomType = snapshot.child("roomType").getValue(String.class);
//            String location = snapshot.child("location").getValue(String.class);
//
//            Accommodation accommodation = new Accommodation(checkIn, checkOut, location, numRooms, roomType);
//            accommodationsList.add(accommodation);
//        }
//
//    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }
    public LiveData<String> getLocation() {
        return location;
    }
    public LiveData<String> getCheckInDate() {
        return checkInDate;
    }
    public LiveData<String> getCheckOutDate() {
        return checkOutDate;
    }
    public LiveData<Integer> getNumberOfRooms() {
        return numberOfRooms;
    }
    public LiveData<String> getRoomType() {
        return roomType;
    }
    public LiveData<String> getTripError() { return tripError; }
    public LiveData<ArrayList<String>> getTripList() { return tripList; }
    public LiveData<String> getDateError() {
        return dateError;
    }
    public LiveData<String> getInputError() {
        return inputError;
    }

}
