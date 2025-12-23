package com.eadl.connect_backend.domain.port.in.chat;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.chat.Conversation;
import com.eadl.connect_backend.domain.model.chat.Message;

/**
 * Port IN - Service Chat
 * Use cases pour la messagerie temps réel
 */
public interface ChatService {
    
    /**
     * Crée une nouvelle conversation
     */
    Conversation createConversation(Long idReservation, Long idClient, Long idTechnician);
    
    /**
     * Récupère une conversation par son ID
     */
    Optional<Conversation> getConversationById(Long idConversation);
    
    /**
     * Récupère la conversation d'une réservation
     */
    Optional<Conversation> getConversationByReservation(Long idReservation);
    
    /**
     * Récupère toutes les conversations d'un utilisateur
     */
    List<Conversation> getUserConversations(Long idUser);
    
    /**
     * Récupère les conversations actives d'un utilisateur
     */
    List<Conversation> getActiveConversations(Long idUser);
    
    /**
     * Ferme une conversation
     */
    Conversation closeConversation(Long idConversation);
    
    /**
     * Rouvre une conversation
     */
    Conversation reopenConversation(Long idConversation);
    
    /**
     * Envoie un message texte
     */
    Message sendTextMessage(Long idConversation, Long senderId, String content);
    
    /**
     * Envoie un message avec image
     */
    Message sendImageMessage(Long idConversation, Long senderId, 
                            String caption, byte[] imageData, String fileName);
    
    /**
     * Envoie un message avec fichier
     */
    Message sendFileMessage(Long idConversation, Long senderId, 
                           byte[] fileData, String fileName);
    
    /**
     * Envoie un message système
     */
    Message sendSystemMessage(Long idConversation, String content);
    
    /**
     * Récupère un message par son ID
     */
    Optional<Message> getMessageById(Long idMessage);
    
    /**
     * Récupère tous les messages d'une conversation
     */
    List<Message> getConversationMessages(Long idConversation);
    
    /**
     * Récupère les messages non lus d'une conversation
     */
    List<Message> getUnreadMessages(Long idConversation, Long userId);
    
    /**
     * Marque un message comme lu
     */
    Message markMessageAsRead(Long idMessage);
    
    /**
     * Marque tous les messages d'une conversation comme lus
     */
    void markAllMessagesAsRead(Long idConversation, Long userId);
    
    /**
     * Compte les messages non lus pour un utilisateur
     */
    Long countUnreadMessages(Long idUser);
    
}