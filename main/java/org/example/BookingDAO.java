package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingDAO {

    public static void addBooking(Booking booking) {
        // Check if the userID exists
        boolean userExists = checkUserExists(booking.getUserID());
        if (!userExists) {
            System.out.println("User ID does not exist.");
            return; // Exit the method if user does not exist
        }

        String sqlQuery = "INSERT INTO Booking (bookingID, roomID, checkInDate, checkOutDate, userID, bookingInfo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, booking.getBookingID().toString());
            preparedStatement.setInt(2, booking.getRoomID());
            preparedStatement.setDate(3, Date.valueOf(booking.getCheckInDate()));
            preparedStatement.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            preparedStatement.setString(5, booking.getUserID().toString());
            preparedStatement.setString(6, booking.getBookingInfo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUserExists(UUID userId) {
        String query = "SELECT COUNT(*) AS count FROM user WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static void deleteBooking(UUID bookingID) {
        String sqlQuery = "DELETE FROM Booking WHERE bookingID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, bookingID.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Booking getBooking(UUID bookingID) {
        String sqlQuery = "SELECT * FROM Booking WHERE bookingID = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, bookingID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer roomID = resultSet.getInt("roomID");
                LocalDate checkInDate = resultSet.getDate("checkInDate").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("checkOutDate").toLocalDate();
                UUID userID = UUID.fromString(resultSet.getString("userID"));
                String bookingInfo = resultSet.getString("bookingInfo");
                return new Booking(bookingID, roomID, checkInDate, checkOutDate, userID, bookingInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if booking not found
    }

    public static List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Booking";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                UUID bookingID = UUID.fromString(resultSet.getString("bookingID"));
                Integer roomID = resultSet.getInt("roomID");
                LocalDate checkInDate = resultSet.getDate("checkInDate").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("checkOutDate").toLocalDate();
                UUID userID = UUID.fromString(resultSet.getString("userID"));
                String bookingInfo = resultSet.getString("bookingInfo");
                bookingList.add(new Booking(bookingID, roomID, checkInDate, checkOutDate, userID, bookingInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    /**
     * Retrieves a summary of room bookings grouped by hotels for a given date range.
     * @param startDate The start date of the period to retrieve bookings for.
     * @param endDate The end date of the period to retrieve bookings for.
     * @return A formatted string containing the booking summary.
     */
    public static String getRoomBookingsByHotelAndDate(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        // Ensure your SQL query matches your database schema.
        String sqlQuery = "SELECT h.name AS hotelName, COUNT(b.bookingID) AS bookings, r.roomID " +
                "FROM Booking b " +
                "JOIN Room r ON b.roomID = r.roomID " +
                "JOIN Hotel h ON r.hotelID = h.hotelID " +
                "WHERE b.checkInDate >= ? AND b.checkOutDate <= ? " +
                "GROUP BY h.hotelID, r.roomID " +
                "ORDER BY h.name, bookings DESC";

        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String hotelName = resultSet.getString("hotelName");
                int bookings = resultSet.getInt("bookings");
                int roomID = resultSet.getInt("roomID");
                sb.append(String.format("Hotel: %s, Room ID: %d, Bookings: %d\n", hotelName, roomID, bookings));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to fetch data.";
        }
        return sb.toString();
    }

    // In BookingDAO.java
    public static List<Booking> getBookingsByEmail(String email) {
        List<Booking> bookings = new ArrayList<>();
        // You'll need to adjust this SQL based on your schema. This is a simplified example.
        String sql = "SELECT * FROM Booking b JOIN User u ON b.userID = u.userID WHERE u.email = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseDAO.URL, DatabaseDAO.USERNAME, DatabaseDAO.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID bookingID = UUID.fromString(rs.getString("bookingID"));
                Integer roomID = rs.getInt("roomID");
                LocalDate checkInDate = rs.getDate("checkInDate").toLocalDate();
                LocalDate checkOutDate = rs.getDate("checkOutDate").toLocalDate();
                UUID userID = UUID.fromString(rs.getString("userID"));
                String bookingInfo = rs.getString("bookingInfo");
                bookings.add(new Booking(bookingID, roomID, checkInDate, checkOutDate, userID, bookingInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }



    // Utility method to print all bookings - for demonstration or debugging
    public static void showAllBookings() {
        List<Booking> bookings = getAllBookings();
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingID() + ", Room ID: " + booking.getRoomID() + ", Check-In Date: " + booking.getCheckInDate() + ", Check-Out Date: " + booking.getCheckOutDate() + ", User ID: " + booking.getUserID() + ", Booking Info: " + booking.getBookingInfo());
        }
    }
}
