package com.eadl.connect_backend.domain.model.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private Reservation createPendingReservation() {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setPrice(new BigDecimal("15000"));
        reservation.setScheduledTime(LocalDateTime.now().plusDays(1));
        return reservation;
    }

    // ================= ACCEPT / REJECT =================

    @Test
    @DisplayName("Accepter une réservation PENDING")
    void shouldAcceptPendingReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // WHEN
        reservation.accept();

        // THEN
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.ACCEPTED);
        assertThat(reservation.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Refuser une réservation PENDING")
    void shouldRejectPendingReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // WHEN
        reservation.reject();

        // THEN
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.REJECTED);
        assertThat(reservation.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Impossible d'accepter une réservation non PENDING")
    void shouldFailWhenAcceptingNonPendingReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();

        // THEN
        assertThatThrownBy(reservation::accept)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("en attente");
    }

    // ================= ROUTE / WORK =================

    @Test
    @DisplayName("Démarrer la route après acceptation")
    void shouldStartRouteAfterAccept() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();

        // WHEN
        reservation.startRoute();

        // THEN
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.EN_ROUTE);
    }

    @Test
    @DisplayName("Démarrer le travail depuis ACCEPTED ou EN_ROUTE")
    void shouldStartWorkFromAcceptedOrEnRoute() {
        // ACCEPTED
        Reservation r1 = createPendingReservation();
        r1.accept();
        r1.startWork();
        assertThat(r1.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);

        // EN_ROUTE
        Reservation r2 = createPendingReservation();
        r2.accept();
        r2.startRoute();
        r2.startWork();
        assertThat(r2.getStatus()).isEqualTo(ReservationStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Impossible de démarrer le travail depuis un statut invalide")
    void shouldFailWhenStartingWorkFromInvalidStatus() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // THEN
        assertThatThrownBy(reservation::startWork)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Statut invalide");
    }

    // ================= COMPLETE =================

    @Test
    @DisplayName("Compléter une réservation IN_PROGRESS")
    void shouldCompleteReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();
        reservation.startWork();

        // WHEN
        reservation.complete();

        // THEN
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(reservation.getCompletedAt()).isNotNull();
        assertThat(reservation.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Impossible de compléter une réservation non IN_PROGRESS")
    void shouldFailWhenCompletingInvalidReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // THEN
        assertThatThrownBy(reservation::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("en cours");
    }

    // ================= CANCEL =================

    @Test
    @DisplayName("Annuler une réservation avec une raison")
    void shouldCancelReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // WHEN
        reservation.cancel("Client indisponible");

        // THEN
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(reservation.getCancellationReason()).isEqualTo("Client indisponible");
        assertThat(reservation.canBeCancelled()).isFalse();
    }

    @Test
    @DisplayName("Impossible d'annuler une réservation COMPLETED")
    void shouldFailWhenCancellingCompletedReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();
        reservation.startWork();
        reservation.complete();

        // THEN
        assertThatThrownBy(() -> reservation.cancel("Erreur"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ne peut pas être annulée");
    }

    // ================= RESCHEDULE =================

    @Test
    @DisplayName("Replanifier une réservation autorisée")
    void shouldRescheduleReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        LocalDateTime newDate = LocalDateTime.now().plusDays(3);

        // WHEN
        reservation.reschedule(newDate);

        // THEN
        assertThat(reservation.getScheduledTime()).isEqualTo(newDate);
        assertThat(reservation.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Impossible de replanifier une réservation IN_PROGRESS")
    void shouldFailWhenReschedulingInProgressReservation() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();
        reservation.startWork();

        // THEN
        assertThatThrownBy(() -> reservation.reschedule(LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ne peut pas être replanifiée");
    }

    // ================= PRICE =================

    @Test
    @DisplayName("Modifier le prix avant le début de l'intervention")
    void shouldUpdatePriceBeforeWorkStarts() {
        // GIVEN
        Reservation reservation = createPendingReservation();

        // WHEN
        reservation.updatePrice(new BigDecimal("20000"));

        // THEN
        assertThat(reservation.getPrice()).isEqualByComparingTo("20000");
    }

    @Test
    @DisplayName("Impossible de modifier le prix après le début de l'intervention")
    void shouldFailWhenUpdatingPriceAfterStart() {
        // GIVEN
        Reservation reservation = createPendingReservation();
        reservation.accept();
        reservation.startWork();

        // THEN
        assertThatThrownBy(() -> reservation.updatePrice(new BigDecimal("25000")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("prix ne peut être modifié");
    }

    // ================= STATE HELPERS =================

    @Test
    @DisplayName("isActive retourne true pour ACCEPTED, EN_ROUTE, IN_PROGRESS")
    void shouldReturnTrueWhenReservationIsActive() {
        Reservation r1 = createPendingReservation();
        r1.accept();
        assertThat(r1.isActive()).isTrue();

        Reservation r2 = createPendingReservation();
        r2.accept();
        r2.startRoute();
        assertThat(r2.isActive()).isTrue();

        Reservation r3 = createPendingReservation();
        r3.accept();
        r3.startWork();
        assertThat(r3.isActive()).isTrue();
    }

    @Test
    @DisplayName("isPending retourne true uniquement pour PENDING")
    void shouldReturnTrueWhenPending() {
        Reservation reservation = createPendingReservation();
        assertThat(reservation.isPending()).isTrue();
    }
}
