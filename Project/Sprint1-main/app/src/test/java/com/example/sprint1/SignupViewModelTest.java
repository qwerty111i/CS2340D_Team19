package com.example.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.example.sprint1.viewmodel.LoginViewModel;
import com.example.sprint1.viewmodel.SignupViewModel;

import org.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class SignupViewModelTest {
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

    // Adwaith
    @Test
    public void validUsernamePassword() {
        viewModel.signInValidation();
        viewModel.setUsername("test@gmail.com");
        viewModel.setPassword("123456");
        viewModel.signInValidation();
        assertNull(viewModel.getUsernameError().getValue());
        assertNull(viewModel.getPasswordError().getValue());

        viewModel.setUsername("asdf@gmail.com");
        viewModel.setPassword("");
        viewModel.signInValidation();
        assertNull(viewModel.getUsernameError().getValue());
        assertEquals("Cannot contain Whitespace!", viewModel.getPasswordError().getValue());

        viewModel.setUsername("s f @gmail.com");
        viewModel.setPassword("asdfasdfds");
        viewModel.signInValidation();
        assertEquals("Cannot contain Whitespace!", viewModel.getUsernameError().getValue());
        assertNull(viewModel.getPasswordError().getValue());

        viewModel.setUsername("asdf@ .com");
        viewModel.setPassword(" 3");
        viewModel.signInValidation();
        assertEquals("Cannot contain Whitespace!", viewModel.getUsernameError().getValue());
        assertEquals("Cannot contain Whitespace!", viewModel.getPasswordError().getValue());
    }
}
