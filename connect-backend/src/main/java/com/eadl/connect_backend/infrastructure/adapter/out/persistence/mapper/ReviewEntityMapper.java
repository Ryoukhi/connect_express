package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

/**
 * Mapper ReviewEntity <-> Review (Domain)
 *
 * <p>
 * Assure la conversion entre le modèle de persistance (JPA)
 * et le modèle métier (Domain), sans logique métier.
 * </p>
 */
@Component
public class ReviewEntityMapper {

    /**
     * Convertit un Review du domaine vers une ReviewEntity JPA
     */
    public ReviewEntity toEntity(Review review) {
        if (review == null) {
            return null;
        }

        ReviewEntity entity = new ReviewEntity();
        entity.setIdReview(review.getIdReview());
        entity.setRating(review.getRating() != null
                ? review.getRating().getValue()
                : null);
        entity.setComment(review.getComment());
        entity.setCreatedAt(review.getCreatedAt());

        // Association client (par référence uniquement)
        if (review.getIdClient() != null) {
            UserEntity clientRef = new UserEntity();
            clientRef.setIdUser(review.getIdClient());
            entity.setClient(clientRef);
        }

        return entity;
    }

    /**
     * Convertit une ReviewEntity JPA vers un Review du domaine
     */
    public Review toDomain(ReviewEntity entity) {
        if (entity == null) {
            return null;
        }

        Review review = new Review();
        review.setIdReview(entity.getIdReview());
        review.setRating(entity.getRating() != null
                ? Rating.fromValue(entity.getRating())
                : null);
        review.setComment(entity.getComment());
        review.setCreatedAt(entity.getCreatedAt());

        if (entity.getClient() != null) {
            review.setIdClient(entity.getClient().getIdUser());
        }

        return review;
    }

    /**
     * Convertit une liste d'entités vers le domaine
     */
    public List<Review> toDomainList(List<ReviewEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de modèles domaine vers des entités
     */
    public List<ReviewEntity> toEntityList(List<Review> reviews) {
        if (reviews == null) {
            return List.of();
        }
        return reviews.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

	public void updateEntity(ReviewEntity entity, Review review) {
    if (review.getRating() != null) {
        entity.setRating(review.getRating().getValue());
    }
    entity.setComment(review.getComment());
}

	public ReviewEntity toEntityIdOnly(Long idReview) {
		if (idReview == null) {
            throw new IllegalArgumentException("Review id must not be null");
        }

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setIdReview(idReview);
        return reviewEntity;
	}

}