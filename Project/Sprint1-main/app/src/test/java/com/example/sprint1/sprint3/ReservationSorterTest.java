package com.example.sprint1.sprint3;

import static org.junit.Assert.assertTrue;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.viewmodel.ReservationSorter;
import com.example.sprint1.viewmodel.SortByName;
import com.example.sprint1.viewmodel.SortByDateAndTime;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReservationSorterTest {

    // james
    @Test
    public void testSortByLocation() {
        ReservationDetails reserve1 = new ReservationDetails(
                "Reservation 2", "Chicago", "www.chicago.com", "03/23/23", "12:00 PM", "Trip A");
        ReservationDetails reserve2 = new ReservationDetails(
                "Reservation 1", "Atlanta", "www.atlanta.com", "03/22/23", "1:00 PM", "Trip A");
        ReservationDetails reserve3 = new ReservationDetails(
                "Reservation 3", "Boston", "www.boston.com", "03/24/23", "2:00 PM", "Trip A");

        List<ReservationDetails> reservations = new ArrayList<>();
        reservations.add(reserve1);
        reservations.add(reserve2);
        reservations.add(reserve3);

        ReservationSorter sorter = new ReservationSorter();
        sorter.setStrategy(new SortByName());
        sorter.sortReservations(reservations);

        Assert.assertEquals("Reservation 2", reservations.get(0).getName());
        Assert.assertEquals("Reservation 1", reservations.get(1).getLocation());
        Assert.assertEquals("Reservation 3", reservations.get(2).getLocation());
    }

    // james
    @Test
    public void testSortByStartDate() {
        ReservationDetails reserve1 = new ReservationDetails(
                "Reservation 1", "Location", "www.example.com", "03/22/23", "10:00 AM", "Trip A");
        ReservationDetails reserve2 = new ReservationDetails(
                "Reservation 2", "Location", "www.example.com", "03/22/23", "2:00 PM", "Trip A");
        ReservationDetails reserve3 = new ReservationDetails(
                "Reservation 3", "Location", "www.example.com", "03/22/23", null, "Trip A"); // No time
        ReservationDetails reserve4 = new ReservationDetails(
                "Reservation 4", "Location", "www.example.com", "03/21/23", "9:00 AM", "Trip A");
        ReservationDetails reserve5 = new ReservationDetails(
                "Reservation 5", "Location", "www.example.com", "03/23/23", "8:00 AM", "Trip A");

        List<ReservationDetails> reservations = new ArrayList<>();
        reservations.add(reserve1);
        reservations.add(reserve2);
        reservations.add(reserve3);
        reservations.add(reserve4);
        reservations.add(reserve5);

        ReservationSorter sorter = new ReservationSorter();
        sorter.setStrategy(new SortByDateAndTime());
        sorter.sortReservations(reservations);

        Assert.assertEquals("03/21/23", reservations.get(0).getDate());
        Assert.assertEquals("9:00 AM", reservations.get(0).getTime());

        Assert.assertEquals("03/22/23", reservations.get(1).getDate());
        Assert.assertEquals("10:00 AM", reservations.get(1).getTime());

        Assert.assertEquals("03/22/23", reservations.get(2).getDate());
        Assert.assertEquals("2:00 PM", reservations.get(2).getTime());

        Assert.assertEquals("03/22/23", reservations.get(3).getDate());
        Assert.assertNull(reservations.get(3).getTime()); // no time

        Assert.assertEquals("03/23/23", reservations.get(4).getDate());
        Assert.assertEquals("8:00 AM", reservations.get(4).getTime());
    }

    // Matthew
    @Test
    public void testSortByLocationEmpty() {
        List<ReservationDetails> reservations = new ArrayList<>();

        ReservationSorter sorter = new ReservationSorter();
        sorter.setStrategy(new SortByName());
        sorter.sortReservations(reservations);

        assertTrue(reservations.isEmpty());
    }

    // Matthew
    @Test
    public void testSortByStartDateEmpty() {
        List<ReservationDetails> reservations = new ArrayList<>();

        ReservationSorter sorter = new ReservationSorter();
        sorter.setStrategy(new SortByDateAndTime());
        sorter.sortReservations(reservations);

        assertTrue(reservations.isEmpty());
    }
}
