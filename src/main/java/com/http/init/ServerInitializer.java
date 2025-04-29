package com.http.init;

import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerInitializer {
    private static final Logger logger = LogManager.getLogger(ServerInitializer.class);
    public static void main(String[] args) {
        try {
            Configurator.setRootLevel(Level.INFO);
            HttpHandler httpHandler;
            Properties properties = new Properties();
            String root = System.getProperty("user.dir").replace("\\", File.separator);
            String path = root + "/build/resources/main/";
            String fileMapper = "handlers.properties";
            Map<String, HttpHandler> handlerMap = new HashMap<>();
            try (FileInputStream fis = new FileInputStream(path + fileMapper)) {
                properties.load(fis);
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    String key = entry.getKey().toString();
                    String className = "com.http.handlers." + entry.getValue().toString();
                    Class<?> clazz = Class.forName(className);
                    httpHandler = (HttpHandler) clazz.getDeclaredConstructor().newInstance();
                    handlerMap.put(key, httpHandler);
                }
            }

            SimpleHttpServer simpleServer = new SimpleHttpServer();
            simpleServer.setConfigMap(handlerMap);
            simpleServer.start(8000);
        } catch (Exception e ){
            logger.error(e.getMessage(),e);
        }
     //   simpleServer.stop();
    }
}
