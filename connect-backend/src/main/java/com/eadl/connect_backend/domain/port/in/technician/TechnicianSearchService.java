package com.eadl.connect_backend.domain.port.in.technician;

import java.math.BigDecimal;
import java.util.List;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;

public interface TechnicianSearchService {
    
    /**
     * Recherche des techniciens par catégorie
     */
    List<TechnicianProfile> searchByCategory(Long idCategory);
    
    /**
     * Recherche des techniciens par localisation (rayon en km)
     */
    List<TechnicianProfile> searchByLocation(BigDecimal latitude, 
                                            BigDecimal longitude, 
                                            Double radiusKm);
    
    /**
     * Recherche des techniciens par fourchette de tarif
     */
    List<TechnicianProfile> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Recherche des techniciens disponibles
     */
    List<TechnicianProfile> searchAvailable();
    
    /**
     * Recherche des techniciens avec une note minimale
     */
    List<TechnicianProfile> searchByMinRating(BigDecimal minRating);
    
    /**
     * Recherche multi-critères
     */
    List<TechnicianProfile> searchTechnicians(
        Long idCategory,
        BigDecimal latitude,
        BigDecimal longitude,
        Double radiusKm,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        BigDecimal minRating,
        Boolean availableOnly
    );
    
    /**
     * Récupère les techniciens les mieux notés
     */
    List<TechnicianProfile> getTopRatedTechnicians(int limit);
    
    /**
     * Récupère les techniciens les plus expérimentés
     */
    List<TechnicianProfile> getMostExperiencedTechnicians(int limit);
}