package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;

public class ServerInitializer {
    public static void main(String[] args) throws IOException {
        Configurator.setRootLevel(Level.INFO);
        SimpleHttpServer simpleServer = new SimpleHttpServer();
        simpleServer.start(8000);
     //   simpleServer.stop();
    }
}
