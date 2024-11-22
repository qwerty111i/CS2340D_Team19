package com.example.sprint1.sprint4;

import com.example.sprint1.model.TFEUser;
import com.example.sprint1.model.TravelFormEntry;

import org.junit.Assert;
import org.junit.Test;

public class TFEUserTest {
    // Michael Zuo
    @Test
    public void tfeusertest1() {
        TravelFormEntry t1 = new TravelFormEntry("03/20/23", "03/23/23", "atlanta", "hyatt", "wendys", "trip1");
        TFEUser tu = new TFEUser(t1, "a2");
        Assert.assertEquals(tu.getTFE(), t1);
        Assert.assertEquals(tu.getUserId(), "a2");
    }

    // Michael Zuo
    @Test
    public void tfeusertest2() {
        TravelFormEntry t1 = new TravelFormEntry("03/20/23", "03/23/23", "atlanta", "hyatt", "wendys", "trip1");
        Assert.assertEquals(t1.getDining(), "wendys");
        Assert.assertEquals(t1.getAccommodation(), "hyatt");
        Assert.assertEquals(t1.getStartDate(), "03/20/23");
    }
}
