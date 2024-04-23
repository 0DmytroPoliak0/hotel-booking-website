package org.example;

import java.util.UUID;

public class Payment {
    private String creditCardNumber;
    private UUID bookingID;
    private String paymentType;
    private UUID staffID;

    // Constructor
    public Payment(String creditCardNumber, UUID bookingID, String paymentType, UUID staffID) {
        this.creditCardNumber = creditCardNumber;
        this.bookingID = bookingID;
        this.paymentType = paymentType;
        this.staffID = staffID;
    }

    // Getters and Setters
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public UUID getBookingID() {
        return bookingID;
    }

    public void setBookingID(UUID bookingID) {
        this.bookingID = bookingID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public UUID getStaffID() {
        return staffID;
    }

    public void setStaffID(UUID staffID) {
        this.staffID = staffID;
    }
}
