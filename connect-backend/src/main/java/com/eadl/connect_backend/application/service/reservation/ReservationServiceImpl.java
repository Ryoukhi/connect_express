package com.eadl.connect_backend.application.service.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.exception.AvailabilityConflictException;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.exception.ReservationNotFoundException;
import com.eadl.connect_backend.domain.port.in.reservation.ReservationService;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implémentation des cas d'utilisation liés aux réservations.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        log.info("Création d'une nouvelle réservation pour clientId={} et technicianId={}",
                reservation.getIdClient(), reservation.getIdTechnician());

        validateReservationForCreation(reservation);

        boolean available = reservationRepository.existsTechnicianReservationBetween(
                reservation.getIdTechnician(),
                reservation.getScheduledTime(),
                reservation.getScheduledTime()
        );

        if (available) {
            log.error("Conflit de disponibilité pour le technicien id={} à {}",
                    reservation.getIdTechnician(), reservation.getScheduledTime());
            throw new AvailabilityConflictException("Technician is not available at the requested time");
        }

        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.save(reservation);
        log.info("Réservation créée avec succès id={}, status={}", saved.getId(), saved.getStatus());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long idReservation) {
        log.debug("Récupération de la réservation id={}", idReservation);
        return reservationRepository.findById(idReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getClientReservations(Long idClient) {
        log.debug("Récupération des réservations pour le client id={}", idClient);
        List<Reservation> reservations = reservationRepository.findByClientId(idClient);
        log.debug("Nombre de réservations récupérées pour le client id={}: {}", idClient, reservations.size());
        return reservations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getTechnicianReservations(Long idTechnician) {
        log.debug("Récupération des réservations pour le technicien id={}", idTechnician);
        List<Reservation> reservations = reservationRepository.findByTechnicianId(idTechnician);
        log.debug("Nombre de réservations récupérées pour le technicien id={}: {}", idTechnician, reservations.size());
        return reservations;
    }

    @Override
    public Reservation updateReservation(Long idReservation, Reservation updated) {
        log.info("Mise à jour de la réservation id={}", idReservation);

        Reservation existing = getExistingReservation(idReservation);

        if (existing.getStatus() == ReservationStatus.CANCELLED ||
                existing.getStatus() == ReservationStatus.COMPLETED) {
            log.error("Impossible de mettre à jour la réservation id={} : déjà terminée ou annulée", idReservation);
            throw new BusinessException("Cannot update a finished or cancelled reservation");
        }

        updated.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.update(idReservation, updated);
        log.info("Réservation id={} mise à jour avec succès, status={}", saved.getId(), saved.getStatus());
        return saved;
    }

    @Override
    public Reservation cancelReservation(Long idReservation, Long cancelledByUserId, String reason) {
        log.info("Annulation de la réservation id={} par utilisateur id={}, raison='{}'",
                idReservation, cancelledByUserId, reason);

        Reservation reservation = getExistingReservation(idReservation);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            log.error("Réservation id={} déjà annulée", idReservation);
            throw new BusinessException("Reservation already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancellationReason(reason);
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.update(idReservation, reservation);
        log.info("Réservation id={} annulée avec succès", saved.getId());
        return saved;
    }

    @Override
    public Reservation changeStatus(Long idReservation, ReservationStatus newStatus) {
        log.info("Changement de statut de la réservation id={} vers {}", idReservation, newStatus);

        Reservation reservation = getExistingReservation(idReservation);
        reservation.setStatus(newStatus);
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.update(idReservation, reservation);
        log.info("Statut de la réservation id={} mis à jour avec succès: {}", saved.getId(), saved.getStatus());
        return saved;
    }

    @Override
    public Reservation completeReservation(Long idReservation) {
        log.info("Marquer la réservation id={} comme complétée", idReservation);

        Reservation reservation = getExistingReservation(idReservation);

        if (reservation.getStatus() != ReservationStatus.ACCEPTED) {
            log.error("Impossible de compléter la réservation id={} : status actuel={}",
                    idReservation, reservation.getStatus());
            throw new BusinessException("Only accepted reservations can be completed");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setCompletedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.update(idReservation, reservation);
        log.info("Réservation id={} complétée avec succès", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTechnicianAvailable(Long idTechnician, LocalDateTime start, LocalDateTime end) {
        log.debug("Vérification disponibilité du technicien id={} entre {} et {}", idTechnician, start, end);
        boolean available = !reservationRepository.existsTechnicianReservationBetween(idTechnician, start, end);
        log.debug("Disponibilité du technicien id={} : {}", idTechnician, available);
        return available;
    }

    @Override
    public void deleteReservation(Long idReservation, Long idAdmin) {
        log.info("Suppression de la réservation id={} par l'administrateur id={}", idReservation, idAdmin);
        Reservation reservation = getExistingReservation(idReservation);
        reservationRepository.delete(reservation);
        log.info("Réservation id={} supprimée avec succès", idReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countReservations() {
        log.debug("Comptage total des réservations");
        Long count = reservationRepository.count();
        log.debug("Nombre total de réservations : {}", count);
        return count;
    }

    /* =======================
       Méthodes privées
       ======================= */

    private Reservation getExistingReservation(Long idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() -> {
                    log.error("Réservation introuvable pour id={}", idReservation);
                    return new ReservationNotFoundException("Reservation not found with id: " + idReservation);
                });
    }

    private void validateReservationForCreation(Reservation reservation) {
        if (reservation.getIdClient() == null ||
                reservation.getIdTechnician() == null ||
                reservation.getScheduledTime() == null) {
            log.error("Données invalides pour la création de réservation : {}", reservation);
            throw new BusinessException("Invalid reservation data");
        }

        if (reservation.getScheduledTime().isBefore(LocalDateTime.now())) {
            log.error("La réservation est programmée dans le passé : {}", reservation.getScheduledTime());
            throw new BusinessException("Reservation time must be in the future");
        }

        log.debug("Données de réservation valides pour création : {}", reservation);
    }
}
