package com.eadl.connect_backend.domain.port.in.reservation;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * Port IN - ReservationService
 *
 * Cas d'utilisation liés au cycle de vie d'une réservation.
 * Interface exposée au monde extérieur (API, UI, etc.).
 */
public interface ReservationService {

    /**
     * Crée une nouvelle réservation
     */
    Reservation createReservation(Reservation reservation);

    /**
     * Récupère une réservation par son identifiant
     */
    Optional<Reservation> getReservationById(Long idReservation);

    /**
     * Récupère toutes les réservations d'un client
     */
    List<Reservation> getClientReservations(Long idClient);

    /**
     * Récupère toutes les réservations d'un technicien
     */
    List<Reservation> getTechnicianReservations(Long idTechnician);

    /**
     * Met à jour une réservation
     */
    Reservation updateReservation(Long idReservation, Reservation reservation);

    /**
     * Annule une réservation
     */
    Reservation cancelReservation(
            Long idReservation,
            Long cancelledByUserId,
            String reason
    );

    /**
     * Change le statut d'une réservation
     */
    Reservation changeStatus(
            Long idReservation,
            ReservationStatus newStatus
    );

    /**
     * Marque une réservation comme terminée
     */
    Reservation completeReservation(Long idReservation);

    /**
     * Vérifie la disponibilité d'un technicien sur un créneau
     */
    boolean isTechnicianAvailable(
            Long idTechnician,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Supprime une réservation (admin uniquement)
     */
    void deleteReservation(Long idReservation, Long idAdmin);

    /**
     * Compte le nombre total de réservations
     */
    Long countReservations();

        /**
         * Compte le nombre de réservations ACCEPTED programmées pour maintenant
         */
        Long countAcceptedNow();

        /**
         * Calcule le revenu total d'un technicien (somme des `price`) pour les réservations COMPLETED
         */
        BigDecimal getTechnicianRevenue(Long technicianId);

        /**
         * Calcule la moyenne des notes (rating) d'un technicien sur ses réservations COMPLETED
         * Retourne un Double entre 0 et 5, ou 0 si aucune réservation complétée n'existe
         */
        Double getTechnicianAverageRating(Long technicianId);

        /**
         * Compte le nombre de réservations avec le statut PENDING
         */
        Long countPendingReservations();
}