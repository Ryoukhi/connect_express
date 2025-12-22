package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.review.ReviewResponse;
import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Mapper utilitaire pour les avis (review)
 */
public class ReviewMapper {

    /**
     * Convertit un domaine Review en ReviewResponse. Optionnellement on peut fournir
     * le nom du client pour l'inclure dans la DTO.
     */
    public static ReviewResponse toResponse(Review review) {
        return toResponse(review, null);
    }

    public static ReviewResponse toResponse(Review review, String clientName) {
        if (review == null) return null;
        ReviewResponse dto = new ReviewResponse();
        dto.setIdReview(review.getIdReview());
        dto.setIdReservation(review.getIdReservation());
        dto.setIdClient(review.getIdClient());
        dto.setClientName(clientName);
        dto.setIdTechnician(review.getIdTechnician());
        dto.setRating(review.getRating() != null ? review.getRating().getValue() : null);
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setReported(review.isReported());
        return dto;
    }

    public static List<ReviewResponse> toResponses(List<Review> reviews) {
        if (reviews == null) return null;
        return reviews.stream().map(ReviewMapper::toResponse).collect(Collectors.toList());
    }
}

