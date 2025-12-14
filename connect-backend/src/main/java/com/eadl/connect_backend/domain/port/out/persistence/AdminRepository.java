package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.Admin;

/**
 * Port OUT - Repository Admin
 */
public interface AdminRepository {
    
    /**
     * Sauvegarde un admin
     */
    Admin save(Admin admin);
    
    /**
     * Récupère un admin par son ID
     */
    Optional<Admin> findById(Long idAdmin);
    
    /**
     * Récupère un admin par son ID utilisateur
     */
    Optional<Admin> findByUserId(Long idUser);
    
    /**
     * Récupère tous les admins
     */
    List<Admin> findAll();
    
    /**
     * Compte le nombre d'admins
     */
    Long count();
}
