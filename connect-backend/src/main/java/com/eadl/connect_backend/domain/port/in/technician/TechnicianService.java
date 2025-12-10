package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.user.Technician;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Service Technicien
 * Use cases généraux pour les techniciens
 */
public interface TechnicianService {
    
    /**
     * Récupère un technicien par son ID
     */
    Optional<Technician> getTechnicianById(Long idTechnician);
    
    /**
     * Récupère un technicien par son ID utilisateur
     */
    Optional<Technician> getTechnicianByUserId(Long idUser);
    
    /**
     * Récupère tous les techniciens
     */
    List<Technician> getAllTechnicians();
    
    /**
     * Récupère les techniciens actifs
     */
    List<Technician> getActiveTechnicians();
    
    /**
     * Récupère les techniciens vérifiés
     */
    List<Technician> getVerifiedTechnicians();
    
    /**
     * Crée un compte technicien
     */
    Technician createTechnician(String firstName, String lastName, 
                               String email, String phone, String password);
    
    /**
     * Met à jour les informations de base d'un technicien
     */
    Technician updateTechnician(Long idTechnician, String firstName, 
                               String lastName, String phone);
    
    /**
     * Compte le nombre total de techniciens
     */
    Long countTechnicians();
    
    /**
     * Compte le nombre de techniciens actifs
     */
    Long countActiveTechnicians();
}
