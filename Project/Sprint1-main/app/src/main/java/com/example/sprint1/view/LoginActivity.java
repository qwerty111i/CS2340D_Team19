package com.example.sprint1.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprint1.BR;
import com.example.sprint1.R;
import com.example.sprint1.databinding.ActivityLoginBinding;
import com.example.sprint1.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private Button signIn;
    private Button signUp;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflating the layout
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setting the ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        // Receives messages from Sign Up
        String message = getIntent().getStringExtra("creation_successful");
        if (message != null) {
            // Creates and displays message
            Snackbar display = Snackbar.make(binding.getRoot(),
                            "Account Creation Successful!", Snackbar.LENGTH_LONG)
                    .setAction("OK", v -> { });
            display.setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar_background));
            display.setTextColor(ContextCompat.getColor(this, R.color.white));
            display.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_text));
            display.show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Listeners to check button inputs
        listeners(binding);

        // Observes changes in LiveData
        observers(binding);

        // Live check to see if inputs are edited after errors
        textWatchers();
    }

    private void listeners(ActivityLoginBinding binding) {
        // Linking components from XML to activity
        email = binding.emailLayout;
        password = binding.passwordLayout;
        emailText = binding.email;
        passwordText = binding.password;
        signIn = binding.signIn;
        signUp = binding.signUp;

        // Sign In button listener
        signIn.setOnClickListener(v -> {
            // Setting values in the viewModel
            viewModel.setEmail(email.getEditText().getText().toString());
            viewModel.setPassword(password.getEditText().getText().toString());

            // Calling the validation function in viewModel
            viewModel.signInValidation();
        });

        // Sign Up button listener
        signUp.setOnClickListener(v -> {
            // Sends the user to the Sign Up page
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Sets the username text field back to the start when unfocused
        emailText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailText.setSelection(0);
            }
        });

        // Sets the password text field back to the start when unfocused
        passwordText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailText.setSelection(0);
            }
        });
    }

    private void observers(ActivityLoginBinding binding) {
        // Obtains username error using getUsernameError in viewModel
        // Updates new variable errorMessage to match the username error
        viewModel.getEmailError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                email.setError(errorMessage);
            } else {
                email.setError(null);
            }
        });

        // Obtains password error using getPasswordError in viewModel
        // Updates new variable errorMessage to match the password error
        viewModel.getPasswordError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                password.setError(errorMessage);
            } else {
                password.setError(null);
            }
        });

        // Obtains sign in error using getLoginError in viewModel
        // Updates new variable errorMessage to match the specific sign in error
        viewModel.getLoginError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Snackbar display =
                        Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("OK", v -> { });
                display.setBackgroundTint(
                        ContextCompat.getColor(this, R.color.snackbar_background));
                display.setTextColor(
                        ContextCompat.getColor(this, R.color.white));
                display.setActionTextColor(
                        ContextCompat.getColor(this, R.color.snackbar_action_text));
                display.show();
            } else {
                // Successful sign in
                Intent intent = new Intent(LoginActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        // Obtains validity using areInputsValid in viewModel
        // Updates new variable isValid to match the validity
        viewModel.areInputsValid().observe(this, isValid -> {
            if (viewModel.areInputsValid().getValue()) {
                // Check database
                viewModel.login();
            }
        });
    }

    private void textWatchers() {
        // Checks if username text field is edited after error is shown
        email.getEditText().addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                email.setError(null);
            }
        });

        // Checks if password text field is edited after error is shown
        password.getEditText().addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(android.text.Editable s) {
                // Sets error to null if field is edited
                password.setError(null);
            }
        });
    }
}