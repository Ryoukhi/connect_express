package com.eadl.connect_backend.application.service.technician;

import java.math.BigDecimal;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.exception.TechnicianProfileNotFoundException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianProfileService;
import com.eadl.connect_backend.domain.port.out.external.StorageService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service TechnicianProfile
 */
@Service
@Transactional
public class TechnicianProfileServiceImpl implements TechnicianProfileService {
    
    private final TechnicianProfileRepository profileRepository;
    private final StorageService storageService;
    
    public TechnicianProfileServiceImpl(TechnicianProfileRepository profileRepository,
                                        StorageService storageService) {
        this.profileRepository = profileRepository;
        this.storageService = storageService;
    }
    
    @Override
    public TechnicianProfile createProfile(Long idTechnician, String bio, 
                                          Integer yearsExperience, BigDecimal hourlyRate) {
        // 1. Vérifier si un profil existe déjà
        Optional<TechnicianProfile> existingProfile = 
            profileRepository.findByTechnicianId(idTechnician);
        
        if (existingProfile.isPresent()) {
            throw new IllegalStateException(
                "Un profil existe déjà pour ce technicien");
        }
        
        // 2. Créer le profil
        TechnicianProfile profile = TechnicianProfile.create(
            idTechnician, bio, yearsExperience, hourlyRate
        );
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianProfile> getProfileById(Long idProfile) {
        return profileRepository.findById(idProfile);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianProfile> getProfileByTechnician(Long idTechnician) {
        return profileRepository.findByTechnicianId(idTechnician);
    }
    
    @Override
    public TechnicianProfile updateProfile(Long idProfile, String bio, 
                                          Integer yearsExperience, BigDecimal hourlyRate) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Mettre à jour
        profile.updateProfile(bio, yearsExperience, hourlyRate);
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile updateProfilePhoto(Long idProfile, String photoUrl) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Mettre à jour la photo
        profile.updateProfilePhoto(photoUrl);
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    /**
     * Upload et met à jour la photo de profil
     */
    public TechnicianProfile uploadProfilePhoto(Long idProfile, byte[] photoData, 
                                                String fileName) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Upload de la photo vers S3
        String folder = "profiles/photos/" + idProfile;
        String photoUrl = storageService.uploadFile(
            photoData, fileName, folder, "image/jpeg"
        );
        
        // 3. Mettre à jour le profil
        profile.updateProfilePhoto(photoUrl);
        
        // 4. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile updateLocation(Long idProfile, BigDecimal latitude, 
                                           BigDecimal longitude) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Mettre à jour la localisation
        profile.updateLocation(latitude, longitude);
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile updateAvailability(Long idProfile, AvailabilityStatus status) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Mettre à jour le statut selon l'enum
        switch (status) {
            case AVAILABLE -> profile.setAvailable();
            case BUSY -> profile.setBusy();
            case UNAVAILABLE -> profile.setUnavailable();
            case ON_BREAK -> profile.setOnBreak();
        }
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile verifyProfile(Long idProfile) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Vérifier le profil
        profile.verify();
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile incrementCompletedJobs(Long idProfile) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Incrémenter le compteur
        profile.incrementCompletedJobs();
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
    
    @Override
    public TechnicianProfile updateAverageRating(Long idProfile, BigDecimal rating) {
        // 1. Récupérer le profil
        TechnicianProfile profile = profileRepository.findById(idProfile)
            .orElseThrow(() -> new TechnicianProfileNotFoundException(
                "Profil non trouvé avec l'ID: " + idProfile));
        
        // 2. Mettre à jour la note moyenne
        profile.updateAverageRating(rating);
        
        // 3. Sauvegarder
        return profileRepository.save(profile);
    }
}