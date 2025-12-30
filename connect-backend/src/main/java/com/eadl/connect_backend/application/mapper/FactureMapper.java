package com.eadl.connect_backend.application.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.application.dto.FactureDto;
import com.eadl.connect_backend.domain.model.payment.Facture;

@Component
public class FactureMapper {

    // =========================
    // Facture <-> FactureDTO
    // =========================

    public FactureDto toDto(Facture facture) {
        if (facture == null) return null;

        return FactureDto.builder()
                .idFacture(facture.getIdFacture())
                .idReservation(facture.getIdReservation())
                .pdfUrl(facture.getPdfUrl())
                .amount(facture.getAmount())
                .generatedAt(facture.getGeneratedAt())
                .invoiceNumber(facture.getInvoiceNumber())
                .build();
    }

    public Facture toModel(FactureDto dto) {
        if (dto == null) return null;

        Facture facture = new Facture();
        facture.setIdFacture(dto.getIdFacture());
        facture.setIdReservation(dto.getIdReservation());
        facture.setPdfUrl(dto.getPdfUrl());
        facture.setAmount(dto.getAmount());
        facture.setGeneratedAt(dto.getGeneratedAt());
        facture.setInvoiceNumber(dto.getInvoiceNumber());

        return facture;
    }
}