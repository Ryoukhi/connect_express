package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Port IN - Service de profil technicien
 * Use cases pour la gestion du profil professionnel
 */
public interface TechnicianProfileService {
    
    /**
     * Crée un profil technicien
     */
    TechnicianProfile createProfile(Long idTechnician, String bio, 
                                   Integer yearsExperience, BigDecimal hourlyRate);
    
    /**
     * Récupère un profil par son ID
     */
    Optional<TechnicianProfile> getProfileById(Long idProfile);
    
    /**
     * Récupère le profil d'un technicien
     */
    Optional<TechnicianProfile> getProfileByTechnician(Long idTechnician);
    
    /**
     * Met à jour le profil
     */
    TechnicianProfile updateProfile(Long idProfile, String bio, 
                                   Integer yearsExperience, BigDecimal hourlyRate);
    
    /**
     * Met à jour la photo de profil
     */
    TechnicianProfile updateProfilePhoto(Long idProfile, String photoUrl);
    
    /**
     * Met à jour la localisation
     */
    TechnicianProfile updateLocation(Long idProfile, BigDecimal latitude, 
                                    BigDecimal longitude);
    
    /**
     * Change le statut de disponibilité
     */
    TechnicianProfile updateAvailability(Long idProfile, AvailabilityStatus status);
    
    /**
     * Marque le profil comme vérifié
     */
    TechnicianProfile verifyProfile(Long idProfile);
    
    /**
     * Incrémente le nombre de jobs complétés
     */
    TechnicianProfile incrementCompletedJobs(Long idProfile);
    
    /**
     * Met à jour la note moyenne
     */
    TechnicianProfile updateAverageRating(Long idProfile, BigDecimal rating);
}
