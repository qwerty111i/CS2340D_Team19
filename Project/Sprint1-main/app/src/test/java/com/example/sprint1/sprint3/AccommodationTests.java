package com.example.sprint1.sprint3;

import com.example.sprint1.model.Accommodation;

import org.junit.Assert;
import org.junit.Test;

public class AccommodationTests {
    // Michael Zuo
    @Test
    public void accom1_check() {
        Accommodation a1 = new Accommodation("03/20/23", "03/23/23", "atlanta", 3, "Hotel");
        Assert.assertEquals(a1.getCheckIn(), "03/20/23");
        Assert.assertEquals(a1.getCheckOut(), "03/23/23");
        Assert.assertEquals(a1.getLocation(), "atlanta");
        Assert.assertEquals(a1.getNumRooms(), 3);
        Assert.assertEquals(a1.getRoomType(), "Hotel");
    }

    // Michael Zuo
    @Test
    public void accom2_check() {
        Accommodation a1 = new Accommodation("03/20/23", "03/23/23", "atlanta", 3, "Hotel");
        a1.setLocation("ohio");
        a1.setRoomType("cubic");
        a1.setTrip("trip1");
        Assert.assertEquals(a1.getCheckIn(), "03/20/23");
        Assert.assertEquals(a1.getCheckOut(), "03/23/23");
        Assert.assertEquals(a1.getLocation(), "ohio");
        Assert.assertEquals(a1.getNumRooms(), 3);
        Assert.assertEquals(a1.getRoomType(), "cubic");
        Assert.assertEquals(a1.getTrip(), "trip1");
    }
}
