package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.TechnicianProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité TechnicianProfile
 */
@Repository
public interface TechnicianProfileJpaRepository extends JpaRepository<TechnicianProfile, Long> {

}