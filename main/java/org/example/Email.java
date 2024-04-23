package org.example;

import java.time.LocalDateTime;
import java.util.UUID;

public class Email {
    private Integer emailID; // Nullable for auto-increment
    private String creditCardNumber;
    private UUID bookingID;
    private String emailBody;
    private LocalDateTime dateOfEmail;
    private UUID staffID;

    // Constructor without emailID for creating new Email instances before persisting
    public Email(String creditCardNumber, UUID bookingID, String emailBody, LocalDateTime dateOfEmail, UUID staffID) {
        this.creditCardNumber = creditCardNumber;
        this.bookingID = bookingID;
        this.emailBody = emailBody;
        this.dateOfEmail = dateOfEmail;
        this.staffID = staffID;
    }

    // Constructor including emailID, for instances retrieved from the database
    public Email(Integer emailID, String creditCardNumber, UUID bookingID, String emailBody, LocalDateTime dateOfEmail, UUID staffID) {
        this.emailID = emailID;
        this.creditCardNumber = creditCardNumber;
        this.bookingID = bookingID;
        this.emailBody = emailBody;
        this.dateOfEmail = dateOfEmail;
        this.staffID = staffID;
    }

    // Getters and Setters
    public Integer getEmailID() {
        return emailID;
    }

    public void setEmailID(Integer emailID) {
        this.emailID = emailID;
    }

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

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public LocalDateTime getDateOfEmail() {
        return dateOfEmail;
    }

    public void setDateOfEmail(LocalDateTime dateOfEmail) {
        this.dateOfEmail = dateOfEmail;
    }

    public UUID getStaffID() {
        return staffID;
    }

    public void setStaffID(UUID staffID) {
        this.staffID = staffID;
    }
}
