package com.example.crime_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;

public class ViewCrimesActivity extends AppCompatActivity {
    private ListView crimesListView;
    private CrimeReportAdapter crimeReportAdapter;
    private CrimeReportViewModel crimeReportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_crimes);

        // Setup toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Set your home icon drawable
        }

        // Initialize the ListView and set the adapter
        crimesListView = findViewById(R.id.crimesListView);
        crimeReportAdapter = new CrimeReportAdapter(this, new ArrayList<>()); // Initialize with an empty list
        crimesListView.setAdapter(crimeReportAdapter);

        // Initialize the ViewModel
        crimeReportViewModel = new ViewModelProvider(this).get(CrimeReportViewModel.class);
        crimeReportViewModel.getAllCrimeReports().observe(this, new Observer<List<CrimeReport>>() {
            @Override
            public void onChanged(List<CrimeReport> crimeReports) {
                if (crimeReports != null) {
                    crimeReportAdapter.updateCrimeReports(crimeReports);
                } else {
                    Toast.makeText(ViewCrimesActivity.this, "Failed to load crime reports", Toast.LENGTH_LONG).show();
                }
            }
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
