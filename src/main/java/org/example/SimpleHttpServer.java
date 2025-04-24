package org.example;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.example.handlers.FormEncodedHandler;
import org.example.handlers.HelloHandler;
import org.example.handlers.MultipartHandler;
import org.example.handlers.UploadHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class SimpleHttpServer {
    private HttpServer server;
    private final Logger logger = LogManager.getLogger(SimpleHttpServer.class);
    public void start(int port) throws IOException {
        Configurator.setRootLevel(Level.INFO);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/hello", new HelloHandler());
        server.createContext("/upload", new UploadHandler());
        server.createContext("/readForm", new FormEncodedHandler());
        server.createContext("/multipart", new MultipartHandler());
        server.start();

        logger.info(" start ");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}