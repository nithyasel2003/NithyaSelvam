package com.medicare.claims;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) { }
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/health", new HealthHandler());
        server.createContext("/generate", new GenerateHandler());

        server.setExecutor(null);
        System.out.println("HTTP server started on port " + port);
        server.start();
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><head><title>Medicare Claims XML Generator</title></head>" +
                    "<body>" +
                    "<h1>Medicare Claims XML Generator</h1>" +
                    "<p>Use <code>/generate</code> to create sample XML.</p>" +
                    "<ul>" +
                    "<li><a href=\"/health\">/health</a></li>" +
                    "<li><a href=\"/generate\">/generate</a> (GET: sample defaults; query params supported)</li>" +
                    "</ul>" +
                    "</body></html>";
            sendResponse(exchange, 200, "text/html; charset=utf-8", response.getBytes(StandardCharsets.UTF_8));
        }
    }

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendResponse(exchange, 200, "text/plain; charset=utf-8", "OK".getBytes(StandardCharsets.UTF_8));
        }
    }

    static class GenerateHandler implements HttpHandler {
        private final GenAIXmlGenerator generator = new GenAIXmlGenerator();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> membershipInput = new HashMap<>();

            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getRawQuery();
            if (query != null && !query.isEmpty()) {
                membershipInput.putAll(parseQueryParams(query));
            }

            // Fill defaults if not provided
            membershipInput.putIfAbsent("memberId", "MBR123456789");
            membershipInput.putIfAbsent("medicareId", "1AB2CD3EF45");
            membershipInput.putIfAbsent("firstName", "John");
            membershipInput.putIfAbsent("lastName", "Doe");
            membershipInput.putIfAbsent("dateOfBirth", "1945-06-15");
            membershipInput.putIfAbsent("gender", "M");
            membershipInput.putIfAbsent("street", "123 Main Street");
            membershipInput.putIfAbsent("city", "Anytown");
            membershipInput.putIfAbsent("state", "CA");
            membershipInput.putIfAbsent("zipCode", "90210");
            membershipInput.putIfAbsent("effectiveDate", "2023-01-01");
            membershipInput.putIfAbsent("planType", "Medicare Part B");
            membershipInput.putIfAbsent("serviceDate", "2023-11-15");

            String requirementPath = "src/main/resources/sample_requirement.txt";
            String xml = generator.generateXmlFromRequirement(requirementPath, membershipInput);

            boolean valid = generator.validateXml(xml);
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/xml; charset=utf-8");
            headers.add("X-XML-Valid", valid ? "true" : "false");

            // Save to file as well for convenience
            try {
                generator.saveXmlToFile(xml, "generated_medicare_claim.xml");
            } catch (IOException ignored) { }

            sendResponse(exchange, 200, "application/xml; charset=utf-8", xml.getBytes(StandardCharsets.UTF_8));
        }

        private Map<String, String> parseQueryParams(String query) {
            Map<String, String> map = new HashMap<>();
            for (String pair : query.split("&")) {
                if (pair.isEmpty()) continue;
                String[] parts = pair.split("=", 2);
                String key = urlDecode(parts[0]);
                String value = parts.length > 1 ? urlDecode(parts[1]) : "";
                map.put(key, value);
            }
            return map;
        }

        private String urlDecode(String s) {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        }
    }

    private static void sendResponse(HttpExchange exchange, int status, String contentType, byte[] body) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        if (contentType != null && !contentType.isEmpty()) {
            headers.set("Content-Type", contentType);
        }
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }
}