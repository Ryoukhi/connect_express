package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.payment;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.FactureDto;
import com.eadl.connect_backend.domain.model.payment.Facture;
import com.eadl.connect_backend.domain.port.in.payment.FactureService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/factures")
@Tag(name = "Factures", description = "Gestion des factures")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @Operation(summary = "Créer une facture et générer le PDF (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Facture créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FactureDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> createFacture(@Valid @RequestBody FactureDto factureDto) {
        Facture facture = new Facture();
        facture.setIdReservation(factureDto.getIdReservation());
        facture.setAmount(factureDto.getAmount());
        facture.setInvoiceNumber(factureDto.getInvoiceNumber());

        FactureDto created = factureService.createFacture(facture);
        String pdfUrl = factureService.generatePdf(facture);
        created.setPdfUrl(pdfUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Mettre à jour une facture (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facture mise à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FactureDto.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PutMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> updateFacture(
            @Parameter(description = "ID de la facture") @PathVariable Long idFacture,
            @Valid @RequestBody FactureDto factureDto) {

        Facture facture = new Facture();
        facture.setAmount(factureDto.getAmount());
        facture.setPdfUrl(factureDto.getPdfUrl());
        facture.setGeneratedAt(factureDto.getGeneratedAt());
        facture.setInvoiceNumber(factureDto.getInvoiceNumber());

        FactureDto updated = factureService.updateFacture(idFacture, facture);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Récupérer une facture par son ID (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facture trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FactureDto.class))),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @GetMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> getFactureById(
            @Parameter(description = "ID de la facture") @PathVariable Long idFacture
    ) {
        return factureService.getFactureById(idFacture)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Récupérer toutes les factures (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des factures",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FactureDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FactureDto>> getAllFactures() {
        List<FactureDto> factures = factureService.getAllFactures();
        return ResponseEntity.ok(factures);
    }

    @Operation(summary = "Supprimer une facture (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Facture supprimée"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @DeleteMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFacture(
            @Parameter(description = "ID de la facture") @PathVariable Long idFacture
    ) {
        factureService.deleteFacture(idFacture);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer toutes les factures d'une réservation (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des factures associées à la réservation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FactureDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @GetMapping("/reservation/{idReservation}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FactureDto>> getFacturesByReservation(
            @Parameter(description = "ID de la réservation") @PathVariable Long idReservation
    ) {
        List<FactureDto> factures = factureService.getFacturesByReservationId(idReservation);
        return ResponseEntity.ok(factures);
    }
}
