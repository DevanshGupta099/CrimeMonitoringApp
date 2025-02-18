package com.example.crime_monitoring_app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crime_reports")
public class CrimeReport {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String description;
    private String crimeType;
    private String date;
    private String time;
    private String vehicleMakeModel;
    private String fileUrl;
    private String timestamp;

    // Constructor
    public CrimeReport(int userId, String description, String crimeType, String date, String time, String vehicleMakeModel, String fileUrl, String timestamp) {
        this.userId = userId;
        this.description = description;
        this.crimeType = crimeType;
        this.date = date;
        this.time = time;
        this.vehicleMakeModel = vehicleMakeModel;
        this.fileUrl = fileUrl;
        this.timestamp = timestamp;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVehicleMakeModel() {
        return vehicleMakeModel;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setter methods for id (since it's auto-generated)
    public void setId(int id) {
        this.id = id;
    }
}
