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
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TechnicianDocumentRepositoryAdapter
        implements TechnicianDocumentRepository {

    private final TechnicianDocumentJpaRepository technicianDocumentJpaRepository;
    private final TechnicianDocumentEntityMapper mapper;

    @Override
    public TechnicianDocument save(TechnicianDocument document) {
        log.info("Saving TechnicianDocument");

        TechnicianDocumentEntity entity = mapper.toEntity(document);
        TechnicianDocumentEntity saved =
                technicianDocumentJpaRepository.save(entity);

        log.info("TechnicianDocument saved with id={}", saved.getIdDocument());
        return mapper.toModel(saved);
    }

    @Override
    public Optional<TechnicianDocument> findById(Long idDocument) {
        log.debug("Finding TechnicianDocument by id={}", idDocument);

        return technicianDocumentJpaRepository.findById(idDocument)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianDocument> findByProfileId(Long idProfile) {
        log.debug("Finding TechnicianDocuments by profileId={}", idProfile);

        return technicianDocumentJpaRepository
                .findBySkill_IdSkill(idProfile)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<TechnicianDocument> findByProfileIdAndType(
            Long idProfile,
            DocumentType type
    ) {
        log.debug(
                "Finding TechnicianDocument by profileId={} and type={}",
                idProfile,
                type
        );

        return technicianDocumentJpaRepository
                .findBySkill_IdSkillAndDocumentType(idProfile, type)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianDocument> findByVerified(boolean verified) {
        log.debug("Finding TechnicianDocuments by verified={}", verified);

        return technicianDocumentJpaRepository
                .findByVerified(verified)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<TechnicianDocument> findPendingDocuments() {
        log.debug("Finding pending TechnicianDocuments (verified=false)");

        return technicianDocumentJpaRepository
                .findByVerifiedFalse()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(TechnicianDocument document) {
        if (document.getIdDocument() == null) {
            log.warn("Attempt to delete TechnicianDocument with null id");
            throw new IllegalArgumentException("Document id must not be null");
        }

        log.info("Deleting TechnicianDocument with id={}",
                document.getIdDocument());

        technicianDocumentJpaRepository
                .deleteById(document.getIdDocument());
    }

    @Override
    public void deleteById(Long idDocument) {
        log.info("Deleting TechnicianDocument by id={}", idDocument);
        technicianDocumentJpaRepository.deleteById(idDocument);
    }
}
