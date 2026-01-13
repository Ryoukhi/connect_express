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
     * Récupère toutes les compétences d’un technicien via son user id
     */
    List<TechnicianSkill> findByUserId(Long userId);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkill> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId
    );

    /**
     * Vérifie si une compétence existe déjà pour un technicien
     * (évite doublons : même nom + même technicien)
     */
    boolean existsByUserIdAndName(
            Long userId,
            String name
    );

    /**
     * Supprime une compétence
     */
    void delete(TechnicianSkill skill);

    /**
     * Supprime toutes les compétences d’un technicien (ex : suppression de l'utilisateur)
     */
    void deleteByUserId(Long userId);

    /**
     * Récupère toutes les compétences
     */
    List<TechnicianSkill> findAll();

    List<TechnicianSkill> findByCategoryId(Long id);
}