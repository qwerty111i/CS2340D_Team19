package com.example.sprint1.sprint4;

import com.example.sprint1.model.TransportationDetails;
import com.example.sprint1.viewmodel.CustomSorter;
import com.example.sprint1.viewmodel.SortByName;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TransportationSorterTest {
    // james
    @Test
    public void testSortByStartLocationName() {
        TransportationDetails transport1 = new TransportationDetails("Boat", "A", "B", "03/23/23", "12:00 PM", "Trip A");
        TransportationDetails transport2 = new TransportationDetails("Boat", "Z", "B", "03/23/23", "12:00 PM", "Trip A");
        TransportationDetails transport3 = new TransportationDetails("Boat", "C", "B", "03/23/23", "12:00 PM", "Trip A");

        List<TransportationDetails> transportations = new ArrayList<>();
        transportations.add(transport1);
        transportations.add(transport2);
        transportations.add(transport3);

        CustomSorter sorter = new CustomSorter();
        sorter.setStrategy(new SortByName());
        sorter.sortTransportation(transportations);

        Assert.assertEquals("A", transportations.get(0).getStartLocation());
        Assert.assertEquals("C", transportations.get(1).getStartLocation());
        Assert.assertEquals("Z", transportations.get(2).getStartLocation());
    }

    // james
    @Test
    public void testSortByStartDate() {
        TransportationDetails transport1 = new TransportationDetails("Boat", "A", "B", "03/23/23", "12:00 PM", "Trip A");
        TransportationDetails transport2 = new TransportationDetails("Boat", "Z", "B", "03/25/23", "12:00 PM", "Trip A");
        TransportationDetails transport3 = new TransportationDetails("Boat", "C", "B", "03/24/23", "12:00 PM", "Trip A");

        List<TransportationDetails> transportations = new ArrayList<>();
        transportations.add(transport1);
        transportations.add(transport2);
        transportations.add(transport3);

        CustomSorter sorter = new CustomSorter();
        sorter.setStrategy(new SortByName());
        sorter.sortTransportation(transportations);

        Assert.assertEquals("03/23/23", transportations.get(0).getStartDate());
        Assert.assertEquals("03/24/23", transportations.get(1).getStartDate());
        Assert.assertEquals("03/25/23", transportations.get(2).getStartDate());
    }
}
