package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.Accommodation;
import com.example.sprint1.model.AccommodationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public void setAccommodationDetails(String location, String checkIn, String checkOut,
                                        int numRooms, String roomType) {
        this.location.setValue(location);
        this.checkInDate.setValue(checkIn);
        this.checkOutDate.setValue(checkOut);
        this.numberOfRooms.setValue(numRooms);
        this.roomType.setValue(roomType);

        // Checks whether location and date are valid
        boolean validLocation = checkInput(location);
        boolean validRoomType = checkInput(roomType);
        boolean validNumberOfRooms = checkInput(numRooms);
        boolean validDates = checkDates(checkIn, checkOut);

        // Set input errors based on validation
        if (!validLocation) {
            inputError.setValue("Location is required!");
        } else if (!validRoomType) {
            inputError.setValue("Room type is required!");
        } else if (!validNumberOfRooms) {
            inputError.setValue("Number of rooms must be greater than 0!");
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
        validInputs.setValue(validLocation && validRoomType && validNumberOfRooms && validDates);
    }

    public void saveDetails() {
        Accommodation accommodation = new Accommodation(
                checkInDate.getValue(),
                checkOutDate.getValue(),
                location.getValue(),
                numberOfRooms.getValue(),
                roomType.getValue());

        AccommodationModel.getInstance().storeAccommodation(accommodation);
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
    public LiveData<String> getDateError() {
        return dateError;
    }
    public LiveData<String> getInputError() {
        return inputError;
    }

}
