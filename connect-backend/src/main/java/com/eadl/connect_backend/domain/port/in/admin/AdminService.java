package com.eadl.connect_backend.domain.port.in.admin;

import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.Admin;

/**
 * Port IN - Service Admin
 * Use cases généraux pour les administrateurs
 */
public interface AdminService {
    
    /**
     * Récupère un admin par son ID
     */
    Optional<Admin> getAdminById(Long idAdmin);
    
    /**
     * Récupère un admin par son email
     */
    Optional<Admin> getAdminByEmail(String email);
    
    /**
     * Crée un nouveau compte admin
     */
    Admin createAdmin(String firstName, String lastName, String email, 
                     String phone, String password);
    
    /**
     * Met à jour le profil d'un admin
     */
    Admin updateAdminProfile(Long idAdmin, String firstName, 
                            String lastName, String phone);
    
    /**
     * Change le mot de passe d'un admin
     */
    void changePassword(Long idAdmin, String oldPassword, String newPassword);
}