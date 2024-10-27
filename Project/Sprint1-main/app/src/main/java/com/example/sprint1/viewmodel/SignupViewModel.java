package com.example.sprint1.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprint1.model.User;
import com.example.sprint1.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class SignupViewModel extends ViewModel {
    // Creating the LiveData
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<String> emailError = new MutableLiveData<>();
    private MutableLiveData<String> usernameError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> validationError = new MutableLiveData<>();

    private FirebaseAuth mAuth;
    private UserModel userModel;

    // Initializes the FirebaseAuth and UserModel instances
    public SignupViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userModel = UserModel.getInstance();
    }

    // Testing constructor
    public SignupViewModel(Boolean test) {

    }
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    // Method to set username to user input
    public void setEmail(String email) {
        this.email.setValue(email);
    }

    // Method to set password to user input
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    // Checks if username and password are valid
    public void signInValidation() {
        boolean valid = true;
        emailError.setValue(null);
        usernameError.setValue(null);
        passwordError.setValue(null);

        // Email error
        if (!checkInput(email.getValue())) {
            valid = false;
            emailError.setValue("Cannot contain Whitespace!");
        }
        if (username.getValue() != null && !username.getValue().isEmpty() && !checkInput(username.getValue())) {
            valid = false;
            usernameError.setValue("Username cannot contain whitespace!");
        }

        // Password error
        if (!checkInput(password.getValue())) {
            valid = false;
            passwordError.setValue("Cannot contain Whitespace!");
        }

        // Sets the value of the LiveData validInputs
        validInputs.setValue(valid);
    }

    public void signUp() {
        // Creates a new user with an email and password
        Log.d("SignupViewModel", "Email: "
                + email.getValue() + ", Password: " + password.getValue());
        mAuth.createUserWithEmailAndPassword(
                email.getValue(), password.getValue()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieves the authenticated user
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Calls the storeUser method in userModel, which handles data with firebase
                        userModel.setUserId(user.getUid());

                        // Now store the user data under the correct userId
                        User newUser;
                        if (username.getValue() != null && !username.getValue().isEmpty()) {
                            newUser = new User(email.getValue(), username.getValue());
                        } else {
                            newUser = new User(email.getValue());
                        }
                        userModel.storeUser(newUser);
                        userModel.loadUserMap();
                        validationError.setValue(null);
                    } else {
                        Exception exception = task.getException();
                        FirebaseAuthException authException = (FirebaseAuthException) exception;
                        String errorCode = authException.getErrorCode();
                        if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                            emailError.setValue("The email address is invalid.");
                        } else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                            emailError.setValue("The email is already in used.");
                        } else if (errorCode.equals("ERROR_WEAK_PASSWORD")) {
                            passwordError.setValue("Must be at least 6 characters.");
                        } else {
                            validationError.setValue("Something went wrong.  Try again later.");
                        }
                    }
                });
    }

    // Method to check whether inputs are formatted correctly
    public boolean checkInput(String input) {
        return input != null && !input.isEmpty() && !input.contains(" ");
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }
}
