package com.eadl.connect_backend.application.service.technician;

import org.springframework.stereotype.Service;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianDocumentService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicianDocumentServiceImpl implements TechnicianDocumentService {

    private final TechnicianDocumentRepository documentRepository;
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    public TechnicianDocumentServiceImpl(
            TechnicianDocumentRepository documentRepository,
            CurrentUserProvider currentUserProvider) {
        this.documentRepository = documentRepository;
        this.currentUserProvider = currentUserProvider;
        this.userRepository = null;
    }

    @Override
    public TechnicianDocument addDocument(TechnicianDocument document) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
        // Seul le technicien propriétaire du profil peut ajouter un document
        if (!currentUser.getRole().equals(Role.TECHNICIAN)) {
            throw new IllegalStateException("Only technicians can add documents");
        }

        TechnicianDocument newDocument = new TechnicianDocument();
        newDocument.setIdProfile(document.getIdProfile());
        newDocument.setType(document.getType());
        newDocument.setUrl(document.getUrl());
        newDocument.setUploadedAt(LocalDateTime.now());
        newDocument.setVerified(false);
        newDocument.setVerificationNote(null);

        return documentRepository.save(newDocument);
    }

    @Override
    public List<TechnicianDocument> getDocumentsByProfileId(Long technicianProfileId) {
        return documentRepository.findByProfileId(technicianProfileId);
    }

    @Override
    public Optional<TechnicianDocument> getDocumentById(Long documentId) {
        return documentRepository.findById(documentId);
    }

    @Override
    public void verifyDocument(Long documentId, String verificationNote) {
         Long currentUserId = currentUserProvider.getCurrentUserId();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));

        // Seul un admin peut valider un document
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new IllegalStateException("Only admins can verify documents");
        }

        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        document.setVerified(true);
        document.setVerificationNote(verificationNote);
        documentRepository.save(document);
    }

    @Override
    public void rejectDocument(Long documentId, String verificationNote) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new IllegalStateException("Only admins can reject documents");
        }

        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        document.setVerified(false);
        document.setVerificationNote(verificationNote);
        documentRepository.save(document);
    }

    @Override
    public void deleteDocument(Long documentId) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        // Le technicien propriétaire ou un admin peut supprimer le document
        if (!currentUser.getRole().equals(Role.ADMIN) &&
            !document.getIdProfile().equals(currentUser.getIdUser())) {
            throw new IllegalStateException("You are not allowed to delete this document");
        }

        documentRepository.delete(document);
    }
}