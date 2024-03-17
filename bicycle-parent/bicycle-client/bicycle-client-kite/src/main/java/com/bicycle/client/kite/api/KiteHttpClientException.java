package com.bicycle.client.kite.api;

import lombok.Getter;

@Getter
public class KiteHttpClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final int statusCode;
    private final String message;

    public KiteHttpClientException(int statusCode, String message) {
        super(String.join(" : ", String.valueOf(statusCode), message));
        this.message = message;
        this.statusCode = statusCode;
    }
    
}
