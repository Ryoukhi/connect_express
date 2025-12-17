package com.eadl.connect_backend.application.dto.request.reservation;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour la création d'une réservation
 */
public class CreateReservationRequest {
    
    @NotNull(message = "L'ID du technicien est obligatoire")
    private Long idTechnician;
    
    @NotNull(message = "La date et l'heure sont obligatoires")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime scheduledTime;
    
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal price;
    
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;
    
    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;
    
    // Getters & Setters
    public Long getIdTechnician() {
        return idTechnician;
    }
    
    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
    }
    
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}