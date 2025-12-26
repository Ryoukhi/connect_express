package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.TechnicianSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité TechnicianSkill
 */
@Repository
public interface TechnicianSkillJpaRepository extends JpaRepository<TechnicianSkill, Long> {

}