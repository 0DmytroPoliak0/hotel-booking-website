package org.example;

import java.sql.*;

public class RoomTypeDAO {

    public static void addRoomType(RoomType roomType) {
        String sqlQuery = "INSERT INTO RoomType (roomType) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, roomType.getRoomType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRoomType(String roomType) {
        String sqlQuery = "DELETE FROM RoomType WHERE roomType = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, roomType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static RoomType getRoomType(String roomType) {
        String sqlQuery = "SELECT * FROM RoomType WHERE roomType = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, roomType);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RoomType(roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if room type not found
    }

    public static void showAllRoomTypes() {
        String sqlQuery = "SELECT * FROM RoomType";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                String type = resultSet.getString("roomType");
                System.out.println("Room Type: " + type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
