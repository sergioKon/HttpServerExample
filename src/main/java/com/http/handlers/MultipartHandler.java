package com.http.handlers;

import com.file.actions.FileSaver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.infra.Response.sendResponse;


public class MultipartHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(MultipartHandler.class);
    private  String uploadId;
    public void handle(HttpExchange exchange) throws IOException {
        try {
            parse(exchange);
            String msg="success";
            sendResponse(exchange, msg,200);
        } catch (IOException e) {
            sendResponse(exchange,e.getMessage(),500);
        }
    }

    public void parse(HttpExchange exchange) throws IOException {

        List<String> content = new ArrayList<>();
        if (!isMultipart(exchange)){
            return;
        }

        StringBuilder buffer= new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            String line;
               while ((line = reader.readLine()) != null){
                   if (line.contains(uploadId)){
                       if (buffer.isEmpty()) {
                           continue;
                       }
                       content.add(buffer.toString());
                       buffer.setLength(0);
                       continue;
                   }
                     buffer.append(line).append("\t");
               }
            saveData(content);
        }
    }

    private  void saveData(List<String> content) throws IOException {
        for(String data : content) {
            logger.debug("data = {}", data);
            FileSaver fileSaver=new FileSaver(data);
            fileSaver.start();
        }
    }

    private boolean isMultipart(HttpExchange exchange) throws IOException {
        String[] formHeader= exchange.getRequestHeaders().get("Content-Type").getFirst().split(";");
        String form=formHeader[0];
        if(!form.equals("multipart/form-data")) {
            String errorMessage=  "BadRequest";
            exchange.sendResponseHeaders(400,errorMessage.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorMessage.getBytes(StandardCharsets.UTF_8));

            }
            logger.error(" this is not multipart ");
            return false;
        }
        uploadId= formHeader[1].replace("-","");
        uploadId= uploadId.split("=")[1];
        return true;
    }
}

