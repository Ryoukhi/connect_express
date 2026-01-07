package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import org.springframework.util.StringUtils;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
    public List<TechnicianProfile> findTopRated(String city, int limit) {
        log.info("Recherche des top techniciens à {}", city);

        if (!StringUtils.hasText(city)) {
            log.error("La ville est vide pour la recherche top-rated");
            throw new IllegalArgumentException("City must not be empty");
        }

        if (limit <= 0) {
            limit = 10; // valeur par défaut métier
            log.debug("Limit invalide, valeur par défaut utilisée: {}", limit);
        }

        List<TechnicianProfile> topRated = searchRepository.findTopRatedByCity(
                city,
                true, // uniquement profils validés
                limit
        );

        log.debug("Top {} techniciens récupérés pour la ville {}: {} résultats", limit, city, topRated.size());
        return topRated;
    }

    /**
     * Recherche multi-critères, tous critères optionnels
     */
    @Override
    public List<TechnicianProfile> search(
            com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchCriteria criteria
    ) {
        log.info("Recherche de techniciens avec critères: {}", criteria);

        Long categoryId = null;
        if (criteria.getCategoryName() != null && !criteria.getCategoryName().trim().isEmpty()) {
            categoryId = categoryService
                    .getCategoryByName(criteria.getCategoryName())
                    .map(c -> c.getIdCategory())
                    .orElse(null);

            if (categoryId == null) {
                log.debug("Catégorie non trouvée pour le nom: {}", criteria.getCategoryName());
            } else {
                log.debug("Catégorie résolue: {} -> id={}", criteria.getCategoryName(), categoryId);
            }
        }

        List<TechnicianProfile> results = searchRepository.search(
                criteria.getCity(),
                criteria.getNeighborhood(),
                categoryId,
                Boolean.TRUE,    // only verified profiles
                Boolean.TRUE,    // only active technicians
                criteria.getAvailabilityStatus(),
                criteria.getMinPrice(),
                criteria.getMaxPrice()
        );

        log.debug("Résultats bruts trouvés: {}", results.size());

        // Filter by minimum rating if provided
        if (criteria.getMinRating() != null) {
            java.math.BigDecimal minRating = criteria.getMinRating();
            results = results.stream()
                    .filter(p -> p.getAverageRating() != null && p.getAverageRating().compareTo(minRating) >= 0)
                    .toList();
            log.debug("Résultats filtrés par note minimale {}: {}", minRating, results.size());
        }

        log.info("Recherche terminée, {} résultats finaux", results.size());
        return results;
    }
}
