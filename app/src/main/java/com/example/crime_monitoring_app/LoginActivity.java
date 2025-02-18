package com.example.crime_monitoring_app;

// Import necessary Android and Room classes
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.room.Room;
import java.util.concurrent.Executors;

// Define the LoginActivity class which extends AppCompatActivity
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText; // UI elements for user email and password input
    private Button loginButton, signUpButton; // Buttons for login and sign-up actions
    private AppDatabase db; // Database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database instance
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();

        // Find the UI elements by their ID
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        // Set click listeners for the buttons
        loginButton.setOnClickListener(v -> loginUser());
        signUpButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    // Method to handle user login
    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if email is empty
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        // Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return;
        }

        // Check if password is empty
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Execute database operations in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().login(email, password); // Attempt to find the user in the database
            runOnUiThread(() -> { // Update UI on the main thread
                if (user != null) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", true); // Save login state
                    editor.apply();

                    // Navigate to the MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // Finish the current activity
                } else {
                    // Show a toast message if authentication fails
                    Toast.makeText(LoginActivity.this, "Authentication failed: Invalid email or password", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
