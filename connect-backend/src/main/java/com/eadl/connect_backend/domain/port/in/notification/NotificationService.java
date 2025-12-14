package com.eadl.connect_backend.domain.port.in.notification;

import com.eadl.connect_backend.domain.model.Notification;
import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour la gestion des notifications
 * Définit les opérations CRUD et les fonctionnalités de notification
 */
public interface NotificationService {

    // ========== CREATE ==========

    /**
     * Crée une nouvelle notification
     * @param notification L'objet notification à créer
     * @return La notification créée avec son ID généré
     */
    Notification createNotification(Notification notification);

    /**
     * Envoie une notification à un utilisateur
     * @param userId L'identifiant de l'utilisateur destinataire
     * @param title Le titre de la notification
     * @param message Le contenu de la notification
     * @param type Le type de notification (INFO, WARNING, SUCCESS, ERROR)
     * @return La notification créée et envoyée
     */
    Notification sendNotification(Long userId, String title, String message, String type);

    /**
     * Envoie une notification à plusieurs utilisateurs
     * @param userIds Liste des identifiants des utilisateurs destinataires
     * @param title Le titre de la notification
     * @param message Le contenu de la notification
     * @param type Le type de notification
     * @return Liste des notifications créées
     */
    List<Notification> sendBulkNotification(List<Long> userIds, String title, String message, String type);

    /**
     * Envoie une notification à tous les utilisateurs
     * @param title Le titre de la notification
     * @param message Le contenu de la notification
     * @param type Le type de notification
     * @return Liste des notifications créées
     */
    List<Notification> sendNotificationToAll(String title, String message, String type);

    /**
     * Envoie une notification avec un lien d'action
     * @param userId L'identifiant de l'utilisateur
     * @param title Le titre de la notification
     * @param message Le contenu de la notification
     * @param actionUrl L'URL de redirection au clic
     * @param type Le type de notification
     * @return La notification créée
     */
    Notification sendNotificationWithAction(Long userId, String title, String message, String actionUrl, String type);

    // ========== READ ==========

    /**
     * Récupère une notification par son identifiant
     * @param id L'identifiant de la notification
     * @return Optional contenant la notification si trouvée, sinon Optional vide
     */
    Optional<Notification> getNotificationById(Long id);

    /**
     * Récupère toutes les notifications d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des notifications de l'utilisateur, triées par date (plus récentes en premier)
     */
    List<Notification> getUserNotifications(Long userId);

    /**
     * Récupère les notifications non lues d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des notifications non lues
     */
    List<Notification> getUnreadNotifications(Long userId);

    /**
     * Récupère les notifications lues d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des notifications lues
     */
    List<Notification> getReadNotifications(Long userId);

    /**
     * Récupère les notifications par type pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param type Le type de notification à filtrer
     * @return Liste des notifications du type spécifié
     */
    List<Notification> getNotificationsByType(Long userId, String type);

    /**
     * Récupère les notifications récentes d'un utilisateur (dernières 24h)
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des notifications des dernières 24 heures
     */
    List<Notification> getRecentNotifications(Long userId);

    /**
     * Récupère les notifications avec pagination
     * @param userId L'identifiant de l'utilisateur
     * @param page Le numéro de page (commence à 0)
     * @param size Le nombre de notifications par page
     * @return Liste des notifications paginées
     */
    List<Notification> getUserNotifications