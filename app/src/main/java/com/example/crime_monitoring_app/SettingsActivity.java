package com.example.crime_monitoring_app;

import android.content.Intent; // Import the Intent class
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationSwitch, themeSwitch, locationSwitch;
    private EditText usernameEditText;
    private Spinner languageSpinner;
    private Button saveUsernameButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setup toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Set your home icon drawable
        }

        // Initialize views
        notificationSwitch = findViewById(R.id.notificationSwitch);
        themeSwitch = findViewById(R.id.themeSwitch);
        locationSwitch = findViewById(R.id.locationSwitch);
        usernameEditText = findViewById(R.id.usernameEditText);
        languageSpinner = findViewById(R.id.languageSpinner);
        saveUsernameButton = findViewById(R.id.saveUsernameButton);
        backButton = findViewById(R.id.backButton);

        // Load settings from preferences
        loadSettings();

        // Setup click listeners
        setupClickListeners();
    }

    private void loadSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load switches states
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        boolean darkThemeEnabled = preferences.getBoolean("dark_theme_enabled", false);
        boolean locationTrackingEnabled = preferences.getBoolean("location_tracking_enabled", true);

        notificationSwitch.setChecked(notificationsEnabled);
        themeSwitch.setChecked(darkThemeEnabled);
        locationSwitch.setChecked(locationTrackingEnabled);

        // Load username
        String username = preferences.getString("username", "");
        usernameEditText.setText(username);

        // Load selected language
        // Note: Adapter for languageSpinner should be set before calling setSelection
        int selectedLanguagePosition = preferences.getInt("selected_language_position", 0);
        languageSpinner.setSelection(selectedLanguagePosition);
    }

    private void saveSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save switches states
        editor.putBoolean("notifications_enabled", notificationSwitch.isChecked());
        editor.putBoolean("dark_theme_enabled", themeSwitch.isChecked());
        editor.putBoolean("location_tracking_enabled", locationSwitch.isChecked());

        // Save username
        String username = usernameEditText.getText().toString();
        editor.putString("username", username);

        // Save selected language position
        int selectedLanguagePosition = languageSpinner.getSelectedItemPosition();
        editor.putInt("selected_language_position", selectedLanguagePosition);

        editor.apply();
    }

    private void setupClickListeners() {
        saveUsernameButton.setOnClickListener(v -> saveSettings());

        backButton.setOnClickListener(v -> {
            saveSettings();
            onBackPressed();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
