package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.DocumentType;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Service de documents technicien
 * Use cases pour la gestion des documents KYC
 */
public interface TechnicianDocumentService {
    
    /**
     * Upload un document
     */
    TechnicianDocument uploadDocument(Long idProfile, DocumentType type, 
                                     byte[] fileData, String fileName);
    
    /**
     * Récupère un document par son ID
     */
    Optional<TechnicianDocument> getDocumentById(Long idDocument);
    
    /**
     * Récupère tous les documents d'un profil
     */
    List<TechnicianDocument> getDocumentsByProfile(Long idProfile);
    
    /**
     * Récupère les documents d'un profil par type
     */
    Optional<TechnicianDocument> getDocumentByType(Long idProfile, DocumentType type);
    
    /**
     * Supprime un document
     */
    void deleteDocument(Long idDocument);
    
    /**
     * Vérifie si tous les documents requis sont uploadés
     */
    boolean hasAllRequiredDocuments(Long idProfile);
}