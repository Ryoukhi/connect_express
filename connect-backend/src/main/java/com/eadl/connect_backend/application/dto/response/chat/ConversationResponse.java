package com.eadl.connect_backend.application.dto.response.chat;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse conversation
 */
public class ConversationResponse {
    
    private Long idConversation;
    private Long idReservation;
    private Long idClient;
    private String clientName;
    private Long idTechnician;
    private String technicianName;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    private boolean active;
    private Integer unreadCount;
    private MessageResponse lastMessage;
    public Long getIdConversation() {
        return idConversation;
    }
    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }
    public Long getIdReservation() {
        return idReservation;
    }
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    public Long getIdClient() {
        return idClient;
    }
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public Long getIdTechnician() {
        return idTechnician;
    }
    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
    }
    public String getTechnicianName() {
        return technicianName;
    }
    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }
    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Integer getUnreadCount() {
        return unreadCount;
    }
    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
    public MessageResponse getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }

    
}