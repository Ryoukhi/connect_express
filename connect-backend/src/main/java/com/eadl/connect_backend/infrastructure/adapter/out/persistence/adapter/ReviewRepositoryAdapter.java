package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.ReviewEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * Adapter PERSISTENCE - Review
 *
 * Implémentation du port OUT ReviewRepository
 * Responsable de la conversion Domain <-> Entity
 */
@Repository
@RequiredArgsConstructor
@Transactional
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewEntityMapper reviewEntityMapper;

    /* =========================
       CREATE
       ========================= */
    @Override
    public Review save(Review review) {
        ReviewEntity entity = reviewEntityMapper.toEntity(review);
        ReviewEntity saved = reviewJpaRepository.save(entity);
        return reviewEntityMapper.toModel(saved);
    }

    /* =========================
       UPDATE
       ========================= */
    @Override
    public Review update(Long idReview, Review review) {
        ReviewEntity entity = reviewJpaRepository.findById(idReview)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Review not found with id " + idReview));

        reviewEntityMapper.updateEntity(entity, review);

        ReviewEntity updated = reviewJpaRepository.save(entity);
        return reviewEntityMapper.toModel(updated);
    }

    /* =========================
       FIND BY ID
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(Long idReview) {
        return reviewJpaRepository.findById(idReview)
                .map(reviewEntityMapper::toModel);
    }

    /* =========================
       FIND BY CLIENT
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public List<Review> findByClientId(Long idClient) {
        return reviewJpaRepository.findByClient_IdUser(idClient)
                .stream()
                .map(reviewEntityMapper::toModel)
                .toList();
    }

    /* =========================
       COUNT
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return reviewJpaRepository.count();
    }

    /* =========================
       DELETE
       ========================= */
    @Override
    public void delete(Review review) {
        if (review.getIdReview() == null) {
            throw new IllegalArgumentException("Review id must not be null");
        }
        reviewJpaRepository.deleteById(review.getIdReview());
    }

    /* =========================
       BUSINESS – AVG RATING
       ========================= */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAverageRating(List<Long> reviewIds) {

        if (reviewIds == null || reviewIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<Integer> ratings = reviewJpaRepository
                .findRatingsByReviewIds(reviewIds);

        if (ratings.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = ratings.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(
                BigDecimal.valueOf(ratings.size()),
                2,
                RoundingMode.HALF_UP
        );
    }

    @Override
    public Optional<Review> findByClientIdAndReservationId(
            Long clientId,
            Long reservationId
    ) {
        return reviewJpaRepository
                .findByClient_IdUserAndReservation_IdReservation(clientId, reservationId)
                .map(reviewEntityMapper::toModel);
    }
}
