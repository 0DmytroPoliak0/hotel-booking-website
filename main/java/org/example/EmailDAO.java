package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmailDAO {

    public static void addEmail(Email email) {
        String sqlQuery = "INSERT INTO Email (creditCardNumber, bookingID, emailBody, dateofEmail, staffID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, email.getCreditCardNumber());
            preparedStatement.setString(2, email.getBookingID().toString());
            preparedStatement.setString(3, email.getEmailBody());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(email.getDateOfEmail()));
            preparedStatement.setString(5, email.getStaffID().toString());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    email.setEmailID(generatedKeys.getInt(1)); // Set the auto-generated emailID to the email object
                } else {
                    throw new SQLException("Creating email failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmail(int emailID) {
        String sqlQuery = "DELETE FROM Email WHERE emailID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, emailID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Email getEmail(int emailID) {
        String sqlQuery = "SELECT * FROM Email WHERE emailID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, emailID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String creditCardNumber = resultSet.getString("creditCardNumber");
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                String emailBody = resultSet.getString("emailBody");
                LocalDateTime dateOfEmail = resultSet.getTimestamp("dateofEmail").toLocalDateTime();
                UUID staffID = UUID.fromString(resultSet.getString("staffID"));
                return new Email(emailID, creditCardNumber, bookingID, emailBody, dateOfEmail, staffID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if email not found
    }

    public static List<Email> getAllEmails() {
        List<Email> emailList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Email";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int emailID = resultSet.getInt("emailID");
                String creditCardNumber = resultSet.getString("creditCardNumber");
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                String emailBody = resultSet.getString("emailBody");
                LocalDateTime dateOfEmail = resultSet.getTimestamp("dateofEmail").toLocalDateTime();
                UUID staffID = UUID.fromString(resultSet.getString("staffID"));
                emailList.add(new Email(emailID, creditCardNumber, bookingID, emailBody, dateOfEmail, staffID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emailList;
    }

    // Utility method to print all emails - for demonstration or debugging
    public static void showAllEmails() {
        List<Email> emails = getAllEmails();
        for (Email email : emails) {
            System.out.println("Email ID: " + email.getEmailID() + ", Credit Card Number: " + email.getCreditCardNumber() + ", Booking ID: " + email.getBookingID() + ", Email Body: " + email.getEmailBody() + ", Date of Email: " + email.getDateOfEmail() + ", Staff ID: " + email.getStaffID());
        }
    }
}
