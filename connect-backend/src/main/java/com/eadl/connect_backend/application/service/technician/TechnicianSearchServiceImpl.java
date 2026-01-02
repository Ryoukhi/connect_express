package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import org.springframework.util.StringUtils;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service TechnicianSearch
 */
@Service
public class TechnicianSearchServiceImpl implements TechnicianSearchService {

    private final TechnicianProfileRepository searchRepository;

    public TechnicianSearchServiceImpl(
            TechnicianProfileRepository searchRepository
    ) {
        this.searchRepository = searchRepository;
    }

    /**
     * 🔍 Recherche simple par ville
     * Cas d’usage client basique
     */
    // @Override
    // public List<TechnicianProfile> searchByCity(String city) {

    //     if (!StringUtils.hasText(city)) {
    //         throw new IllegalArgumentException("City must not be empty");
    //     }

    //     TechnicianSearchCriteria criteria = new TechnicianSearchCriteria();
    //     criteria.setCity(city);
    //     criteria.setActiveOnly(true);
    //     criteria.setVerifiedOnly(true);



    //     return searchRepository.search(criteria);
    // }

    /**
     * 🔎 Recherche avancée multi-critères
     */
    // @Override
    // public List<TechnicianProfile> search(
    //         TechnicianSearchCriteria criteria
    // ) {

    //     if (criteria == null) {
    //         throw new IllegalArgumentException("Search criteria must not be null");
    //     }

    //     // Règle métier par défaut
    //     if (criteria.getAvailabilityStatus() == null) {
    //         criteria.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    //     }

    //     return searchRepository.search(criteria);
    // }

    /**
     * ⭐ Top techniciens les mieux notés
     */
    @Override
    public List<TechnicianProfile> findTopRated(
            String city,
            int limit
    ) {

        if (!StringUtils.hasText(city)) {
            throw new IllegalArgumentException("City must not be empty");
        }

        if (limit <= 0) {
            limit = 10; // valeur par défaut métier
        }

        return searchRepository.findTopRatedByCity(
                city,
                true,      // uniquement profils validés
                limit
        );
    }

    /**
     * 📍 Techniciens disponibles autour d’un point
     */
    // @Override
    // public List<TechnicianProfile> findNearbyAvailable(
    //         BigDecimal latitude,
    //         BigDecimal longitude,
    //         BigDecimal radiusKm
    // ) {

    //     if (latitude == null || longitude == null) {
    //         throw new IllegalArgumentException("Latitude and longitude are required");
    //     }

    //     if (radiusKm == null || radiusKm.compareTo(BigDecimal.ZERO) <= 0) {
    //         radiusKm = BigDecimal.valueOf(10); // rayon par défaut : 10 km
    //     }

    //     return searchRepository.findNearbyAvailable(
    //             latitude,
    //             longitude,
    //             radiusKm,
    //             AvailabilityStatus.AVAILABLE
    //     );
    // }

}