package com.example.sprint1.sprint4;

import com.example.sprint1.model.ReservationDetails;
import com.example.sprint1.model.TFEUser;
import com.example.sprint1.model.TravelFormEntry;

import org.junit.Assert;
import org.junit.Test;

public class TravelFormEntryTest {
    // Leila Kazemzadeh
    @Test
    public void TravelFormEntryCheck1() {
        TravelFormEntry entry1 = new TravelFormEntry("05/21/23", "05/24/23", "Atlanta", "a Hotel", "a cafe, a restaurant", "3");
        Assert.assertEquals(entry1.getStartDate(), "05/21/23");
        Assert.assertEquals(entry1.getEndDate(), "05/24/23");
        Assert.assertEquals(entry1.getDestination(), "Atlanta");
        Assert.assertEquals(entry1.getAccommodation(), "a Hotel");
        Assert.assertEquals(entry1.getDining(), "a cafe, a restaurant");
        Assert.assertEquals(entry1.getRating(), "3");
    }
//Leila Kazemzadeh
    @Test
    public void TravelFormEntryCheck2() {
        TravelFormEntry entry1 = new TravelFormEntry("10/21/20", "10/30/20", "Paris", "somePlace", "eatery", "10");
        Assert.assertEquals(entry1.getStartDate(), "10/21/20");
        Assert.assertEquals(entry1.getEndDate(), "10/30/20");
        Assert.assertEquals(entry1.getDestination(), "Paris");
        Assert.assertEquals(entry1.getAccommodation(), "somePlace");
        Assert.assertEquals(entry1.getDining(), "eatery");
        Assert.assertEquals(entry1.getRating(), "10");
    }
    
    // Matthew Sebastian
    @Test
    public void TravelFormEntryCheck3() {
        TravelFormEntry entry = new TravelFormEntry();
        Assert.assertNull(entry.getStartDate());
        Assert.assertNull(entry.getEndDate());
        Assert.assertNull(entry.getDestination());
        Assert.assertNull(entry.getAccommodation());
        Assert.assertNull(entry.getDining());
        Assert.assertNull(entry.getRating());
    }

    // Matthew Sebastian
    @Test
    public void TravelFormEntryCheck4() {
        TravelFormEntry entry = new TravelFormEntry(null, null, null, null, null, null);
        Assert.assertNull(entry.getStartDate());
        Assert.assertNull(entry.getEndDate());
        Assert.assertNull(entry.getDestination());
        Assert.assertNull(entry.getAccommodation());
        Assert.assertNull(entry.getDining());
        Assert.assertNull(entry.getRating());
    }
}
