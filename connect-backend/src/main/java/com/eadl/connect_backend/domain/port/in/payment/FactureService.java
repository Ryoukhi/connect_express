package com.eadl.connect_backend.domain.port.in.payment;


/**
 * Port IN - Service de facturation
 * Use cases pour la gestion des factures
 */
public interface FactureService {
    
    
    /**
     * Télécharge une facture en PDF
     */
    byte[] downloadFacturePdf(Long idFacture);
}