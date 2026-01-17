package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.ReviewEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ReviewJpaRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ReservationJpaRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;

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

    private static final Logger log = LoggerFactory.getLogger(ReviewRepositoryAdapter.class);

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final ReviewEntityMapper reviewEntityMapper;

    /*
     * =========================
     * CREATE
     * =========================
     */
    @Override
    public Review save(Review review) {
        log.info("Saving review: {}", review);

        ReviewEntity entity = reviewEntityMapper.toEntity(review);

        // Handle Reservation link
        if (review.getIdReservation() != null) {
            ReservationEntity reservation = reservationJpaRepository.findById(review.getIdReservation())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Reservation not found: " + review.getIdReservation()));
            entity.setReservation(reservation);

            // Save review first to get ID
            ReviewEntity saved = reviewJpaRepository.save(entity);

            // Update reservation with review reference
            reservation.setReview(saved);
            reservationJpaRepository.save(reservation);

            Review savedReview = reviewEntityMapper.toModel(saved);
            log.info("Review saved with id {} and linked to reservation {}", savedReview.getIdReview(),
                    review.getIdReservation());
            return savedReview;
        }

        ReviewEntity saved = reviewJpaRepository.save(entity);
        Review savedReview = reviewEntityMapper.toModel(saved);

        log.info("Review saved with id {}", savedReview.getIdReview());
        return savedReview;
    }

    /*
     * =========================
     * UPDATE
     * =========================
     */
    @Override
    public Review update(Long idReview, Review review) {
        log.info("Updating review id {} with data: {}", idReview, review);

        ReviewEntity entity = reviewJpaRepository.findById(idReview)
                .orElseThrow(() -> {
                    log.error("Review not found with id {}", idReview);
                    return new IllegalArgumentException(
                            "Review not found with id " + idReview);
                });

        reviewEntityMapper.updateEntity(entity, review);

        ReviewEntity updated = reviewJpaRepository.save(entity);
        Review updatedReview = reviewEntityMapper.toModel(updated);

        log.info("Review updated successfully: {}", updatedReview);
        return updatedReview;
    }

    /*
     * =========================
     * FIND BY ID
     * =========================
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(Long idReview) {
        log.info("Finding review by id {}", idReview);

        Optional<Review> review = reviewJpaRepository.findById(idReview)
                .map(reviewEntityMapper::toModel);

        review.ifPresentOrElse(
                r -> log.info("Review found: {}", r),
                () -> log.warn("No review found with id {}", idReview));

        return review;
    }

    /*
     * =========================
     * FIND BY CLIENT
     * =========================
     */
    @Override
    @Transactional(readOnly = true)
    public List<Review> findByClientId(Long idClient) {
        log.info("Finding reviews for client id {}", idClient);

        List<Review> reviews = reviewJpaRepository.findByClient_IdUser(idClient)
                .stream()
                .map(reviewEntityMapper::toModel)
                .toList();

        log.info("Found {} reviews for client id {}", reviews.size(), idClient);
        return reviews;
    }

    /*
     * =========================
     * COUNT
     * =========================
     */
    @Override
    @Transactional(readOnly = true)
    public Long count() {
        log.info("Counting all reviews");

        Long count = reviewJpaRepository.count();

        log.info("Total reviews count: {}", count);
        return count;
    }

    /*
     * =========================
     * DELETE
     * =========================
     */
    @Override
    public void delete(Review review) {
        if (review.getIdReview() == null) {
            log.error("Cannot delete review: id is null");
            throw new IllegalArgumentException("Review id must not be null");
        }

        log.info("Deleting review with id {}", review.getIdReview());
        reviewJpaRepository.deleteById(review.getIdReview());
        log.info("Review deleted successfully");
    }

    /*
     * =========================
     * BUSINESS – AVG RATING
     * =========================
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAverageRating(List<Long> reviewIds) {
        log.info("Calculating average rating for review IDs: {}", reviewIds);

        if (reviewIds == null || reviewIds.isEmpty()) {
            log.warn("Review IDs list is empty or null – returning 0");
            return BigDecimal.ZERO;
        }

        List<Integer> ratings = reviewJpaRepository.findRatingsByReviewIds(reviewIds);

        if (ratings.isEmpty()) {
            log.warn("No ratings found for review IDs {}", reviewIds);
            return BigDecimal.ZERO;
        }

        BigDecimal sum = ratings.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(
                BigDecimal.valueOf(ratings.size()),
                2,
                RoundingMode.HALF_UP);

        log.info("Average rating calculated: {}", average);
        return average;
    }

    /*
     * =========================
     * FIND BY CLIENT + RESERVATION
     * =========================
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findByClientIdAndReservationId(
            Long clientId,
            Long reservationId) {
        log.info(
                "Finding review for client {} and reservation {}",
                clientId,
                reservationId);

        Optional<Review> review = reviewJpaRepository
                .findByClient_IdUserAndReservation_IdReservation(
                        clientId, reservationId)
                .map(reviewEntityMapper::toModel);

        review.ifPresentOrElse(
                r -> log.info("Review found: {}", r),
                () -> log.warn(
                        "No review found for client {} and reservation {}",
                        clientId,
                        reservationId));

        return review;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findByReservationId(Long reservationId) {
        log.info("Finding review for reservation id {}", reservationId);

        return reviewJpaRepository.findByReservation_IdReservation(reservationId)
                .map(reviewEntityMapper::toModel);
    }
}
