package com.eadl.connect_backend.domain.port.in.review;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.review.Review;

/**
 * Port IN - Service Avis
 * Use cases pour la gestion des avis clients
 */
public interface ReviewService {
    
    /**
     * Crée un avis
     */
    Review createReview(Long idReservation, Long idClient, 
                       Long idTechnician, int rating, String comment);
    
    /**
     * Récupère un avis par son ID
     */
    Optional<Review> getReviewById(Long idReview);
    
    /**
     * Récupère l'avis d'une réservation
     */
    Optional<Review> getReviewByReservation(Long idReservation);
    
    /**
     * Récupère tous les avis d'un technicien
     */
    List<Review> getTechnicianReviews(Long idTechnician);
    
    /**
     * Récupère tous les avis d'un client
     */
    List<Review> getClientReviews(Long idClient);
    
    /**
     * Met à jour un avis
     */
    Review updateReview(Long idReview, int newRating, String newComment);
    
    /**
     * Supprime un avis
     */
    void deleteReview(Long idAdmin, Long idReview, String reason);
    
    
    /**
     * Compte le nombre d'avis d'un technicien
     */
    Long countReviews(Long idTechnician);
    
    
   
    
}
