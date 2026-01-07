package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.TechnicianProfileCreateDto;
import com.eadl.connect_backend.application.dto.TechnicianProfileResponseDto;
import com.eadl.connect_backend.application.dto.TechnicianProfileUpdateDto;
import com.eadl.connect_backend.application.mapper.TechnicianProfileMapper;
import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianProfileService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

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
@RequestMapping("/api/technician-profiles")
@RequiredArgsConstructor
@Tag(name = "Profils Technicien", description = "Gestion des profils des techniciens")
public class TechnicianProfileController {

    private final TechnicianProfileService technicianProfileService;
    private final TechnicianProfileMapper mapper;
    private final CurrentUserProvider currentUserProvider;

    /* ================= CREATE ================= */
    @Operation(summary = "Créer un profil technicien (TECHNICIAN)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profil créé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping
    public ResponseEntity<TechnicianProfileResponseDto> createProfile(
            @Parameter(description = "Données du profil") @RequestBody @Valid TechnicianProfileCreateDto dto
    ) {
        TechnicianProfile profile = mapper.toEntity(dto);
        TechnicianProfile created = technicianProfileService.createProfile(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
    }

    /* ================= READ ================= */
    @Operation(summary = "Récupérer le profil d'un technicien par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil trouvé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Profil non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','TECHNICIAN')")
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<TechnicianProfileResponseDto> getProfile(
            @Parameter(description = "ID du technicien") @PathVariable Long technicianId
    ) {
        return technicianProfileService
                .getProfileByTechnicianId(technicianId)
                .map(p -> ResponseEntity.ok(mapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Récupérer le profil du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil trouvé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Profil non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @GetMapping("/me")
    public ResponseEntity<TechnicianProfileResponseDto> getMyProfile() {
        Long technicianId = currentUserProvider.getCurrentUserId();
        return technicianProfileService
                .getProfileByTechnicianId(technicianId)
                .map(p -> ResponseEntity.ok(mapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /* ================= UPDATE ================= */
    @Operation(summary = "Mettre à jour le profil du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil mis à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Profil non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PutMapping
    public ResponseEntity<TechnicianProfileResponseDto> updateProfile(
            @Parameter(description = "Données du profil à mettre à jour") @RequestBody @Valid TechnicianProfileUpdateDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        TechnicianProfile existing = technicianProfileService
                .getProfileByTechnicianId(technicianId)
                .orElseThrow(() -> new BusinessException("Profile not found") {});
        mapper.updateEntity(dto, existing);
        TechnicianProfile updated = technicianProfileService.updateProfile(existing);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /* ================= AVAILABILITY ================= */
    @Operation(summary = "Mettre à jour le statut de disponibilité du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianProfileResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PutMapping("/availability")
    public ResponseEntity<TechnicianProfileResponseDto> updateAvailability(
            @Parameter(description = "Nouveau statut de disponibilité") @RequestParam AvailabilityStatus status
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(
                mapper.toDto(
                        technicianProfileService.updateAvailability(technicianId, status)
                )
        );
    }

    /* ================= ADMIN ================= */
    @Operation(summary = "Valider un profil technicien (ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profil validé"),
            @ApiResponse(responseCode = "404", description = "Profil non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{technicianId}/validate")
    public ResponseEntity<Void> validateProfile(
            @Parameter(description = "ID du technicien") @PathVariable Long technicianId
    ) {
        technicianProfileService.validateProfile(technicianId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer les profils en attente de validation (ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des profils en attente"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public List<TechnicianProfileResponseDto> getPendingProfiles() {
        return technicianProfileService.getPendingProfiles()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
