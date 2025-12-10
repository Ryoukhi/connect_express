package com.eadl.connect_backend.domain.port.in.admin;

import java.util.List;

import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;

/**
 * Port IN - Service de validation KYC
 * Use cases pour la validation des documents techniciens
 */
public interface KycValidationService {
    
    /**
     * Récupère tous les documents en attente de validation
     */
    List<TechnicianDocument> getPendingDocuments();
    
    /**
     * Récupère les documents d'un profil technicien
     */
    List<TechnicianDocument> getDocumentsByProfile(Long idProfile);
    
    /**
     * Approuve un document
     */
    TechnicianDocument approveDocument(Long idAdmin, Long idDocument, String note);
    
    /**
     * Rejette un document
     */
    TechnicianDocument rejectDocument(Long idAdmin, Long idDocument, String reason);
    
    /**
     * Approuve le KYC complet d'un technicien (tous documents validés)
     */
    TechnicianProfile approveKyc(Long idAdmin, Long idTechnician, String note);
    
    /**
     * Rejette le KYC d'un technicien
     */
    TechnicianProfile rejectKyc(Long idAdmin, Long idTechnician, String reason);
    
    /**
     * Vérifie si un technicien a complété son KYC
     */
    boolean isKycComplete(Long idTechnician);
}
