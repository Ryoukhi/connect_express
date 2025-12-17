package com.eadl.connect_backend.application.service.technician;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implémentation du service TechnicianSearch
 */
@Service
@Transactional(readOnly = true)
public class TechnicianSearchServiceImpl implements TechnicianSearchService {
    
    private final TechnicianProfileRepository profileRepository;
    private final TechnicianSkillRepository skillRepository;
    
    public TechnicianSearchServiceImpl(TechnicianProfileRepository profileRepository,
                                       TechnicianSkillRepository skillRepository) {
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
    }
    
    @Override
    public List<TechnicianProfile> searchByCategory(Long idCategory) {
        // Récupérer les profils ayant des compétences dans cette catégorie
        List<Long> profileIds = skillRepository.findByCategoryId(idCategory)
            .stream()
            .map(skill -> skill.getIdProfile())
            .distinct()
            .toList();
        
        // Récupérer les profils vérifiés uniquement
        return profileIds.stream()
            .map(profileRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(TechnicianProfile::isVerified)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TechnicianProfile> searchByLocation(BigDecimal latitude, 
                                                    BigDecimal longitude, 
                                                    Double radiusKm) {
        // Utiliser la recherche géographique du repository
        return profileRepository.findByLocationRadius(latitude, longitude, radiusKm)
            .stream()
            .filter(TechnicianProfile::isVerified)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TechnicianProfile> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return profileRepository.findByHourlyRateBetween(minPrice, maxPrice)
            .stream()
            .filter(TechnicianProfile::isVerified)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TechnicianProfile> searchAvailable() {
        return profileRepository.findAvailable();
    }
    
    @Override
    public List<TechnicianProfile> searchByMinRating(BigDecimal minRating) {
        return profileRepository.findByAverageRatingGreaterThanEqual(minRating);
    }
    
    @Override
    public List<TechnicianProfile> getTopRatedTechnicians(int limit) {
        return profileRepository.findTopRated(limit);
    }
    
    @Override
    public List<TechnicianProfile> getMostExperiencedTechnicians(int limit) {
        return profileRepository.findMostExperienced(limit);
    }
}