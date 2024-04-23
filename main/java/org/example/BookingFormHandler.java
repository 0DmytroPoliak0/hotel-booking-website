package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingFormHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Extract form data
            Map<String, String> formData = parseFormData(exchange.getRequestBody());

            // Extract individual fields from form data
            String email = formData.get("email"); // Email of the user making the booking
            String roomType = formData.get("room-type"); // The selected room type
            LocalDate checkInDate = LocalDate.parse(formData.get("checkin"));
            LocalDate checkOutDate = LocalDate.parse(formData.get("checkout"));
            String comments = formData.get("comments"); // Additional comments

            // In a real application, you would look up the user and room details based on the form input
            UUID userId = findUserIdByEmail(email);
            int roomId = findRoomIdByType(roomType, checkInDate, checkOutDate); // This method would need to consider hotel ID and availability

            // Assuming isRoomAvailable() and the methods to find userId and roomId are correctly implemented
            if (userId != null && roomId != 0 && RoomDAO.isRoomAvailable(roomId, checkInDate, checkOutDate)) {
                Booking booking = new Booking(UUID.randomUUID(), roomId, checkInDate, checkOutDate, userId, comments);

                try {
                    BookingDAO.addBooking(booking); // Insert booking into the database
                    RoomDAO.updateRoomAvailability(roomId, false); // Mark the room as booked/unavailable

                    String response = "Booking successful!";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(StandardCharsets.UTF_8));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                String response = "Invalid booking data or room not available";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private Map<String, String> parseFormData(InputStream is) throws IOException {
        Map<String, String> formData = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String query = reader.readLine();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1) {
                    formData.put(pair[0], java.net.URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name()));
                } else {
                    formData.put(pair[0], "");
                }
            }
        }
        return formData;
    }

    private UUID findUserIdByEmail(String email) {
        // Assuming UserDAO has a method to find user by email and return their ID.
        UUID userId = UserDAO.findUserIdByEmail(email);
        if (userId == null) {
            System.out.println("User with email " + email + " does not exist.");
            // Handle this scenario, perhaps by throwing an exception or returning a specific response.
        }
        return userId;
    }

    private int findRoomIdByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Integer> availableRooms = RoomDAO.findAvailableRoomsByType(roomType, checkInDate, checkOutDate);
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms of type " + roomType + " for the selected dates.");
            // Handle this scenario. For simplicity, we're returning -1, but consider a better approach.
            return -1;
        }
        // For simplicity, choosing the first available room. Consider logic for selecting the best room.
        return availableRooms.get(0);
    }




}
