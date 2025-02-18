package com.example.crime_monitoring_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CrimeReportDao {
    @Insert
    void insertCrimeReport(CrimeReport crimeReport);

    @Query("SELECT * FROM crime_reports")
    LiveData<List<CrimeReport>> getAllCrimeReports();
}
