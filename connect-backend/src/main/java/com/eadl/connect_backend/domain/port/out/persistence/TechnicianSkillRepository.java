package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

/**
 * Port OUT - Repository TechnicianSkill
 */
public interface TechnicianSkillRepository {
    
    /**
     * Sauvegarde une compétence
     */
    TechnicianSkill save(TechnicianSkill skill);
    
    /**
     * Récupère une compétence par son ID
     */
    Optional<TechnicianSkill> findById(Long idSkill);
    
    /**
     * Récupère les compétences d'un profil
     */
    List<TechnicianSkill> findByProfileId(Long idProfile);
    
    /**
     * Récupère les compétences par catégorie
     */
    List<TechnicianSkill> findByCategoryId(Long idCategory);
    
    /**
     * Récupère les compétences d'un profil pour une catégorie
     */
    List<TechnicianSkill> findByProfileIdAndCategoryId(Long idProfile, Long idCategory);
    
    /**
     * Récupère les compétences expertes (niveau >= 4)
     */
    List<TechnicianSkill> findExpertSkills(Long idProfile);
    
    /**
     * Supprime une compétence
     */
    void delete(TechnicianSkill skill);
    
    /**
     * Supprime une compétence par ID
     */
    void deleteById(Long idSkill);
}