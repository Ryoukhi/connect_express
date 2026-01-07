package com.eadl.connect_backend.domain.model.notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void shouldCreateNotificationWithoutData() {
        Notification notification = Notification.create(
                10L,
                NotificationType.RESERVATION_CREATED,
                "Nouvelle réservation",
                "Une nouvelle réservation a été créée"
        );

        assertThat(notification.getIdNotification()).isNull();
        assertThat(notification.getIdUser()).isEqualTo(10L);
        assertThat(notification.getType()).isEqualTo(NotificationType.RESERVATION_CREATED);
        assertThat(notification.getTitle()).isNotBlank();
        assertThat(notification.getMessage()).isNotBlank();
        assertThat(notification.isRead()).isFalse();
        assertThat(notification.getCreatedAt()).isNotNull();
        assertThat(notification.getData()).isNull();
    }

    @Test
    void shouldCreateNotificationWithData() {
        Notification notification = Notification.create(
                10L,
                NotificationType.RESERVATION_ACCEPTED,
                "Réservation acceptée",
                "Votre réservation a été acceptée",
                "{ \"reservationId\": 55 }"
        );

        assertThat(notification.getData()).contains("reservationId");
    }

    @Test
    void shouldMarkNotificationAsRead() throws InterruptedException {
        Notification notification = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Nouveau message",
                "Vous avez reçu un nouveau message"
        );

        assertThat(notification.isRead()).isFalse();

        Thread.sleep(5);
        notification.markAsRead();

        assertThat(notification.isRead()).isTrue();
        assertThat(notification.getReadAt()).isNotNull();
    }

    @Test
    void shouldNotOverrideReadDateIfAlreadyRead() {
        Notification notification = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "Contenu"
        );

        notification.markAsRead();
        LocalDateTime firstReadAt = notification.getReadAt();

        notification.markAsRead();

        assertThat(notification.getReadAt()).isEqualTo(firstReadAt);
    }

    @Test
    void shouldMarkNotificationAsUnread() {
        Notification notification = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "Contenu"
        );

        notification.markAsRead();
        notification.markAsUnread();

        assertThat(notification.isRead()).isFalse();
        assertThat(notification.getReadAt()).isNull();
    }

    @Test
    void shouldDetectExpiredNotification() {
        Notification notification = Notification.create(
                10L,
                NotificationType.SYSTEM_UPDATE,
                "Mise à jour",
                "Mise à jour du système"
        );

        notification.setExpirationDate(LocalDateTime.now().minusMinutes(1));

        assertThat(notification.isExpired()).isTrue();
    }

    @Test
    void shouldNotBeExpiredIfNoExpirationDate() {
        Notification notification = Notification.create(
                10L,
                NotificationType.SYSTEM_UPDATE,
                "Mise à jour",
                "Mise à jour du système"
        );

        assertThat(notification.isExpired()).isFalse();
    }

    @Test
    void shouldSetActionUrl() {
        Notification notification = Notification.create(
                10L,
                NotificationType.RESERVATION_ACCEPTED,
                "Réservation",
                "Voir les détails"
        );

        notification.setActionUrl("https://app/reservations/10");

        assertThat(notification.hasAction()).isTrue();
        assertThat(notification.getActionUrl()).contains("reservations");
    }

    @Test
    void shouldDetectUrgentNotification() {
        Notification notification = Notification.create(
                10L,
                NotificationType.PAYMENT_FAILED,
                "Paiement échoué",
                "Votre paiement a échoué"
        );

        assertThat(notification.isUrgent()).isTrue();
    }

    @Test
    void shouldDetectNonUrgentNotification() {
        Notification notification = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "Nouveau message"
        );

        assertThat(notification.isUrgent()).isFalse();
    }

    @Test
    void shouldDetectSystemNotification() {
        Notification notification = Notification.create(
                10L,
                NotificationType.SYSTEM_MAINTENANCE,
                "Maintenance",
                "Maintenance système programmée"
        );

        assertThat(notification.isSystemNotification()).isTrue();
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        Notification n1 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "A"
        );

        Notification n2 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "B"
        );

        n1.setIdNotification(100L);
        n2.setIdNotification(100L);

        assertThat(n1).isEqualTo(n2);
        assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdDifferent() {
        Notification n1 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "A"
        );

        Notification n2 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "A"
        );

        n1.setIdNotification(1L);
        n2.setIdNotification(2L);

        assertThat(n1).isNotEqualTo(n2);
    }

    @Test
    void shouldNotBeEqualIfIdNull() {
        Notification n1 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "A"
        );

        Notification n2 = Notification.create(
                10L,
                NotificationType.NEW_MESSAGE,
                "Message",
                "A"
        );

        assertThat(n1).isNotEqualTo(n2);
    }
}
