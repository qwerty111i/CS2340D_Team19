package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.CommunityModel;
import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TFEUser;
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

import java.net.HttpCookie;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TravelViewModel extends ViewModel {
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> startDateError = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> endDateError = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();
    private MutableLiveData<String> destination = new MutableLiveData<>();
    private MutableLiveData<String> destinationError = new MutableLiveData<>();
    private MutableLiveData<String> accommodation = new MutableLiveData<>();
    private MutableLiveData<String> accommodationError = new MutableLiveData<>();
    private MutableLiveData<String> dining = new MutableLiveData<>();
    private MutableLiveData<String> diningError = new MutableLiveData<>();
    private MutableLiveData<String> rating = new MutableLiveData<>();
    private MutableLiveData<String> ratingError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<Boolean> validRating2 = new MutableLiveData<>();

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

        boolean validDates = checkDates(startDate, endDate);
        boolean validRating2 = checkRating(rating);

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
        } else if (!validRating2) {
            ratingError.setValue("Rating must be between 0-10!");
        } else {
            ratingError.setValue(null);
        }

        // Sets the Date error message

        if (!validDates) {
            dateError.setValue("Invalid Dates!");
        } else {
            dateError.setValue(null);
        }


        // Sets the value of validInputs (true/false)
        validInputs.setValue(validStartDate && validEndDate && validDestination
                && validAccommodation && validDining && validRating && validDates && validRating2);
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

    // Base check for inputs (empty or not)
    public boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    // Base check for the rating
    public boolean checkRating(String rating) {
        try {
            int r = Integer.parseInt(rating);
            return r >= 0 && r <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
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
        CommunityModel.getInstance().storeTravelFormEntry(tfe);


    }

    private MutableLiveData<List<TFEUser>> travelEntriesLiveData = new MutableLiveData<>();

    public TravelViewModel() {
        loadTravelEntries();
    }

    public LiveData<List<TFEUser>> getTravelEntriesLiveData() {
        return travelEntriesLiveData;
    }

    private void loadTravelEntries() {
        DatabaseReference communityRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");
        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TFEUser> tfeUserList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TFEUser tfeUser = snapshot.getValue(TFEUser.class);
                    if (tfeUser != null) {
                        tfeUserList.add(tfeUser);
                    }
                }
                travelEntriesLiveData.setValue(tfeUserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TravelViewModel", "Failed to load travel entries: " + databaseError.getMessage());
            }
        });
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

    public LiveData<String> getDateError() {
        return dateError;
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
