package com.example.crime_monitoring_app;

// Import necessary Room annotations
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

// Define the UserDao interface as a Data Access Object (DAO) for user operations
@Dao
public interface UserDao {
    // Method to insert a user into the database
    @Insert
    void insert(User user);

    // Method to query and return a user with the specified email and password
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User login(String email, String password);
}
