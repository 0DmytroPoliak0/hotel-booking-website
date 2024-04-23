package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportsDAO {

    public static void addReport(Reports report) {
        String sqlQuery = "INSERT INTO Reports (reportID, title, reportType, content, creationDate, bookingID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, report.getReportID().toString());
            preparedStatement.setString(2, report.getTitle());
            preparedStatement.setString(3, report.getReportType());
            preparedStatement.setString(4, report.getContent());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(report.getCreationDate()));
            preparedStatement.setString(6, report.getBookingID().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReport(UUID reportID) {
        String sqlQuery = "DELETE FROM Reports WHERE reportID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, reportID.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Reports getReport(UUID reportID) {
        String sqlQuery = "SELECT * FROM Reports WHERE reportID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, reportID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String reportType = resultSet.getString("reportType");
                String content = resultSet.getString("content");
                LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                return new Reports(reportID, title, reportType, content, creationDate, bookingID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if report not found
    }

    public static List<Reports> getAllReports() {
        List<Reports> reportList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Reports";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                UUID reportID = UUID.fromString(resultSet.getString("reportID"));
                String title = resultSet.getString("title");
                String reportType = resultSet.getString("reportType");
                String content = resultSet.getString("content");
                LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                reportList.add(new Reports(reportID, title, reportType, content, creationDate, bookingID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportList;
    }

    // Utility method to print all reports - for demonstration or debugging
    public static void showAllReports() {
        List<Reports> reports = getAllReports();
        for (Reports report : reports) {
            System.out.println("Report ID: " + report.getReportID() + ", Title: " + report.getTitle() + ", Type: " + report.getReportType() + ", Creation Date: " + report.getCreationDate() + ", Booking ID: " + report.getBookingID());
        }
    }
}
