package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianDocumentEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnicianDocumentEntityMapper {

    /**
     * Convertit un modèle TechnicianDocument en entity
     */
    public TechnicianDocumentEntity toEntity(TechnicianDocument model) {
        if (model == null) return null;

        TechnicianDocumentEntity entity = new TechnicianDocumentEntity();
        entity.setIdDocument(model.getIdDocument());
        entity.setDocumentType(model.getType());
        entity.setUrl(model.getUrl());
        entity.setVerified(model.isVerified());
        entity.setVerificationNote(model.getVerificationNote());
        entity.setUploadedAt(model.getUploadedAt());

        return entity;
    }

    /**
     * Convertit une entity TechnicianDocumentEntity en modèle
     */
    public TechnicianDocument toModel(TechnicianDocumentEntity entity) {
        if (entity == null) return null;

        TechnicianDocument model = new TechnicianDocument();
        model.setIdDocument(entity.getIdDocument());
        model.setType(entity.getDocumentType());
        model.setUrl(entity.getUrl());
        model.setVerified(entity.isVerified());
        model.setVerificationNote(entity.getVerificationNote());
        model.setIdProfile(entity.getProfile() != null ? entity.getProfile().getIdProfile() : null);
        model.setUploadedAt(entity.getUploadedAt());

        return model;
    }

    /**
     * Convertit une liste de modèles en entities
     */
    public List<TechnicianDocumentEntity> toEntities(List<TechnicianDocument> models) {
        if (models == null) return null;
        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'entities en modèles
     */
    public List<TechnicianDocument> toModels(List<TechnicianDocumentEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}