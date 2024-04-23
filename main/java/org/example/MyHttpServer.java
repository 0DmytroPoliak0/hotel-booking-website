package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class MyHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/topCustomers", new TopCustomersHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
