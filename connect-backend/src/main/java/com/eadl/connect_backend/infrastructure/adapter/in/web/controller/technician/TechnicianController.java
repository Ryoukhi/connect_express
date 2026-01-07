package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.TechnicianProfileResponseDto;
import com.eadl.connect_backend.application.dto.TechnicianSearchDto;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
@Tag(name = "Techniciens", description = "Gestion des techniciens")
public class TechnicianController {

    private final TechnicianService technicianService;
    private final com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchService technicianSearchService;
    private final com.eadl.connect_backend.application.mapper.TechnicianProfileMapper technicianProfileMapper;

    /* =========================
       = CRÉATION / INSCRIPTION =
       ========================= */
    @Operation(summary = "Inscrire un technicien (TECHNICIAN)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Technicien créé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Technician.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping
    public ResponseEntity<Technician> registerTechnician(
            @Parameter(description = "Données du technicien") @RequestBody @Valid Technician technician
    ) {
        Technician saved = technicianService.registerTechnician(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* =========================
       = VALIDATION KYC (ADMIN) =
       ========================= */
    @Operation(summary = "Valider le KYC d'un technicien (ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "KYC validé"),
            @ApiResponse(responseCode = "404", description = "Technicien non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{technicianId}/validate-kyc")
    public ResponseEntity<Void> validateKyc(
            @Parameter(description = "ID du technicien") @PathVariable Long technicianId
    ) {
        technicianService.validateKyc(technicianId);
        return ResponseEntity.noContent().build();
    }

    /* =========================
       = CONSULTATION PUBLIQUE =
       ========================= */
    @Operation(summary = "Récupérer tous les techniciens actifs (ADMIN, CLIENT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des techniciens actifs"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/active")
    public ResponseEntity<List<Technician>> getActiveTechnicians() {
        return ResponseEntity.ok(technicianService.getActiveTechnicians());
    }

    @Operation(summary = "Récupérer un technicien par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Technicien trouvé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Technician.class))),
            @ApiResponse(responseCode = "404", description = "Technicien non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'TECHNICIAN')")
    @GetMapping("/{technicianId}")
    public ResponseEntity<Technician> getTechnicianById(
            @Parameter(description = "ID du technicien") @PathVariable Long technicianId
    ) {
        return technicianService.getTechnicianById(technicianId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =========================
       = FILTRAGE / RECHERCHE =
       ========================= */
    @Operation(summary = "Rechercher des techniciens par ville")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des techniciens filtrés"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/search/city")
    public ResponseEntity<List<Technician>> getByCity(
            @Parameter(description = "Nom de la ville") @RequestParam String city
    ) {
        return ResponseEntity.ok(technicianService.getTechniciansByCity(city));
    }

    @Operation(summary = "Rechercher des techniciens par quartier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des techniciens filtrés"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/search/neighborhood")
    public ResponseEntity<List<Technician>> getByNeighborhood(
            @Parameter(description = "Nom du quartier") @RequestParam String neighborhood
    ) {
        return ResponseEntity.ok(technicianService.getTechniciansByNeighborhood(neighborhood));
    }

    /* =========================
       = RECHERCHE MULTI-CRITÈRES =
       ========================= */
    @Operation(summary = "Rechercher des techniciens (multi-critères)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des techniciens filtrés"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/search")
    public ResponseEntity<List<TechnicianProfileResponseDto>> searchTechnicians(
            @Parameter(description = "Critères de recherche (optionnels)") @ModelAttribute TechnicianSearchDto dto
    ) {
        com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchCriteria criteria =
                new com.eadl.connect_backend.domain.port.in.technician.TechnicianSearchCriteria(
                        dto.getCity(),
                        dto.getNeighborhood(),
                        dto.getCategoryName(),
                        dto.getAvailabilityStatus(),
                        dto.getMinRating(),
                        dto.getMinPrice(),
                        dto.getMaxPrice()
                );

        List<TechnicianProfile> profiles =
                technicianSearchService.search(criteria);

        List<com.eadl.connect_backend.application.dto.TechnicianProfileResponseDto> response =
                profiles.stream().map(technicianProfileMapper::toDto).toList();

        return ResponseEntity.ok(response);
    }

    /* =========================
       = STATISTIQUES (ADMIN) =
       ========================= */
    @Operation(summary = "Compter les techniciens actifs (ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre de techniciens actifs"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/active/count")
    public ResponseEntity<Long> countActiveTechnicians() {
        return ResponseEntity.ok(technicianService.countActiveTechnicians());
    }
}
