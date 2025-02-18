package com.example.crime_monitoring_app;

// Import necessary Room annotations
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Define the User class as a Room entity with table name "users"
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true) // Primary key that auto-generates a unique ID for each entry
    private int id;
    private String email; // User email
    private String password; // User password

    // Constructor to initialize the email and password fields
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter method for the ID
    public int getId() {
        return id;
    }

    // Setter method for the ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for the email
    public String getEmail() {
        return email;
    }

    // Setter method for the email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for the password
    public String getPassword() {
        return password;
    }

    // Setter method for the password
    public void setPassword(String password) {
        this.password = password;
    }
}
