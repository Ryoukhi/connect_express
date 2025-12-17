package com.eadl.connect_backend.domain.port.exception;

public class AdminNotFoundException extends RuntimeException {
    
    public AdminNotFoundException(String message) {
        super(message);
    }

}
