package com.eadl.connect_backend.application.service.review;

import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des avis
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Review createReview(Long idReservation, Long idClient, Long idTechnician, int rating, String comment) {
        log.info("Création d'un avis pour la réservation id={}", idReservation);

        if (reviewRepository.existsByReservationId(idReservation)) {
            throw new IllegalStateException("Un avis existe déjà pour cette réservation");
        }

        var reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable"));

        if (!reservation.isCompleted()) {
            throw new IllegalStateException("L'avis ne peut être créé que pour une réservation complétée");
        }

        if (!reservation.getIdClient().equals(idClient) || !reservation.getIdTechnician().equals(idTechnician)) {
            throw new IllegalArgumentException("Données de réservation incompatibles avec le client/technicien fournis");
        }

        Review review = Review.create(idReservation, idClient, idTechnician, rating, comment);
        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getReviewById(Long idReview) {
        return reviewRepository.findById(idReview);
    }

    @Override
    public Optional<Review> getReviewByReservation(Long idReservation) {
        return reviewRepository.findByReservationId(idReservation);
    }

    @Override
    public List<Review> getTechnicianReviews(Long idTechnician) {
        return reviewRepository.findByTechnicianId(idTechnician);
    }

    @Override
    public List<Review> getClientReviews(Long idClient) {
        return reviewRepository.findByClientId(idClient);
    }

    @Override
    public Review updateReview(Long idReview, int newRating, String newComment) {
        log.info("Mise à jour de l'avis id={}", idReview);
        Review existing = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Avis introuvable"));

        existing.updateReview(newRating, newComment);
        return reviewRepository.save(existing);
    }

    @Override
    public void deleteReview(Long idAdmin, Long idReview, String reason) {
        log.info("Suppression de l'avis id={} par admin id={} raison={}", idReview, idAdmin, reason);
        Review existing = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Avis introuvable"));

        // For auditability you might want to store deletion reason in an audit log; here we just log and delete
        reviewRepository.delete(existing);
    }

    @Override
    public Long countReviews(Long idTechnician) {
        return reviewRepository.countByTechnicianId(idTechnician);
    }
    
}