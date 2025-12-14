package com.eadl.connect_backend.application.service.review;

import com.eadl.connect_backend.domain.model.Review;
import com.eadl.connect_backend.domain.port.in.review.ReviewService;
import com.eadl.connect_backend.domain.port.out.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    // ========== CREATE ==========

    @Override
    public Review createReview(Review review) {
        log.info("Création d'un nouvel avis pour le service {} par l'utilisateur {}", review.getServiceId(), review.getUserId());

        if (hasUserReviewedService(review.getUserId(), review.getServiceId())) {
            throw new IllegalArgumentException("Vous avez déjà laissé un avis pour ce service");
        }

        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }

        if (review.getVerified() == null) {
            review.setVerified(false);
        }

        Review savedReview = reviewRepository.save(review);
        log.info("Avis créé avec succès avec l'ID: {}", savedReview.getId());
        return savedReview;
    }

    @Override
    public Review createReview(Long userId, Long serviceId, int rating, String comment) {
        log.info("Création d'un avis pour le service {} par l'utilisateur {}", serviceId, userId);

        Review review = new Review();
        review.setUserId(userId);
        review.setServiceId(serviceId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setVerified(false);

        return createReview(review);
    }

    @Override
    public Review createReviewWithImages(Long userId, Long serviceId, int rating, String comment, List<String> imageUrls) {
        // TODO: Implémenter
        return null;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        log.debug("Recherche de l'avis avec l'ID: {}", id);
        return reviewRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getServiceReviews(Long serviceId) {
        log.debug("Récupération des avis du service: {}", serviceId);
        return reviewRepository.findByServiceIdOrderByCreatedAtDesc(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getUserReviews(Long userId) {
        log.debug("Récupération des avis de l'utilisateur: {}", userId);
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getServiceReviews(Long serviceId, int page, int size) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByRating(Long serviceId, int rating) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getTopReviews(Long serviceId, int limit) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getRecentReviews(Long serviceId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getVerifiedReviews(Long serviceId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getUserReviewForService(Long userId, Long serviceId) {
        // TODO: Implémenter
        return Optional.empty();
    }

    // ========== UPDATE ==========

    @Override
    public Review updateReview(Long id, Review review) {
        log.info("Mise à jour de l'avis avec l'ID: {}", id);

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis non trouvé avec l'ID: " + id));

        if (review.getRating() != null) {
            existingReview.setRating(review.getRating());
        }
        if (review.getComment() != null) {
            existingReview.setComment(review.getComment());
        }
        if (review.getImageUrls() != null) {
            existingReview.setImageUrls(review.getImageUrls());
        }

        existingReview.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(existingReview);
        log.info("Avis mis à jour avec succès: {}", id);
        return updatedReview;
    }

    @Override
    public Review updateReviewContent(Long reviewId, Long userId, int rating, String comment) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public void markAsVerified(Long reviewId) {
        // TODO: Implémenter
    }

    @Override
    public Review addProviderResponse(Long reviewId, Long providerId, String response) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public void likeReview(Long reviewId, Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void unlikeReview(Long reviewId, Long userId) {
        // TODO: Implémenter
    }

    // ========== DELETE ==========

    @Override
    public void deleteReview(Long id, Long userId) {
        log.info("Suppression de l'avis {} par l'utilisateur {}", id, userId);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis non trouvé avec l'ID: " + id));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Vous n'êtes pas l'auteur de cet avis");
        }

        reviewRepository.delete(review);
        log.info("Avis supprimé avec succès: {}", id);
    }

    @Override
    public void reportReview(Long reviewId, Long userId, String reason) {
        // TODO: Implémenter
    }

    @Override
    public void archiveReview(Long reviewId) {
        // TODO: Implémenter
    }

    @Override
    public void restoreReview(Long reviewId) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ET STATISTIQUES ==========

    @Override
    @Transactional(readOnly = true)
    public double calculateAverageRating(Long serviceId) {
        // TODO: Implémenter
        return 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public long countServiceReviews(Long serviceId) {
        return reviewRepository.countByServiceId(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Object getRatingDistribution(Long serviceId) {
        // TODO: Implémenter
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Object getReviewStatistics(Long serviceId) {
        // TODO: Implémenter
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedService(Long userId, Long serviceId) {
        return reviewRepository.existsByUserIdAndServiceId(userId, serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserReview(Long userId, Long serviceId) {
        // TODO: Implémenter
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> searchReviews(Long serviceId, String keyword) {
        // TODO: Implémenter
        return List.of();
    }
}