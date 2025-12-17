package com.eadl.connect_backend.application.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.admin.ActionType;
import com.eadl.connect_backend.domain.model.admin.AdminAction;
import com.eadl.connect_backend.domain.model.notification.NotificationType;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.user.Technician;
import com.eadl.connect_backend.domain.port.in.admin.KycValidationService;
import com.eadl.connect_backend.domain.port.in.notification.NotificationService;
import com.eadl.connect_backend.domain.port.out.persistence.AdminActionRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;

import java.util.List;

@Service
@Transactional
public class KycValidationServiceImpl implements KycValidationService {
    
    private final TechnicianDocumentRepository documentRepository;
    private final TechnicianProfileRepository profileRepository;
    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final AdminActionRepository adminActionRepository;
    private final NotificationService notificationService;
    
    public KycValidationServiceImpl(TechnicianDocumentRepository documentRepository,
                                   TechnicianProfileRepository profileRepository,
                                   TechnicianRepository technicianRepository,
                                   UserRepository userRepository,
                                   AdminActionRepository adminActionRepository,
                                   NotificationService notificationService) {
        this.documentRepository = documentRepository;
        this.profileRepository = profileRepository;
        this.technicianRepository = technicianRepository;
        this.userRepository = userRepository;
        this.adminActionRepository = adminActionRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TechnicianDocument> getPendingDocuments() {
        return documentRepository.findByVerified(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TechnicianDocument> getDocumentsByProfile(Long idProfile) {
        return documentRepository.findByProfileId(idProfile);
    }
    
    @Override
    public TechnicianDocument approveDocument(Long idAdmin, Long idDocument, String note) {
        TechnicianDocument document = documentRepository.findById(idDocument)
            .orElseThrow(() -> new IllegalArgumentException("Document non trouvé"));
        
        // Approuver le document
        document.approve(note);
        document = documentRepository.save(document);
        
        // Logger l'action admin
        AdminAction action = AdminAction.create(
            idAdmin, ActionType.DOCUMENT_VERIFIED,
            "Document approuvé: " + document.getType()
        );
        adminActionRepository.save(action);
        
        // Récupérer le technicien pour notification
        TechnicianProfile profile = profileRepository.findById(document.getIdProfile())
            .orElseThrow();
        Technician technician = technicianRepository.findById(profile.getIdTechnician())
            .orElseThrow();
        
        // Notifier le technicien
        notificationService.sendNotification(
            technician.getIdUser(),
            NotificationType.DOCUMENT_VERIFIED,
            "Document vérifié",
            "Votre " + document.getType() + " a été approuvé"
        );
        
        return document;
    }
    
    @Override
    public TechnicianDocument rejectDocument(Long idAdmin, Long idDocument, String reason) {
        TechnicianDocument document = documentRepository.findById(idDocument)
            .orElseThrow(() -> new IllegalArgumentException("Document non trouvé"));
        
        // Rejeter le document
        document.reject(reason);
        document = documentRepository.save(document);
        
        // Logger l'action admin
        AdminAction action = AdminAction.create(
            idAdmin, ActionType.DOCUMENT_REJECTED,
            "Document rejeté: " + reason
        );
        adminActionRepository.save(action);
        
        // Récupérer le technicien pour notification
        TechnicianProfile profile = profileRepository.findById(document.getIdProfile())
            .orElseThrow();
        Technician technician = technicianRepository.findById(profile.getIdTechnician())
            .orElseThrow();
        
        // Notifier le technicien
        notificationService.sendNotification(
            technician.getIdUser(),
            NotificationType.DOCUMENT_REJECTED,
            "Document rejeté",
            "Votre " + document.getType() + " a été rejeté: " + reason
        );
        
        return document;
    }
    
    @Override
    public TechnicianProfile approveKyc(Long idAdmin, Long idTechnician, String note) {
        // Récupérer le profil et le technicien
        TechnicianProfile profile = profileRepository.findByTechnicianId(idTechnician)
            .orElseThrow(() -> new IllegalArgumentException("Profil non trouvé"));
        
        Technician technician = technicianRepository.findById(idTechnician)
            .orElseThrow();
        
        // Vérifier que tous les documents sont validés
        List<TechnicianDocument> documents = documentRepository.findByProfileId(profile.getIdProfile());
        boolean allVerified = documents.stream().allMatch(TechnicianDocument::isVerified);
        
        if (!allVerified) {
            throw new IllegalStateException("Tous les documents doivent être validés avant d'approuver le KYC");
        }
        
        // Vérifier le profil
        profile.verify();
        profile = profileRepository.save(profile);
        
        // Activer le compte technicien
        technician.approveKyc();
        userRepository.save(technician);
        
        // Logger l'action admin
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, technician.getIdUser(),
            ActionType.KYC_APPROVED,
            note
        );
        adminActionRepository.save(action);
        
        // Notifier le technicien
        notificationService.sendNotification(
            technician.getIdUser(),
            NotificationType.ACCOUNT_VERIFIED,
            "KYC approuvé",
            "Votre compte a été vérifié et activé. Vous pouvez maintenant recevoir des missions!"
        );
        
        return profile;
    }
    
    @Override
    public TechnicianProfile rejectKyc(Long idAdmin, Long idTechnician, String reason) {
        TechnicianProfile profile = profileRepository.findByTechnicianId(idTechnician)
            .orElseThrow(() -> new IllegalArgumentException("Profil non trouvé"));
        
        Technician technician = technicianRepository.findById(idTechnician)
            .orElseThrow();
        
        // Logger l'action admin
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, technician.getIdUser(),
            ActionType.KYC_REJECTED,
            reason
        );
        adminActionRepository.save(action);
        
        // Notifier le technicien
        notificationService.sendNotification(
            technician.getIdUser(),
            NotificationType.DOCUMENT_REJECTED,
            "KYC rejeté",
            "Votre dossier KYC a été rejeté: " + reason
        );
        
        return profile;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isKycComplete(Long idTechnician) {
        TechnicianProfile profile = profileRepository.findByTechnicianId(idTechnician)
            .orElse(null);
        
        if (profile == null) {
            return false;
        }
        
        // Vérifier que tous les documents requis sont présents et validés
        List<TechnicianDocument> documents = documentRepository.findByProfileId(profile.getIdProfile());
        
        return !documents.isEmpty() && 
               documents.stream().allMatch(TechnicianDocument::isVerified) &&
               profile.isVerified();
    }
}