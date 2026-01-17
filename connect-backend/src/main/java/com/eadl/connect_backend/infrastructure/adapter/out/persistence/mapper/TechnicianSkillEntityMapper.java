package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianSkillEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnicianSkillEntityMapper {
    /**
     * Convertit un domaine en entité JPA
     */
    public TechnicianSkillEntity toEntity(TechnicianSkill skill) {
        if (skill == null) return null;

        TechnicianSkillEntity entity = new TechnicianSkillEntity();
        entity.setIdSkill(skill.getIdSkill());
        entity.setName(skill.getName());
        entity.setDescription(skill.getDescription());
        entity.setLevel(skill.getLevel());
        entity.setYearsExperience(skill.getYearsExperience());
        entity.setHourlyRate(skill.getHourlyRate());
        entity.setAvailabilityStatus(skill.getAvailabilityStatus());
        entity.setVerified(skill.isVerified());
        entity.setCreatedAt(skill.getCreatedAt());
        entity.setUpdatedAt(skill.getUpdatedAt());

        // Création minimale des relations avec uniquement les IDs
        if (skill.getIdUser() != null) {
            UserEntity user = new UserEntity();
            user.setIdUser(skill.getIdUser());
            entity.setTechnician(user);
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
        skill.setName(entity.getName());
        skill.setDescription(entity.getDescription());
        skill.setLevel(entity.getLevel());
        skill.setYearsExperience(entity.getYearsExperience());
        skill.setHourlyRate(entity.getHourlyRate());
        skill.setAvailabilityStatus(entity.getAvailabilityStatus());
        skill.setVerified(entity.isVerified());
        skill.setCreatedAt(entity.getCreatedAt());
        skill.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getTechnician() != null) {
            skill.setIdUser(entity.getTechnician().getIdUser());
        }

        if (entity.getCategory() != null) {
            skill.setIdCategory(entity.getCategory().getIdCategory());
        }

        return skill;
    }
}