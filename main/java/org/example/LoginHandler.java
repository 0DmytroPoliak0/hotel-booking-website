package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.net.URLDecoder;


public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Read request body to get email and password
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line).append("\n");
                }
            }

            // Extract email and password from request body
            String[] bodyParts = requestBody.toString().split("&");
            String email = URLDecoder.decode(bodyParts[0].split("=")[1], "UTF-8");
            String password = URLDecoder.decode(bodyParts[1].split("=")[1], "UTF-8").trim();

            // Authenticate user
            if (UserDAO.authenticate(email, password)) {
                // Generate session ID
                String sessionId = generateSessionId();

                // Save session ID in database
                SessionDAO.saveSession(UUID.fromString(sessionId), email);

                // Send response with session ID
                String response = "Login successful. Welcome, " + email;
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                // Invalid credentials
                String response = "Invalid email or password\n";
                response += "Request Body: " + requestBody.toString(); // Include requestBody in the response
                exchange.sendResponseHeaders(401, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } else {
            // Invalid request method
            String response = "Method not allowed";
            exchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private String generateSessionId() {
        // Generate random session ID (e.g., using UUID)
        return UUID.randomUUID().toString();
    }
}
