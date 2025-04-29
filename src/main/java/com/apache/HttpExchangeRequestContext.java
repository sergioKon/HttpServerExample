package com.apache;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.InputStream;


public class HttpExchangeRequestContext implements RequestContext {
    private final HttpExchange exchange;

    public HttpExchangeRequestContext(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getCharacterEncoding() {
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        String encoding = "UTF-8"; // Default encoding

        if (contentType != null && contentType.contains("charset=")) {
            encoding = contentType.split("charset=")[1];
        }
        return encoding;
    }

    @Override
    public String getContentType() {
        return exchange.getRequestHeaders().getFirst("Content-Type");
    }

    @Override
    public int getContentLength() {
        return exchange.getRequestHeaders().getFirst("Content-Length") != null
                ? Integer.parseInt(exchange.getRequestHeaders().getFirst("Content-Length"))
                : -1;
    }

    @Override
    public InputStream getInputStream() {
        return exchange.getRequestBody();
    }
}

