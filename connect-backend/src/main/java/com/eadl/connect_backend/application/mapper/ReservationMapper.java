package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.domain.model.reservation.Reservation;

@Component
public class ReservationMapper {

    private ReservationMapper() {
        // Utility class
    }

    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) return null;

        ReservationDto dto = new ReservationDto();
        dto.setIdReservation(reservation.getIdReservation());
        dto.setIdClient(reservation.getIdClient());
        dto.setIdTechnician(reservation.getIdTechnician());
        dto.setIdReview(reservation.getIdReview());

        dto.setDateRequested(reservation.getDateRequested());
        dto.setScheduledTime(reservation.getScheduledTime());
        dto.setStatus(reservation.getStatus());

        dto.setPrice(reservation.getPrice());
        dto.setCity(reservation.getCity());
        dto.setNeighborhood(reservation.getNeighborhood());
        dto.setDescription(reservation.getDescription());
        dto.setCancellationReason(reservation.getCancellationReason());

        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        dto.setCompletedAt(reservation.getCompletedAt());

        return dto;
    }

    public Reservation toModel(ReservationDto dto) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setIdReservation(dto.getIdReservation());
        reservation.setIdClient(dto.getIdClient());
        reservation.setIdTechnician(dto.getIdTechnician());
        reservation.setIdReview(dto.getIdReview());

        reservation.setDateRequested(dto.getDateRequested());
        reservation.setScheduledTime(dto.getScheduledTime());
        reservation.setStatus(dto.getStatus());

        reservation.setPrice(dto.getPrice());
        reservation.setCity(dto.getCity());
        reservation.setNeighborhood(dto.getNeighborhood());
        reservation.setDescription(dto.getDescription());
        reservation.setCancellationReason(dto.getCancellationReason());

        reservation.setCreatedAt(dto.getCreatedAt());
        reservation.setUpdatedAt(dto.getUpdatedAt());
        reservation.setCompletedAt(dto.getCompletedAt());

        return reservation;
    }

    public List<ReservationDto> toDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Reservation> toModelList(List<ReservationDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}