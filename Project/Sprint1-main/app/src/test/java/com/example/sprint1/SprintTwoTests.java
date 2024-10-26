package com.example.sprint1;

import org.junit.Assert;
import org.junit.Test;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.sprint1.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = "src/main/AndroidManifest.xml", sdk = 28)
public class SprintTwoTests {
    @Test
    public void dest_travel_check() {
        DestinationModel dmm = new DestinationModel();
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
        DestinationModel dmm = new DestinationModel();
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