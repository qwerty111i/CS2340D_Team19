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

    // Adwaith Test 1
    @Test
    public void TransportationTest3() {
        TransportationDetails test1 = new TransportationDetails(null, null, null, null, null, null);
        Assert.assertNull(test1.getType());
        Assert.assertNull(test1.getStartLocation());
        Assert.assertNull(test1.getEndLocation());
        Assert.assertNull(test1.getStartDate());
        Assert.assertNull(test1.getStartTime());
        Assert.assertNull(test1.getTripName());
    }

    // Adwaith Test 2
    @Test
    public void TransportationTest4() {
        TransportationDetails test2 = new TransportationDetails("Train", "California", null, null, null, "Trip A");
        Assert.assertEquals(test2.getType(), "Train");
        Assert.assertEquals(test2.getStartLocation(), "California");
        Assert.assertNull(test2.getEndLocation());
        Assert.assertNull(test2.getStartDate());
        Assert.assertNull(test2.getStartTime());
        Assert.assertEquals(test2.getTripName(), "Trip A");
    }
}