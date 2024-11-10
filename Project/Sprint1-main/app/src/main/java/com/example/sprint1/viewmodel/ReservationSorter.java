package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import java.util.List;

public class ReservationSorter {
    private ReservationSortingStrategy strategy;

    public void setStrategy(ReservationSortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortReservations(List<ReservationDetails> reservations) {
        if (strategy != null) {
            strategy.sort(reservations);
        }
    }
}
