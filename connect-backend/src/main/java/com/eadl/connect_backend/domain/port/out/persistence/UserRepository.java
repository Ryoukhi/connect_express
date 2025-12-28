package com.eadl.connect_backend.domain.port.out.persistence;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.model.user.Role;
import java.util.List;
import java.util.Optional;

/**
 * Port OUT - Repository User
 * Interface pour la persistence des utilisateurs
 */
public interface UserRepository {
    
    /**
     * Sauvegarde un utilisateur
     */
    User save(User user);
    
    /**
     * Récupère un utilisateur par son ID
     */
    Optional<User> findById(Long idUser);
    
    /**
     * Récupère un utilisateur par son email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Récupère un utilisateur par son téléphone
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * Récupère tous les utilisateurs
     */
    List<User> findAll();
    
    /**
     * Récupère les utilisateurs par rôle
     */
    List<User> findByRole(Role role);
    
    /**
     * Récupère les utilisateurs actifs
     */
    List<User> findByActive(boolean active);
    
    /**
     * Récupère les utilisateurs avec pagination
     */
    List<User> findAll(int page, int size);
    
    /**
     * Vérifie si un email existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Vérifie si un téléphone existe
     */
    boolean existsByPhone(String phone);
    
    /**
     * Compte le nombre d'utilisateurs
     */
    Long count();
    
    /**
     * Compte le nombre d'utilisateurs par rôle
     */
    Long countByRole(Role role);
    
    /**
     * Supprime un utilisateur
     */
    void delete(User user);
    
    /**
     * Supprime un utilisateur par ID
     */
    void deleteById(Long idUser);

    /**
     * récupère les utilisateurs actifs par role
     */
    List<User> findByRoleAndActive(Role role, boolean active);

    /**
     * Compte le nombre d'utilisateurs par rôle et statut actif
     */
    Long countByRoleAndActive(Role role, boolean active);


}
