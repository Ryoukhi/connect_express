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
     * @param skill compétence à ajouter
     * @return compétence créée
     */
    TechnicianSkill addSkill(TechnicianSkill skill);

    /**
     * Met à jour une compétence existante
     * @param skillId ID de la compétence
     * @param skill données mises à jour
     * @return compétence mise à jour
     */
    TechnicianSkill updateSkill(Long skillId, TechnicianSkill skill);

    /**
     * Supprime une compétence
     * @param skillId ID de la compétence
     * @param technicianId ID du technicien qui effectue la suppression
     */
    void deleteSkill(Long skillId, Long technicianId);

    /**
     * Récupère toutes les compétences d’un technicien
     * @param technicianId ID du technicien
     * @return liste de compétences
     */
    List<TechnicianSkill> getSkillsByTechnicianId(Long technicianId);

    /**
     * Récupère les compétences d’un profil technicien par catégorie
     * @param technicianId ID du technicien
     * @param categoryId ID de la catégorie
     * @return liste de compétences filtrée
     */
    List<TechnicianSkill> getSkillsByCategory(Long technicianId, Long categoryId);

    /**
     * Récupère une compétence par son ID
     * @param skillId ID de la compétence
     * @return Optional de compétence
     */
    Optional<TechnicianSkill> getSkillById(Long skillId);
}