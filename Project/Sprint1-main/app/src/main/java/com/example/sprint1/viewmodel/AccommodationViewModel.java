package com.example.sprint1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.Accommodation;
import com.example.sprint1.model.AccommodationModel;

public class AccommodationViewModel extends ViewModel {
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> checkInDate = new MutableLiveData<>();
    private MutableLiveData<String> checkOutDate = new MutableLiveData<>();
    private MutableLiveData<Integer> numberOfRooms = new MutableLiveData<>();
    private MutableLiveData<String> roomType = new MutableLiveData<>();
    public void saveDetails() {
        Accommodation accommodation = new Accommodation(
                checkInDate.getValue(),
                checkOutDate.getValue(),
                location.getValue(),
                numberOfRooms.getValue(),
                roomType.getValue());

        AccommodationModel.getInstance().storeAccommodation(accommodation);
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

}
