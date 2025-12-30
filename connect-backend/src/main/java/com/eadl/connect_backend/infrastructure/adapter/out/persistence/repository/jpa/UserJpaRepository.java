package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Récupère un utilisateur par son ID
     */
    Optional<UserEntity> findById(Long idUser);
    
    /**
     * Récupère un utilisateur par son email
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Récupère un utilisateur par son téléphone
     */
    Optional<UserEntity> findByPhone(String phone);
    
    /**
     * Récupère tous les utilisateurs
     */
    List<UserEntity> findAll();
    
    /**
     * Récupère les utilisateurs par rôle
     */
    List<UserEntity> findByRole(Role role);
    
    /**
     * Récupère les utilisateurs actifs
     */
    List<UserEntity> findByActive(boolean active);
    
    /**
     * Vérifie si un email existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Vérifie si un téléphone existe
     */
    boolean existsByPhone(String phone);
    
    
    /**
     * Compte le nombre d'utilisateurs par rôle
     */
    Long countByRole(Role role);
    
    /**
     * Supprime un utilisateur
     */
    void delete(UserEntity user);
    
    /**
     * Supprime un utilisateur par ID
     */
    void deleteById(Long idUser);

    /**
     * récupère les utilisateurs actifs par role
     */
    List<UserEntity> findByRoleAndActive(Role role, boolean active);

    /**
     * Compte le nombre d'utilisateurs par rôle et statut actif
     */
    Long countByRoleAndActive(Role role, boolean active);

}
