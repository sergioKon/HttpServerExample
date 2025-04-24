package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.example.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class UploadHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        RequestContext ctx = new HttpRequest(exchange);
        try {
            final List<FileItem> items = upload.parseRequest(ctx);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    continue;
                }
                System.out.println("Received file: " + item.getName());
                File uploaded = new File("uploads/" + item.getName());
                item.write(uploaded); // Save the uploaded file
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


