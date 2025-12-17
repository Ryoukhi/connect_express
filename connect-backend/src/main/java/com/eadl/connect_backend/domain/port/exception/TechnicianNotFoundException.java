package com.eadl.connect_backend.domain.port.exception;

public class TechnicianNotFoundException extends RuntimeException {
    
    public TechnicianNotFoundException(String message) {
        super(message);
    }

}
