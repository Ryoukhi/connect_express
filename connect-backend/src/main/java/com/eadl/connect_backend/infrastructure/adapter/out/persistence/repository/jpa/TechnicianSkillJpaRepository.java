package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;

public interface TechnicianSkillJpaRepository extends JpaRepository<TechnicianSkillEntity, Long> {

    /**
     * Récupère toutes les compétences d’un profil technicien
     */
    List<TechnicianSkillEntity> findByProfileId(Long profileId);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkillEntity> findByProfileIdAndCategoryId(
            Long profileId,
            Long categoryId
    );

    /**
     * Vérifie si une compétence existe déjà pour un profil
     * (évite doublons : même skill + même profil)
     */
    boolean existsByProfileIdAndNameSkill(
            Long profileId,
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
    void deleteByProfileId(Long profileId);

    List<TechnicianSkillEntity> findByCategoryId(Long id);

    
}
