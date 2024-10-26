package com.example.sprint1;


import com.example.sprint1.viewmodel.SignupViewModel;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

//Leila's Tests

public class CheckInputTests {

    private SignupViewModel signupViewModelInstance = new SignupViewModel(true);

    @Test
    public void testCheckInputWithValidInput(){
        String validInput = "validinput";
        boolean inputResult = signupViewModelInstance.checkInput(validInput);
        assertTrue(inputResult); // Valid input is expected to return true
    }


    @Test
    public  void testCheckInputWithNull(){
        String nullInput = null;
        boolean inputResult = signupViewModelInstance.checkInput(nullInput);
        assertFalse(inputResult); // Null input is expected to return false
    }


}
