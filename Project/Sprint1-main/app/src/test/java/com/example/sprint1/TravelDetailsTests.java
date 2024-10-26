package com.example.sprint1;

import static org.junit.Assert.assertEquals;

import com.example.sprint1.model.TravelDetails;
import com.example.sprint1.model.TravelModel;

import org.junit.Before;
import org.junit.Test;

//Tests Done by Leila Kazemzadeh

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
}
