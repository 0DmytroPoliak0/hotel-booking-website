package org.example;

import java.util.UUID;

public class Admin {
    private UUID adminID; // Use UUID instead of String for adminID
    private String name;
    private String email;
    private String password;

    // Constructor
    public Admin(UUID adminID, String name, String email, String password) {
        this.adminID = adminID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public UUID getAdminID() {
        return adminID;
    }

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
