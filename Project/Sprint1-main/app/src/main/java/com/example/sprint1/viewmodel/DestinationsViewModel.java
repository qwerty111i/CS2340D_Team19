package com.example.sprint1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.TravelDetails;
import com.example.sprint1.model.TravelModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationsViewModel extends ViewModel {
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> locationError = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<Integer> duration = new MutableLiveData<>();
    private MutableLiveData<String> startVacationDate = new MutableLiveData<>();
    private MutableLiveData<String> endVacationDate = new MutableLiveData<>();

    public void setTravelDetails(String location, String startDate, String endDate) {
        // Sets the values of the MutableLiveData
        this.location.setValue(location);
        this.startDate.setValue(startDate);
        this.endDate.setValue(endDate);

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

    // Saves the details in the database
    public void saveDetails() {
        // Creates a new TravelDetails object, storing all the data
        TravelDetails travelDetails = new TravelDetails(
                location.getValue(),
                startDate.getValue(),
                endDate.getValue());

        // Uses the Singleton implemented Database to store information
        TravelModel.getInstance().storeTravelDetails(travelDetails);
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<String> getLocationError() {
        return locationError;
    }

    public LiveData<String> getDateError() {
        return dateError;
    }
}
