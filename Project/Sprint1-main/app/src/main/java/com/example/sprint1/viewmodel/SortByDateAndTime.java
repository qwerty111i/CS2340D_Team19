package com.example.sprint1.viewmodel;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TransportationDetails;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SortByDateAndTime implements CustomSortingStrategy {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy",
            Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a",
            Locale.getDefault());

    @Override
    public void sortReservation(List<ReservationDetails> reservations) {
        Collections.sort(reservations, new Comparator<ReservationDetails>() {
            @Override
            public int compare(ReservationDetails r1, ReservationDetails r2) {
                try {
                    Date date1 = dateFormat.parse(r1.getDate());
                    Date date2 = dateFormat.parse(r2.getDate());

                    int dateComparison = date1.compareTo(date2);
                    if (dateComparison != 0) {
                        return dateComparison;
                    } else {
                        Date time1 = parseTimeOrDefault(r1.getTime());
                        Date time2 = parseTimeOrDefault(r2.getTime());
                        return time1.compareTo(time2);
                    }
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    public void sortTransportation(List<TransportationDetails> transportation) {
        Collections.sort(transportation, new Comparator<TransportationDetails>() {
            @Override
            public int compare(TransportationDetails r1, TransportationDetails r2) {
                try {
                    Date date1 = dateFormat.parse(r1.getStartDate());
                    Date date2 = dateFormat.parse(r2.getStartDate());

                    int dateComparison = date1.compareTo(date2);
                    if (dateComparison != 0) {
                        return dateComparison;
                    } else {
                        Date time1 = parseTimeOrDefault(r1.getStartDate());
                        Date time2 = parseTimeOrDefault(r2.getStartDate());
                        return time1.compareTo(time2);
                    }
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    private Date parseTimeOrDefault(String timeStr) throws Exception {
        if (timeStr == null || timeStr.isEmpty()) {
            return timeFormat.parse("11:59 PM");
        }
        return timeFormat.parse(timeStr);
    }
}
