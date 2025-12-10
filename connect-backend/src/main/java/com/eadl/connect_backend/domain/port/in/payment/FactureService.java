package com.eadl.connect_backend.domain.port.in.payment;

import com.eadl.connect_backend.domain.model.payment.Facture;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Service de facturation
 * Use cases pour la gestion des factures
 */
public interface FactureService {
    
    /**
     * Génère une facture pour une réservation
     */
    Facture generateFacture(Long idReservation, BigDecimal amount);
    
    /**
     * Récupère une facture par son ID
     */
    Optional<Facture> getFactureById(Long idFacture);
    
    /**
     * Récupère la facture d'une réservation
     */
    Optional<Facture> getFactureByReservation(Long idReservation);
    
    /**
     * Récupère toutes les factures d'un client
     */
    List<Facture> getClientFactures(Long idClient);
    
    /**
     * Récupère toutes les factures d'un technicien
     */
    List<Facture> getTechnicianFactures(Long idTechnician);
    
    /**
     * Régénère le PDF d'une facture
     */
    Facture regenerateFacturePdf(Long idFacture);
    
    /**
     * Télécharge une facture en PDF
     */
    byte[] downloadFacturePdf(Long idFacture);
}