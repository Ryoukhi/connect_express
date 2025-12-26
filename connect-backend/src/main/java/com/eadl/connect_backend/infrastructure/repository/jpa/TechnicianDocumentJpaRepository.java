package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.TechnicianDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité TechnicianDocument
 */
@Repository
public interface TechnicianDocumentJpaRepository extends JpaRepository<TechnicianDocument, Long> {

}