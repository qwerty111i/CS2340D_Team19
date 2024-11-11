package com.example.sprint1.sprint3;

import com.example.sprint1.model.AccommodationDetails;

import org.junit.Assert;
import org.junit.Test;

public class AccommodationDetailTests {
    // Michael Zuo
    @Test
    public void accom1_check() {
        AccommodationDetails a1 = new AccommodationDetails("03/20/23", "03/23/23", "atlanta", 3, "Hotel", "trip1");
        Assert.assertEquals(a1.getCheckIn(), "03/20/23");
        Assert.assertEquals(a1.getCheckOut(), "03/23/23");
        Assert.assertEquals(a1.getLocation(), "atlanta");
        Assert.assertEquals(a1.getNumRooms(), 3);
        Assert.assertEquals(a1.getRoomType(), "Hotel");
    }

    // Michael Zuo
    @Test
    public void accom2_check() {
        AccommodationDetails a1 = new AccommodationDetails("03/20/23", "03/23/23", "atlanta", 3, "Hotel", "trip1");
        a1.setLocation("ohio");
        a1.setRoomType("cubic");
        Assert.assertEquals(a1.getCheckIn(), "03/20/23");
        Assert.assertEquals(a1.getCheckOut(), "03/23/23");
        Assert.assertEquals(a1.getLocation(), "ohio");
        Assert.assertEquals(a1.getNumRooms(), 3);
        Assert.assertEquals(a1.getRoomType(), "cubic");
        Assert.assertEquals(a1.getTripName(), "trip1");
    }

    //Sahadev
    @Test
    public void accom3_check() {
        AccommodationDetails a1 = new AccommodationDetails(null, null, null, 0, null, null);
        Assert.assertNull(a1.getCheckIn());
        Assert.assertNull(a1.getCheckOut());
        Assert.assertNull(a1.getLocation());
        Assert.assertEquals(a1.getNumRooms(), 0);
        Assert.assertNull(a1.getRoomType());
        Assert.assertNull(a1.getTripName());
    }
    //Sahadev
    @Test
    public void accom4_check() {
        AccommodationDetails a1 = new AccommodationDetails("01/01/01", null, "chennai", 0, "Hotel", null);
        Assert.assertEquals(a1.getCheckIn(), "01/01/01");
        Assert.assertNull(a1.getCheckOut());
        Assert.assertEquals(a1.getLocation(), "chennai");
        Assert.assertEquals(a1.getNumRooms(), 0);
        Assert.assertEquals(a1.getRoomType(), "Hotel");
        Assert.assertNull(a1.getTripName());

    }
}