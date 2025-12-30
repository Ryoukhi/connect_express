package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
 * 
 * Connecte le domaine aux repositories JPA (Infrastructure)
 */
@Repository
@RequiredArgsConstructor
@Transactional
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = mapper.toEntity(reservation);
        ReservationEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Reservation update(Long idReservation, Reservation reservation) {
        ReservationEntity existing = jpaRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found with id " + idReservation));

        mapper.updateEntity(existing, reservation);
        ReservationEntity updated = jpaRepository.save(existing);
        return mapper.toModel(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long idReservation) {
        return jpaRepository.findById(idReservation)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByClientId(Long idClient) {
        return jpaRepository.findByClient_IdClient(idClient)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByTechnicianId(Long idTechnician) {
        return jpaRepository.findByTechnician_IdTechnician(idTechnician)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(ReservationStatus status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsTechnicianReservationBetween(
            Long idTechnician,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return jpaRepository
                .existsByTechnician_IdTechnicianAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        idTechnician,
                        end,
                        start
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public void delete(Reservation reservation) {
        ReservationEntity entity = mapper.toEntity(reservation);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByIdTechnicianAndStatus(Long technicianId, ReservationStatus status) {
        return jpaRepository.countByTechnician_IdTechnicianAndStatus(technicianId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findReviewIdsByIdTechnicianAndStatus(
            Long technicianId,
            ReservationStatus status
    ) {
        return jpaRepository.findReviewIdsByTechnicianAndStatus(technicianId, status);
    }
}
