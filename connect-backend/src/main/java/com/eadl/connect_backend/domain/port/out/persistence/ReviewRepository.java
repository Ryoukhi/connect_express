package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

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
}