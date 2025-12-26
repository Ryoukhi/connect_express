package com.eadl.connect_backend.infrastructure.repository.jpa;

import com.eadl.connect_backend.domain.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité Conversation
 */
@Repository
public interface ConversationJpaRepository extends JpaRepository<Conversation, Long> {

}