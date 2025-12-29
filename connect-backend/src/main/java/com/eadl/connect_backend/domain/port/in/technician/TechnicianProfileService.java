package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Use cases métier liés au profil professionnel du technicien
 */
public interface TechnicianProfileService {


    /**
     * Crée le profil professionnel d’un technicien
     */
    TechnicianProfile createProfile(TechnicianProfile profile);
    
    /**
     * Récupère le profil professionnel d’un technicien
     */
    Optional<TechnicianProfile> getProfileByTechnicianId(Long technicianId);

    /**
     * Complète ou met à jour le profil professionnel du technicien
     * (bio, expérience, catégorie, tarif, localisation)
     */
    TechnicianProfile updateProfile(TechnicianProfile profile);

    /**
     * Met à jour la disponibilité du technicien
     */
    TechnicianProfile updateAvailability(
            Long technicianId,
            AvailabilityStatus availabilityStatus
    );

    /**
     * Met à jour le tarif horaire
     */
    TechnicianProfile updateHourlyRate(
            Long technicianId,
            BigDecimal hourlyRate
    );

    /**
     * Valide le profil d’un technicien (KYC / Admin)
     */
    void validateProfile(Long technicianId);

    /**
     * Invalide le profil d’un technicien (Admin)
     */
    void rejectProfile(Long technicianId, String reason);

    /**
     * Récupère les profils en attente de validation
     */
    List<TechnicianProfile> getPendingProfiles();

    /**
     * Met à jour les statistiques après une mission terminée
     */
    void updateStatisticsAfterJob(
            Long technicianId,
            BigDecimal newRating
    );

    /**
     * Recalcule le nombre total de missions complétées
     * à partir des réservations terminées
     */
    void recalculateCompletedJobs(Long technicianId);

    /**
     * Recalcule la note moyenne du technicien
     * à partir des reviews des réservations complétées
     */
    void recalculateAverageRating(Long technicianId);

}