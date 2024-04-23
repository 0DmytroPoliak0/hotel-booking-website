package org.example;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StaticFileHandler implements HttpHandler {
    private String filename;

    public StaticFileHandler(String filename) {
        this.filename = filename;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Path file = Paths.get(filename);
        byte[] data = Files.readAllBytes(file);

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, data.length);
        OutputStream os = exchange.getResponseBody();
        os.write(data);
        os.close();
    }
}
