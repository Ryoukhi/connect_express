package com.eadl.connect_backend.domain.model.chat;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Message - Message dans une conversation
 */
public class Message {
    private Long idMessage;
    private Long idConversation;
    private Long senderId;
    private MessageType type;
    private String content;
    private String fileUrl;
    private LocalDateTime sentAt;
    private boolean read;
    private LocalDateTime readAt;

    private Message() {}

    // ========== Factory Methods ==========
    public static Message createTextMessage(Long idConversation, Long senderId, String content) {
        Message message = new Message();
        message.idConversation = idConversation;
        message.senderId = senderId;
        message.type = MessageType.TEXT;
        message.content = content;
        message.sentAt = LocalDateTime.now();
        message.read = false;
        return message;
    }

    public static Message createImageMessage(Long idConversation, Long senderId, 
                                            String caption, String imageUrl) {
        Message message = new Message();
        message.idConversation = idConversation;
        message.senderId = senderId;
        message.type = MessageType.IMAGE;
        message.content = caption;
        message.fileUrl = imageUrl;
        message.sentAt = LocalDateTime.now();
        message.read = false;
        return message;
    }

    public static Message createFileMessage(Long idConversation, Long senderId, 
                                           String fileName, String fileUrl) {
        Message message = new Message();
        message.idConversation = idConversation;
        message.senderId = senderId;
        message.type = MessageType.FILE;
        message.content = fileName;
        message.fileUrl = fileUrl;
        message.sentAt = LocalDateTime.now();
        message.read = false;
        return message;
    }

    public static Message createSystemMessage(Long idConversation, String content) {
        Message message = new Message();
        message.idConversation = idConversation;
        message.senderId = null; // Message système
        message.type = MessageType.SYSTEM;
        message.content = content;
        message.sentAt = LocalDateTime.now();
        message.read = true; // Les messages système sont automatiquement "lus"
        return message;
    }

    // ========== Business Logic Methods ==========
    public void markAsRead() {
        if (!this.read) {
            this.read = true;
            this.readAt = LocalDateTime.now();
        }
    }

    public boolean isSystemMessage() {
        return type == MessageType.SYSTEM;
    }

    public boolean hasAttachment() {
        return fileUrl != null && !fileUrl.isEmpty();
    }

    public boolean isSentBy(Long userId) {
        return userId.equals(senderId);
    }

    // ========== Getters ==========
    public Long getIdMessage() {
        return idMessage;
    }

    public Long getIdConversation() {
        return idConversation;
    }

    public Long getSenderId() {
        return senderId;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(idMessage, message.idMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMessage);
    }

    @Override
    public String toString() {
        return "Message{" +
                "idMessage=" + idMessage +
                ", type=" + type +
                ", senderId=" + senderId +
                ", read=" + read +
                ", sentAt=" + sentAt +
                '}';
    }
}