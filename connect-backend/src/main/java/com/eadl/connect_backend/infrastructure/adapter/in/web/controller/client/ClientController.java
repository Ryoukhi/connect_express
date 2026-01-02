package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eadl.connect_backend.application.dto.ClientDto;
import com.eadl.connect_backend.application.dto.ReservationDto;
import com.eadl.connect_backend.application.mapper.ClientMapper;
import com.eadl.connect_backend.application.mapper.ReservationMapper;
import com.eadl.connect_backend.domain.port.in.client.ClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final ReservationMapper reservationMapper;

    // ================== CLIENT ==================

    /**
     * Récupère les réservations du client connecté
     */
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

    /**
     * Récupère tous les clients actifs
     */
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

    /**
     * Récupère les clients par ville
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-city")
    public ResponseEntity<List<ClientDto>> getClientsByCity(
            @RequestParam String city
    ) {
        return ResponseEntity.ok(
                clientService.getClientsByCity(city)
                        .stream()
                        .map(clientMapper::toDto)
                        .toList()
        );
    }

    /**
     * Compte le nombre de clients actifs
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count-active")
    public ResponseEntity<Long> countActiveClients() {
        return ResponseEntity.ok(
                clientService.countActiveClients()
        );
    }
}