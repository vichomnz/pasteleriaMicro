package com.pasteleria.pasteleriaMicro.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String error;
    private final int statusCode;
    
    public CustomException(String message, String error, int statusCode) {
        super(message);
        this.error = error;
        this.statusCode = statusCode;
    }
}
