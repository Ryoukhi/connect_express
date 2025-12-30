package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.technician.DocumentType;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianDocumentEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.TechnicianDocumentEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.TechnicianDocumentJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TechnicianDocumentRepositoryAdapter implements TechnicianDocumentRepository {

    private final TechnicianDocumentJpaRepository technicianDocumentJpaRepository;
    private final TechnicianDocumentEntityMapper mapper;

    @Override
    public TechnicianDocument save(TechnicianDocument document) {
        TechnicianDocumentEntity entity = mapper.toEntity(document);
        TechnicianDocumentEntity saved = technicianDocumentJpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<TechnicianDocument> findById(Long idDocument) {
        return technicianDocumentJpaRepository.findById(idDocument)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianDocument> findByProfileId(Long idProfile) {
        return technicianDocumentJpaRepository
                .findByProfile_IdProfile(idProfile)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<TechnicianDocument> findByProfileIdAndType(
            Long idProfile,
            DocumentType type
    ) {
        return technicianDocumentJpaRepository
                .findByProfile_IdProfileAndDocumentType(idProfile, type)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianDocument> findByVerified(boolean verified) {
        return technicianDocumentJpaRepository
                .findByVerified(verified)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<TechnicianDocument> findPendingDocuments() {
        return technicianDocumentJpaRepository
                .findByVerifiedFalse()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(TechnicianDocument document) {
        TechnicianDocumentEntity entity = mapper.toEntity(document);
        technicianDocumentJpaRepository.delete(entity);
    }

    @Override
    public void deleteById(Long idDocument) {
        technicianDocumentJpaRepository.deleteById(idDocument);
    }
}
