package com.example.crime_monitoring_app;

// Import necessary Android and Room classes
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText; // UI elements for user input
    private Button signUpButton; // Button to trigger the sign-up process
    private AppDatabase db; // Database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Set the layout for this activity

        // Initialize the Room database instance
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();

        initializeViews(); // Initialize the UI elements
        setupClickListeners(); // Set up click listeners for the buttons
    }

    // Method to initialize the UI elements
    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
    }

    // Method to set up click listeners for the buttons
    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> validateAndSignUp()); // Set listener for sign-up button
    }

    // Method to validate user input and perform sign-up
    private void validateAndSignUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Check if any fields are empty
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new User object
        User user = new User(email, password);
        // Execute database insertion in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            db.userDao().insert(user);
            // Update UI on the main thread
            runOnUiThread(() -> {
                Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the SignUpActivity
            });
        });
    }
}
