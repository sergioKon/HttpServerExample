package com.http.wrapper;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;


import java.io.InputStream;

public class HttpRequest  implements RequestContext {
    private final HttpExchange httpExchange;
    private boolean hasBeenRead = false;
    public HttpRequest(HttpExchange httpExchange) {
       this.httpExchange=httpExchange;
    }
    @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        public String getContentType() {
            return httpExchange.getRequestHeaders().getFirst("Content-Type");
        }

    @Override
    public int getContentLength() {
        return httpExchange.getRequestHeaders().getFirst("Content-Length") != null
                ? Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-Length"))
                : -1;
    }

        public InputStream getInputStream() {
            if(hasBeenRead){
                throw new IllegalStateException("Request body has already been read once!");
            }
            hasBeenRead = true;
            return httpExchange.getRequestBody();
        }
}

