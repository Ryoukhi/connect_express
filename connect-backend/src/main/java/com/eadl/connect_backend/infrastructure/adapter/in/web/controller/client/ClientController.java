package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.ClientDto;
import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.application.mapper.ClientMapper;
import com.eadl.connect_backend.application.mapper.ReservationMapper;
import com.eadl.connect_backend.domain.port.in.client.ClientService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Endpoints pour la gestion des clients et leurs réservations")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final ReservationMapper reservationMapper;

    // ================== CLIENT ==================

    @Operation(summary = "Récupère les réservations du client connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des réservations du client",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/reservations")
    public ResponseEntity<List<ReservationDto>> getMyReservations() {
        return ResponseEntity.ok(
                clientService.getClientReservations()
                        .stream()
                        .map(reservationMapper::toDto)
                        .toList()
        );
    }

    // ================== ADMIN ==================

    @Operation(summary = "Récupère tous les clients actifs (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des clients actifs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<ClientDto>> getActiveClients() {
        return ResponseEntity.ok(
                clientService.getActiveClients()
                        .stream()
                        .map(clientMapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Récupère les clients par ville (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des clients dans la ville spécifiée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-city")
    public ResponseEntity<List<ClientDto>> getClientsByCity(
            @Parameter(description = "Nom de la ville") @RequestParam String city
    ) {
        return ResponseEntity.ok(
                clientService.getClientsByCity(city)
                        .stream()
                        .map(clientMapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Compte le nombre de clients actifs (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre de clients actifs"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count-active")
    public ResponseEntity<Long> countActiveClients() {
        return ResponseEntity.ok(clientService.countActiveClients());
    }
}
