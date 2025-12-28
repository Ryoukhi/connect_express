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

/**
 * Implémentation des cas d'utilisation liés aux réservations.
 *
 * Couche Application :
 * - orchestre les règles métier
 * - ne dépend que des ports
 * - transactionnelle
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation createReservation(Reservation reservation) {

        validateReservationForCreation(reservation);

        boolean available = reservationRepository.existsTechnicianReservationBetween(
                reservation.getIdTechnician(),
                reservation.getScheduledTime(),
                reservation.getScheduledTime()
        );

        if (available) {
            throw new AvailabilityConflictException(
                    "Technician is not available at the requested time");
        }

        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long idReservation) {
        return reservationRepository.findById(idReservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getClientReservations(Long idClient) {
        return reservationRepository.findByClientId(idClient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getTechnicianReservations(Long idTechnician) {
        return reservationRepository.findByTechnicianId(idTechnician);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation updateReservation(Long idReservation, Reservation updated) {

        Reservation existing = getExistingReservation(idReservation);

        if (existing.getStatus() == ReservationStatus.CANCELLED ||
            existing.getStatus() == ReservationStatus.COMPLETED) {
            throw new BusinessException("Cannot update a finished or cancelled reservation");
        }

        updated.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.update(idReservation, updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation cancelReservation(Long idReservation, Long cancelledByUserId, String reason) {

        Reservation reservation = getExistingReservation(idReservation);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException("Reservation already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancellationReason(reason);
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.update(idReservation, reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation changeStatus(Long idReservation, ReservationStatus newStatus) {

        Reservation reservation = getExistingReservation(idReservation);
        reservation.setStatus(newStatus);
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.update(idReservation, reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation completeReservation(Long idReservation) {

        Reservation reservation = getExistingReservation(idReservation);

        if (reservation.getStatus() != ReservationStatus.ACCEPTED) {
            throw new BusinessException("Only accepted reservations can be completed");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setCompletedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.update(idReservation, reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isTechnicianAvailable(
            Long idTechnician,
            LocalDateTime start,
            LocalDateTime end) {

        return !reservationRepository.existsTechnicianReservationBetween(
                idTechnician, start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReservation(Long idReservation, Long idAdmin) {

        Reservation reservation = getExistingReservation(idReservation);
        reservationRepository.delete(reservation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long countReservations() {
        return reservationRepository.count();
    }

    /* =======================
       Méthodes privées
       ======================= */

    private Reservation getExistingReservation(Long idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + idReservation));
    }

    private void validateReservationForCreation(Reservation reservation) {

        if (reservation.getIdClient() == null ||
            reservation.getIdTechnician() == null ||
            reservation.getScheduledTime() == null) {
            throw new BusinessException("Invalid reservation data");
        }

        if (reservation.getScheduledTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Reservation time must be in the future");
        }
    }
}