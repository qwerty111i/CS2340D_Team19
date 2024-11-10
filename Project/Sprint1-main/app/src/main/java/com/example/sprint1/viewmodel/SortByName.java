package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByName implements ReservationSortingStrategy {
    @Override
    public void sort(List<ReservationDetails> reservations) {
        Collections.sort(reservations, Comparator.comparing(ReservationDetails::getName));
    }
}
