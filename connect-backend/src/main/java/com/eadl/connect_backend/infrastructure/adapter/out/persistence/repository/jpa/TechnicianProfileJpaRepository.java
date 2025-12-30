package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianSearchCriteria;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;

public interface TechnicianProfileJpaRepository extends JpaRepository<TechnicianProfileEntity, Long> {

    Optional<TechnicianProfileEntity> findByTechnicianId(Long idTechnician);

    List<TechnicianProfileEntity> findByCategoryId(Long idCategory);

    List<TechnicianProfileEntity> findByVerified(boolean verified);

    List<TechnicianProfileEntity> findByAvailabilityStatus(AvailabilityStatus status);

    List<TechnicianProfileEntity> findByAvailabilityStatusAndVerified(
            AvailabilityStatus status,
            boolean verified
    );

    /**
     * Récupère les profils dans un rayon géographique
     */
    List<TechnicianProfileEntity> findByLocationRadius(BigDecimal latitude, 
                                                 BigDecimal longitude, 
                                                 Double radiusKm);
    
    /**
     * Récupère les profils par fourchette de tarif
     */
    List<TechnicianProfileEntity> findByHourlyRateBetween(BigDecimal minRate, BigDecimal maxRate);
    
    /**
     * Récupère les profils avec une note minimale
     */
    List<TechnicianProfileEntity> findByAverageRatingGreaterThanEqual(BigDecimal minRating);
    
    /**
     * Récupère les profils les mieux notés
     */
    List<TechnicianProfileEntity> findTopRated(int limit);
    
    /**
     * Récupère les profils les plus expérimentés
     */
    List<TechnicianProfileEntity> findMostExperienced(int limit);
    
    /**
     * Compte le nombre de profils vérifiés
     */
    Long countByVerified(boolean verified);

    /**
     * Recherche avancée multi-critères
     */
    List<TechnicianProfileEntity> search(
            TechnicianSearchCriteria criteria
    );

    /**
     * Récupère les techniciens les mieux notés
     */
    List<TechnicianProfileEntity> findTopRatedByCity(
            String city,
            boolean verifiedOnly,
            int limit
    );

    /**
     * Récupère les techniciens disponibles à proximité
     */
    List<TechnicianProfileEntity> findNearbyAvailable(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm,
            AvailabilityStatus availabilityStatus
    );
}
