package com.eadl.connect_backend.domain.port.out.persistence;

import java.math.BigDecimal;
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
     * Récupère un avis par son ID
     */
    Optional<Review> findById(Long idReview);
    
    /**
     * Récupère l'avis d'une réservation
     */
    Optional<Review> findByReservationId(Long idReservation);
    
    /**
     * Récupère tous les avis d'un technicien
     */
    List<Review> findByTechnicianId(Long idTechnician);
    
    /**
     * Récupère tous les avis d'un client
     */
    List<Review> findByClientId(Long idClient);
    
    /**
     * Récupère les avis signalés
     */
    List<Review> findReported();
    
    /**
     * Récupère les avis en attente de modération
     */
    List<Review> findNeedingModeration();
    
    /**
     * Récupère les avis positifs d'un technicien (rating >= 4)
     */
    List<Review> findPositiveByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les avis négatifs d'un technicien (rating <= 2)
     */
    List<Review> findNegativeByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les avis récents
     */
    List<Review> findRecent(int limit);
    
    /**
     * Calcule la note moyenne d'un technicien
     */
    BigDecimal calculateAverageRating(Long idTechnician);
    
    /**
     * Compte le nombre d'avis d'un technicien
     */
    Long countByTechnicianId(Long idTechnician);
    
    /**
     * Compte le nombre d'avis par note pour un technicien
     */
    Long countByTechnicianIdAndRatingValue(Long idTechnician, int ratingValue);
    
    /**
     * Vérifie si un avis existe pour une réservation
     */
    boolean existsByReservationId(Long idReservation);
    
    /**
     * Compte tous les avis
     */
    Long count();
    
    /**
     * Supprime un avis
     */
    void delete(Review review);
}