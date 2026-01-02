package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.reservation;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.application.mapper.ReservationMapper;
import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.in.reservation.ReservationService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final CurrentUserProvider currentUserProvider;

    // ================== CREATE ==================

    /**
     * Créer une réservation (CLIENT)
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationDto dto
    ) {
        Reservation reservation = reservationMapper.toModel(dto);

        Reservation created = reservationService.createReservation(reservation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationMapper.toDto(created));
    }

    // ================== READ ==================

    /**
     * Récupérer une réservation par ID
     */
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getById(@PathVariable Long id) {

        return reservationService.getReservationById(id)
                .map(reservationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Réservations du client connecté
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/client")
    public ResponseEntity<List<ReservationDto>> getMyClientReservations() {

        Long clientId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok(
                reservationService.getClientReservations(clientId)
                        .stream()
                        .map(reservationMapper::toDto)
                        .toList()
        );
    }

    /**
     * Réservations du technicien connecté
     */
    @PreAuthorize("hasRole('TECHNICIAN')")
    @GetMapping("/me/technician")
    public ResponseEntity<List<ReservationDto>> getMyTechnicianReservations() {

        Long technicianId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok(
                reservationService.getTechnicianReservations(technicianId)
                        .stream()
                        .map(reservationMapper::toDto)
                        .toList()
        );
    }

    // ================== UPDATE ==================

    /**
     * Modifier une réservation
     */
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationDto dto
    ) {
        Reservation updated = reservationMapper.toModel(dto);

        Reservation result = reservationService.updateReservation(id, updated);

        return ResponseEntity.ok(reservationMapper.toDto(result));
    }

    /**
     * Annuler une réservation
     */
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN','ADMIN')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String reason
    ) {
        Long userId = currentUserProvider.getCurrentUserId();

        Reservation cancelled =
                reservationService.cancelReservation(id, userId, reason);

        return ResponseEntity.ok(reservationMapper.toDto(cancelled));
    }

    /**
     * Changer le statut d’une réservation (TECHNICIAN / ADMIN)
     */
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    @PostMapping("/{id}/status")
    public ResponseEntity<ReservationDto> changeStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status
    ) {
        Reservation updated =
                reservationService.changeStatus(id, status);

        return ResponseEntity.ok(reservationMapper.toDto(updated));
    }

/**
     * Marquer une réservation comme complétée
     */
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping("/{id}/complete")
    public ResponseEntity<ReservationDto> completeReservation(
            @PathVariable Long id
    ) {
        Reservation completed =
                reservationService.completeReservation(id);

        return ResponseEntity.ok(reservationMapper.toDto(completed));
    }

    // ================== AVAILABILITY ==================

    /**
     * Vérifier la disponibilité d’un technicien
     */
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long technicianId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        return ResponseEntity.ok(
                reservationService.isTechnicianAvailable(
                        technicianId, start, end)
        );
    }

    // ================== ADMIN ==================

    /**
     * Supprimer une réservation
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {

        Long adminId = currentUserProvider.getCurrentUserId();
        reservationService.deleteReservation(id, adminId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Compter toutes les réservations
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countReservations() {
        return ResponseEntity.ok(reservationService.countReservations());
    }
}