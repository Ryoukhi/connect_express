package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

/**
 * Port OUT - Repository des compétences d’un technicien
 */
public interface TechnicianSkillRepository {

    /**
     * Ajoute ou met à jour une compétence
     */
    TechnicianSkill save(TechnicianSkill skill);

    /**
     * Récupère une compétence par son ID
     */
    Optional<TechnicianSkill> findById(Long skillId);

    /**
     * Récupère toutes les compétences d’un profil technicien
     */
    List<TechnicianSkill> findByProfileId(Long profileId);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkill> findByProfileIdAndCategoryId(
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
    void delete(TechnicianSkill skill);

    /**
     * Supprime toutes les compétences d’un profil
     * (ex : suppression de profil)
     */
    void deleteByProfileId(Long profileId);

    List<TechnicianSkill> findByCategoryId(Long id);
}