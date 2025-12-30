package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.payment.Facture;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.FactureEntity;

@Component
public class FactureEntityMapper {

        public FactureEntity toEntity(Facture facture) {
        if (facture == null) return null;

        FactureEntity entity = new FactureEntity();
        entity.setIdFacture(facture.getIdFacture());
        entity.setPdfUrl(facture.getPdfUrl());
        entity.setAmount(facture.getAmount());
        entity.setGeneratedAt(facture.getGeneratedAt());
        entity.setInvoiceNumber(facture.getInvoiceNumber());
        // Note : idReservation doit être géré via association ManyToOne avec ReservationEntity
        return entity;
    }

    public Facture toModel(FactureEntity entity) {
        if (entity == null) return null;

        Facture facture = new Facture();
        facture.setIdFacture(entity.getIdFacture());
        facture.setIdReservation(entity.getReservation() != null ? entity.getReservation().getIdReservation() : null);
        facture.setPdfUrl(entity.getPdfUrl());
        facture.setAmount(entity.getAmount());
        facture.setGeneratedAt(entity.getGeneratedAt());
        facture.setInvoiceNumber(entity.getInvoiceNumber());

        return facture;
    }

    // Optionnel : mise à jour d'une entity existante
    public void updateEntity(FactureEntity entity, Facture facture) {
        if (entity == null || facture == null) return;

        entity.setPdfUrl(facture.getPdfUrl());
        entity.setAmount(facture.getAmount());
        entity.setGeneratedAt(facture.getGeneratedAt());
        entity.setInvoiceNumber(facture.getInvoiceNumber());
        // Reservation association à gérer séparément si besoin
    }

}
