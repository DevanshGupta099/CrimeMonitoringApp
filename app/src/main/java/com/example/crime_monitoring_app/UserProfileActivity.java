package com.example.crime_monitoring_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private EditText usernameEditText, emailEditText;
    private Button uploadImageButton, saveProfileButton;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Setup toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        }

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        // Load saved profile data
        loadProfileData();

        // Setup click listeners
        setupClickListeners();
    }

    private void loadProfileData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String profileImageUri = preferences.getString("profile_image_uri", null);
        String username = preferences.getString("username", "");
        String email = preferences.getString("email", "");

        if (profileImageUri != null) {
            imageUri = Uri.parse(profileImageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
            }
        }

        usernameEditText.setText(username);
        emailEditText.setText(email);
    }

    private void setupClickListeners() {
        profileImageView.setOnClickListener(v -> chooseImage());
        uploadImageButton.setOnClickListener(v -> chooseImage());
        saveProfileButton.setOnClickListener(v -> saveProfileData());
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
                profileImageView.setImageBitmap(bitmap);
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
        editor.putString("profile_image_uri", uri.toString());
        editor.apply(); // Make sure to apply changes
    }

    private void saveProfileData() {
        String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (imageUri != null) {
            editor.putString("profile_image_uri", imageUri.toString());
        }
        editor.putString("username", usernameEditText.getText().toString());
        editor.putString("email", email);
        editor.apply();

        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
