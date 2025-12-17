package com.eadl.connect_backend.application.service.technician;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.technician.DocumentType;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.port.exception.DocumentNotFoundException;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianDocumentService;
import com.eadl.connect_backend.domain.port.out.external.StorageService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service TechnicianDocument
 */
@Service
@Transactional
public class TechnicianDocumentServiceImpl implements TechnicianDocumentService {
    
    private final TechnicianDocumentRepository documentRepository;
    private final StorageService storageService;
    
    // Documents requis pour le KYC
    private static final List<DocumentType> REQUIRED_DOCUMENTS = Arrays.asList(
        DocumentType.IDENTITY_CARD,
        DocumentType.CERTIFICATE
    );
    
    public TechnicianDocumentServiceImpl(TechnicianDocumentRepository documentRepository,
                                         StorageService storageService) {
        this.documentRepository = documentRepository;
        this.storageService = storageService;
    }
    
    @Override
    public TechnicianDocument uploadDocument(Long idProfile, DocumentType type, 
                                            byte[] fileData, String fileName) {
        // 1. Vérifier si un document de ce type existe déjà
        Optional<TechnicianDocument> existingDoc = 
            documentRepository.findByProfileIdAndType(idProfile, type);
        
        if (existingDoc.isPresent()) {
            // Supprimer l'ancien document du storage
            storageService.deleteFile(existingDoc.get().getUrl());
            // Supprimer de la base
            documentRepository.delete(existingDoc.get());
        }
        
        // 2. Upload du fichier vers S3
        String folder = "technicians/documents/" + idProfile;
        String fileUrl = storageService.uploadFile(
            fileData, fileName, folder, getContentType(fileName)
        );
        
        // 3. Créer le document
        TechnicianDocument document = TechnicianDocument.create(
            idProfile, type, fileUrl
        );
        
        // 4. Sauvegarder
        return documentRepository.save(document);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianDocument> getDocumentById(Long idDocument) {
        return documentRepository.findById(idDocument);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TechnicianDocument> getDocumentsByProfile(Long idProfile) {
        return documentRepository.findByProfileId(idProfile);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianDocument> getDocumentByType(Long idProfile, DocumentType type) {
        return documentRepository.findByProfileIdAndType(idProfile, type);
    }
    
    @Override
    public void deleteDocument(Long idDocument) {
        // 1. Récupérer le document
        TechnicianDocument document = documentRepository.findById(idDocument)
            .orElseThrow(() -> new DocumentNotFoundException(
                "Document non trouvé avec l'ID: " + idDocument));
        
        // 2. Supprimer du storage
        storageService.deleteFile(document.getUrl());
        
        // 3. Supprimer de la base
        documentRepository.delete(document);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasAllRequiredDocuments(Long idProfile) {
        // Récupérer tous les documents du profil
        List<TechnicianDocument> documents = documentRepository.findByProfileId(idProfile);
        
        // Extraire les types de documents présents
        List<DocumentType> presentTypes = documents.stream()
            .map(TechnicianDocument::getType)
            .toList();
        
        // Vérifier que tous les documents requis sont présents
        return presentTypes.containsAll(REQUIRED_DOCUMENTS);
    }
    
    /**
     * Télécharge un document depuis le storage
     */
    public byte[] downloadDocument(Long idDocument) {
        // 1. Récupérer le document
        TechnicianDocument document = documentRepository.findById(idDocument)
            .orElseThrow(() -> new DocumentNotFoundException(
                "Document non trouvé avec l'ID: " + idDocument));
        
        // 2. Télécharger depuis le storage
        return storageService.downloadFile(document.getUrl());
    }
    
    /**
     * Détermine le type MIME du fichier
     */
    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> "application/octet-stream";
        };
    }
}