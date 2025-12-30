package com.eadl.connect_backend.domain.port.in.technician;

import java.math.BigDecimal;
import java.util.List;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;

public interface TechnicianSearchService {

    /**
     * Recherche simple par ville
     */
//     List<TechnicianProfile> searchByCity(String city);

//     /**
//      * Recherche avancée multi-critères
//      */
//     List<TechnicianProfile> search(
//             TechnicianSearchCriteria criteria
//     );

    /**
     * Top techniciens les mieux notés
     */
    List<TechnicianProfile> findTopRated(
            String city,
            int limit
    );

    /**
     * Techniciens disponibles autour d’un point
     */
//     List<TechnicianProfile> findNearbyAvailable(
//             BigDecimal latitude,
//             BigDecimal longitude,
//             BigDecimal radiusKm
//     );
}
