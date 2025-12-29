package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des documents KYC d’un technicien
 */
public interface TechnicianDocumentService {

    /**
     * Ajoute un nouveau document pour un profil de technicien
     */
    TechnicianDocument addDocument(TechnicianDocument document);

    /**
     * Récupère tous les documents d’un profil de technicien
     */
    List<TechnicianDocument> getDocumentsByProfileId(Long technicianProfileId);

    /**
     * Récupère un document par son ID
     */
    Optional<TechnicianDocument> getDocumentById(Long documentId);

    /**
     * Vérifie / valide un document KYC (Admin)
     */
    void verifyDocument(Long documentId, String verificationNote);

    /**
     * Rejette un document KYC (Admin)
     */
    void rejectDocument(Long documentId, String verificationNote);

    /**
     * Supprime un document d’un profil de technicien
     */
    void deleteDocument(Long documentId);

    
}