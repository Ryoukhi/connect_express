package com.eadl.connect_backend.application.service.technician;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianDocumentService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnicianDocumentServiceImpl implements TechnicianDocumentService {

    private final TechnicianDocumentRepository documentRepository;
    private final TechnicianSkillRepository skillRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    public TechnicianDocument addDocument(TechnicianDocument document) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Ajout d'un document pour la compétence id={} par l'utilisateur id={}", document.getIdProfile(), currentUserId);

        if (document.getIdProfile() == null) {
            throw new IllegalArgumentException("L'identifiant de la compétence (idProfile) doit être renseigné");
        }

        // Verify the skill exists and belongs to the current user
        TechnicianSkill skill = skillRepository.findById(document.getIdProfile())
                .orElseThrow(() -> new IllegalArgumentException("Compétence introuvable"));

        if (!skill.getIdUser().equals(currentUserId)) {
            log.error("Utilisateur id={} non propriétaire de la compétence id={}", currentUserId, document.getIdProfile());
            throw new SecurityException("Vous ne pouvez pas ajouter un document pour cette compétence");
        }

        TechnicianDocument saved = documentRepository.save(document);
        log.debug("Document ajouté id={}", saved.getIdDocument());
        return saved;
    }

    @Override
    public List<TechnicianDocument> getDocumentsByTechnicianSkillId(Long technicianSkillId) {
        log.debug("Récupération des documents pour la compétence id={}", technicianSkillId);
        return documentRepository.findByProfileId(technicianSkillId);
    }

    @Override
    public Optional<TechnicianDocument> getDocumentById(Long documentId) {
        log.debug("Récupération du document id={}", documentId);
        return documentRepository.findById(documentId);
    }

    @Override
    public void verifyDocument(Long documentId, String verificationNote) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Vérification du document id={} par utilisateur id={}", documentId, currentUserId);

        // Only admins may verify documents
        try {
            User u = currentUserProvider.getCurrentUser();
            if (u == null || !u.isAdmin()) {
                log.error("Utilisateur id={} non autorisé à vérifier des documents", currentUserId);
                throw new SecurityException("Accès refusé : opération réservée aux administrateurs");
            }
        } catch (IllegalStateException ise) {
            log.error("Aucun utilisateur authentifié pour vérifier le document id={}", documentId);
            throw new SecurityException("Utilisateur non authentifié");
        }

        TechnicianDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));

        doc.approve(verificationNote);
        documentRepository.save(doc);
        log.debug("Document id={} vérifié", documentId);
    }

    @Override
    public void rejectDocument(Long documentId, String verificationNote) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Rejet du document id={} par utilisateur id={}", documentId, currentUserId);

        // Only admins may reject documents
        try {
            User u = currentUserProvider.getCurrentUser();
            if (u == null || !u.isAdmin()) {
                log.error("Utilisateur id={} non autorisé à rejeter des documents", currentUserId);
                throw new SecurityException("Accès refusé : opération réservée aux administrateurs");
            }
        } catch (IllegalStateException ise) {
            log.error("Aucun utilisateur authentifié pour rejeter le document id={}", documentId);
            throw new SecurityException("Utilisateur non authentifié");
        }

        TechnicianDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));

        doc.reject(verificationNote);
        documentRepository.save(doc);
        log.debug("Document id={} rejeté", documentId);
    }

    @Override
    public void deleteDocument(Long documentId) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Suppression document id={} demandée par utilisateur id={}", documentId, currentUserId);

        TechnicianDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));

        // Allow owner of the skill or admin
        TechnicianSkill skill = skillRepository.findById(doc.getIdProfile())
                .orElseThrow(() -> new IllegalArgumentException("Compétence introuvable associée au document"));

        try {
            User u = currentUserProvider.getCurrentUser();
            if (!u.isAdmin() && !skill.getIdUser().equals(currentUserId)) {
                log.error("Utilisateur id={} non autorisé à supprimer le document id={}", currentUserId, documentId);
                throw new SecurityException("Accès refusé");
            }
        } catch (IllegalStateException ise) {
            log.error("Aucun utilisateur authentifié pour supprimer le document id={}", documentId);
            throw new SecurityException("Utilisateur non authentifié");
        }

        documentRepository.delete(doc);
        log.debug("Document id={} supprimé", documentId);
    }

}
