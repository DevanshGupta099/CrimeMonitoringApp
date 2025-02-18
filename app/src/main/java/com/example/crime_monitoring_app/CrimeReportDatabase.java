package com.example.crime_monitoring_app;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CrimeReport.class}, version = 1, exportSchema = false)
public abstract class CrimeReportDatabase extends RoomDatabase {
    public abstract CrimeReportDao crimeReportDao();

    private static volatile CrimeReportDatabase INSTANCE;

    public static CrimeReportDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CrimeReportDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CrimeReportDatabase.class, "crime_report_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
