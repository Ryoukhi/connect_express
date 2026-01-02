package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    /* =========================
       = CRÉATION / INSCRIPTION =
       ========================= */

    @PostMapping
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<Technician> registerTechnician(
            @RequestBody @Valid Technician technician
    ) {
        Technician saved = technicianService.registerTechnician(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* =========================
       = VALIDATION KYC (ADMIN) =
       ========================= */

    @PutMapping("/{technicianId}/validate-kyc")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> validateKyc(
            @PathVariable Long technicianId
    ) {
        technicianService.validateKyc(technicianId);
        return ResponseEntity.noContent().build();
    }

    /* =========================
       = CONSULTATION PUBLIQUE =
       ========================= */

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<Technician>> getActiveTechnicians() {
        return ResponseEntity.ok(technicianService.getActiveTechnicians());
    }

    @GetMapping("/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'TECHNICIAN')")
    public ResponseEntity<Technician> getTechnicianById(
            @PathVariable Long technicianId
    ) {
        return technicianService.getTechnicianById(technicianId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =========================
       = FILTRAGE / RECHERCHE =
       ========================= */

    @GetMapping("/search/city")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<Technician>> getByCity(
            @RequestParam String city
    ) {
        return ResponseEntity.ok(
                technicianService.getTechniciansByCity(city)
        );
    }

    @GetMapping("/search/neighborhood")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<Technician>> getByNeighborhood(
            @RequestParam String neighborhood
    ) {
        return ResponseEntity.ok(
                technicianService.getTechniciansByNeighborhood(neighborhood)
        );
    }

    /* =========================
       = STATISTIQUES (ADMIN) =
       ========================= */

    @GetMapping("/stats/active/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActiveTechnicians() {
        return ResponseEntity.ok(
                technicianService.countActiveTechnicians()
        );
    }
}