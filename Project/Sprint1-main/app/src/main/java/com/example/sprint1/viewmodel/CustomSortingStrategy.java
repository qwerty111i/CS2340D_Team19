package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TransportationDetails;

import java.util.List;

public interface CustomSortingStrategy {
    void sortReservation(List<ReservationDetails> reservations);
    void sortTransportation(List<TransportationDetails> transportations);
}
