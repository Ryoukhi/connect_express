package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

@Component
public class ReviewEntityMapper {

    /* =========================
       Entity -> Domain
       ========================= */
    public Review toModel(ReviewEntity entity) {
        if (entity == null) {
            return null;
        }

        Review review = new Review();
        review.setIdReview(entity.getIdReview());
        review.setComment(entity.getComment());
        review.setCreatedAt(entity.getCreatedAt());

        // Rating (Value Object)
        if (entity.getRating() != null) {
            review.setRating(Rating.fromValue(entity.getRating().getValue()));
        }

        // Client (ID only)
        if (entity.getClient() != null) {
            review.setIdClient(entity.getClient().getIdUser());
        }

        return review;
    }

    /* =========================
       Domain -> Entity (CREATE)
       ========================= */
    public ReviewEntity toEntity(Review review) {
        if (review == null) {
            return null;
        }

        ReviewEntity entity = new ReviewEntity();
        entity.setIdReview(review.getIdReview());
        entity.setComment(review.getComment());
        entity.setCreatedAt(review.getCreatedAt());

        // Rating
        if (review.getRating() != null) {
            entity.setRating(Rating.fromValue(review.getRating().getValue()));
        }

        // Client reference
        if (review.getIdClient() != null) {
            UserEntity client = new UserEntity();
            client.setIdUser(review.getIdClient());
            entity.setClient(client);
        }

        return entity;
    }

    /* =========================
       UPDATE EXISTING ENTITY
       ========================= */
    public void updateEntity(ReviewEntity entity, Review review) {

        if (review.getComment() != null) {
            entity.setComment(review.getComment());
        }

        if (review.getRating() != null) {
            entity.setRating(Rating.fromValue(review.getRating().getValue()));
        }
    }
}
