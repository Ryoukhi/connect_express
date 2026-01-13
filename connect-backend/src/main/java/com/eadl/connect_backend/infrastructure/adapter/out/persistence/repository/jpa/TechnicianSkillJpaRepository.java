package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;

public interface TechnicianSkillJpaRepository extends JpaRepository<TechnicianSkillEntity, Long> {

    /**
     * Récupère toutes les compétences d’un technicien via son user id
     */
    List<TechnicianSkillEntity> findByTechnician_IdUser(Long idUser);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkillEntity> findByTechnician_IdUserAndCategory_IdCategory(
            Long idUser,
            Long categoryId
    );

    /**
     * Vérifie si une compétence existe déjà pour un technicien
     * (évite doublons : même name + même technicien)
     */
    boolean existsByTechnician_IdUserAndName(
            Long idUser,
            String name
    );

    /**
     * Supprime une compétence
     */
    void delete(TechnicianSkillEntity skill);

    /**
     * Supprime toutes les compétences d’un technicien (ex : suppression de l'utilisateur)
     */
    void deleteByTechnician_IdUser(Long idUser);

    /**
     * Récupère les profils par catégorie
     */
    List<TechnicianSkillEntity> findByCategory_IdCategory(Long id);

    // NOTE: Use findByTechnician_IdUser to retrieve skills by user id



    



    
}
