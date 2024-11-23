package com.example.sprint1.sprint4;




import com.example.sprint1.model.TransportationDetails;

import org.junit.Assert;
import org.junit.Test;
public class TransportationTest {
    //Sahadev Test1
    @Test
    public void transportationtest1() {
        TransportationDetails test1 = new TransportationDetails("Car", "chennai", "mumbai", "10/10/2023", "10:30PM", "Trip1");
        Assert.assertEquals(test1.getType(), "Car");
        Assert.assertEquals(test1.getStartLocation(), "chennai");
        Assert.assertEquals(test1.getEndLocation(), "mumbai");
        Assert.assertEquals(test1.getStartDate(), "10/10/2023");
        Assert.assertEquals(test1.getStartTime(), "10:30PM");
        Assert.assertEquals(test1.getTripName(), "Trip1");
    }

    //Sahadev Test2
    @Test
    public void transportationtest2() {
        TransportationDetails test2 = new TransportationDetails("Bus", "Florida", "Atlanta", "10/11/2024", "11:00AM", "Trip2");
        Assert.assertEquals(test2.getType(), "Bus");
        Assert.assertEquals(test2.getStartLocation(), "Florida");
        Assert.assertEquals(test2.getEndLocation(), "Atlanta");
        Assert.assertEquals(test2.getStartDate(), "10/11/2024");
        Assert.assertEquals(test2.getStartTime(), "11:00AM");
        Assert.assertEquals(test2.getTripName(), "Trip2");
    }
}