package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TransportationDetails;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByName implements CustomSortingStrategy {
    @Override
    public void sortReservation(List<ReservationDetails> reservations) {
        Collections.sort(reservations, Comparator.comparing(ReservationDetails::getName));
    }
    @Override
    public void sortTransportation(List<TransportationDetails> transportations) {
        Collections.sort(transportations, Comparator.comparing(TransportationDetails::getStartLocation));
    }
}
