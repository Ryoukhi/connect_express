package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;

public class TechnicianSkillEntityMapper {
    /**
     * Convertit un domaine en entité JPA
     */
    public TechnicianSkillEntity toEntity(TechnicianSkill skill) {
        if (skill == null) return null;

        TechnicianSkillEntity entity = new TechnicianSkillEntity();
        entity.setIdSkill(skill.getIdSkill());
        entity.setName(skill.getNameSkill());
        entity.setDescription(skill.getDescription());
        entity.setLevel(skill.getLevel());

        // Création minimale des relations avec uniquement les IDs
        if (skill.getIdProfile() != null) {
            TechnicianProfileEntity profile = new TechnicianProfileEntity();
            profile.setIdProfile(skill.getIdProfile());
            entity.setProfile(profile);
        }

        if (skill.getIdCategory() != null) {
            CategoryEntity category = new CategoryEntity();
            category.setIdCategory(skill.getIdCategory());
            entity.setCategory(category);
        }

        return entity;
    }

    /**
     * Convertit une entité JPA en domaine
     */
    public TechnicianSkill toDomain(TechnicianSkillEntity entity) {
        if (entity == null) return null;

        TechnicianSkill skill = new TechnicianSkill();
        skill.setIdSkill(entity.getIdSkill());
        skill.setNameSkill(entity.getName());
        skill.setDescription(entity.getDescription());
        skill.setLevel(entity.getLevel());

        if (entity.getProfile() != null) {
            skill.setIdProfile(entity.getProfile().getIdProfile());
        }

        if (entity.getCategory() != null) {
            skill.setIdCategory(entity.getCategory().getIdCategory());
        }

        return skill;
    }
}