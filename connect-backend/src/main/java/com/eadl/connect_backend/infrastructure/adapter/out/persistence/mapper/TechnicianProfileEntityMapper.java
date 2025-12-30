package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnicianProfileEntityMapper {

    /**
     * Convertit un modèle TechnicianProfile en entity
     */
    public TechnicianProfileEntity toEntity(TechnicianProfile profile) {
        TechnicianProfileEntity entity = new TechnicianProfileEntity();
        entity.setIdProfile(profile.getIdProfile());
        entity.setBio(profile.getBio());
        entity.setYearsExperience(profile.getYearsExperience());
        entity.setHourlyRate(profile.getHourlyRate());
        entity.setAvailabilityStatus(profile.getAvailabilityStatus());
        entity.setCompletedJobs(profile.getCompletedJobs());
        entity.setAverageRating(profile.getAverageRating());
        entity.setVerified(profile.isVerified());
        entity.setCreatedAt(profile.getCreatedAt());
        entity.setUpdatedAt(profile.getUpdatedAt());
        // ici, technician reste null → à gérer dans le service
        return entity;
    }


    /**
     * Convertit une entity TechnicianProfileEntity en modèle
     */
    public TechnicianProfile toModel(TechnicianProfileEntity entity) {
        if (entity == null) return null;

        TechnicianProfile model = new TechnicianProfile();
        model.setIdProfile(entity.getIdProfile());
        model.setBio(entity.getBio());
        model.setYearsExperience(entity.getYearsExperience());
        model.setHourlyRate(entity.getHourlyRate());
        model.setVerified(entity.isVerified());
        model.setAvailabilityStatus(entity.getAvailabilityStatus());
        model.setCompletedJobs(entity.getCompletedJobs());
        model.setAverageRating(entity.getAverageRating());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setIdTechnician(entity.getTechnician() != null ? entity.getTechnician().getIdUser() : null);

        return model;
    }

    /**
     * Convertit une liste de modèles en entities
     */
    public List<TechnicianProfileEntity> toEntities(List<TechnicianProfile> models) {
        if (models == null) return null;
        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'entities en modèles
     */
    public List<TechnicianProfile> toModels(List<TechnicianProfileEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}