package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.application.dto.TechnicianResultSearchDto;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.user.Technician;
import java.util.List;
import java.util.Optional;


/**
 * Port IN - Service Technicien
 * Use cases généraux pour les techniciens
 */
public interface TechnicianService {

    Technician registerTechnician(Technician technician);

    List<Technician> getActiveTechnicians();

    Long countActiveTechnicians();

    List<Technician> getTechniciansByCity(String city);

    Optional<Technician> getTechnicianById(Long technicianId);

    void validateKyc(Long technicianId);

    List<Technician> getTechniciansByNeighborhood(String neighborhood);

    List<TechnicianResultSearchDto> searchTechnicians(
            String city,
            String neighborhood,
            String categoryName,
            AvailabilityStatus availabilityStatus,
            Double minRating,
            Double minPrice,
            Double maxPrice
    );
    
    void updateAvailabilityStatus(Long technicianId, AvailabilityStatus status);
    
}
