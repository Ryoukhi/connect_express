package com.eadl.connect_backend.application.dto.request.review;

import jakarta.validation.constraints.*;

/**
 * DTO pour la création d'un avis
 */
public class CreateReviewRequest {
    
    @NotNull(message = "L'ID de la réservation est obligatoire")
    private Long idReservation;
    
    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    private Integer rating;
    
    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 1000, message = "Le commentaire ne peut pas dépasser 1000 caractères")
    private String comment;
    
    // Getters & Setters
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
}