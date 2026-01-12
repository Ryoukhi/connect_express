package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.ReservationEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ReservationJpaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Adapter pour le port sortant ReservationRepository
 * Connecte le domaine aux repositories JPA (Infrastructure)
 */
@Repository
@RequiredArgsConstructor
@Transactional
public class ReservationRepositoryAdapter implements ReservationRepository {

    private static final Logger log = LoggerFactory.getLogger(ReservationRepositoryAdapter.class);

    private final ReservationJpaRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    @Override
    public Reservation save(Reservation reservation) {
        log.info("Saving reservation: {}", reservation);

        ReservationEntity entity = mapper.toEntity(reservation);
        ReservationEntity saved = jpaRepository.save(entity);
        Reservation savedReservation = mapper.toModel(saved);

        log.info("Saved reservation: {}", savedReservation);
        return savedReservation;
    }

    @Override
    public Reservation update(Long idReservation, Reservation reservation) {
        log.info("Updating reservation id {} with data: {}", idReservation, reservation);

        ReservationEntity existing = jpaRepository.findById(idReservation)
                .orElseThrow(() -> {
                    log.error("Reservation not found with id {}", idReservation);
                    return new IllegalArgumentException("Reservation not found with id " + idReservation);
                });

        mapper.updateEntity(existing, reservation);
        ReservationEntity updated = jpaRepository.save(existing);
        Reservation updatedReservation = mapper.toModel(updated);

        log.info("Updated reservation: {}", updatedReservation);
        return updatedReservation;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long idReservation) {
        log.info("Finding reservation by id: {}", idReservation);

        Optional<Reservation> reservation = jpaRepository.findById(idReservation)
                .map(mapper::toModel);

        reservation.ifPresentOrElse(
                r -> log.info("Found reservation: {}", r),
                () -> log.warn("No reservation found with id: {}", idReservation)
        );

        return reservation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByClientId(Long idClient) {
        log.info("Finding reservations by client id: {}", idClient);

        List<Reservation> reservations = jpaRepository.findByClient_IdUser(idClient)
                .stream()
                .map(mapper::toModel)
                .toList();

        log.info("Found {} reservations for client id {}", reservations.size(), idClient);
        return reservations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByTechnicianId(Long idTechnician) {
        log.info("Finding reservations by technician id: {}", idTechnician);

        List<Reservation> reservations = jpaRepository.findByTechnician_IdUser(idTechnician)
                .stream()
                .map(mapper::toModel)
                .toList();

        log.info("Found {} reservations for technician id {}", reservations.size(), idTechnician);
        return reservations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(ReservationStatus status) {
        log.info("Finding reservations by status: {}", status);

        List<Reservation> reservations = jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toModel)
                .toList();

        log.info("Found {} reservations with status {}", reservations.size(), status);
        return reservations;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsTechnicianReservationBetween(Long idTechnician, LocalDateTime start, LocalDateTime end) {
        log.info("Checking if technician {} has reservation between {} and {}", idTechnician, start, end);

        boolean exists = jpaRepository.existsByTechnician_IdUserAndScheduledTimeBetween(idTechnician, end, start);

        log.info("Technician {} has reservation in range: {}", idTechnician, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        log.info("Counting all reservations");
        Long total = jpaRepository.count();
        log.info("Total reservations count: {}", total);
        return total;
    }

    @Override
    public void delete(Reservation reservation) {
        log.info("Deleting reservation: {}", reservation);

        ReservationEntity entity = mapper.toEntity(reservation);
        jpaRepository.delete(entity);

        log.info("Deleted reservation: {}", reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByIdTechnicianAndStatus(Long technicianId, ReservationStatus status) {
        log.info("Counting reservations for technician {} with status {}", technicianId, status);
        long count = jpaRepository.countByTechnician_IdUserAndStatus(technicianId, status);
        log.info("Found {} reservations", count);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findReviewIdsByIdTechnicianAndStatus(Long technicianId, ReservationStatus status) {
        log.info("Finding review IDs for technician {} with status {}", technicianId, status);

        List<Long> reviewIds = jpaRepository.findReviewIdsByTechnicianAndStatus(technicianId, status);

        log.info("Found {} review IDs", reviewIds.size());
        return reviewIds;
    }

    @Override
    public long countByStatusAndScheduledTimeBetween(ReservationStatus status, LocalDateTime start, LocalDateTime end) {
        log.info("Counting reservations with status {} between {} and {}", status, start, end);
        long count = jpaRepository.countByStatusAndScheduledTimeBetween(status, start, end);
        log.info("Found {} reservations", count);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(ReservationStatus status) {
        log.info("Counting reservations with status {}", status);
        long count = jpaRepository.countByStatus(status);
        log.info("Found {} reservations with status {}", count, status);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public java.math.BigDecimal sumPriceByTechnicianIdAndStatus(Long technicianId, ReservationStatus status) {
        log.info("Summing price for technician {} with status {}", technicianId, status);
        java.math.BigDecimal sum = jpaRepository.sumPriceByTechnicianAndStatus(technicianId, status);
        if (sum == null) {
            sum = java.math.BigDecimal.ZERO;
        }
        log.info("Sum for technician {}: {}", technicianId, sum);
        return sum;
    }

    @Override
    @Transactional(readOnly = true)
    public Double averageRatingByTechnicianIdAndStatus(Long technicianId, ReservationStatus status) {
        log.info("Calculating average rating for technician {} with status {}", technicianId, status);
        Double average = jpaRepository.averageRatingByTechnicianAndStatus(technicianId, status);
        if (average == null) {
            average = 0.0;
        }
        log.info("Average rating for technician {}: {}", technicianId, average);
        return average;
    }
}
