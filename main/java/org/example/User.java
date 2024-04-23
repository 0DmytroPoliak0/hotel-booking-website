package org.example;
import java.sql.*;
import java.util.UUID;

public class User {
    private UUID userID;
    private String username;
    private String email;

    public User(UUID userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    // Getters and setters
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

