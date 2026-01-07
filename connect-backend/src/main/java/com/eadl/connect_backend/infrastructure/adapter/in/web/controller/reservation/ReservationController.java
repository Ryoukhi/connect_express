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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion des réservations pour clients, techniciens et admin")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final CurrentUserProvider currentUserProvider;

    // ================== CREATE ==================
    @Operation(summary = "Créer une réservation (CLIENT)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto dto) {
        Reservation reservation = reservationMapper.toModel(dto);
        Reservation created = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toDto(created));
    }

    // ================== READ ==================
    @Operation(summary = "Récupérer une réservation par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getById(@Parameter(description = "ID de la réservation") @PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Réservations du client connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des réservations"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/client")
    public ResponseEntity<List<ReservationDto>> getMyClientReservations() {
        Long clientId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(
                reservationService.getClientReservations(clientId).stream().map(reservationMapper::toDto).toList()
        );
    }

    @Operation(summary = "Réservations du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des réservations"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @GetMapping("/me/technician")
    public ResponseEntity<List<ReservationDto>> getMyTechnicianReservations() {
        Long technicianId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(
                reservationService.getTechnicianReservations(technicianId).stream().map(reservationMapper::toDto).toList()
        );
    }

    // ================== UPDATE ==================
    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Long id,
            @RequestBody ReservationDto dto
    ) {
        Reservation updated = reservationMapper.toModel(dto);
        Reservation result = reservationService.updateReservation(id, updated);
        return ResponseEntity.ok(reservationMapper.toDto(result));
    }

    @Operation(summary = "Annuler une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation annulée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('CLIENT','TECHNICIAN','ADMIN')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Long id,
            @Parameter(description = "Raison de l'annulation") @RequestParam(required = false) String reason
    ) {
        Long userId = currentUserProvider.getCurrentUserId();
        Reservation cancelled = reservationService.cancelReservation(id, userId, reason);
        return ResponseEntity.ok(reservationMapper.toDto(cancelled));
    }

    @Operation(summary = "Changer le statut d’une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    @PostMapping("/{id}/status")
    public ResponseEntity<ReservationDto> changeStatus(
            @Parameter(description = "ID de la réservation") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam ReservationStatus status
    ) {
        Reservation updated = reservationService.changeStatus(id, status);
        return ResponseEntity.ok(reservationMapper.toDto(updated));
    }

    @Operation(summary = "Marquer une réservation comme complétée")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation complétée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping("/{id}/complete")
    public ResponseEntity<ReservationDto> completeReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Long id
    ) {
        Reservation completed = reservationService.completeReservation(id);
        return ResponseEntity.ok(reservationMapper.toDto(completed));
    }

    // ================== AVAILABILITY ==================
    @Operation(summary = "Vérifier la disponibilité d’un technicien")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilité renvoyée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @Parameter(description = "ID du technicien") @RequestParam Long technicianId,
            @Parameter(description = "Début de la période") @RequestParam LocalDateTime start,
            @Parameter(description = "Fin de la période") @RequestParam LocalDateTime end
    ) {
        return ResponseEntity.ok(reservationService.isTechnicianAvailable(technicianId, start, end));
    }

    // ================== ADMIN ==================
    @Operation(summary = "Supprimer une réservation (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@Parameter(description = "ID de la réservation") @PathVariable Long id) {
        Long adminId = currentUserProvider.getCurrentUserId();
        reservationService.deleteReservation(id, adminId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Compter toutes les réservations (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre total de réservations"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countReservations() {
        return ResponseEntity.ok(reservationService.countReservations());
    }
}
