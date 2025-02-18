package com.example.crime_monitoring_app

// Import necessary Room annotations
import androidx.room.Entity
import androidx.room.PrimaryKey

// Define the CrimeReportData class as a Room entity with table name "crime_reports"
@Entity(tableName = "crime_reports")
data class CrimeReportData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key that auto-generates a unique ID for each entry, default value is 0
    val userId: Int, // ID of the user who filed the report, should be of type Int
    val description: String, // Description of the crime report, should be of type String
    val imageUrl: String, // URL of any image attached to the report, should be of type String
    val timestamp: String // Timestamp of when the report was filed, should be of type String
)
