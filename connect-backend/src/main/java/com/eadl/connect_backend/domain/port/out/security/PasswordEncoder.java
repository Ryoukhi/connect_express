package com.eadl.connect_backend.domain.port.out.security;

/**
 * Port OUT - Encodeur de mots de passe
 * Interface pour le hachage et la vérification des mots de passe
 */
public interface PasswordEncoder {
    
    /**
     * Encode (hash) un mot de passe
     */
    String encode(String rawPassword);

    boolean matches(String oldPassword, String password);
    
}