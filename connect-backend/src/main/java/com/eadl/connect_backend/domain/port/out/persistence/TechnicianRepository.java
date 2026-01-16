package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.Technician;

/**
 * Port OUT - Repository Technician
 */
public interface TechnicianRepository {
    
    /**
     * Sauvegarde un technicien
     */
    Technician save(Technician technician);
    
    /**
     * Récupère un technicien par son ID
     */
    Optional<Technician> findById(Long idTechnician);
    
    /**
     * Récupère un technicien par son ID utilisateur
     */
    Optional<Technician> findByidUser(Long idUser);
    
    /**
     * Récupère tous les techniciens
     */
    List<Technician> findAll();
    
    /**
     * Compte le nombre de techniciens
     */
    Long count();
    
    /**
     * Supprime un technicien
     */
    void delete(Technician technician);

    /**
     * Récupère les techniciens actifs
     */
    List<Technician> findByActiveTrue();

    List<Technician> findByRoleAndActiveTrue(Role role);
}