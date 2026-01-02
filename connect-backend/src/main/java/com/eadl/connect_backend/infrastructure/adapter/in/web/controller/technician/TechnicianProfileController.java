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


@RestController
@RequestMapping("/api/technician-profiles")
@RequiredArgsConstructor
public class TechnicianProfileController {

    private final TechnicianProfileService technicianProfileService;
    private final TechnicianProfileMapper mapper;
    private final CurrentUserProvider currentUserProvider;

    /* ================= CREATE ================= */

    @PostMapping
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianProfileResponseDto> createProfile(
            @RequestBody @Valid TechnicianProfileCreateDto dto
    ) {
        TechnicianProfile profile = mapper.toEntity(dto);

        TechnicianProfile created =
                technicianProfileService.createProfile(profile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(created));
    }

    /* ================= READ ================= */

    @GetMapping("/technician/{technicianId}")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','TECHNICIAN')")
    public ResponseEntity<TechnicianProfileResponseDto> getProfile(
            @PathVariable Long technicianId
    ) {
        return technicianProfileService
                .getProfileByTechnicianId(technicianId)
                .map(p -> ResponseEntity.ok(mapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianProfileResponseDto> getMyProfile() {

        Long technicianId = currentUserProvider.getCurrentUserId();

        return technicianProfileService
                .getProfileByTechnicianId(technicianId)
                .map(p -> ResponseEntity.ok(mapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /* ================= UPDATE ================= */

    @PutMapping
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianProfileResponseDto> updateProfile(
            @RequestBody @Valid TechnicianProfileUpdateDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();

        TechnicianProfile existing =
                technicianProfileService
                        .getProfileByTechnicianId(technicianId)
                        .orElseThrow(() ->
                                new BusinessException("Profile not found") {});

        mapper.updateEntity(dto, existing);

        TechnicianProfile updated =
                technicianProfileService.updateProfile(existing);

        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /* ================= AVAILABILITY ================= */

    @PutMapping("/availability")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianProfileResponseDto> updateAvailability(
            @RequestParam AvailabilityStatus status
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok(
                mapper.toDto(
                        technicianProfileService
                                .updateAvailability(technicianId, status)
                )
        );
    }

    /* ================= ADMIN ================= */

    @PutMapping("/{technicianId}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> validateProfile(
            @PathVariable Long technicianId
    ) {
        technicianProfileService.validateProfile(technicianId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TechnicianProfileResponseDto> getPendingProfiles() {
        return technicianProfileService.getPendingProfiles()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}