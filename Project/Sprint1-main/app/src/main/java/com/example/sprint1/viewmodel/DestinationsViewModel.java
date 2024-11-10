package com.example.sprint1.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.TravelDetails;
import com.example.sprint1.model.Trip;
import com.example.sprint1.model.UserModel;
import com.example.sprint1.model.VacationTime;
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
import java.util.Calendar;

public class DestinationsViewModel extends ViewModel {
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> locationError = new MutableLiveData<>();
    private MutableLiveData<String> durationError = new MutableLiveData<>();
    private MutableLiveData<String> startDateError = new MutableLiveData<>();
    private MutableLiveData<String> endDateError = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<Boolean> validCalcInputs = new MutableLiveData<>();
    private MutableLiveData<String> duration = new MutableLiveData<>();
    private MutableLiveData<String> startVacationDate = new MutableLiveData<>();
    private MutableLiveData<String> endVacationDate = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<String> tripName = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> tripList = new MutableLiveData<>();

    public void setTravelDetails(String location, String startDate,
                                 String endDate, String tripName) {
        // Sets the values of the MutableLiveData
        this.location.setValue(location);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);
        this.tripName.setValue(tripName);

        // Checks whether location and date are valid
        boolean validLocation = checkInput(location);
        boolean validDates = checkDates(startDate, endDate);

        // Sets the Location error message
        if (!validLocation) {
            locationError.setValue("Invalid Location Input!");
        } else {
            locationError.setValue(null);
        }

        // Sets the Date error message
        if (!validDates) {
            dateError.setValue("Invalid Dates!");
        } else {
            dateError.setValue(null);
        }

        // Sets the value of validInputs (true/false)
        validInputs.setValue(validLocation && validDates);
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

    public void calculateVacationTime(String duration, String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

        boolean isDurationValid = isValidDuration(duration);
        boolean isStartDateValid = isValidDate(startDate, formatter);
        boolean isEndDateValid = isValidDate(endDate, formatter);
        boolean validDates = checkDates(startDate, endDate);

        if (!isStartDateValid && !isEndDateValid && !isDurationValid) {
            durationError.setValue("Invalid Duration!");
            startDateError.setValue("Invalid Start Date!");
            endDateError.setValue("Invalid End Date!");
            validCalcInputs.setValue(false);
        } else if (!isStartDateValid && !isEndDateValid) {
            durationError.setValue(null);
            startDateError.setValue("Invalid Start Date!");
            endDateError.setValue("Invalid End Date!");
            validCalcInputs.setValue(false);
        } else if (!isDurationValid && !isEndDateValid) {
            durationError.setValue("Invalid Duration!");
            startDateError.setValue(null);
            endDateError.setValue("Invalid End Date!");
            validCalcInputs.setValue(false);
        } else if (!isStartDateValid && !isDurationValid) {
            startDateError.setValue("Invalid Start Date!");
            durationError.setValue("Invalid Duration!");
            endDateError.setValue(null);
            validCalcInputs.setValue(false);
        } else if (isStartDateValid && isEndDateValid && !validDates) {
            startDateError.setValue("Invalid Start Date!");
            endDateError.setValue("Invalid End Date!");
            if (!isDurationValid) {
                durationError.setValue("Invalid Duration!");
            } else {
                durationError.setValue(null);
            }
            validCalcInputs.setValue(false);
        } else if (!isDurationValid) {
            try { //valid case
                // Calculate duration
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                int dur = (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
                this.duration.setValue("" + dur);
                this.startVacationDate.setValue(startDate);
                this.endVacationDate.setValue(endDate);
                startDateError.setValue(null);
                durationError.setValue(null);
                endDateError.setValue(null);
                validCalcInputs.setValue(true);

                toastMessage.setValue("Your calculated duration is " + dur + " days.");
            } catch (ParseException e) {
                // This should not occur since we have already validated the date formats
            }
        } else if (!isEndDateValid) {
            try {  //valid case
                // Calculate end date
                Date start = formatter.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(duration));
                Date end = calendar.getTime();
                this.endVacationDate.setValue(formatter.format(end));
                this.startVacationDate.setValue(startDate);
                this.duration.setValue(duration);
                startDateError.setValue(null);
                durationError.setValue(null);
                endDateError.setValue(null);
                validCalcInputs.setValue(true);

                toastMessage.setValue("Your calculated End Date is " + formatter.format(end) + ".");
            } catch (ParseException e) {
                // This should not occur since we have already validated the date formats
            }
        } else if (!isStartDateValid) {
            try {  //valid case
                // Calculate start date
                Date end = formatter.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(duration));
                Date start = calendar.getTime();
                this.startVacationDate.setValue(formatter.format(start));
                this.endVacationDate.setValue(endDate);
                this.duration.setValue(duration);
                startDateError.setValue(null);
                durationError.setValue(null);
                endDateError.setValue(null);
                validCalcInputs.setValue(true);

                toastMessage.setValue(
                        "Your calculated Start Date is " + formatter.format(start) + ".");
            } catch (ParseException e) {
                // This should not occur since we have already validated the date formats
            }
        } else {
            try {
                // Calculate duration
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                int dur = (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
                int d2 = Integer.parseInt(duration);
                if (dur != d2) {
                    durationError.setValue("Duration isn't correctly calculated!");
                    startDateError.setValue("Invalid Start Date!");
                    endDateError.setValue("Invalid End Date!");
                    validCalcInputs.setValue(false);
                } else { //valid case
                    this.startVacationDate.setValue(startDate);
                    this.endVacationDate.setValue(endDate);
                    this.duration.setValue(duration);
                    startDateError.setValue(null);
                    durationError.setValue(null);
                    endDateError.setValue(null);
                    validCalcInputs.setValue(true);

                    toastMessage.setValue("All values are valid!");
                }
            } catch (ParseException e) {
                // This should not occur since we have already validated the date formats
            }
        }
    }

    // Base check for inputs (empty or not)
    public boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    // Checks if dates are valid
    public boolean checkDates(String date1, String date2) {
        if (date1 != null && date2 != null) {
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

    // Helper method to validate date format
    private boolean isValidDate(String dateStr, SimpleDateFormat formatter) {
        if (dateStr == null) {
            return true;
        }  // Null means it's not required in some cases
        try {
            formatter.setLenient(false);
            formatter.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Helper method to validate duration as an integer
    private boolean isValidDuration(String duration) {
        if (duration == null) {
            return true;
        }  // Null means it's not required in some cases
        try {
            Integer.parseInt(duration);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Saves the details in the database
    public void saveTravelDetails() {
        // Creates a new TravelDetails object, storing all the data
        TravelDetails travelDetails = new TravelDetails(
                location.getValue(),
                startDate.getValue(),
                endDate.getValue(),
                tripName.getValue());

        // Uses the Singleton implemented Database to store information
        UserModel.getInstance().storeTravelDetails(travelDetails);
    }

    // Saves the calculation details in the database
    public void saveVacationTimeDetails() {
        // Creates a new VacationTime object, storing all the data
        VacationTime vtime = new VacationTime(
                startVacationDate.getValue(),
                endVacationDate.getValue(),
                Integer.parseInt(duration.getValue()));

        // Uses the Singleton implemented user Database to store information
        UserModel.getInstance().storeVacation(vtime);
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<Boolean> areCalcInputsValid() {
        return validCalcInputs;
    }

    public LiveData<String> getLocationError() {
        return locationError;
    }

    public LiveData<String> getDurationError() {
        return durationError;
    }

    public LiveData<String> getDateError() {
        return dateError;
    }

    public LiveData<String> getStartDateError() {
        return startDateError;
    }

    public LiveData<String> getEndDateError() {
        return endDateError;
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public LiveData<String> getStartDate() {
        return startDate;
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<ArrayList<String>> getTripList() {
        return tripList;
    }
}
