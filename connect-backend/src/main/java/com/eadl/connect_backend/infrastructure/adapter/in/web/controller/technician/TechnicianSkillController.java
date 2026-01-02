package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.TechnicianSkillDto;
import com.eadl.connect_backend.application.mapper.TechnicianSkillMapper;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSkillService;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/technician-skills")
@RequiredArgsConstructor
public class TechnicianSkillController {

    private final TechnicianSkillService technicianSkillService;
    private final CurrentUserProvider currentUserProvider;
    private final TechnicianSkillMapper technicianSkillMapper;

    /* ================= CREATE ================= */

    @PostMapping
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianSkillDto> addSkill(
            @RequestBody @Valid TechnicianSkillDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        dto.setIdProfile(technicianId); // Associer la compétence au profil du technicien
        TechnicianSkill skill = technicianSkillMapper.toModel(dto);

        TechnicianSkill created = technicianSkillService.addSkill(skill);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(technicianSkillMapper.toDto(created));
    }

    /* ================= READ ================= */

    @GetMapping("/me")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public List<TechnicianSkillDto> getMySkills() {
        Long technicianId = currentUserProvider.getCurrentUserId();
        List<TechnicianSkill> skills = technicianSkillService.getSkillsByTechnicianId(technicianId);

        return technicianSkillMapper.toDtoList(skills);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TechnicianSkillDto>> getSkillsByCategory(@PathVariable Long categoryId) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        List<TechnicianSkill> skills = technicianSkillService.getSkillsByCategory(technicianId, categoryId);
        
        return ResponseEntity.ok(technicianSkillMapper.toDtoList(skills));
    }

    /* ================= UPDATE ================= */

    @PutMapping("/{idSkill}")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianSkillDto> updateSkill(
            @PathVariable Long idSkill,
            @RequestBody @Valid TechnicianSkillDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        dto.setIdProfile(technicianId);
        TechnicianSkill updated = technicianSkillService.updateSkill(idSkill, technicianSkillMapper.toModel(dto));

        return ResponseEntity.ok(technicianSkillMapper.toDto(updated));
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{idSkill}")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long idSkill) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        technicianSkillService.deleteSkill(idSkill, technicianId);
        return ResponseEntity.noContent().build();
    }
}