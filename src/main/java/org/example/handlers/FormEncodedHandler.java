package org.example.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class FormEncodedHandler implements HttpHandler {
    private final static Logger logger = Logger.getLogger(FormEncodedHandler.class.getName());
    public void handle(HttpExchange exchange) throws IOException {

        StringBuilder buffer= getClientData(exchange);
        Headers headers= exchange.getRequestHeaders();
        List<String> separator= headers.get("separator");
        String symbol = "";
        if(separator==null) {
            symbol="&";
        }
        else {
            symbol=separator.getFirst();
        }
        Map<String, Object> mapParams = createMap(buffer, symbol);
        if (mapParams == null ) {
            exchange.sendResponseHeaders(400, "no params".length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write( "no params".getBytes(StandardCharsets.UTF_8));
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(mapParams);

        exchange.sendResponseHeaders(200, json.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
           }
        }

    private StringBuilder getClientData(HttpExchange exchange) throws IOException {
        String line;
        InputStream inputStream= exchange.getRequestBody();
        BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder buffer= new StringBuilder();
        while ((line= stream.readLine()) != null) {
            buffer.append(line);
        }
        return  buffer;
    }

    private static Map<String, Object> createMap(StringBuilder buffer, String separator) {
        Map<String, Object> mapParams = new HashMap<>();
        String[] params = buffer.toString().split(separator);
        if(params.length < 2) {
            logger.severe(" there is no way to parse parameters ");
            return null;
        }
        for (String p : params){
            String[] pair= p.split("=");
            String key = pair[0];
            String value = pair[1];
            mapParams.put(key, value);
        }
        return mapParams;
    }
}

