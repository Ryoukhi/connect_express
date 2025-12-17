package com.eadl.connect_backend.domain.port.exception;

public class DocumentNotFoundException extends RuntimeException {
    
    public DocumentNotFoundException(String message) {
        super(message);
    }

}
