package com.example.sprint1;

import static org.junit.Assert.assertEquals;

import com.example.sprint1.model.TravelDetails;
import com.example.sprint1.model.TravelModel;

import org.junit.Before;
import org.junit.Test;

//Tests for each of the methods in Travel Details (should I delete?)

public class TravelDetailsTests {

    private TravelDetails travelDetails;

    @Before
    public void before(){
        travelDetails = new TravelDetails("LA", "10/13/24", "12/17/25");
    }

    @Test
    public void testingGetLocation(){
        assertEquals("LA", travelDetails.getLocation());
    }

    @Test
    public void testingGetStartDate(){
        assertEquals("10/13/24", travelDetails.getStartDate());
    }

    @Test
    public void testingGetEndDate(){
        assertEquals("12/17/25", travelDetails.getEndDate());
    }

    //Sahadev
    @Test
    public void testingAllThree() {
        assertEquals("LA", travelDetails.getLocation());
        assertEquals("10/13/24", travelDetails.getStartDate());
        assertEquals("12/17/25", travelDetails.getEndDate());
    }
}
