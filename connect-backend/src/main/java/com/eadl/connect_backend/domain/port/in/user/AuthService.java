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
     * Génère un token JWT
     */
    String generateToken(User user);
    
    /**
     * Valide un token JWT
     */
    boolean validateToken(String token);
    
    /**
     * Récupère l'utilisateur depuis un token
     */
    User getUserFromToken(String token);
    
    /**
     * Rafraîchit un token
     */
    String refreshToken(String refreshToken);
    
    /**
     * Envoie un email de réinitialisation de mot de passe
     */
    void sendPasswordResetEmail(String email);
    
    /**
     * Réinitialise le mot de passe avec un token
     */
    void resetPassword(String token, String newPassword);
    
    /**
     * Envoie un code de vérification par SMS
     */
    void sendPhoneVerificationCode(String phone);
    
    /**
     * Vérifie le code de vérification téléphonique
     */
    boolean verifyPhoneCode(String phone, String code);
}