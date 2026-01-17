package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.technician;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.TechnicianDocumentDto;
import com.eadl.connect_backend.application.mapper.TechnicianDocumentMapper;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianDocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/technician-documents")
@RequiredArgsConstructor
@Tag(name = "Documents Technicien", description = "Gestion des documents KYC des techniciens")
public class TechnicianDocumentController {

    private final TechnicianDocumentService documentService;

    /* ================= CREATE ================= */
    @Operation(summary = "Ajouter un document pour une compétence (technician)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Document créé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TechnicianDocumentDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PostMapping
    public ResponseEntity<TechnicianDocumentDto> addDocument(
            @Parameter(description = "Données du document") @RequestBody @Valid TechnicianDocumentDto dto
    ) {
        TechnicianDocument model = TechnicianDocumentMapper.toModel(dto);
        TechnicianDocument created = documentService.addDocument(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(TechnicianDocumentMapper.toDto(created));
    }

    /* ================= READ ================= */
    @Operation(summary = "Récupérer les documents d'une compétence")
    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<TechnicianDocumentDto>> getBySkill(
            @Parameter(description = "ID de la compétence") @PathVariable Long skillId
    ) {
        List<TechnicianDocument> docs = documentService.getDocumentsByTechnicianSkillId(skillId);
        return ResponseEntity.ok(TechnicianDocumentMapper.toDtoList(docs));
    }

    @Operation(summary = "Récupérer un document par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDocumentDto> getById(@PathVariable Long id) {
        Optional<TechnicianDocument> doc = documentService.getDocumentById(id);
        return doc.map(d -> ResponseEntity.ok(TechnicianDocumentMapper.toDto(d)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ================= VERIFY / REJECT (ADMIN) ================= */
    @Operation(summary = "Vérifier un document (Admin)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/verify")
    public ResponseEntity<Void> verifyDocument(@PathVariable Long id, @RequestParam(required = false) String note) {
        documentService.verifyDocument(id, note);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rejeter un document (Admin)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectDocument(@PathVariable Long id, @RequestParam(required = false) String note) {
        documentService.rejectDocument(id, note);
        return ResponseEntity.ok().build();
    }

    /* ================= DELETE ================= */
    @Operation(summary = "Supprimer un document (owner ou admin)")
    @PreAuthorize("hasAnyRole('ADMIN','TECHNICIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
