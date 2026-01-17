package com.eadl.connect_backend.application.service.review;

import com.eadl.connect_backend.domain.model.review.Rating;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Implémentation du service métier Review
 */
@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(Long idClient, Long idReservation, Rating rating, String comment) {
        log.info("Création d'un nouvel avis pour le client id={} sur la réservation id={}", idClient, idReservation);

        // Enforce one review per reservation
        Optional<Review> existing = reviewRepository.findByClientIdAndReservationId(idClient, idReservation);
        if (existing.isPresent()) {
            log.warn("Le client id={} a déjà laissé un avis pour la réservation id={}", idClient, idReservation);
            throw new IllegalArgumentException(
                    "Un avis existe déjà pour cette réservation. Vous pouvez le modifier si nécessaire.");
        }

        try {
            validateRating(rating);
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la validation de la note pour client id={}: {}", idClient, e.getMessage());
            throw e;
        }

        Review review = new Review();
        review.setIdClient(idClient);
        review.setIdReservation(idReservation); // LINK RESERVATION
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        log.info("Avis créé avec succès id={}, clientId={}, reservationId={}, rating={}",
                saved.getIdReview(), idClient, idReservation, rating.getValue());
        return saved;
    }

    @Override
    public Optional<Review> getReviewById(Long idReview) {
        log.debug("Récupération de l'avis id={}", idReview);
        return reviewRepository.findById(idReview);
    }

    @Override
    public List<Review> getClientReviews(Long idClient) {
        log.debug("Récupération des avis du client id={}", idClient);
        List<Review> reviews = reviewRepository.findByClientId(idClient);
        log.debug("Nombre d'avis récupérés pour client id={}: {}", idClient, reviews.size());
        return reviews;
    }

    @Override
    public Review updateReview(Long idReview, Review updatedReview) {
        log.info("Mise à jour de l'avis id={}", idReview);

        Review existingReview = reviewRepository.findById(idReview)
                .orElseThrow(() -> {
                    log.error("Avis introuvable pour id={}", idReview);
                    return new IllegalArgumentException("Avis introuvable pour l'id : " + idReview);
                });

        try {
            validateRating(updatedReview.getRating());
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la validation de la note pour avis id={}: {}", idReview, e.getMessage());
            throw e;
        }

        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());

        Review saved = reviewRepository.update(idReview, existingReview);
        log.info("Avis id={} mis à jour avec succès, rating={}", saved.getId(), saved.getRating().getValue());
        return saved;
    }

    @Override
    public void deleteReview(Long idAdmin, Long idReview, String reason) {
        log.info("Suppression de l'avis id={} par admin id={}, raison='{}'", idReview, idAdmin, reason);

        if (reason == null || reason.isBlank()) {
            log.error("Tentative de suppression de l'avis id={} sans raison", idReview);
            throw new IllegalArgumentException("Une raison est obligatoire pour supprimer un avis");
        }

        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> {
                    log.error("Avis introuvable pour suppression id={}", idReview);
                    return new IllegalArgumentException("Avis introuvable pour l'id : " + idReview);
                });

        reviewRepository.delete(review);
        log.info("Avis id={} supprimé avec succès", idReview);
    }

    @Override
    public Long countReviews() {
        log.debug("Comptage total des avis");
        Long count = reviewRepository.count();
        log.debug("Nombre total d'avis : {}", count);
        return count;
    }

    private void validateRating(Rating rating) {
        if (rating == null) {
            log.error("Note obligatoire non fournie");
            throw new IllegalArgumentException("La note est obligatoire");
        }

        if (rating.getValue() < 1 || rating.getValue() > 5) {
            log.error("Note invalide: {}", rating.getValue());
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5");
        }

        log.debug("Note validée: {}", rating.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewByClientAndReservation(Long clientId, Long reservationId) {
        log.debug("Récupération de l'avis pour client id={} et réservation id={}", clientId, reservationId);

        if (clientId == null || reservationId == null) {
            log.error("ClientId ou ReservationId manquant pour récupération d'avis");
            throw new IllegalArgumentException("ClientId and ReservationId are required");
        }

        return reviewRepository.findByClientIdAndReservationId(clientId, reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewByReservationId(Long reservationId) {
        log.debug("Récupération de l'avis pour la réservation id={}", reservationId);

        if (reservationId == null) {
            log.error("ReservationId manquant pour récupération d'avis");
            throw new IllegalArgumentException("ReservationId is required");
        }

        return reviewRepository.findByReservationId(reservationId);
    }
}
