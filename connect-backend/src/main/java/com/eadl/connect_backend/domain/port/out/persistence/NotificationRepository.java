package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;
import com.eadl.connect_backend.domain.model.notification.Notification;
import com.eadl.connect_backend.domain.model.notification.NotificationType;

/**
 * Port OUT - Repository Notification
 */
public interface NotificationRepository {
    
    /**
     * Sauvegarde une notification
     */
    Notification save(Notification notification);
    
    /**
     * Récupère une notification par son ID
     */
    Optional<Notification> findById(Long idNotification);
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    List<Notification> findByUserId(Long idUser);
    
    /**
     * Récupère les notifications non lues d'un utilisateur
     */
    List<Notification> findUnreadByUserId(Long idUser);
    
    /**
     * Récupère les notifications par type
     */
    List<Notification> findByType(NotificationType type);
    
    /**
     * Récupère les notifications récentes d'un utilisateur
     */
    List<Notification> findRecentByUserId(Long idUser, int days);
    
    /**
     * Récupère les notifications expirées
     */
    List<Notification> findExpired();
    
    /**
     * Compte les notifications non lues d'un utilisateur
     */
    Long countUnreadByUserId(Long idUser);
    
    /**
     * Compte les notifications d'un utilisateur
     */
    Long countByUserId(Long idUser);
    
    /**
     * Supprime une notification
     */
    void delete(Notification notification);
    
    /**
     * Supprime les notifications lues d'un utilisateur
     */
    void deleteReadByUserId(Long idUser);
    
    /**
     * Supprime les notifications expirées
     */
    void deleteExpired();
}