package org.example;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequest  implements RequestContext {
    private final HttpExchange httpExchange;
    private final String content;
    public HttpRequest(HttpExchange httpExchange) {
       this.httpExchange=httpExchange;
        try {
            byte[] stringBytes= httpExchange.getRequestBody().readAllBytes();
            content= new String(stringBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        public String getContentType() {
            return httpExchange.getRequestHeaders().getFirst("Content-Type");
        }

        public int getContentLength() {
            return 0;
        }

        public InputStream getInputStream() {
            return httpExchange.getRequestBody();
        }

    public String getContent() {
        return content;
    }
}

