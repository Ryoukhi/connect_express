package com.eadl.connect_backend.domain.port.in.user;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.User;

/**
 * Port IN - Service Utilisateur
 * Use cases généraux pour les utilisateurs
 */
public interface UserService {
    
    /**
     * Récupère un utilisateur par son ID
     */
    Optional<User> getUserById(Long idUser);
    
    /**
     * Récupère un utilisateur par son email
     */
    Optional<User> getUserByEmail(String email);
    
    /**
     * Récupère un utilisateur par son téléphone
     */
    Optional<User> getUserByPhone(String phone);
    
    /**
     * Met à jour le profil utilisateur
     */
    User updateProfile(Long idUser, String firstName, String lastName, String phone);
    
    /**
     * Change le mot de passe
     */
    void changePassword(Long idUser, String oldPassword, String newPassword);
    
    /**
     * Vérifie l'email d'un utilisateur
     */
    User verifyEmail(Long idUser, String verificationToken);
    
    /**
     * Vérifie le téléphone d'un utilisateur
     */
    User verifyPhone(Long idUser, String verificationCode);
    
    /**
     * Active un compte utilisateur
     */
    User activateAccount(Long idUser);
    
    /**
     * Désactive un compte utilisateur
     */
    User deactivateAccount(Long idUser);
    
    /**
     * Vérifie si un email existe déjà
     */
    boolean emailExists(String email);
    
    /**
     * Vérifie si un téléphone existe déjà
     */
    boolean phoneExists(String phone);

    /**
     * Retourne la liste de tous les utilisateurs
     */
    List<User> getAllUsers();

    /**
     * Recherche des utilisateurs par rôle
     */
    List<User> getUsersByRole(String role);
}