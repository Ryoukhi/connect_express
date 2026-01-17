package com.eadl.connect_backend.domain.port.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Port OUT - Repository Review
 */
public interface ReviewRepository {

    /**
     * Sauvegarde un avis
     */
    Review save(Review review);

    /**
     * Modifie un avis
     */
    Review update(Long idReview, Review review);

    /**
     * Récupère un avis par son ID
     */
    Optional<Review> findById(Long idReview);

    /**
     * Récupère tous les avis d'un client
     */
    List<Review> findByClientId(Long idClient);

    /**
     * Compte tous les avis
     */
    Long count();

    /**
     * Supprime un avis
     */
    void delete(Review review);

    @Query("""
                SELECT AVG(r.rating.value)
                FROM Review r
                WHERE r.idReview IN :reviewIds
            """)
    BigDecimal calculateAverageRating(List<Long> reviewIds);

    Optional<Review> findByClientIdAndReservationId(
            Long clientId,
            Long reservationId);

    Optional<Review> findByReservationId(Long reservationId);
}