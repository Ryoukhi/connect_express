package com.eadl.connect_backend.application.dto.request.profile;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO pour la mise à jour du profil professionnel technicien
 */
public class UpdateTechnicianProfileRequest {
    
    @NotBlank(message = "La biographie est obligatoire")
    @Size(max = 1000, message = "La biographie ne peut pas dépasser 1000 caractères")
    private String bio;
    
    @NotNull(message = "Les années d'expérience sont obligatoires")
    @Min(value = 0, message = "Les années d'expérience doivent être positives")
    @Max(value = 50, message = "Les années d'expérience ne peuvent pas dépasser 50")
    private Integer yearsExperience;
    
    @NotNull(message = "Le tarif horaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être supérieur à 0")
    private BigDecimal hourlyRate;
    
    // Getters & Setters
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public Integer getYearsExperience() {
        return yearsExperience;
    }
    
    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
    
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}

