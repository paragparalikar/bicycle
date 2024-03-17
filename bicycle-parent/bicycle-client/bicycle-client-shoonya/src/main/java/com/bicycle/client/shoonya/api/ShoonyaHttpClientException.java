package com.bicycle.client.shoonya.api;

import lombok.Getter;

@Getter
public class ShoonyaHttpClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final int statusCode;
    
    public ShoonyaHttpClientException(int statusCode, String message) {
        super(String.join(" : ", String.valueOf(statusCode), message));
        this.message = message;
        this.statusCode = statusCode;
    }
    
}
