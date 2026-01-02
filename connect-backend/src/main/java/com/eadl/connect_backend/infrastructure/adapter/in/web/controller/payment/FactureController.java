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


@RestController
@RequestMapping("/api/factures")
@Tag(name = "Factures", description = "Gestion des factures")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @Operation(summary = "Créer une facture et générer le PDF")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> createFacture(@Valid @RequestBody FactureDto factureDto) {
        // Convert DTO -> Model
        Facture facture = new Facture();
        facture.setIdReservation(factureDto.getIdReservation());
        facture.setAmount(factureDto.getAmount());
        facture.setInvoiceNumber(factureDto.getInvoiceNumber());

        // Création de la facture
        FactureDto created = factureService.createFacture(facture);

        // Génération du PDF
        String pdfUrl = factureService.generatePdf(facture);
        created.setPdfUrl(pdfUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Mettre à jour une facture")
    @PutMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> updateFacture(
            @PathVariable Long idFacture,
            @Valid @RequestBody FactureDto factureDto) {

        Facture facture = new Facture();
        facture.setAmount(factureDto.getAmount());
        facture.setPdfUrl(factureDto.getPdfUrl());
        facture.setGeneratedAt(factureDto.getGeneratedAt());
        facture.setInvoiceNumber(factureDto.getInvoiceNumber());

        FactureDto updated = factureService.updateFacture(idFacture, facture);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Récupérer une facture par son ID")
    @GetMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FactureDto> getFactureById(@PathVariable Long idFacture) {
        return factureService.getFactureById(idFacture)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Récupérer toutes les factures")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FactureDto>> getAllFactures() {
        List<FactureDto> factures = factureService.getAllFactures();
        return ResponseEntity.ok(factures);
    }

    @Operation(summary = "Supprimer une facture")
    @DeleteMapping("/{idFacture}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long idFacture) {
        factureService.deleteFacture(idFacture);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer toutes les factures d'une réservation")
    @GetMapping("/reservation/{idReservation}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FactureDto>> getFacturesByReservation(@PathVariable Long idReservation) {
        List<FactureDto> factures = factureService.getFacturesByReservationId(idReservation);
        return ResponseEntity.ok(factures);
    }
}