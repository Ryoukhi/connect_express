package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;

public interface TechnicianSkillJpaRepository extends JpaRepository<TechnicianSkillEntity, Long> {

    /**
     * Récupère toutes les compétences d’un profil technicien
     */
    List<TechnicianSkillEntity> findByProfile_IdProfile(Long idProfile);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkillEntity> findByProfile_IdProfileAndCategory_IdCategory(
            Long idProfile,
            Long categoryId
    );

    /**
     * Vérifie si une compétence existe déjà pour un profil
     * (évite doublons : même skill + même profil)
     */
    boolean existsByProfile_IdProfileAndName(
            Long idProfile,
            String nameSkill
    );

    /**
     * Supprime une compétence
     */
    void delete(TechnicianSkillEntity skill);

    /**
     * Supprime toutes les compétences d’un profil
     * (ex : suppression de profil)
     */
    void deleteByProfile_IdProfile(Long idProfile);

    /**
     * Récupère les profils par catégorie
     */
    List<TechnicianSkillEntity> findByCategory_IdCategory(Long id);

    /**
     * Récupère les compétences d’un technicien via son user ID
     */
    List<TechnicianSkillEntity> findByProfile_Technician_IdUser(Long idTechnician);


    



    
}
