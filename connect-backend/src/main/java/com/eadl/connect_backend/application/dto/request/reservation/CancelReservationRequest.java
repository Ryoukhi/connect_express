package com.eadl.connect_backend.application.dto.request.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CancelReservationRequest {
    
    @NotBlank(message = "La raison d'annulation est obligatoire")
    @Size(max = 500)
    private String reason;
    
    // Getters & Setters
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}