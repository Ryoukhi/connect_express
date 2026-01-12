package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;

/**
 * Mapper pour convertir entre Reservation (domaine) et ReservationEntity (JPA)
 */
@Component
public class ReservationEntityMapper {

    /* =========================
       Entity -> Domain
       ========================= */
    public Reservation toModel(ReservationEntity entity) {
        if (entity == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setIdReservation(entity.getIdReservation());
        reservation.setDateRequested(entity.getDateRequested());
        reservation.setScheduledTime(entity.getScheduledTime());
        reservation.setStatus(entity.getStatus());
        reservation.setPrice(entity.getPrice());
        reservation.setCity(entity.getCity());
        reservation.setNeighborhood(entity.getNeighborhood());
        reservation.setDescription(entity.getDescription());
        reservation.setCancellationReason(entity.getCancellationReason());
        reservation.setCompletedAt(entity.getCompletedAt());
        reservation.setCreatedAt(entity.getCreatedAt());
        reservation.setUpdatedAt(entity.getUpdatedAt());

        // Relations (ID only)
        reservation.setIdClient(
                entity.getClient() != null ? entity.getClient().getIdUser() : null
        );

        reservation.setIdTechnician(
                entity.getTechnician() != null ? entity.getTechnician().getIdUser() : null
        );

        reservation.setIdReview(
                entity.getReview() != null ? entity.getReview().getIdReview() : null
        );

        return reservation;
    }

    /* =========================
       Domain -> Entity (CREATE)
       ========================= */
    public ReservationEntity toEntity(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationEntity entity = new ReservationEntity();
        entity.setIdReservation(reservation.getIdReservation());
        entity.setDateRequested(reservation.getDateRequested());
        entity.setScheduledTime(reservation.getScheduledTime());
        entity.setStatus(reservation.getStatus());
        entity.setPrice(reservation.getPrice());
        entity.setCity(reservation.getCity());
        entity.setNeighborhood(reservation.getNeighborhood());
        entity.setDescription(reservation.getDescription());
        entity.setCancellationReason(reservation.getCancellationReason());
        entity.setCompletedAt(reservation.getCompletedAt());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedAt(reservation.getUpdatedAt());

        // Client
        if (reservation.getIdClient() != null) {
            UserEntity client = new UserEntity();
            client.setIdUser(reservation.getIdClient());
            entity.setClient(client);
        }

        // Technician
        if (reservation.getIdTechnician() != null) {
            UserEntity technician = new UserEntity();
            technician.setIdUser(reservation.getIdTechnician());
            entity.setTechnician(technician);
        }

        // Review
        if (reservation.getIdReview() != null) {
            ReviewEntity review = new ReviewEntity();
            review.setIdReview(reservation.getIdReview());
            entity.setReview(review);
        }

        return entity;
    }

    /* =========================
       UPDATE EXISTING ENTITY
       ========================= */
    public void updateEntity(ReservationEntity entity, Reservation reservation) {

        if (reservation.getScheduledTime() != null) {
            entity.setScheduledTime(reservation.getScheduledTime());
        }

        if (reservation.getStatus() != null) {
            entity.setStatus(reservation.getStatus());
        }

        if (reservation.getPrice() != null) {
            entity.setPrice(reservation.getPrice());
        }

        if (reservation.getCity() != null) {
            entity.setCity(reservation.getCity());
        }

        if (reservation.getNeighborhood() != null) {
            entity.setNeighborhood(reservation.getNeighborhood());
        }

        if (reservation.getDescription() != null) {
            entity.setDescription(reservation.getDescription());
        }

        if (reservation.getCancellationReason() != null) {
            entity.setCancellationReason(reservation.getCancellationReason());
        }

        if (reservation.getCompletedAt() != null) {
            entity.setCompletedAt(reservation.getCompletedAt());
        }

        if (reservation.getIdReview() != null) {
            ReviewEntity review = new ReviewEntity();
            review.setIdReview(reservation.getIdReview());
            entity.setReview(review);
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
