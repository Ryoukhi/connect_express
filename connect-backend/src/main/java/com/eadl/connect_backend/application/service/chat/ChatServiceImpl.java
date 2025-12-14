package com.eadl.connect_backend.application.service.chat;

import com.eadl.connect_backend.domain.model.Chat;
import com.eadl.connect_backend.domain.model.Message;
import com.eadl.connect_backend.domain.port.in.chat.ChatService;
import com.eadl.connect_backend.domain.port.out.ChatRepository;
import com.eadl.connect_backend.domain.port.out.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des chats
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    // ========== CREATE ==========

    @Override
    public Chat createChat(Chat chat) {
        log.info("Création d'un nouveau chat");

        Chat savedChat = chatRepository.save(chat);
        log.info("Chat créé avec succès avec l'ID: {}", savedChat.getId());
        return savedChat;
    }

    @Override
    public Chat getOrCreateChatBetweenUsers(Long userId1, Long userId2) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public Message sendMessage(Long chatId, Long senderId, String content) {
        log.info("Envoi d'un message dans le chat: {}", chatId);

        if (!chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException("Chat non trouvé avec l'ID: " + chatId);
        }

        Message message = new Message();
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setIsRead(false);
        message.setSentAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        log.info("Message envoyé avec succès: {}", savedMessage.getId());
        return savedMessage;
    }

    @Override
    public Message sendMessageWithAttachment(Long chatId, Long senderId, String content, String attachmentUrl) {
        // TODO: Implémenter
        return null;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Chat> getChatById(Long id) {
        log.debug("Recherche du chat avec l'ID: {}", id);
        return chatRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getUserChats(Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getActiveChats(Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chat> getChatBetweenUsers(Long userId1, Long userId2) {
        // TODO: Implémenter
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getChatMessages(Long chatId) {
        log.debug("Récupération des messages du chat: {}", chatId);
        return messageRepository.findByChatIdOrderBySentAtAsc(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getChatMessages(Long chatId, int page, int size) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(Long messageId) {
        log.debug("Recherche du message avec l'ID: {}", messageId);
        return messageRepository.findById(messageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getUnreadMessages(Long chatId, Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    // ========== UPDATE ==========

    @Override
    public Chat updateChat(Long id, Chat chat) {
        log.info("Mise à jour du chat avec l'ID: {}", id);

        Chat existingChat = chatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat non trouvé avec l'ID: " + id));

        if (chat.getLastMessageAt() != null) {
            existingChat.setLastMessageAt(chat.getLastMessageAt());
        }

        Chat updatedChat = chatRepository.save(existingChat);
        log.info("Chat mis à jour avec succès: {}", id);
        return updatedChat;
    }

    @Override
    public void markMessageAsRead(Long messageId, Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void markAllMessagesAsRead(Long chatId, Long userId) {
        // TODO: Implémenter
    }

    @Override
    public Message updateMessage(Long messageId, String newContent) {
        // TODO: Implémenter
        return null;
    }

    // ========== DELETE ==========

    @Override
    public void deleteChat(Long id) {
        log.info("Suppression du chat avec l'ID: {}", id);

        if (!chatRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat non trouvé avec l'ID: " + id);
        }

        messageRepository.deleteByChatId(id);
        chatRepository.deleteById(id);
        log.info("Chat et ses messages supprimés avec succès: {}", id);
    }

    @Override
    public void archiveChat(Long chatId, Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        log.info("Suppression du message {} par l'utilisateur {}", messageId, userId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message non trouvé avec l'ID: " + messageId));

        if (!message.getSenderId().equals(userId)) {
            throw new IllegalArgumentException("Seul l'expéditeur peut supprimer ce message");
        }

        messageRepository.delete(message);
        log.info("Message supprimé avec succès: {}", messageId);
    }

    @Override
    public void deleteMessageForEveryone(Long messageId, Long userId) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ==========

    @Override
    @Transactional(readOnly = true)
    public long countUnreadMessages(Long chatId, Long userId) {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalUnreadMessages(Long userId) {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> searchMessagesInChat(Long chatId, String keyword) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserInChat(Long chatId, Long userId) {
        // TODO: Implémenter
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> getLastMessage(Long chatId) {
        // TODO: Implémenter
        return Optional.empty();
    }
}