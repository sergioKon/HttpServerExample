package com.http.init;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;


public class SimpleHttpServer {
    private HttpServer server;
    private final Logger logger = LogManager.getLogger(SimpleHttpServer.class);
    private Map<String, HttpHandler> httpHandler;


    public void start(int port) throws IOException {
        this.start("127.0.0.1", port);
    }
    public void start(String ipAddr, int port) throws IOException {
        Configurator.setRootLevel(Level.INFO);

        server = HttpServer.create(new InetSocketAddress(ipAddr, port), 0);
        httpHandler.forEach((key, value) -> server.createContext("/"+key,  value));
       /*
        server.createContext("/hello", new HelloHandler());
        server.createContext("/upload", new UploadHandler());
        server.createContext("/readForm", new FormEncodedHandler());
        server.createContext("/multipart", new MultipartHandler());
*/
        server.start();

        logger.info(" start ");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public void setConfigMap(Map<String, HttpHandler> httpHandler) {
        this.httpHandler = httpHandler;
    }
}