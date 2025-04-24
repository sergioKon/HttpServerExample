package com.file.op;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FileSaver {
    private  String data;
    private String filename;
    private static final Logger logger= LogManager.getLogger(FileSaver.class);
    public FileSaver(byte[] data){
        this.data= new String(data);
    }
    public FileSaver(String data) {
        this.data=data;
        readFileName();
        final String contentType = "Content-Type: application/octet-stream";
        int iStart = data.indexOf(contentType);
        if (iStart != -1) {
            this.data = data.substring(iStart + contentType.length());
        }
        else {
            logger.error(" this is not standard http format");
        }
    }

    private void readFileName() {
        // // data = Content-Disposition: form-data; name="file"; filename="file1"	Content-Type: application/octet-stream		xyz	123	mydata	end
        int iStart = data.indexOf("filename=");
        int iEnd= data.indexOf("\"", iStart+"filename=".length()+1);
        filename= data.substring(iStart+"filename=".length()+1,iEnd);
    }

    public void start() throws IOException {

        Thread thread = new Thread(() -> {
            try {
                createFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private void createFile() throws IOException {
        String dir= System.getProperty("user.dir")+File.separator+ "out/";

        Path dirPath = Paths.get(dir);
        Files.createDirectories(dirPath);
        filename= dir.replace('\\', '/') +filename+".txt";
        try (
              FileOutputStream outputStream = new FileOutputStream(filename) ) {
              outputStream.write(data.getBytes());
              Calendar calendar = Calendar.getInstance(); // current date/time
              SimpleDateFormat formatter = new SimpleDateFormat("MMM dd  HH:mm:ss:sss");
              String formattedDate = formatter.format(calendar.getTime());
              logger.info(" created at {} file = {} thread = {}",
                                   formattedDate, filename,Thread.currentThread().threadId());
        }
    }
}
