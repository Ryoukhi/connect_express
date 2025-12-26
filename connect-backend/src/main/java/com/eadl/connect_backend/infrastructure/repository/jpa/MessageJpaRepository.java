package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité Message
 */
@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {

}