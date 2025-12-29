package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.ReviewDto;
import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Mapper utilitaire pour les avis (review)
 */
public class ReviewMapper {

    private ReviewMapper() {
        // Utility class
    }

    public static ReviewDto toDto(Review review) {
        if (review == null) return null;

        ReviewDto dto = new ReviewDto();
        dto.setIdReview(review.getIdReview());
        dto.setIdClient(review.getIdClient());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        return dto;
    }

    public static List<ReviewDto> toDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }
}