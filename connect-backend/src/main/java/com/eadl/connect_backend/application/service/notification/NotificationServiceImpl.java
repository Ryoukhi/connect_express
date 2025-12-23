package com.eadl.connect_backend.application.service.notification;

import com.eadl.connect_backend.domain.model.notification.Notification;
import com.eadl.connect_backend.domain.model.notification.NotificationType;
import com.eadl.connect_backend.domain.port.in.notification.NotificationService;
import com.eadl.connect_backend.domain.port.out.persistence.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implémentation du service de gestion des notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(Long idUser, NotificationType type, String title, String message) {
        log.info("Création de notification pour user id={}", idUser);
        if (idUser == null) throw new IllegalArgumentException("idUser est requis");
        if (type == null) throw new IllegalArgumentException("type est requis");
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("title est requis");

        Notification notification = Notification.create(idUser, type, title, message);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendNotification(Long idUser, NotificationType type, String title, String message) {
        log.info("Envoi de notification pour user id={}", idUser);
        Notification notification = createNotification(idUser, type, title, message);

        // If needed, add expiration for certain types
        if (notification.isUrgent()) {
            notification.setExpirationDate(LocalDateTime.now().plusDays(3));
        }

        // Persist any changes
        Notification saved = notificationRepository.save(notification);

        // Simulate sending: in real setup, push to WebSocket / FCM / email would occur here
        log.info("Notification envoyée à user id={} notificationId={}", idUser, saved.getIdNotification());
        return saved;
    }

    @Override
    public Optional<Notification> getNotificationById(Long idNotification) {
        return notificationRepository.findById(idNotification);
    }

    @Override
    public List<Notification> getUserNotifications(Long idUser) {
        return notificationRepository.findByUserId(idUser);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long idUser) {
        return notificationRepository.findUnreadByUserId(idUser);
    }

    @Override
    public Notification markAsRead(Long idNotification) {
        Notification notification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new EntityNotFoundException("Notification introuvable"));
        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long idUser) {
        List<Notification> unread = notificationRepository.findUnreadByUserId(idUser);
        for (Notification n : unread) {
            n.markAsRead();
            notificationRepository.save(n);
        }
        log.info("Toutes les notifications marquées comme lues pour user id={}", idUser);
    }

    @Override
    public void deleteNotification(Long idNotification) {
        Notification notification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new EntityNotFoundException("Notification introuvable"));
        notificationRepository.delete(notification);
        log.info("Notification supprimée id={}", idNotification);
    }

    @Override
    public void deleteReadNotifications(Long idUser) {
        notificationRepository.deleteReadByUserId(idUser);
        log.info("Notifications lues supprimées pour user id={}", idUser);
    }

    @Override
    public Long countUnreadNotifications(Long idUser) {
        return notificationRepository.countUnreadByUserId(idUser);
    }

    
}