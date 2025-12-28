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
        return mapper.toDomain(saved);
    }

    @Override
    public Reservation update(Long idReservation, Reservation reservation) {
        Optional<ReservationEntity> optional = jpaRepository.findById(idReservation);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found with id: " + idReservation);
        }
        ReservationEntity entity = optional.get();

        // Mise à jour des champs modifiables
        entity.setScheduledTime(reservation.getScheduledTime());
        entity.setStatus(reservation.getStatus());
        entity.setPrice(reservation.getPrice());
        entity.setAddress(reservation.getAddress());
        entity.setDescription(reservation.getDescription());
        entity.setCancellationReason(reservation.getCancellationReason());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        entity.setCompletedAt(reservation.getCompletedAt());

        ReservationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long idReservation) {
        return jpaRepository.findById(idReservation).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByClientId(Long idClient) {
        return mapper.toDomains(jpaRepository.findByClientId(idClient));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByTechnicianId(Long idTechnician) {
        return mapper.toDomains(jpaRepository.findByTechnicianId(idTechnician));
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
    public boolean existsTechnicianReservationBetween(Long idTechnician, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.existsByTechnicianIdAndScheduledTimeBetween(idTechnician, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("ReservationStatus cannot be null");
        }
        List<ReservationEntity> entities = jpaRepository.findByStatus(status);
        return mapper.toDomains(entities);
    }

}