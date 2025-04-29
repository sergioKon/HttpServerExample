package com.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.infra.Response.sendResponse;


public class UploadHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(UploadHandler.class);
    public void handle(HttpExchange exchange) throws IOException {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        RequestContext ctx = new HttpRequest(exchange);
        try {
            final List<FileItem> items = upload.parseRequest(ctx);
            if(items.isEmpty()) {
                sendResponse(exchange, "there is no files ",400);
            }
            for (FileItem item : items) {
                if (item.isFormField()) {
                    continue;
                }
                logger.info("Received file: {}", item.getName());
                String dir= System.getProperty("user.dir");
                File folder = new File(dir+ "/uploads/");
                if( folder.mkdir() ) {
                    logger.trace(" folder created");
                }
                else {
                    logger.trace(" folder already  exists ");
                }
                String uploadedFile= folder.getAbsolutePath()+"/"+item.getName();
                File uploaded = new File(uploadedFile);
                item.write(uploaded); // Save the uploaded file
            }
            sendResponse(exchange, "Received " + items.size() +" files ",200);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            sendResponse(exchange, e.getMessage(),500);
        }
    }
}


