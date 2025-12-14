package com.eadl.connect_backend.domain.port.in.review;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
     * Récupère les avis positifs d'un technicien
     */
    List<Review> getPositiveReviews(Long idTechnician);
    
    /**
     * Récupère les avis négatifs d'un technicien
     */
    List<Review> getNegativeReviews(Long idTechnician);
    
    /**
     * Récupère les avis signalés
     */
    List<Review> getReportedReviews();
    
    /**
     * Récupère les avis en attente de modération
     */
    List<Review> getReviewsNeedingModeration();
    
    /**
     * Met à jour un avis
     */
    Review updateReview(Long idReview, int newRating, String newComment);
    
    /**
     * Signale un avis
     */
    Review reportReview(Long idReview, Long reporterId, String reason);
    
    /**
     * Modère un avis (admin)
     */
    Review moderateReview(Long idAdmin, Long idReview);
    
    /**
     * Supprime le signalement d'un avis
     */
    Review clearReport(Long idAdmin, Long idReview);
    
    /**
     * Supprime un avis
     */
    void deleteReview(Long idAdmin, Long idReview, String reason);
    
    /**
     * Calcule la note moyenne d'un technicien
     */
    BigDecimal calculateAverageRating(Long idTechnician);
    
    /**
     * Compte le nombre d'avis d'un technicien
     */
    Long countReviews(Long idTechnician);
    
    /**
     * Compte le nombre d'avis par note (distribution)
     */
    Map<Integer, Long> getRatingDistribution(Long idTechnician);
    
    /**
     * Vérifie si un client peut laisser un avis pour une réservation
     */
    boolean canReview(Long idClient, Long idReservation);
    
    /**
     * Vérifie si un avis existe pour une réservation
     */
    boolean reviewExists(Long idReservation);
}
