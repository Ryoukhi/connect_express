package com.eadl.connect_backend.domain.port.in.technician;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;

/**
 * Port IN - Service métier pour la gestion des compétences technicien
 */
public interface TechnicianSkillService {

    /**
     * Ajoute une compétence à un profil technicien
     */
    TechnicianSkill addSkill(TechnicianSkill skill);

    /**
     * Met à jour une compétence existante
     */
    TechnicianSkill updateSkill(TechnicianSkill skill);

    /**
     * Supprime une compétence
     */
    void removeSkill(Long skillId);

    /**
     * Récupère toutes les compétences d’un technicien
     */
    List<TechnicianSkill> getSkillsByTechnician(Long technicianId);

    /**
     * Récupère les compétences d’un technicien par catégorie
     */
    List<TechnicianSkill> getSkillsByCategory(
            Long technicianId,
            Long categoryId
    );

    /**
     * Récupère une compétence par son ID
     */
    Optional<TechnicianSkill> getSkillById(Long skillId);
}