package com.eadl.connect_backend.domain.port.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.TechnicianSearchCriteria;

/**
 * Port OUT - Repository TechnicianProfile
 */
public interface TechnicianProfileRepository {
    
    /**
     * Sauvegarde un profil
     */
    TechnicianProfile save(TechnicianProfile profile);
    
    /**
     * Récupère un profil par son ID
     */
    Optional<TechnicianProfile> findById(Long idProfile);
    
    /**
     * Récupère le profil d'un technicien
     */
    Optional<TechnicianProfile> findByTechnicianId(Long idTechnician);

    List<TechnicianProfile> findByCategoryId(Long idCategory);
    
    /**
     * Récupère tous les profils
     */
    List<TechnicianProfile> findAll();
    
    /**
     * Récupère les profils vérifiés
     */
    List<TechnicianProfile> findByVerified(boolean verified);
    
    /**
     * Récupère les profils par statut de disponibilité
     */
    List<TechnicianProfile> findByAvailabilityStatus(AvailabilityStatus status);
    
    /**
     * Récupère les profils disponibles
     */
    List<TechnicianProfile> findAvailable();
    
    /**
     * Récupère les profils dans un rayon géographique
     */
    List<TechnicianProfile> findByLocationRadius(BigDecimal latitude, 
                                                 BigDecimal longitude, 
                                                 Double radiusKm);
    
    /**
     * Récupère les profils par fourchette de tarif
     */
    List<TechnicianProfile> findByHourlyRateBetween(BigDecimal minRate, BigDecimal maxRate);
    
    /**
     * Récupère les profils avec une note minimale
     */
    List<TechnicianProfile> findByAverageRatingGreaterThanEqual(BigDecimal minRating);
    
    /**
     * Récupère les profils les mieux notés
     */
    List<TechnicianProfile> findTopRated(int limit);
    
    /**
     * Récupère les profils les plus expérimentés
     */
    List<TechnicianProfile> findMostExperienced(int limit);
    
    /**
     * Compte le nombre de profils vérifiés
     */
    Long countByVerified(boolean verified);
    
    /**
     * Supprime un profil
     */
    void delete(TechnicianProfile profile);

    Optional<TechnicianProfile> findByIdTechnician(Long technicianId);

    List<TechnicianProfile> findByVerifiedFalse();

    /**
     * Recherche avancée multi-critères
     */
    List<TechnicianProfile> search(
            TechnicianSearchCriteria criteria
    );

    /**
     * Récupère les techniciens les mieux notés
     */
    List<TechnicianProfile> findTopRatedByCity(
            String city,
            boolean verifiedOnly,
            int limit
    );

    /**
     * Récupère les techniciens disponibles à proximité
     */
    List<TechnicianProfile> findNearbyAvailable(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm,
            AvailabilityStatus availabilityStatus
    );
}