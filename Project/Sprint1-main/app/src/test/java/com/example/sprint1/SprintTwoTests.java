package com.example.sprint1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

//import android.os.Looper;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;

import com.example.sprint1.view.LoginActivity;
import com.example.sprint1.viewmodel.DestinationsViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = "src/main/AndroidManifest.xml", sdk = 28)
public class SprintTwoTests {
    // Adwaith
    @Test
    public void login_whitespace_check() {

    }

    // Adwaith
    @Test
    public void login_invalidUser_check() {
        
    }

    @Test
    public void dest_travel_check() {
        DestinationMockModel dmm = new DestinationMockModel();
        dmm.setTravelDetails("", "10/28/31", "10/29/31");
        Assert.assertEquals(dmm.getLocationError(), "Invalid Location Input!");

        dmm.setTravelDetails("Atlanta, GA", "10/28/31", "10/29/31");
        Assert.assertEquals(dmm.getLocationError(), null);
        Assert.assertEquals(dmm.areInputsValid(), true);

        dmm.setTravelDetails("Atlanta, GA", "awefefwa", "10/29/31");
        Assert.assertEquals(dmm.getDateError(), "Invalid Dates!");
    }

    @Test
    public void dest_vacation_check() {
        DestinationMockModel dmm = new DestinationMockModel();
        dmm.calculateVacationTime("32", "10/28/31", "10/29/31");
        Assert.assertEquals(dmm.getDurationError(), "Duration isn't correctly calculated!");

        dmm.calculateVacationTime("1", "10/28/31", "10/29/31");
        Assert.assertEquals(dmm.getDurationError(), null);

        dmm.calculateVacationTime("1", "10/28/31", "");
        Assert.assertEquals(dmm.getDurationError(), null);

        dmm.calculateVacationTime("", "10/28/31", "10/30/31");
        Assert.assertEquals(dmm.getDurationError(), null);

        dmm.calculateVacationTime("102", "", "10/30/31");
        Assert.assertEquals(dmm.getDurationError(), null);
    }
}