package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.ReviewDto;
import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Mapper utilitaire pour les avis (review)
 */
@Component
public class ReviewMapper {

    private ReviewMapper() {
        // Utility class
    }

    public ReviewDto toDto(Review review) {
        if (review == null) return null;

        ReviewDto dto = new ReviewDto();
        dto.setIdReview(review.getIdReview());
        dto.setIdClient(review.getIdClient());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        return dto;
    }

    public Review toModel(ReviewDto dto) {
        if (dto == null) return null;

        Review review = new Review();
        review.setIdReview(dto.getIdReview());
        review.setIdClient(dto.getIdClient());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(dto.getCreatedAt());

        return review;
    }

    public List<ReviewDto> toDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Review> toModelList(List<ReviewDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}