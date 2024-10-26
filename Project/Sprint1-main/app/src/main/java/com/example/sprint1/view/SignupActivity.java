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
import com.example.sprint1.databinding.ActivitySignupBinding;
import com.example.sprint1.viewmodel.SignupViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private Button signUp;
    private Button signIn;
    private SignupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initializing Firebase
        FirebaseApp.initializeApp(this);

        // Inflating the layout
        ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creating the ViewModel
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        // Binding the ViewModel
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
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

    private void listeners(ActivitySignupBinding binding) {
        // Linking components from XML to activity
        email = binding.emailLayout;
        password = binding.passwordLayout;
        emailText = binding.email;
        passwordText = binding.password;
        signUp = binding.signUp;
        signIn = binding.signIn;

        // Sign Up button listener
        signUp.setOnClickListener(v -> {
            // Setting values in the viewModel
            viewModel.setEmail(email.getEditText().getText().toString());
            viewModel.setPassword(password.getEditText().getText().toString());

            // Calling the validation function in viewModel
            viewModel.signInValidation();
        });

        // Sign In button listener
        signIn.setOnClickListener(v -> {
            // Sends the user to the Sign Up page
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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

    private void observers(ActivitySignupBinding binding) {
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

        // Obtains account creation errors using getValidationError in viewModel
        // Updates new variable errorMessage to get the specific error
        // Checks if account can be created
        viewModel.getValidationError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Snackbar display = Snackbar.make(binding.getRoot(), errorMessage,
                                Snackbar.LENGTH_LONG)
                        .setAction("OK", v -> { });
                display.setBackgroundTint(
                        ContextCompat.getColor(this, R.color.snackbar_background));
                display.setTextColor(
                        ContextCompat.getColor(this, R.color.white));
                display.setActionTextColor(
                        ContextCompat.getColor(this, R.color.snackbar_action_text));
                display.show();
            } else {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                // Tells the Login screen to display a message
                intent.putExtra("creation_successful",
                        "Account Creation Successful!  Please sign in.");
                startActivity(intent);
            }
        });

        // Obtains validity using areInputsValid in viewModel
        // Updates new variable isValid to match the validity
        // Checks if inputs are valid
        viewModel.areInputsValid().observe(this, isValid -> {
            if (viewModel.areInputsValid().getValue()) {
                // Create account method in viewModel
                viewModel.signUp();
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