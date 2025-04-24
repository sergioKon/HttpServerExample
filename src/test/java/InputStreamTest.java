import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InputStreamTest {

    @Test
    void StreamReader() throws IOException, URISyntaxException {
        String path="C:/Users/Boris/OneDrive/Documents/letter+with_C++.txt";
        try (InputStream inputStream = new FileInputStream(path);) {
            InputStream stream = new ByteArrayInputStream(inputStream.readAllBytes());
            long size =  new File(path).length();
            byte[] data=stream.readAllBytes();

                String text = new String(data, StandardCharsets.UTF_8);
                System.out.println("------------------------------------------------");
                System.out.println(text);
                System.out.println("------------------------------------------------");

        }
    }
    @Test
    void saveFile() throws Exception {
        String filename="file1.txt";
       try(  FileOutputStream outputStream = new FileOutputStream(filename)){
           outputStream.write("data".getBytes());
       };
    }
}
