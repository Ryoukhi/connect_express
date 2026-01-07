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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TechnicianDocumentServiceImpl implements TechnicianDocumentService {

    private final TechnicianDocumentRepository documentRepository;
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    public TechnicianDocumentServiceImpl(
            TechnicianDocumentRepository documentRepository,
            CurrentUserProvider currentUserProvider,
            UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.currentUserProvider = currentUserProvider;
        this.userRepository = userRepository;
    }

    @Override
    public TechnicianDocument addDocument(TechnicianDocument document) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Ajout d'un document pour le profil id={} par l'utilisateur id={}", document.getIdProfile(), currentUserId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Utilisateur courant introuvable id={}", currentUserId);
                    return new IllegalStateException("Current user not found");
                });

        if (!currentUser.getRole().equals(Role.TECHNICIAN)) {
            log.error("L'utilisateur id={} n'est pas technicien et ne peut pas ajouter de documents", currentUserId);
            throw new IllegalStateException("Only technicians can add documents");
        }

        TechnicianDocument newDocument = new TechnicianDocument();
        newDocument.setIdProfile(document.getIdProfile());
        newDocument.setType(document.getType());
        newDocument.setUrl(document.getUrl());
        newDocument.setUploadedAt(LocalDateTime.now());
        newDocument.setVerified(false);
        newDocument.setVerificationNote(null);

        TechnicianDocument saved = documentRepository.save(newDocument);
        log.info("Document ajouté avec succès id={}, url={}", saved.getId(), saved.getUrl());
        return saved;
    }

    @Override
    public List<TechnicianDocument> getDocumentsByProfileId(Long technicianProfileId) {
        log.debug("Récupération des documents pour le profil id={}", technicianProfileId);
        List<TechnicianDocument> documents = documentRepository.findByProfileId(technicianProfileId);
        log.debug("Nombre de documents récupérés pour le profil id={}: {}", technicianProfileId, documents.size());
        return documents;
    }

    @Override
    public Optional<TechnicianDocument> getDocumentById(Long documentId) {
        log.debug("Récupération du document id={}", documentId);
        return documentRepository.findById(documentId);
    }

    @Override
    public void verifyDocument(Long documentId, String verificationNote) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Validation du document id={} par l'administrateur id={}", documentId, currentUserId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Utilisateur courant introuvable id={}", currentUserId);
                    return new IllegalStateException("Current user not found");
                });

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            log.error("L'utilisateur id={} n'est pas admin et ne peut pas valider le document id={}", currentUserId, documentId);
            throw new IllegalStateException("Only admins can verify documents");
        }

        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    log.error("Document introuvable id={}", documentId);
                    return new IllegalArgumentException("Document not found");
                });

        document.setVerified(true);
        document.setVerificationNote(verificationNote);
        documentRepository.save(document);
        log.info("Document id={} validé avec succès, note='{}'", documentId, verificationNote);
    }

    @Override
    public void rejectDocument(Long documentId, String verificationNote) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Rejet du document id={} par l'administrateur id={}", documentId, currentUserId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Utilisateur courant introuvable id={}", currentUserId);
                    return new IllegalStateException("Current user not found");
                });

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            log.error("L'utilisateur id={} n'est pas admin et ne peut pas rejeter le document id={}", currentUserId, documentId);
            throw new IllegalStateException("Only admins can reject documents");
        }

        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    log.error("Document introuvable id={}", documentId);
                    return new IllegalArgumentException("Document not found");
                });

        document.setVerified(false);
        document.setVerificationNote(verificationNote);
        documentRepository.save(document);
        log.info("Document id={} rejeté avec succès, note='{}'", documentId, verificationNote);
    }

    @Override
    public void deleteDocument(Long documentId) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Suppression du document id={} par l'utilisateur id={}", documentId, currentUserId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Utilisateur courant introuvable id={}", currentUserId);
                    return new IllegalStateException("Current user not found");
                });

        TechnicianDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    log.error("Document introuvable id={}", documentId);
                    return new IllegalArgumentException("Document not found");
                });

        if (!currentUser.getRole().equals(Role.ADMIN) &&
                !document.getIdProfile().equals(currentUser.getIdUser())) {
            log.error("Utilisateur id={} n'a pas le droit de supprimer le document id={}", currentUserId, documentId);
            throw new IllegalStateException("You are not allowed to delete this document");
        }

        documentRepository.delete(document);
        log.info("Document id={} supprimé avec succès", documentId);
    }
}
