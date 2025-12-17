package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.user.Technician;
import java.util.List;
import java.util.Optional;


/**
 * Port IN - Service Technicien
 * Use cases généraux pour les techniciens
 */
public interface TechnicianService {
    
    Optional<Technician> getTechnicianById(Long idTechnician);

    Optional<Technician> getTechnicianByUserId(Long idUser);

    List<Technician> getAllTechnicians();

    List<Technician> getActiveTechnicians();

    List<Technician> getVerifiedTechnicians();

    Technician createTechnician(String firstName, String lastName, String email, String phone, String password);

    Technician updateTechnician(Long idTechnician, String firstName, String lastName, String phone);

    Long countTechnicians();

    Long countActiveTechnicians();

}
