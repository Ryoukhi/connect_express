package com.eadl.connect_backend.domain.port.in.user;

import com.eadl.connect_backend.domain.model.user.User;

/**
 * Port IN - Service de profil utilisateur
 * Use cases pour la gestion du profil
 */
public interface ProfileService {
    
    /**
     * Met à jour la photo de profil
     */
    User updateProfilePhoto(Long idUser, byte[] photoData, String fileName);
    
    /**
     * Supprime la photo de profil
     */
    User deleteProfilePhoto(Long idUser);
    
    
    /**
     * Supprime définitivement un compte
     */
    void deleteAccount(Long idUser, String password);
}