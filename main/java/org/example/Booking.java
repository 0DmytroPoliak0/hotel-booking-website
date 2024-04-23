package org.example;

import java.time.LocalDate;
import java.util.UUID;

public class Booking {
    private UUID bookingID;
    private Integer roomID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private UUID userID;
    private String bookingInfo;

    // Constructor for creating new Booking instances
    public Booking(UUID bookingID, Integer roomID, LocalDate checkInDate, LocalDate checkOutDate, UUID userID, String bookingInfo) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.userID = userID;
        this.bookingInfo = bookingInfo;
    }

    // Getters and Setters
    public UUID getBookingID() {
        return bookingID;
    }

    public void setBookingID(UUID bookingID) {
        this.bookingID = bookingID;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getBookingInfo() {
        return bookingInfo;
    }

    public void setBookingInfo(String bookingInfo) {
        this.bookingInfo = bookingInfo;
    }
}
