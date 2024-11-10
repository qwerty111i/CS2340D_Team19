package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import java.util.List;

public interface ReservationSortingStrategy {
    void sort(List<ReservationDetails> reservations);
}
