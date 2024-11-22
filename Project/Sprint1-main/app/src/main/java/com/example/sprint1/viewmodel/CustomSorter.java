package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TransportationDetails;

import java.util.List;

public class CustomSorter {
    private CustomSortingStrategy strategy;

    public void setStrategy(CustomSortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortReservations(List<ReservationDetails> reservations) {
        if (strategy != null) {
            strategy.sortReservation(reservations);
        }
    }

    public void sortTransportation(List<TransportationDetails> transportations) {
        if (strategy != null) {
            strategy.sortTransportation(transportations);
        }
    }
}
