package com.eadl.connect_backend.domain.port.in.payment;

import java.util.*;

import com.eadl.connect_backend.application.dto.FactureDto;
import com.eadl.connect_backend.domain.model.payment.Facture;

/**
 * Port IN - Service de facturation
 * Use cases pour la gestion des factures
 */
public interface FactureService {

    /**
     * Crée une nouvelle facture pour une réservation donnée
     */
    FactureDto createFacture(Facture facture);

    /**
     * Met à jour une facture existante
     */
    FactureDto updateFacture(Long idFacture, Facture facture);

    /**
     * Récupère une facture par son ID
     */
    Optional<FactureDto> getFactureById(Long idFacture);

    /**
     * Récupère toutes les factures
     */
    List<FactureDto> getAllFactures();

    /**
     * Supprime une facture
     */
    void deleteFacture(Long idFacture);

    /**
     * Récupère toutes les factures liées à une réservation
     */
    List<FactureDto> getFacturesByReservationId(Long idReservation);

    /**
     * Génère un numéro de facture unique (utile lors de la création)
     */
    String generateInvoiceNumber();

    /**
     * Convertit une facture en PDF et renvoie son URL
     */
    String generatePdf(Facture facture);
}