package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminDAO {

    public static void addAdmin(Admin admin) {
        String sqlQuery = "INSERT INTO Admin (adminID, name, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, admin.getAdminID().toString());
            preparedStatement.setString(2, admin.getName());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAdmin(UUID adminID) {
        String sqlQuery = "DELETE FROM Admin WHERE adminID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, adminID.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Admin getAdmin(UUID adminID) {
        String sqlQuery = "SELECT * FROM Admin WHERE adminID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, adminID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                return new Admin(adminID, name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if admin not found
    }

    public static List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Admin";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                UUID adminID = UUID.fromString(resultSet.getString("adminID"));
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                adminList.add(new Admin(adminID, name, email, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    // Utility method to print all admins - for demonstration or debugging
    public static void showAllAdmins() {
        List<Admin> admins = getAllAdmins();
        for (Admin admin : admins) {
            System.out.println("Admin ID: " + admin.getAdminID() + ", Name: " + admin.getName() + ", Email: " + admin.getEmail());
        }
    }
}
