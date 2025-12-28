package com.eadl.connect_backend.domain.port.in.user;

import com.eadl.connect_backend.domain.model.user.User;

/**
 * Port IN - Service d'authentification
 * Use cases pour l'authentification et l'autorisation
 */
public interface AuthService {
    
    /**
     * Authentifie un utilisateur
     */
    User login(String email, String password);
    
    /**
     * Déconnecte un utilisateur
     */
    void logout(Long idUser);
    
    /**
     * Inscrit un nouvel utilisateur
     */
    User register(User user);

    /**
     * Modifier le profil d'un utilisateur
     */

    User updateProfile(Long idUser, User updatedUser);
    
}