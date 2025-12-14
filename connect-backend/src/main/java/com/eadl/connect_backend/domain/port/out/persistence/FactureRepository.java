package com.eadl.connect_backend.domain.port.out.persistence;

import java.time.LocalDateTime;
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
     * Récupère une facture par numéro
     */
    Optional<Facture> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Récupère la facture d'une réservation
     */
    Optional<Facture> findByReservationId(Long idReservation);
    
    /**
     * Récupère toutes les factures d'un client
     */
    List<Facture> findByClientId(Long idClient);
    
    /**
     * Récupère toutes les factures d'un technicien
     */
    List<Facture> findByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les factures dans une période
     */
    List<Facture> findByGeneratedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Compte les factures
     */
    Long count();
    
    /**
     * Supprime une facture
     */
    void delete(Facture facture);
}