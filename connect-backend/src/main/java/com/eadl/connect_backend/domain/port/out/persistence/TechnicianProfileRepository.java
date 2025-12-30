package com.eadl.connect_backend.domain.port.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.TechnicianSearchCriteria;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;

/**
 * Port OUT - Repository TechnicianProfile
 */
public interface TechnicianProfileRepository {
    
    /**
     * Sauvegarde un profil
     */
    TechnicianProfile save(TechnicianProfile profile);
    
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
     * Récupère les profils les mieux notés
     */
    List<TechnicianProfile> findTopRated(int limit);
    
    /**
     * Supprime un profil
     */
    void delete(TechnicianProfile profile);

    Optional<TechnicianProfile> findByIdTechnician(Long technicianId);

    List<TechnicianProfile> findByVerifiedFalse();

//     /**
//      * Recherche avancée multi-critères
//      */
    List<TechnicianProfile> search(
        String city,
        Long categoryId,
        Boolean verifiedOnly,
        Boolean activeOnly,
        AvailabilityStatus availabilityStatus,
        BigDecimal minHourlyRate,
        BigDecimal maxHourlyRate
    );

    /**
     * Récupère les techniciens les mieux notés
     */
    List<TechnicianProfile> findTopRatedByCity(
            String city,
            boolean verifiedOnly,
            int limit
    );
}