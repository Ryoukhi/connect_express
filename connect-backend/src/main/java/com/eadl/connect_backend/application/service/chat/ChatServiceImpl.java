package com.eadl.connect_backend.application.service.chat;

import com.eadl.connect_backend.domain.model.chat.Conversation;
import com.eadl.connect_backend.domain.model.chat.Message;
import com.eadl.connect_backend.domain.port.in.chat.ChatService;
import com.eadl.connect_backend.domain.port.out.persistence.ConversationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.MessageRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Conversation createConversation(Long idReservation, Long idClient, Long idTechnician) {
        log.info("Création d'une conversation pour réservation id={}", idReservation);

        Optional<Conversation> existing = conversationRepository.findByReservationId(idReservation);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Validate reservation exists
        reservationRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable"));

        Conversation conversation = Conversation.create(idReservation, idClient, idTechnician);
        return conversationRepository.save(conversation);
    }

    @Override
    public Optional<Conversation> getConversationById(Long idConversation) {
        return conversationRepository.findById(idConversation);
    }

    @Override
    public Optional<Conversation> getConversationByReservation(Long idReservation) {
        return conversationRepository.findByReservationId(idReservation);
    }

    @Override
    public List<Conversation> getUserConversations(Long idUser) {
        return conversationRepository.findByUserId(idUser);
    }

    @Override
    public List<Conversation> getActiveConversations(Long idUser) {
        return conversationRepository.findActiveByUserId(idUser);
    }

    @Override
    public Conversation closeConversation(Long idConversation) {
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));
        conversation.close();
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation reopenConversation(Long idConversation) {
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));
        conversation.reopen();
        return conversationRepository.save(conversation);
    }

    @Override
    public Message sendTextMessage(Long idConversation, Long senderId, String content) {
        log.info("Envoi message texte vers conversation id={} par user id={}", idConversation, senderId);
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));

        if (!conversation.isParticipant(senderId)) {
            throw new IllegalArgumentException("Utilisateur non participant à la conversation");
        }

        Message message = Message.createTextMessage(idConversation, senderId, content);
        Message saved = messageRepository.save(message);

        conversation.updateLastMessageTime();
        conversationRepository.save(conversation);
        return saved;
    }

    @Override
    public Message sendImageMessage(Long idConversation, Long senderId, String caption, byte[] imageData,
            String fileName) {
        log.info("Envoi image vers conversation id={} par user id={}", idConversation, senderId);
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));

        if (!conversation.isParticipant(senderId)) {
            throw new IllegalArgumentException("Utilisateur non participant à la conversation");
        }

        try {
            Path dir = Paths.get("uploads", "chat", String.valueOf(idConversation));
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, imageData);
            String url = filePath.toAbsolutePath().toString();

            Message message = Message.createImageMessage(idConversation, senderId, caption, url);
            Message saved = messageRepository.save(message);

            conversation.updateLastMessageTime();
            conversationRepository.save(conversation);
            return saved;
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement de l'image pour conversation {}", idConversation, e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
        }
    }

    @Override
    public Message sendFileMessage(Long idConversation, Long senderId, byte[] fileData, String fileName) {
        log.info("Envoi fichier vers conversation id={} par user id={}", idConversation, senderId);
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));

        if (!conversation.isParticipant(senderId)) {
            throw new IllegalArgumentException("Utilisateur non participant à la conversation");
        }

        try {
            Path dir = Paths.get("uploads", "chat", String.valueOf(idConversation));
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, fileData);
            String url = filePath.toAbsolutePath().toString();

            Message message = Message.createFileMessage(idConversation, senderId, fileName, url);
            Message saved = messageRepository.save(message);

            conversation.updateLastMessageTime();
            conversationRepository.save(conversation);
            return saved;
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement du fichier pour conversation {}", idConversation, e);
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    @Override
    public Message sendSystemMessage(Long idConversation, String content) {
        log.info("Envoi message système vers conversation id={}", idConversation);
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new EntityNotFoundException("Conversation introuvable"));

        Message message = Message.createSystemMessage(idConversation, content);
        Message saved = messageRepository.save(message);

        conversation.updateLastMessageTime();
        conversationRepository.save(conversation);
        return saved;
    }

    @Override
    public Optional<Message> getMessageById(Long idMessage) {
        return messageRepository.findById(idMessage);
    }

    @Override
    public List<Message> getConversationMessages(Long idConversation) {
        return messageRepository.findByConversationIdOrderBySentAt(idConversation);
    }

    @Override
    public List<Message> getUnreadMessages(Long idConversation, Long userId) {
        return messageRepository.findUnreadByConversationIdAndReceiverId(idConversation, userId);
    }

    @Override
    public Message markMessageAsRead(Long idMessage) {
        Message message = messageRepository.findById(idMessage)
                .orElseThrow(() -> new EntityNotFoundException("Message introuvable"));
        message.markAsRead();
        return messageRepository.save(message);
    }

    @Override
    public void markAllMessagesAsRead(Long idConversation, Long userId) {
        List<Message> unread = messageRepository.findUnreadByConversationIdAndReceiverId(idConversation, userId);
        for (Message m : unread) {
            m.markAsRead();
            messageRepository.save(m);
        }
        log.info("Tous les messages marqués comme lus pour conversation id={} user id={}", idConversation, userId);
    }

    @Override
    public Long countUnreadMessages(Long idUser) {
        return messageRepository.countUnreadByUserId(idUser);
    }

    
}