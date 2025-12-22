package com.eadl.connect_backend.application.dto.response.review;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse avis
 */
public class ReviewResponse {
    
    private Long idReview;
    private Long idReservation;
    private Long idClient;
    private String clientName;
    private Long idTechnician;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private boolean reported;
    
    // Getters & Setters
    public Long getIdReview() {
        return idReview;
    }
    
    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }
    
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public Long getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public Long getIdTechnician() {
        return idTechnician;
    }
    
    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isReported() {
        return reported;
    }
    
    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
