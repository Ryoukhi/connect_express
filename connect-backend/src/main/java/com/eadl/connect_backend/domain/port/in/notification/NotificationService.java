package com.eadl.connect_backend.domain.port.in.notification;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.notification.Notification;
import com.eadl.connect_backend.domain.model.notification.NotificationType;

/**
 * Port IN - Service Notification
 * Use cases pour la gestion des notifications
 */
public interface NotificationService {
    
    /**
     * Crée une notification simple
     */
    Notification createNotification(Long idUser, NotificationType type, 
                                   String title, String message);
    
    /**
     * Envoie une notification à un utilisateur
     */
    Notification sendNotification(Long idUser, NotificationType type, 
                                  String title, String message);
    
    
    /**
     * Récupère une notification par son ID
     */
    Optional<Notification> getNotificationById(Long idNotification);
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    List<Notification> getUserNotifications(Long idUser);
    
    /**
     * Récupère les notifications non lues d'un utilisateur
     */
    List<Notification> getUnreadNotifications(Long idUser);
    
    // /**
    //  * Récupère les notifications récentes (derniers jours)
    //  */
    // List<Notification> getRecentNotifications(Long idUser, int days);
    
    /**
     * Marque une notification comme lue
     */
    Notification markAsRead(Long idNotification);
    
    /**
     * Marque toutes les notifications comme lues
     */
    void markAllAsRead(Long idUser);
    
    /**
     * Supprime une notification
     */
    void deleteNotification(Long idNotification);
    
    /**
     * Supprime toutes les notifications lues
     */
    void deleteReadNotifications(Long idUser);
    
    /**
     * Compte les notifications non lues
     */
    Long countUnreadNotifications(Long idUser);
    
    // /**
    //  * Envoie une notification par email
    //  */
    // void sendEmailNotification(Long idUser, String subject, String message);
    
    // /**
    //  * Envoie une notification par SMS
    //  */
    // void sendSmsNotification(Long idUser, String message);
}