package com.example.crime_monitoring_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button reportCrimeButton, viewCrimesButton, settingsButton, logoutButton, emergencyButton, heatmapButton;
    private ImageView profileIcon;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoginStatus(); // Check if the user is logged in
        setContentView(R.layout.activity_main); // Set the layout for this activity

        // Setup toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileIcon = findViewById(R.id.profileIcon); // Initialize the profile icon
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Set your home icon drawable
        }

        initializeViews(); // Initialize the UI elements
        loadProfileImage(); // Load the user's profile image
        setupClickListeners(); // Set up click listeners for the buttons
    }

    private void checkLoginStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false); // Retrieve login status from shared preferences
        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class); // If not logged in, navigate to LoginActivity
            startActivity(intent);
            finish(); // Prevent returning to MainActivity without login
        }
    }

    private void initializeViews() {
        reportCrimeButton = findViewById(R.id.reportCrimeButton);
        viewCrimesButton = findViewById(R.id.viewCrimesButton);
        settingsButton = findViewById(R.id.settingsButton);
        logoutButton = findViewById(R.id.logoutButton);
        emergencyButton = findViewById(R.id.emergencyButton); // Initialize the emergency button
        heatmapButton = findViewById(R.id.heatmapButton); // Initialize the heatmap button
        profileIcon = findViewById(R.id.profileIcon);
    }

    private void loadProfileImage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String profileImageUri = preferences.getString("profile_image", null);
        if (profileImageUri != null) {
            Uri imageUri = Uri.parse(profileImageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileIcon.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupClickListeners() {
        reportCrimeButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Report Crime Clicked", Toast.LENGTH_SHORT).show();
            openReportCrimeActivity(); // Navigate to ReportCrimeActivity
        });

        viewCrimesButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "View Crimes Clicked", Toast.LENGTH_SHORT).show();
            openViewCrimesActivity(); // Navigate to ViewCrimesActivity
        });

        settingsButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            openSettingsActivity(); // Navigate to SettingsActivity
        });

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
            performLogout(); // Perform logout
        });

        profileIcon.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            chooseImage(); // Open image picker
        });

        emergencyButton.setOnClickListener(v -> showEmergencyConfirmationDialog()); // Show emergency confirmation dialog

        heatmapButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Heatmap Clicked", Toast.LENGTH_SHORT).show();
            openHeatmapActivity(); // Navigate to HeatmapActivity
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileIcon.setImageBitmap(bitmap);
                saveProfileImage(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfileImage(Uri uri) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profile_image", uri.toString());
    }

    private void openReportCrimeActivity() {
        Intent intent = new Intent(MainActivity.this, ReportCrimeActivity.class);
        startActivity(intent);
    }

    private void openViewCrimesActivity() {
        Intent intent = new Intent(MainActivity.this, ViewCrimesActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openUserProfileActivity() {
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void openHeatmapActivity() {
        Intent intent = new Intent(MainActivity.this, HeatmapActivity.class);
        startActivity(intent);
    }

    private void performLogout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false); // Update login status in shared preferences
        editor.apply();
        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Navigate to LoginActivity
        startActivity(intent);
        finish(); // Finish the current activity
    }

    private void showEmergencyConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Emergency SOS")
                .setMessage("Are you sure you want to press the Emergency SOS to alert nearby authorities of your respective location?")
                .setPositiveButton("YES", (dialog, which) -> showEmergencyAlertedDialog())
                .setNegativeButton("NO", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, android.R.color.black));
        });
        dialog.show();
    }

    private void showEmergencyAlertedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Emergency SOS")
                .setMessage("Nearby authorities have been alerted of your location and will be reaching your location as soon as possible.")
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, android.R.color.black)));
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
