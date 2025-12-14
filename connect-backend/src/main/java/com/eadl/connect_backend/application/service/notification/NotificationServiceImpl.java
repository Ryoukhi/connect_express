package com.eadl.connect_backend.application.service.notification;

import com.eadl.connect_backend.domain.model.Notification;
import com.eadl.connect_backend.domain.port.in.notification.NotificationService;
import com.eadl.connect_backend.domain.port.out.NotificationRepository;
import com.eadl.connect_backend.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ========== CREATE ==========

    @Override
    public Notification createNotification(Notification notification) {
        log.info("Création d'une nouvelle notification pour l'utilisateur: {}", notification.getUserId());

        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }

        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification créée avec succès avec l'ID: {}", savedNotification.getId());
        return savedNotification;
    }

    @Override
    public Notification sendNotification(Long userId, String title, String message, String type) {
        log.info("Envoi d'une notification à l'utilisateur: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId);
        }

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification envoyée avec succès: {}", savedNotification.getId());
        return savedNotification;
    }

    @Override
    public List<Notification> sendBulkNotification(List<Long> userIds, String title, String message, String type) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    public List<Notification> sendNotificationToAll(String title, String message, String type) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    public Notification sendNotificationWithAction(Long userId, String title, String message, String actionUrl, String type) {
        // TODO: Implémenter
        return null;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> getNotificationById(Long id) {
        log.debug("Recherche de la notification avec l'ID: {}", id);
        return notificationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        log.debug("Récupération des notifications de l'utilisateur: {}", userId);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getReadNotifications(Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByType(Long userId, String type) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getRecentNotifications(Long userId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId, int page, int size) {
        // TODO: Implémenter
        return List.of();
    }

    // ========== UPDATE ==========

    @Override
    public Notification updateNotification(Long id, Notification notification) {
        log.info("Mise à jour de la notification avec l'ID: {}", id);

        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée avec l'ID: " + id));

        if (notification.getTitle() != null) {
            existingNotification.setTitle(notification.getTitle());
        }
        if (notification.getMessage() != null) {
            existingNotification.setMessage(notification.getMessage());
        }
        if (notification.getType() != null) {
            existingNotification.setType(notification.getType());
        }
        if (notification.getActionUrl() != null) {
            existingNotification.setActionUrl(notification.getActionUrl());
        }

        Notification updatedNotification = notificationRepository.save(existingNotification);
        log.info("Notification mise à jour avec succès: {}", id);
        return updatedNotification;
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void markAllAsRead(Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void markAsUnread(Long notificationId, Long userId) {
        // TODO: Implémenter
    }

    // ========== DELETE ==========

    @Override
    public void deleteNotification(Long id, Long userId) {
        log.info("Suppression de la notification {} par l'utilisateur {}", id, userId);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée avec l'ID: " + id));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette notification n'appartient pas à cet utilisateur");
        }

        notificationRepository.delete(notification);
        log.info("Notification supprimée avec succès: {}", id);
    }

    @Override
    public void deleteAllNotifications(Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void deleteReadNotifications(Long userId) {
        // TODO: Implémenter
    }

    @Override
    public void deleteOldNotifications(Long userId, int daysOld) {
        // TODO: Implémenter
    }

    @Override
    public void archiveNotification(Long notificationId, Long userId) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ==========

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications(Long userId) {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllNotifications(Long userId) {
        return notificationRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countNotificationsByType(Long userId, String type) {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUnreadNotifications(Long userId) {
        // TODO: Implémenter
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Object getNotificationStatistics(Long userId) {
        // TODO: Implémenter
        return null;
    }
}