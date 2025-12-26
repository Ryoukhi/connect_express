package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public class ReviewEntityMapper {

	public static ReviewEntity toEntity(Review model) {
		if (model == null) return null;
		ReviewEntity entity = new ReviewEntity();
		entity.setIdReview(model.getIdReview());
		entity.setRating(model.getRating() != null ? Integer.valueOf(model.getRating().getValue()) : null);
		entity.setComment(model.getComment());
		entity.setCreatedAt(model.getCreatedAt());

		if (model.getIdClient() != null) {
			UserEntity client = new UserEntity();
			client.setIdUser(model.getIdClient());
			entity.setClient(client);
		}
		if (model.getIdTechnician() != null) {
			UserEntity tech = new UserEntity();
			tech.setIdUser(model.getIdTechnician());
			entity.setTechnician(tech);
		}

		return entity;
	}

	public static Review toModel(ReviewEntity entity) {
		if (entity == null) return null;

		Long clientId = entity.getClient() != null ? entity.getClient().getIdUser() : null;
		Long techId = entity.getTechnician() != null ? entity.getTechnician().getIdUser() : null;

		int ratingValue = entity.getRating() != null ? entity.getRating() : 1;
		Review model = Review.create(null, clientId, techId, ratingValue, entity.getComment());
		model.setIdReview(entity.getIdReview());
		model.setRating(Rating.of(ratingValue));
		model.setCreatedAt(entity.getCreatedAt());

		return model;
	}

	public static List<ReviewEntity> toEntities(List<Review> models) {
		if (models == null) return null;
		return models.stream().map(ReviewEntityMapper::toEntity).collect(Collectors.toList());
	}

	public static List<Review> toModels(List<ReviewEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(ReviewEntityMapper::toModel).collect(Collectors.toList());
	}

}
