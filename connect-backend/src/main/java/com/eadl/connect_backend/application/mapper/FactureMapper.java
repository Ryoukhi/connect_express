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

        FactureDto newDto = new FactureDto();
        newDto.setIdFacture(facture.getIdFacture());
        newDto.setIdReservation(facture.getIdReservation());
        newDto.setPdfUrl(facture.getPdfUrl());
        newDto.setAmount(facture.getAmount());
        newDto.setGeneratedAt(facture.getGeneratedAt());
        newDto.setInvoiceNumber(facture.getInvoiceNumber());

        return newDto;
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