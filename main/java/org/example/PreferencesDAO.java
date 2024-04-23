package org.example;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PreferencesDAO {

    public static void addPreference(Preferences preference) {
        String sqlQuery = "INSERT INTO Preferences (userID, roomType, numberOfPeople, rate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, preference.getUserID().toString());
            preparedStatement.setString(2, preference.getRoomType());
            preparedStatement.setInt(3, preference.getNumberOfPeople());
            preparedStatement.setBigDecimal(4, preference.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePreference(UUID userID, String roomType, int numberOfPeople, BigDecimal rate) {
        String sqlQuery = "DELETE FROM Preferences WHERE userID = ? AND roomType = ? AND numberOfPeople = ? AND rate = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, userID.toString());
            preparedStatement.setString(2, roomType);
            preparedStatement.setInt(3, numberOfPeople);
            preparedStatement.setBigDecimal(4, rate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Preferences getPreference(UUID userID) {
        // This method assumes there's only one preference per userID, which might not align with your schema.
        // Adjust according to your actual requirements.
        String sqlQuery = "SELECT * FROM Preferences WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, userID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String roomType = resultSet.getString("roomType");
                int numberOfPeople = resultSet.getInt("numberOfPeople");
                BigDecimal rate = resultSet.getBigDecimal("rate");
                return new Preferences(userID, roomType, numberOfPeople, rate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if preference not found
    }

    public static List<Preferences> getAllPreferences() {
        List<Preferences> preferencesList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Preferences";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                UUID userID = UUID.fromString(resultSet.getString("userID"));
                String roomType = resultSet.getString("roomType");
                int numberOfPeople = resultSet.getInt("numberOfPeople");
                BigDecimal rate = resultSet.getBigDecimal("rate");
                preferencesList.add(new Preferences(userID, roomType, numberOfPeople, rate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preferencesList;
    }

    // Utility method to print all preferences - for demonstration or debugging
    public static void showAllPreferences() {
        List<Preferences> preferences = getAllPreferences();
        for (Preferences preference : preferences) {
            System.out.println("User ID: " + preference.getUserID() + ", Room Type: " + preference.getRoomType() + ", Number Of People: " + preference.getNumberOfPeople() + ", Rate: " + preference.getRate());
        }
    }
}
