package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.domain.model.reservation.Reservation;

public class ReservationMapper {

    private ReservationMapper() {
        // Utility class
    }

    public static ReservationDto toDto(Reservation reservation) {
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
        dto.setAddress(reservation.getAddress());
        dto.setDescription(reservation.getDescription());
        dto.setCancellationReason(reservation.getCancellationReason());

        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        dto.setCompletedAt(reservation.getCompletedAt());

        return dto;
    }

    public static List<ReservationDto> toDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
    }
}