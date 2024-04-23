package org.example;

import java.util.UUID;

public class Hotel {
    private Integer hotelID; // Nullable to accommodate auto-increment before persistence
    private String name;
    private String address;
    private String contact;
    private UUID adminID; // Represents the foreign key relationship with the Admin table

    // Constructor for creating new Hotel instances before inserting into the database
    public Hotel(String name, String address, String contact, UUID adminID) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.adminID = adminID;
    }

    // Constructor including hotelID, for instances retrieved from the database
    public Hotel(Integer hotelID, String name, String address, String contact, UUID adminID) {
        this.hotelID = hotelID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.adminID = adminID;
    }

    // Getters and Setters
    public Integer getHotelID() {
        return hotelID;
    }

    public void setHotelID(Integer hotelID) {
        this.hotelID = hotelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }
}
