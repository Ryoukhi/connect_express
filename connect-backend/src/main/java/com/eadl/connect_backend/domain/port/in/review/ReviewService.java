package com.eadl.connect_backend.domain.port.in.review;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Port IN - Service Avis
 * Use cases pour la gestion des avis clients
 */
public interface ReviewService {

    /**
     * Crée un avis
     */
    Review createReview(Long idClient, Long idReservation, Rating rating, String comment);

    /**
     * Récupère un avis par son ID
     */
    Optional<Review> getReviewById(Long idReview);

    /**
     * Récupère tous les avis d'un client
     */
    List<Review> getClientReviews(Long idClient);

    /**
     * Met à jour un avis
     */
    Review updateReview(Long idReview, Review review);

    /**
     * Supprime un avis
     */
    void deleteReview(Long idAdmin, Long idReview, String reason);

    /**
     * Compte tous les avis
     */
    Long countReviews();

    Optional<Review> getReviewByClientAndReservation(
            Long clientId,
            Long reservationId);

    Optional<Review> getReviewByReservationId(Long reservationId);
}
