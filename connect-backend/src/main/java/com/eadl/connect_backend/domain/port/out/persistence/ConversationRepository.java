package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.chat.Conversation;

/**
 * Port OUT - Repository Conversation
 */
public interface ConversationRepository {
    
    /**
     * Sauvegarde une conversation
     */
    Conversation save(Conversation conversation);
    
    /**
     * Récupère une conversation par son ID
     */
    Optional<Conversation> findById(Long idConversation);
    
    /**
     * Récupère la conversation d'une réservation
     */
    Optional<Conversation> findByReservationId(Long idReservation);
    
    /**
     * Récupère toutes les conversations d'un utilisateur (client ou technicien)
     */
    List<Conversation> findByUserId(Long idUser);
    
    /**
     * Récupère les conversations actives d'un utilisateur
     */
    List<Conversation> findActiveByUserId(Long idUser);
    
    /**
     * Récupère les conversations d'un client
     */
    List<Conversation> findByClientId(Long idClient);
    
    /**
     * Récupère les conversations d'un technicien
     */
    List<Conversation> findByTechnicianId(Long idTechnician);
    
    /**
     * Compte les conversations
     */
    Long count();
    
    /**
     * Supprime une conversation
     */
    void delete(Conversation conversation);
}
