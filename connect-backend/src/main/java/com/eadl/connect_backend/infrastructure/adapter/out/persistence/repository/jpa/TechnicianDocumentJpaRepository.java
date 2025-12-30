package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eadl.connect_backend.domain.model.technician.DocumentType;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianDocumentEntity;

public interface TechnicianDocumentJpaRepository
        extends JpaRepository<TechnicianDocumentEntity, Long> {

    List<TechnicianDocumentEntity> findByProfile_IdProfile(Long idProfile);

    Optional<TechnicianDocumentEntity> findByProfile_IdProfileAndDocumentType(
            Long idProfile,
            DocumentType documentType
    );

    List<TechnicianDocumentEntity> findByVerified(boolean verified);

    List<TechnicianDocumentEntity> findByVerifiedFalse();
}
