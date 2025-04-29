package  com.infra;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {

    public static void sendResponse(HttpExchange exchange, String msg, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, msg.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(msg.getBytes());
        }
    }
}
