package com.http.init;

import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.yaml.snakeyaml.Yaml;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServerInitializer {
    private static final Logger logger = LogManager.getLogger(ServerInitializer.class);
    private  static Map<String, Object> mapConfig;

    public static void main(String[] args) {
        SimpleHttpServer  simpleServer = new SimpleHttpServer();
        try {
            Configurator.setRootLevel(Level.INFO);
            Properties properties = new Properties();
            String root = System.getProperty("user.dir").replace("\\", File.separator);

            InputStream inputStream = ServerInitializer.class.getClassLoader().getResourceAsStream("config.yaml");
            Yaml yaml = new Yaml();
            mapConfig = yaml.load(inputStream);

            String path = root + mapConfig.get("mapper.path"); // "/build/resources/main/";
            Object fileMapper =mapConfig.get("handlers");
            Map<String, HttpHandler> handlerMap = new HashMap<>();
            try (FileInputStream fis = new FileInputStream(path + fileMapper)) {
                properties.load(fis);
                fillMap(handlerMap, properties);
            }
            int port= (int) mapConfig.get("port");
            simpleServer.setConfigMap(handlerMap);
            simpleServer.start(port);
        } catch (Exception e ){
            logger.error(e.getMessage(),e);
            simpleServer.stop();
        }
    }

    private static void fillMap(Map<String, HttpHandler> handlerMap, Properties prop) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        HttpHandler httpHandler;
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            String key = entry.getKey().toString();
            String className =mapConfig.get("package") /* "com.http.handlers."*/ + entry.getValue().toString();
            Class<?> clazz = Class.forName(className);
            httpHandler = (HttpHandler) clazz.getDeclaredConstructor().newInstance();
            handlerMap.put(key, httpHandler);
        }
    }
}
