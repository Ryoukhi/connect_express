package com.eadl.connect_backend.domain.port.in.technician;

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
}
