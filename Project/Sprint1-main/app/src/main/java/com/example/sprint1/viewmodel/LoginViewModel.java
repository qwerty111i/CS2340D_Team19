package com.example.sprint1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprint1.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


public class LoginViewModel extends ViewModel {
    // Creating the LiveData
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<String> emailError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> loginError = new MutableLiveData<>();

    private FirebaseAuth mAuth;
    private UserModel userModel;

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userModel = UserModel.getInstance();
    }

    // Testing constructor
    public LoginViewModel(Boolean test) {

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
        passwordError.setValue(null);

        // Username Error
        if (!checkInput(email.getValue())) {
            valid = false;
            emailError.setValue("Invalid Username.");
        }

        // Password Error
        if (!checkInput(password.getValue())) {
            valid = false;
            passwordError.setValue("Invalid Password.");
        }

        // Sets the value of the LiveData validInputs
        validInputs.setValue(valid);
    }

    public void login() {
        mAuth.signInWithEmailAndPassword(email.getValue(),
                password.getValue()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the userid of logged-in user
                        String userId = mAuth.getCurrentUser().getUid();

                        // new userId setup
                        userModel.setUserId(userId);

                        // clear login errors
                        loginError.setValue(null);
                    } else {
                        Exception exception = task.getException();
                        FirebaseAuthException authException = (FirebaseAuthException) exception;
                        String errorCode = authException.getErrorCode();
                        if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                            emailError.setValue("The email address is invalid.");
                        } else {
                            emailError.setValue("Invalid email or password");
                            passwordError.setValue("Invalid email or password");
                        }
                    }
                });
    }

    // Method to check whether inputs are formatted correctly
    private boolean checkInput(String input) {
        return input != null && !input.isEmpty() && !input.contains(" ");
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }
}
