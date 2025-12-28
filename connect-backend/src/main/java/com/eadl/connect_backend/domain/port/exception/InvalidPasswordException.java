package com.eadl.connect_backend.domain.port.exception;

public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String message) {
        super(message);
    }

}
