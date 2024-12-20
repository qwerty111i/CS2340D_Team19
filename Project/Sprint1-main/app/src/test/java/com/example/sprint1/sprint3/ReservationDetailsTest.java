package com.example.sprint1.sprint3;

import com.example.sprint1.model.AccommodationDetails;
import com.example.sprint1.model.ReservationDetails;

import org.junit.Assert;
import org.junit.Test;

public class ReservationDetailsTest {

    // Leila Kazemzadeh
    @Test
    public void reservation1_check() {
        ReservationDetails reserve1 = new ReservationDetails("Papa Johns", "Atlanta", "www.papajohns.com", "03/23/23", "12:00 PM", "US Trip");
        Assert.assertEquals(reserve1.getName(), "Papa Johns");
        Assert.assertEquals(reserve1.getLocation(), "Atlanta");
        Assert.assertEquals(reserve1.getWebsite(), "www.papajohns.com");
        Assert.assertEquals(reserve1.getDate(), "03/23/23");
        Assert.assertEquals(reserve1.getTime(), "12:00 PM");
        Assert.assertEquals(reserve1.getTripName(), "US Trip");
    }

    // Leila Kazemzadeh
    @Test
    public void reservation2_check() {
        ReservationDetails reserve2 = new ReservationDetails("My Cafe", "Madrid", "www.myCafe.com", "12/27/23", "8:00 PM", "Europe Trip");
        Assert.assertEquals(reserve2.getName(), "My Cafe");
        Assert.assertEquals(reserve2.getLocation(), "Madrid");
        Assert.assertEquals(reserve2.getWebsite(), "www.myCafe.com");
        Assert.assertEquals(reserve2.getDate(), "12/27/23");
        Assert.assertEquals(reserve2.getTime(), "8:00 PM");
        Assert.assertEquals(reserve2.getTripName(), "Europe Trip");
    }

    // Adwaith
    @Test
    public void reservation3_check() {
        ReservationDetails reserve1 = new ReservationDetails(null, null, null, null, null, null);
        Assert.assertNull(reserve1.getName());
        Assert.assertNull(reserve1.getLocation());
        Assert.assertNull(reserve1.getWebsite());
        Assert.assertNull(reserve1.getDate());
        Assert.assertNull(reserve1.getTime());
        Assert.assertNull(reserve1.getTripName());
    }

    // Adwaith
    @Test
    public void reservation4_check() {
        ReservationDetails reserve2 = new ReservationDetails("My Cafe", null, "www.myCafe.com", "12/27/23", "8:00 PM", "Europe Trip");
        Assert.assertEquals(reserve2.getName(), "My Cafe");
        Assert.assertNull(reserve2.getLocation());
        Assert.assertEquals(reserve2.getWebsite(), "www.myCafe.com");
        Assert.assertEquals(reserve2.getDate(), "12/27/23");
        Assert.assertEquals(reserve2.getTime(), "8:00 PM");
        Assert.assertEquals(reserve2.getTripName(), "Europe Trip");
    }
}
