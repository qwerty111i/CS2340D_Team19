package com.example.sprint1.sprint2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.sprint1.viewmodel.SignupViewModel;

import org.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class UsernameTest {
    // Allows live data to execute synchronously
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SignupViewModel viewModel;

    // Creating a new instance of LoginViewModel
    @Before
    public void setUp() {
        Boolean test = true;
        viewModel = new SignupViewModel(test);
    }

    // Matthew
    @Test
    public void noUsername() {
        viewModel.signInValidation();
        viewModel.setEmail("ad@min.com");
        viewModel.setUsername("");
        viewModel.setPassword("123456");
        viewModel.signInValidation();
        assertNull(viewModel.getEmailError().getValue());
        assertNull(viewModel.getUsernameError().getValue());
        assertNull(viewModel.getPasswordError().getValue());
    }
    public void validUsername() {
        viewModel.signInValidation();
        viewModel.setEmail("ad@min.com");
        viewModel.setUsername("nonsense");
        viewModel.setPassword("123456");
        viewModel.signInValidation();
        assertNull(viewModel.getEmailError().getValue());
        assertNull(viewModel.getUsernameError().getValue());
        assertNull(viewModel.getPasswordError().getValue());
    }
    public void invalidUsername(){
        viewModel.signInValidation();
        viewModel.setEmail("ad@min.com");
        viewModel.setUsername("  ");
        viewModel.setPassword("123456");
        viewModel.signInValidation();
        assertNull(viewModel.getEmailError().getValue());
        assertEquals("Username cannot contain whitespace!", viewModel.getUsernameError().getValue());
        assertNull(viewModel.getPasswordError().getValue());

    }
}
