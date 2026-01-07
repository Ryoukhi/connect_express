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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/technician-skills")
@RequiredArgsConstructor
@Tag(name = "Compétences Technicien", description = "Gestion des compétences des techniciens")
public class TechnicianSkillController {

    private final TechnicianSkillService technicianSkillService;
    private final CurrentUserProvider currentUserProvider;
    private final TechnicianSkillMapper technicianSkillMapper;

    /* ================= CREATE ================= */
    @Operation(summary = "Ajouter une compétence au profil du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compétence créée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianSkillDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping
    public ResponseEntity<TechnicianSkillDto> addSkill(
            @Parameter(description = "Données de la compétence à ajouter") @RequestBody @Valid TechnicianSkillDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        dto.setIdProfile(technicianId);
        TechnicianSkill skill = technicianSkillMapper.toModel(dto);
        TechnicianSkill created = technicianSkillService.addSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianSkillMapper.toDto(created));
    }

    /* ================= READ ================= */
    @Operation(summary = "Récupérer toutes les compétences du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des compétences récupérée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianSkillDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @GetMapping("/me")
    public List<TechnicianSkillDto> getMySkills() {
        Long technicianId = currentUserProvider.getCurrentUserId();
        List<TechnicianSkill> skills = technicianSkillService.getSkillsByTechnicianId(technicianId);
        return technicianSkillMapper.toDtoList(skills);
    }

    @Operation(summary = "Récupérer les compétences du technicien connecté pour une catégorie donnée")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des compétences filtrée par catégorie",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianSkillDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TechnicianSkillDto>> getSkillsByCategory(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categoryId
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        List<TechnicianSkill> skills = technicianSkillService.getSkillsByCategory(technicianId, categoryId);
        return ResponseEntity.ok(technicianSkillMapper.toDtoList(skills));
    }

    /* ================= UPDATE ================= */
    @Operation(summary = "Mettre à jour une compétence du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compétence mise à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianSkillDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PutMapping("/{idSkill}")
    public ResponseEntity<TechnicianSkillDto> updateSkill(
            @Parameter(description = "ID de la compétence à modifier") @PathVariable Long idSkill,
            @Parameter(description = "Données de la compétence à mettre à jour") @RequestBody @Valid TechnicianSkillDto dto
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        dto.setIdProfile(technicianId);
        TechnicianSkill updated = technicianSkillService.updateSkill(idSkill, technicianSkillMapper.toModel(dto));
        return ResponseEntity.ok(technicianSkillMapper.toDto(updated));
    }

    /* ================= DELETE ================= */
    @Operation(summary = "Supprimer une compétence du technicien connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Compétence supprimée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @DeleteMapping("/{idSkill}")
    public ResponseEntity<Void> deleteSkill(
            @Parameter(description = "ID de la compétence à supprimer") @PathVariable Long idSkill
    ) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        technicianSkillService.deleteSkill(idSkill, technicianId);
        return ResponseEntity.noContent().build();
    }
}
