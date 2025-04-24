
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sun.net.httpserver.HttpServer;
import org.example.handlers.HelloHandler;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class SimpleHttpServerTest {
    private static HttpServer server;
    private static final int PORT = 8080;
    private static HttpClient client;


    @BeforeAll
    static void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/hello", new HelloHandler());
        server.setExecutor(null);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        server.stop(0);
    }

    @Test
    void testHelloEndpoint() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/hello"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Hello, World!", response.body());
        server.stop(0);
    }
}