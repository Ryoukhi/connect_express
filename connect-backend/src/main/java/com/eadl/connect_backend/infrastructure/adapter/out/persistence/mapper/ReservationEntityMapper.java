package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

/**
 * Mapper pour convertir entre Reservation (domaine) et ReservationEntity (JPA)
 */
@Component
public class ReservationEntityMapper {

    private final UserEntityMapper userEntityMapper;
	private final ReviewEntityMapper reviewEntityMapper;

    public ReservationEntityMapper(UserEntityMapper userEntityMapper, ReviewEntityMapper reviewEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.reviewEntityMapper = reviewEntityMapper;
    }

    /* ===========================
       Conversion Domaine → Entity
       =========================== */

    public ReservationEntity toEntity(Reservation reservation) {
        if (reservation == null) return null;

        ReservationEntity entity = new ReservationEntity();

        entity.setIdReservation(reservation.getIdReservation());
        entity.setClient(userEntityMapper.toEntityIdOnly(reservation.getIdClient()));
        entity.setTechnician(userEntityMapper.toEntityIdOnly(reservation.getIdTechnician()));
		entity.setReview(reviewEntityMapper.toEntityIdOnly(reservation.getIdReview()));
        entity.setScheduledTime(reservation.getScheduledTime());
        entity.setDateRequested(reservation.getDateRequested());
        entity.setStatus(reservation.getStatus() != null ? reservation.getStatus() : ReservationStatus.PENDING);
        entity.setPrice(reservation.getPrice());
        entity.setAddress(reservation.getAddress());
        entity.setDescription(reservation.getDescription());
        entity.setCancellationReason(reservation.getCancellationReason());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        entity.setCompletedAt(reservation.getCompletedAt());

        if (reservation.getIdReview() != null) {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setIdReview(reservation.getIdReview());
            entity.setReview(reviewEntity);
        }

        return entity;
    }

    /* ===========================
       Conversion Entity → Domaine
       =========================== */

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;

        Reservation reservation = new Reservation();

        reservation.setIdReservation(entity.getIdReservation());
        reservation.setIdClient(entity.getClient() != null ? entity.getClient().getIdUser() : null);
        reservation.setIdTechnician(entity.getTechnician() != null ? entity.getTechnician().getIdUser() : null);
        reservation.setScheduledTime(entity.getScheduledTime());
        reservation.setDateRequested(entity.getDateRequested());
        reservation.setStatus(entity.getStatus());
        reservation.setPrice(entity.getPrice());
        reservation.setAddress(entity.getAddress());
        reservation.setDescription(entity.getDescription());
        reservation.setCancellationReason(entity.getCancellationReason());
        reservation.setCreatedAt(entity.getCreatedAt());
        reservation.setUpdatedAt(entity.getUpdatedAt());
        reservation.setCompletedAt(entity.getCompletedAt());
        reservation.setIdReview(entity.getReview() != null ? entity.getReview().getIdReview() : null);

        return reservation;
    }

    /* ===========================
       Listes
       =========================== */

    public List<ReservationEntity> toEntities(List<Reservation> reservations) {
        if (reservations == null) return null;
        return reservations.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<Reservation> toDomains(List<ReservationEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }
}