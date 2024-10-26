package com.example.sprint1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.sprint1.viewmodel.SignupViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

//Leila's Tests

public class SignUpViewModelTests {

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
