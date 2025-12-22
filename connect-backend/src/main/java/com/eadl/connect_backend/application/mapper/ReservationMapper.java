package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.reservation.ReservationResponse;
import com.eadl.connect_backend.domain.model.reservation.Reservation;

/**
 * Mapper utilitaire pour les réservations
 */
public class ReservationMapper {

    public static ReservationResponse toResponse(Reservation reservation) {
        return toResponse(reservation, null, null);
    }

    public static ReservationResponse toResponse(Reservation reservation, String clientName, String technicianName) {
        if (reservation == null) return null;
        ReservationResponse dto = new ReservationResponse();
        dto.setIdReservation(reservation.getIdReservation());
        dto.setIdClient(reservation.getIdClient());
        dto.setClientName(clientName);
        dto.setIdTechnician(reservation.getIdTechnician());
        dto.setTechnicianName(technicianName);
        dto.setDateRequested(reservation.getDateRequested());
        dto.setScheduledTime(reservation.getScheduledTime());
        dto.setStatus(reservation.getStatus() != null ? reservation.getStatus().name() : null);
        dto.setPrice(reservation.getPrice());
        dto.setAddress(reservation.getAddress());
        dto.setDescription(reservation.getDescription());
        dto.setCancellationReason(reservation.getCancellationReason());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        dto.setCompletedAt(reservation.getCompletedAt());
        return dto;
    }

    public static List<ReservationResponse> toResponses(List<Reservation> reservations) {
        if (reservations == null) return null;
        return reservations.stream().map(ReservationMapper::toResponse).collect(Collectors.toList());
    }
}
