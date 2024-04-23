package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO {
    public static List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        ResultSet resultSet = DatabaseDAO.executeQuery("SELECT * FROM User");
        try {
            while (resultSet.next()) {
                UUID userID = UUID.fromString(resultSet.getString("userID"));
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                userList.add(new User(userID, name, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void addUser(User user) {
        String sqlQuery = "INSERT INTO User (userID, name, email) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getUserID().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getAllUsersFromDatabase() {
        List<User> userList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);

            // Create a statement
            stmt = conn.createStatement();

            // Execute query
            String sqlQuery = "SELECT * FROM User";
            rs = stmt.executeQuery(sqlQuery);

            // Process the results
            while (rs.next()) {
                // Retrieve data from the result set
                UUID userID = UUID.fromString(rs.getString("userID"));
                String name = rs.getString("name");
                String email = rs.getString("email");

                // Create User object and add to the list
                userList.add(new User(userID, name, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userList;
    }
    public static void showUsersTable(){
    // Retrieve all users from the database
    List<User> users = UserDAO.getAllUsersFromDatabase();

    // Print each user
        for (User user : users) {
        System.out.println("ID: " + user.getUserID() + ", Name: " + user.getUsername());
    }
    }

    public static String getTop3Customers() {
        StringBuilder response = new StringBuilder();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);

            // SQL query to select the top 3 customers based on booking count
            String sqlQuery =
                    "SELECT u.userID, u.name, u.email, COUNT(b.bookingID) AS booking_count " +
                            "FROM User u " +
                            "LEFT JOIN Booking b ON u.userID = b.userID " +
                            "GROUP BY u.userID, u.name, u.email " +
                            "ORDER BY booking_count DESC " +
                            "LIMIT 3";

            // Prepare and execute the SQL statement
            stmt = conn.prepareStatement(sqlQuery);
            rs = stmt.executeQuery();

            // Iterate over the result set and append data to the response
            while (rs.next()) {
                UUID userID = UUID.fromString(rs.getString("userID"));
                String name = rs.getString("name");
                String email = rs.getString("email");
                int bookingCount = rs.getInt("booking_count");

                // Append the retrieved data to the response, including booking count
                response.append(userID)
                        .append(", ")
                        .append(name)
                        .append(", ")
                        .append(email)
                        .append(", ")
                        .append(bookingCount)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider handling the exception more gracefully, e.g., returning an error message
        } finally {
            // Clean up JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Return the generated response
        return response.toString().trim(); // Trim to ensure no trailing newline
    }

    //For User Authentication -Dan
    public static boolean authenticate(String email, String password) {
        // Connect to the database and check if the provided email/password match
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE email = ? AND password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Check the SQL query string
            System.out.println("SQL Query: " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            System.out.println("Attempting to authenticate user with email: " + email + " and password: " + password); // logging
            // If authentication fails, log a detailed error message
            System.err.println("Authentication failed for email: " + email);
            return rs.next(); // Return true if the user exists and the password matches
        } catch (SQLException e) {
            // Log any SQL exceptions that occur during authentication
            System.err.println("SQL Error occurred while authenticating user:");
            System.err.println("Email: " + email);
            System.err.println("Password: " + password);
            e.printStackTrace();

            return false;
        } catch (Exception e) {
            // Log any other exceptions that occur during authentication
            System.err.println("Error occurred during authentication for email: " + email);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a user's UUID by their email address.
     * @param email The email of the user.
     * @return The UUID of the user if found, null otherwise.
     */
    public static UUID findUserIdByEmail(String email) {
        String sqlQuery = "SELECT userID FROM User WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UUID.fromString(rs.getString("userID"));
            } else {
                System.out.println("User not found with email: " + email);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database error occurred while finding user by email: " + email);
            return null;
        }
    }

}

 /*
    public static void getTop3Customers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);

            // Prepare SQL query to get top 3 customers based on the number of bookings
            String sqlQuery = "SELECT u.userID, u.name, u.email, COUNT(b.bookingID) AS booking_count " +
                    "FROM User u " +
                    "LEFT JOIN Booking b ON u.userID = b.userID " +
                    "GROUP BY u.userID, u.name, u.email " +
                    "ORDER BY booking_count DESC " +
                    "LIMIT 3";

            // Create a prepared statement
            stmt = conn.prepareStatement(sqlQuery);

            // Execute the query
            rs = stmt.executeQuery();

            // Print the top 3 customers
            int count = 1;
            while (rs.next()) {
                UUID userID = UUID.fromString(rs.getString("userID"));
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Top " + count + " Customer:");
                System.out.println("UserID: " + userID + ", Name: " + name + ", Email: " + email);
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getTop3Customers() {
        StringBuilder response = new StringBuilder();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);

            // Prepare SQL query to get top 3 customers based on the number of bookings
            String sqlQuery = "SELECT u.userID, u.name, u.email, COUNT(b.bookingID) AS booking_count " +
                    "FROM User u " +
                    "LEFT JOIN Booking b ON u.userID = b.userID " +
                    "GROUP BY u.userID, u.name, u.email " +
                    "ORDER BY booking_count DESC " +
                    "LIMIT 3";

            // Create a prepared statement
            stmt = conn.prepareStatement(sqlQuery);

            // Execute the query
            rs = stmt.executeQuery();

            // Append customer data to the response
            while (rs.next()) {
                UUID userID = UUID.fromString(rs.getString("userID"));
                String name = rs.getString("name");
                String email = rs.getString("email");
                response.append(userID).append(", ").append(name).append(", ").append(email).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Return the response as a string
        return response.toString();
    }

     */
