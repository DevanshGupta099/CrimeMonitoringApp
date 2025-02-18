package com.example.crime_monitoring_app;

// Import necessary classes from Room and Android context
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

// Define the AppDatabase class which extends RoomDatabase
@Database(entities = {User.class}, version = 1, exportSchema = false) // Specifies the entities and database version
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE; // Singleton instance to ensure only one instance of database is created

    // Abstract method to get the UserDao
    public abstract UserDao userDao();

    // Method to get the database instance
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) { // Check if the instance is null
            synchronized (AppDatabase.class) { // Synchronize block to ensure thread safety
                if (INSTANCE == null) { // Double-check if the instance is still null
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user_database") // Build the database instance
                            .build();
                }
            }
        }
        return INSTANCE; // Return the database instance
    }
}
