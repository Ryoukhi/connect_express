package com.eadl.connect_backend.domain.model.review;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Review - Avis client sur une prestation
 */
public class Review {
    private Long idReview;
    private Long idReservation;
    private Long idClient;
    private Long idTechnician;
    private Rating rating;
    private String comment;
    private LocalDateTime createdAt;

    private Review() {}

    // ========== Factory Method ==========
    public static Review create(Long idReservation, Long idClient, 
                               Long idTechnician, int ratingValue, 
                               String comment) {
        Review review = new Review();
        review.idReservation = idReservation;
        review.idClient = idClient;
        review.idTechnician = idTechnician;
        review.rating = Rating.of(ratingValue);
        review.comment = comment;
        review.createdAt = LocalDateTime.now();
        return review;
    }

    // ========== Business Logic Methods ==========
    public void updateReview(int newRating, String newComment) {
        this.rating = Rating.of(newRating);
        this.comment = newComment;
    }


    public boolean isPositive() {
        return rating.isExcellent();
    }

    public boolean isNegative() {
        return rating.isPoor();
    }

    // ========== Getters ==========
    public Long getIdReview() {
        return idReview;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public Long getIdClient() {
        return idClient;
    }

    public Long getIdTechnician() {
        return idTechnician;
    }

    public Rating getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    
    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(idReview, review.idReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReview);
    }
}
