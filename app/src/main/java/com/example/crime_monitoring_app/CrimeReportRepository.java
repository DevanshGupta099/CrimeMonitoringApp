package com.example.crime_monitoring_app;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrimeReportRepository {
    private CrimeReportDao crimeReportDao;
    private LiveData<List<CrimeReport>> allCrimeReports;
    private ExecutorService executorService;

    public CrimeReportRepository(Application application) {
        CrimeReportDatabase db = CrimeReportDatabase.getDatabase(application);
        crimeReportDao = db.crimeReportDao();
        allCrimeReports = crimeReportDao.getAllCrimeReports();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CrimeReport>> getAllCrimeReports() {
        return allCrimeReports;
    }
}
