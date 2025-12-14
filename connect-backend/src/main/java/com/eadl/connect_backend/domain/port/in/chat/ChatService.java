package com.eadl.connect_backend.domain.port.in.chat;

import com.eadl.connect_backend.domain.model.Chat;
import com.eadl.connect_backend.domain.model.Message;
import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour la gestion des chats et messages
 * Définit les opérations CRUD et les fonctionnalités de messagerie
 */
public interface ChatService {

    // ========== CREATE ==========

    /**
     * Crée une nouvelle conversation de chat
     * @param chat L'objet chat à créer
     * @return Le chat créé avec son ID généré
     */
    Chat createChat(Chat chat);

    /**
     * Crée ou récupère un chat entre deux utilisateurs
     * Si un chat existe déjà entre ces deux utilisateurs, le retourne
     * @param userId1 L'identifiant du premier utilisateur
     * @param userId2 L'identifiant du second utilisateur
     * @return Le chat créé ou existant
     */
    Chat getOrCreateChatBetweenUsers(Long userId1, Long userId2);

    /**
     * Envoie un message dans un chat
     * @param chatId L'identifiant du chat
     * @param senderId L'identifiant de l'expéditeur
     * @param content Le contenu du message
     * @return Le message créé
     */
    Message sendMessage(Long chatId, Long senderId, String content);

    /**
     * Envoie un message avec une pièce jointe (image, fichier)
     * @param chatId L'identifiant du chat
     * @param senderId L'identifiant de l'expéditeur
     * @param content Le contenu du message
     * @param attachmentUrl L'URL de la pièce jointe
     * @return Le message créé avec la pièce jointe
     */
    Message sendMessageWithAttachment(Long chatId, Long senderId, String content, String attachmentUrl);

    // ========== READ ==========

    /**
     * Récupère un chat par son identifiant
     * @param id L'identifiant du chat
     * @return Optional contenant le chat si trouvé, sinon Optional vide
     */
    Optional<Chat> getChatById(Long id);

    /**
     * Récupère tous les chats d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des chats de l'utilisateur, triés par date du dernier message
     */
    List<Chat> getUserChats(Long userId);

    /**
     * Récupère les chats actifs d'un utilisateur (avec messages non lus)
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des chats avec messages non lus
     */
    List<Chat> getActiveChats(Long userId);

    /**
     * Récupère un chat spécifique entre deux utilisateurs
     * @param userId1 L'identifiant du premier utilisateur
     * @param userId2 L'identifiant du second utilisateur
     * @return Optional contenant le chat si trouvé, sinon Optional vide
     */
    Optional<Chat> getChatBetweenUsers(Long userId1, Long userId2);

    /**
     * Récupère tous les messages d'un chat
     * @param chatId L'identifiant du chat
     * @return Liste des messages du chat, triés chronologiquement
     */
    List<Message> getChatMessages(Long chatId);

    /**
     * Récupère les messages d'un chat avec pagination
     * @param chatId L'identifiant du chat
     * @param page Le numéro de page (commence à 0)
     * @param size Le nombre de messages par page
     * @return Liste des messages paginés
     */
    List<Message> getChatMessages(Long chatId, int page, int size);

    /**
     * Récupère un message par son identifiant
     * @param messageId L'identifiant du message
     * @return Optional contenant le message si trouvé, sinon Optional vide
     */
    Optional<Message> getMessageById(Long messageId);

    /**
     * Récupère les messages non lus d'un utilisateur dans un chat
     * @param chatId L'identifiant du chat
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des messages non lus
     */
    List<Message> getUnreadMessages(Long chatId, Long userId);

    // ========== UPDATE ==========

    /**
     * Met à jour un chat
     * @param id L'identifiant du chat à modifier
     * @param chat L'objet chat avec les nouvelles données
     * @return Le chat mis à jour
     */
    Chat updateChat(Long id, Chat chat);

    /**
     * Marque un message comme lu
     * @param messageId L'identifiant du message
     * @param userId L'identifiant de l'utilisateur qui lit le message
     */
    void markMessageAsRead(Long messageId, Long userId);

    /**
     * Marque tous les messages d'un chat comme lus pour un utilisateur
     * @param chatId L'identifiant du chat
     * @param userId L'identifiant de l'utilisateur
     */
    void markAllMessagesAsRead(Long chatId, Long userId);

    /**
     * Met à jour le contenu d'un message (édition)
     * @param messageId L'identifiant du message
     * @param newContent Le nouveau contenu
     * @return Le message mis à jour
     * @throws UnauthorizedException si l'utilisateur n'est pas l'expéditeur
     */
    Message updateMessage(Long messageId, String newContent);

    // ========== DELETE ==========

    /**
     * Supprime définitivement un chat et tous ses messages
     * @param id L'identifiant du chat à supprimer
     */
    void deleteChat(Long id);

    /**
     * Archive un chat pour un utilisateur (soft delete)
     * Le chat reste accessible mais n'apparaît plus dans la liste principale
     * @param chatId L'identifiant du chat
     * @param userId L'identifiant de l'utilisateur
     */
    void archiveChat(Long chatId, Long userId);

    /**
     * Supprime un message
     * @param messageId L'identifiant du message à supprimer
     * @param userId L'identifiant de l'utilisateur (doit être l'expéditeur)
     * @throws UnauthorizedException si l'utilisateur n'est pas l'expéditeur
     */
    void deleteMessage(Long messageId, Long userId);

    /**
     * Supprime un message pour tous les participants du chat
     * @param messageId L'identifiant du message
     * @param userId L'identifiant de l'utilisateur (doit être l'expéditeur)
     */
    void deleteMessageForEveryone(Long messageId, Long userId);

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Compte le nombre de messages non lus pour un utilisateur dans un chat
     * @param chatId L'identifiant du chat
     * @param userId L'identifiant de l'utilisateur
     * @return Le nombre de messages non lus
     */
    long countUnreadMessages(Long chatId, Long userId);

    /**
     * Compte le nombre total de messages non lus pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Le nombre total de messages non lus
     */
    long countTotalUnreadMessages(Long userId);

    /**
     * Recherche des messages dans un chat par mot-clé
     * @param chatId L'identifiant du chat
     * @param keyword Le mot-clé de recherche
     * @return Liste des messages correspondants
     */
    List<Message> searchMessagesInChat(Long chatId, String keyword);

    /**
     * Vérifie si un utilisateur fait partie d'un chat
     * @param chatId L'identifiant du chat
     * @param userId L'identifiant de l'utilisateur
     * @return true si l'utilisateur est participant, false sinon
     */
    boolean isUserInChat(Long chatId, Long userId);

    /**
     * Récupère le dernier message d'un chat
     * @param chatId L'identifiant du chat
     * @return Optional contenant le dernier message si existant
     */
    Optional<Message> getLastMessage(Long chatId);
}