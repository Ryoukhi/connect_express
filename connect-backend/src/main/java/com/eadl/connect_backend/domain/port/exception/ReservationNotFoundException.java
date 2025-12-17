package com.eadl.connect_backend.domain.port.exception;

public class ReservationNotFoundException extends RuntimeException {
    
    public ReservationNotFoundException(String message) {
        super(message);
    }

}
