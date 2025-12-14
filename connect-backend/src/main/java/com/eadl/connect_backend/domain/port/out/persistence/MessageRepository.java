package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.chat.Message;
import com.eadl.connect_backend.domain.model.chat.MessageType;

/**
 * Port OUT - Repository Message
 */
public interface MessageRepository {
    
    /**
     * Sauvegarde un message
     */
    Message save(Message message);
    
    /**
     * Récupère un message par son ID
     */
    Optional<Message> findById(Long idMessage);
    
    /**
     * Récupère tous les messages d'une conversation
     */
    List<Message> findByConversationId(Long idConversation);
    
    /**
     * Récupère les messages d'une conversation triés par date
     */
    List<Message> findByConversationIdOrderBySentAt(Long idConversation);
    
    /**
     * Récupère les messages non lus d'une conversation pour un utilisateur
     */
    List<Message> findUnreadByConversationIdAndReceiverId(Long idConversation, Long receiverId);
    
    /**
     * Récupère les messages par type
     */
    List<Message> findByType(MessageType type);
    
    /**
     * Compte les messages non lus pour un utilisateur
     */
    Long countUnreadByUserId(Long idUser);
    
    /**
     * Compte les messages d'une conversation
     */
    Long countByConversationId(Long idConversation);
    
    /**
     * Supprime un message
     */
    void delete(Message message);
}