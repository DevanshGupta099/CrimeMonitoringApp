package com.example.crime_monitoring_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import java.util.Calendar;
import java.util.concurrent.Executors;

public class ReportCrimeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RECORD_VIDEO_REQUEST = 2;

    private EditText descriptionEditText, dateEditText, timeEditText, vehicleMakeModelEditText;
    private Spinner crimeTypeSpinner;
    private Button uploadImageButton, recordVideoButton, submitButton, backButton;
    private ImageView imageView;
    private Uri fileUri;
    private CrimeReportDatabase crimeReportDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);

        // Setup toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Set your home icon drawable
        }

        // Initialize views
        backButton = findViewById(R.id.backButton);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        crimeTypeSpinner = findViewById(R.id.crimeTypeSpinner);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        vehicleMakeModelEditText = findViewById(R.id.vehicleMakeModelEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        recordVideoButton = findViewById(R.id.recordVideoButton);
        submitButton = findViewById(R.id.submitButton);
        imageView = findViewById(R.id.imageView);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting report...");

        // Initialize database
        crimeReportDatabase = Room.databaseBuilder(getApplicationContext(),
                        CrimeReportDatabase.class, "crime_report_database")
                .fallbackToDestructiveMigration()
                .setQueryExecutor(Executors.newSingleThreadExecutor())
                .build();

        // Populate the spinner with crime types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crime_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crimeTypeSpinner.setAdapter(adapter);

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
        uploadImageButton.setOnClickListener(v -> chooseImage());
        recordVideoButton.setOnClickListener(v -> recordVideo());
        submitButton.setOnClickListener(v -> validateAndSubmit());
        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        timeEditText.setOnClickListener(v -> showTimePickerDialog());
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, RECORD_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            if (fileUri != null) {
                Log.d("ReportCrimeActivity", "fileUri: " + fileUri.toString());
            }
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageView.setImageURI(fileUri); // Display the selected image
            } else if (requestCode == RECORD_VIDEO_REQUEST) {
                // Handle video file
                Toast.makeText(this, "Video recorded successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("ReportCrimeActivity", "Result not OK or data is null");
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerTheme,
                (view, year1, month1, dayOfMonth) -> dateEditText.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CustomTimePickerTheme,
                (view, hourOfDay, minute1) -> timeEditText.setText(hourOfDay + ":" + String.format("%02d", minute1)), hour, minute, true);
        timePickerDialog.show();
    }

    private void validateAndSubmit() {
        String description = descriptionEditText.getText().toString().trim();
        String crimeType = crimeTypeSpinner.getSelectedItem().toString();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String vehicleMakeModel = vehicleMakeModelEditText.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Please enter a description");
            return;
        }
        if (TextUtils.isEmpty(crimeType)) {
            Toast.makeText(this, "Please select a crime type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(date)) {
            dateEditText.setError("Please select a date");
            return;
        }
        if (TextUtils.isEmpty(time)) {
            timeEditText.setError("Please select a time");
            return;
        }
        if (TextUtils.isEmpty(vehicleMakeModel)) {
            vehicleMakeModelEditText.setError("Please enter vehicle make and model");
            return;
        }
        if (fileUri == null) {
            Toast.makeText(this, "Please select an image or record a video", Toast.LENGTH_SHORT).show();
            return;
        }

        new SubmitCrimeReportTask().execute(description, crimeType, date, time, vehicleMakeModel); // Execute AsyncTask to submit report
    }

    private class SubmitCrimeReportTask extends AsyncTask<String, Void, Boolean> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            progressDialog.show(); // Show progress dialog
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String description = params[0];
            String crimeType = params[1];
            String date = params[2];
            String time = params[3];
            String vehicleMakeModel = params[4];
            String timestamp = date + " " + time;
            String fileUrl = fileUri.toString();
            int userId = 1234; // Replace this with the actual user ID as needed

            // Create a new CrimeReport object
            CrimeReport crimeReport = new CrimeReport(userId, description, crimeType, date, time, vehicleMakeModel, fileUrl, timestamp);
            try {
                // Insert the crime report into the database
                crimeReportDatabase.crimeReportDao().insertCrimeReport(crimeReport);
                return true;
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressDialog.dismiss(); // Dismiss progress dialog
            if (success) {
                Toast.makeText(ReportCrimeActivity.this, "Crime report submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ReportCrimeActivity.this, "Failed to submit crime report: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ReportCrimeActivity", "Error submitting crime report: " + errorMessage);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
