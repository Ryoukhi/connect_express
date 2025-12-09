package com.eadl.connect_backend.domain.model.notification;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Notification - Notification envoyée à un utilisateur
 */
public class Notification {
    private Long idNotification;
    private Long idUser;
    private NotificationType type;
    private String title;
    private String message;
    private String data; // JSON data supplémentaires (idReservation, etc.)
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String actionUrl; // URL pour redirection (optionnel)

    private Notification() {}

    // ========== Factory Methods ==========
    public static Notification create(Long idUser, NotificationType type, 
                                     String title, String message) {
        Notification notification = new Notification();
        notification.idUser = idUser;
        notification.type = type;
        notification.title = title;
        notification.message = message;
        notification.read = false;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    public static Notification create(Long idUser, NotificationType type, 
                                     String title, String message, String data) {
        Notification notification = create(idUser, type, title, message);
        notification.data = data;
        return notification;
    }

    // ========== Business Logic Methods ==========
    public void markAsRead() {
        if (!this.read) {
            this.read = true;
            this.readAt = LocalDateTime.now();
        }
    }

    public void markAsUnread() {
        this.read = false;
        this.readAt = null;
    }

    public void setActionUrl(String url) {
        this.actionUrl = url;
    }

    public void setExpirationDate(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean hasAction() {
        return actionUrl != null && !actionUrl.isEmpty();
    }

    public boolean isUrgent() {
        return type == NotificationType.PAYMENT_FAILED ||
               type == NotificationType.ACCOUNT_SUSPENDED ||
               type == NotificationType.RESERVATION_CANCELLED ||
               type == NotificationType.DOCUMENT_REJECTED;
    }

    public boolean isSystemNotification() {
        return type == NotificationType.SYSTEM_MAINTENANCE ||
               type == NotificationType.SYSTEM_UPDATE;
    }

    // ========== Getters ==========
    public Long getIdNotification() {
        return idNotification;
    }

    public Long getIdUser() {
        return idUser;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setData(String data) {
        this.data = data;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(idNotification, that.idNotification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNotification);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", read=" + read +
                ", createdAt=" + createdAt +
                '}';
    }
}

