package org.example;

import java.io.IOException;

public class ServerInitializer {
    public static void main(String[] args) throws IOException {
        SimpleHttpServer simpleServer = new SimpleHttpServer();
        simpleServer.start(8080);
     //   simpleServer.stop();
    }
}
