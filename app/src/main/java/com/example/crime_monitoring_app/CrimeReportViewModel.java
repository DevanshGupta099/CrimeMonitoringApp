package com.example.crime_monitoring_app;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CrimeReportViewModel extends AndroidViewModel {
    private CrimeReportRepository repository;
    private LiveData<List<CrimeReport>> allCrimeReports;

    public CrimeReportViewModel(Application application) {
        super(application);
        repository = new CrimeReportRepository(application);
        allCrimeReports = repository.getAllCrimeReports();
    }

    public LiveData<List<CrimeReport>> getAllCrimeReports() {
        return allCrimeReports;
    }
}
