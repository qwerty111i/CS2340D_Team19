package com.example.sprint1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginViewModel extends ViewModel {
    // Creating the LiveData
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<Boolean> validInputs = new MutableLiveData<>();
    private MutableLiveData<String> usernameError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> loginError = new MutableLiveData<>();

    private FirebaseAuth mAuth;

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Method to set username to user input
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    // Method to set password to user input
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    // Checks if username and password are valid
    public void signInValidation() {
        boolean valid = true;
        usernameError.setValue(null);
        passwordError.setValue(null);

        // Username Error
        if (!CheckInput(username.getValue())) {
            valid = false;
            usernameError.setValue("Invalid Username.");
        }

        // Password Error
        if (!CheckInput(password.getValue())) {
            valid = false;
            passwordError.setValue("Invalid Password.");
        }

        // Sets the value of the LiveData validInputs
        validInputs.setValue(valid);
    }

    public void Login() {
        mAuth.signInWithEmailAndPassword(username.getValue(), password.getValue()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginError.setValue(null);
            } else {
                Exception exception = task.getException();
                FirebaseAuthException authException = (FirebaseAuthException) exception;
                String errorCode = authException.getErrorCode();
                if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                    usernameError.setValue("The email address is invalid.");
                } else {
                    usernameError.setValue("Invalid email or password");
                    passwordError.setValue("Invalid email or password");
                }
            }
        });
    }

    // Method to check whether inputs are formatted correctly
    private boolean CheckInput(String input) {
        return input != null && !input.isEmpty() && !input.contains(" ");
    }

    public LiveData<Boolean> areInputsValid() {
        return validInputs;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getLoginError() { return loginError; }
}
