package com.example.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.example.sprint1.viewmodel.LoginViewModel;
import org.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {
    // Allows live data to execute synchronously
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LoginViewModel viewModel;

    // Creating a new instance of LoginViewModel
    @Before
    public void setUp() {
        Boolean test = true;
        viewModel = new LoginViewModel(test);
    }

    // Adwaith
    @Test
    public void validUsernamePassword() {
        viewModel.setEmail("test@gmail.com");
        viewModel.setPassword("123456");
        viewModel.signInValidation();
        assertNull(viewModel.getEmailError().getValue());
        assertNull(viewModel.getPasswordError().getValue());

        viewModel.setEmail("asdf@gmail.com");
        viewModel.setPassword("");
        viewModel.signInValidation();
        assertNull(viewModel.getEmailError().getValue());
        assertEquals("Invalid Password.", viewModel.getPasswordError().getValue());
    }
}
