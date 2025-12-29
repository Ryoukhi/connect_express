package com.eadl.connect_backend.application.service.review;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service métier Review
 * 
 * <p>
 * Ce service contient les règles métier liées à la gestion
 * des avis clients (création, consultation, modification, suppression).
 * </p>
 *
 * <p>
 * Architecture : Hexagonale (Use Case / Application Service)
 * </p>
 */
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Review createReview() {
        throw new UnsupportedOperationException(
            "La création d'un avis nécessite des paramètres métier"
        );
    }

    /**
     * Crée un nouvel avis client
     *
     * @param idClient identifiant du client
     * @param rating note attribuée
     * @param comment commentaire du client
     * @return avis créé
     */
    public Review createReview(Long idClient, Rating rating, String comment) {

        validateRating(rating);

        Review review = new Review();
        review.setIdClient(idClient);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Review> getReviewById(Long idReview) {
        return reviewRepository.findById(idReview);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getClientReviews(Long idClient) {
        return reviewRepository.findByClientId(idClient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Review updateReview(Long idReview, Review updatedReview) {

        Review existingReview = reviewRepository.findById(idReview)
            .orElseThrow(() -> new IllegalArgumentException(
                "Avis introuvable pour l'id : " + idReview
            ));

        validateRating(updatedReview.getRating());

        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());

        return reviewRepository.update(idReview, existingReview);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReview(Long idAdmin, Long idReview, String reason) {

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                "Une raison est obligatoire pour supprimer un avis"
            );
        }

        Review review = reviewRepository.findById(idReview)
            .orElseThrow(() -> new IllegalArgumentException(
                "Avis introuvable pour l'id : " + idReview
            ));

        // Ici tu peux tracer l'action (audit)
        // auditService.logAdminAction(idAdmin, "DELETE_REVIEW", reason);

        reviewRepository.delete(review);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countReviews() {
        return reviewRepository.count();
    }

    /* =========================
       = MÉTHODES MÉTIER PRIVÉES =
       ========================= */

    /**
     * Valide la note attribuée à un avis
     */
    private void validateRating(Rating rating) {
        if (rating == null) {
            throw new IllegalArgumentException("La note est obligatoire");
        }

        if (rating.getValue() < 1 || rating.getValue() > 5) {
            throw new IllegalArgumentException(
                "La note doit être comprise entre 1 et 5"
            );
        }
    }
}