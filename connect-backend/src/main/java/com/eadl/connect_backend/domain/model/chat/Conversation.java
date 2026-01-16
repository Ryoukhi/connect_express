package com.eadl.connect_backend.domain.model.chat;

import java.time.LocalDateTime;

/**
 * Conversation - Discussion entre un client et un technicien
 */
public class Conversation {
    private Long idConversation;
    private Long idReservation;
    private Long idClient;
    private Long idTechnician;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    private boolean active;

    private Conversation() {}

    // ========== Factory Method ==========
    public static Conversation create(Long idReservation, Long idClient, Long idTechnician) {
        Conversation conversation = new Conversation();
        conversation.idReservation = idReservation;
        conversation.idClient = idClient;
        conversation.idTechnician = idTechnician;
        conversation.createdAt = LocalDateTime.now();
        conversation.lastMessageAt = LocalDateTime.now();
        conversation.active = true;
        return conversation;
    }

    // ========== Business Logic Methods ==========
    public void updateLastMessageTime() {
        this.lastMessageAt = LocalDateTime.now();
    }

    public void close() {
        this.active = false;
    }

    public void reopen() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isParticipant(Long userId) {
        if (userId == null) return false;
        return userId.equals(idClient) || userId.equals(idTechnician);
    }

    // ========== Getters ==========
    public Long getIdConversation() {
        return idConversation;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public Long getIdClient() {
        return idClient;
    }

    public Long getIdTechnician() {
        return idTechnician;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public boolean getActive() {
        return active;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;

        // Si l'un des deux ID est null, considérer que les objets ne sont pas égaux
        if (this.idConversation == null || that.idConversation == null) {
            return false;
        }

        return this.idConversation.equals(that.idConversation);
    }

    @Override
    public int hashCode() {
        // Si idConversation est null, retourner un hashCode unique basé sur l'objet lui-même
        return idConversation != null ? idConversation.hashCode() : System.identityHashCode(this);
    }


    @Override
    public String toString() {
        return "Conversation{" +
                "idConversation=" + idConversation +
                ", idClient=" + idClient +
                ", idTechnician=" + idTechnician +
                ", active=" + active +
                '}';
    }
}