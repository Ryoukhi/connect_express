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
    private final com.eadl.connect_backend.domain.port.in.category.CategoryService categoryService;

    public TechnicianSearchServiceImpl(
            TechnicianProfileRepository searchRepository,
            com.eadl.connect_backend.domain.port.in.category.CategoryService categoryService
    ) {
        this.searchRepository = searchRepository;
        this.categoryService = categoryService;
    }

    

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
     * Recherche multi-critères, tous critères optionnels
     */
    @Override
    public List<TechnicianProfile> search(
            com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchCriteria criteria
    ) {

        // Resolve category name to ID when provided
        Long categoryId = null;
        if (criteria.getCategoryName() != null && !criteria.getCategoryName().trim().isEmpty()) {
            categoryId = categoryService
                    .getCategoryByName(criteria.getCategoryName())
                    .map(c -> c.getIdCategory())
                    .orElse(null);
        }

        List<TechnicianProfile> results = searchRepository.search(
                criteria.getCity(),
                criteria.getNeighborhood(),
                categoryId,
                Boolean.TRUE,    // only verified profiles for public search
                Boolean.TRUE,    // only active technicians
                criteria.getAvailabilityStatus(),
                criteria.getMinPrice(),
                criteria.getMaxPrice()
        );

        // Filter by minimum rating if provided (in-memory)
        if (criteria.getMinRating() != null) {
            java.math.BigDecimal minRating = criteria.getMinRating();
            results = results.stream()
                    .filter(p -> p.getAverageRating() != null && p.getAverageRating().compareTo(minRating) >= 0)
                    .toList();
        }

        return results;
    }

    

}