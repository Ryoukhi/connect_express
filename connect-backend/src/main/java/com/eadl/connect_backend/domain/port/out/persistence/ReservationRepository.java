package com.eadl.connect_backend.domain.port.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;

/**
 * Port OUT - Repository Reservation
 *
 * Contrat d'accès à la persistance des réservations.
 * Le domaine ne connaît pas l'implémentation (JPA, Mongo, etc.)
 */
public interface ReservationRepository {

    /**
     * Sauvegarde une réservation
     */
    Reservation save(Reservation reservation);

    /**
     * Met à jour une réservation existante
     */
    Reservation update(Long idReservation, Reservation reservation);

    /**
     * Récupère une réservation par son identifiant
     */
    Optional<Reservation> findById(Long idReservation);

    /**
     * Récupère toutes les réservations d'un client
     */
    List<Reservation> findByClientId(Long idClient);

    /**
     * Récupère toutes les réservations d'un technicien
     */
    List<Reservation> findByTechnicianId(Long idTechnician);

    /**
     * Récupère les réservations selon leur statut
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Vérifie si un technicien est déjà réservé sur un créneau donné
     */
    boolean existsTechnicianReservationBetween(
            Long idTechnician,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Compte le nombre total de réservations
     */
    Long count();

    /**
     * Supprime une réservation
     */
    void delete(Reservation reservation);

//     @Query("""
//         SELECT AVG(r.rating)
//         FROM Reservation r
//         WHERE r.idTechnician = :technicianId
//         AND r.status = 'COMPLETED'
//         AND r.rating IS NOT NULL
//     """)
//     BigDecimal calculateAverageRating(Long technicianId);

    long countByIdTechnicianAndStatus(
            Long technicianId,
            ReservationStatus status
    );

    long countByStatusAndScheduledTimeBetween(
            ReservationStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    BigDecimal sumPriceByTechnicianIdAndStatus(Long technicianId, ReservationStatus status);

    Double averageRatingByTechnicianIdAndStatus(Long technicianId, ReservationStatus status);

        long countByStatus(ReservationStatus status);

    List<Long> findReviewIdsByIdTechnicianAndStatus(
            Long technicianId,
            ReservationStatus status
    );
}