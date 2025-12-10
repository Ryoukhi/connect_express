package com.eadl.connect_backend.domain.port.in.admin;

import java.util.List;

import com.eadl.connect_backend.domain.model.user.User;

/**
 * Port IN - Service de gestion des utilisateurs
 * Use cases pour la gestion des comptes utilisateurs par les admins
 */
public interface UserManagementService {
    
    /**
     * Récupère tous les utilisateurs avec pagination
     */
    List<User> getAllUsers(int page, int size);
    
    /**
     * Recherche des utilisateurs par critères
     */
    List<User> searchUsers(String searchTerm, String role);
    
    /**
     * Suspend un utilisateur
     */
    User suspendUser(Long idAdmin, Long idUser, String reason);
    
    /**
     * Réactive un utilisateur suspendu
     */
    User reactivateUser(Long idAdmin, Long idUser, String reason);
    
    /**
     * Supprime définitivement un utilisateur
     */
    void deleteUser(Long idAdmin, Long idUser, String reason);
    
    /**
     * Change le rôle d'un utilisateur
     */
    User changeUserRole(Long idAdmin, Long idUser, String newRole, String reason);
    
    /**
     * Récupère les utilisateurs suspendus
     */
    List<User> getSuspendedUsers();
    
    /**
     * Récupère les utilisateurs récemment créés
     */
    List<User> getRecentUsers(int days);
}