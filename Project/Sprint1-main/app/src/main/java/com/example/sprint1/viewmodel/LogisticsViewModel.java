package com.example.sprint1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class LogisticsViewModel extends ViewModel {
    private MutableLiveData<Integer> allottedTime;
    private MutableLiveData<Integer> plannedTime;
    public LiveData<Integer> getAllottedTime() {
        return allottedTime;
    }
    public  LiveData<Integer> getPlannedTime() {
        return plannedTime;
    }
}
