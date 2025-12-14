package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.DocumentType;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;

/**
 * Port OUT - Repository TechnicianDocument
 */
public interface TechnicianDocumentRepository {
    
    /**
     * Sauvegarde un document
     */
    TechnicianDocument save(TechnicianDocument document);
    
    /**
     * Récupère un document par son ID
     */
    Optional<TechnicianDocument> findById(Long idDocument);
    
    /**
     * Récupère les documents d'un profil
     */
    List<TechnicianDocument> findByProfileId(Long idProfile);
    
    /**
     * Récupère un document par profil et type
     */
    Optional<TechnicianDocument> findByProfileIdAndType(Long idProfile, DocumentType type);
    
    /**
     * Récupère les documents non vérifiés
     */
    List<TechnicianDocument> findByVerified(boolean verified);
    
    /**
     * Récupère tous les documents en attente de validation
     */
    List<TechnicianDocument> findPendingDocuments();
    
    /**
     * Supprime un document
     */
    void delete(TechnicianDocument document);
    
    /**
     * Supprime un document par ID
     */
    void deleteById(Long idDocument);
}
