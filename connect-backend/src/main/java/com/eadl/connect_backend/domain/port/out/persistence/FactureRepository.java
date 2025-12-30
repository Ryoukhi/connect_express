package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.payment.Facture;

/**
 * Port OUT - Repository Facture
 */
public interface FactureRepository {
    
    /**
     * Sauvegarde une facture
     */
    Facture save(Facture facture);
    
    /**
     * Récupère une facture par son ID
     */
    Optional<Facture> findById(Long idFacture);

    /**
     * Récupère toutes les factures
     */
    List<Facture> findAll();

    /**
     * Supprime une facture par son ID
     */
    void deleteById(Long idFacture);
    
    /**
     * Récupère une facture par numéro
     */
    Optional<Facture> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Récupère la facture d'une réservation
     */
    Optional<Facture> findByReservationId(Long idReservation);

    
    /**
     * Supprime une facture
     */
    void delete(Facture facture);
}