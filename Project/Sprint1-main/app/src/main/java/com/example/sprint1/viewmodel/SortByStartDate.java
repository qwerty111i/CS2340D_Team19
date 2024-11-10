package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SortByStartDate implements ReservationSortingStrategy {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy", Locale.getDefault());

    @Override
    public void sort(List<ReservationDetails> reservations) {
        Collections.sort(reservations, (r1, r2) -> {
            try {
                Date date1 = dateFormat.parse(r1.getStartDate());
                Date date2 = dateFormat.parse(r2.getStartDate());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
    }
}
