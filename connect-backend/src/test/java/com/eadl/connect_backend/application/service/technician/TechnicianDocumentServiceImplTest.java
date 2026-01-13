package com.eadl.connect_backend.application.service.technician;

import com.eadl.connect_backend.domain.model.technician.DocumentType;
import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianDocumentServiceImplTest {

    @Mock
    private TechnicianDocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private TechnicianDocumentServiceImpl service;

    private User technician;
    private User admin;
    private TechnicianDocument document;

    @BeforeEach
    void setUp() {
        technician = new User();
        technician.setIdUser(1L);
        technician.setRole(Role.TECHNICIAN);

        admin = new User();
        admin.setIdUser(2L);
        admin.setRole(Role.ADMIN);

        // Crée un document via constructeur + setters (pas setId())
        document = new TechnicianDocument();
        document.setIdProfile(technician.getIdUser());
        document.setType(DocumentType.DIPLOMA);
        document.setUrl("http://document.url/diploma.pdf");
    }

    // ===================== ADD DOCUMENT =====================

    @Test
    void addDocument_shouldSaveDocument_whenUserIsTechnician() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(technician));
        when(documentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TechnicianDocument result = service.addDocument(document);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(DocumentType.DIPLOMA);
        assertThat(result.isVerified()).isFalse();
        assertThat(result.getUploadedAt()).isNotNull();

        verify(documentRepository).save(any());
    }

    @Test
    void addDocument_shouldThrowException_whenUserIsNotTechnician() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> service.addDocument(document))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Only technicians can add documents");

        verify(documentRepository, never()).save(any());
    }

    // ===================== GET DOCUMENTS =====================

    @Test
    void getDocumentsByProfileId_shouldReturnDocuments() {
        when(documentRepository.findByProfileId(1L))
                .thenReturn(List.of(document));

        List<TechnicianDocument> result = service.getDocumentsByProfileId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(DocumentType.DIPLOMA);
        verify(documentRepository).findByProfileId(1L);
    }

    @Test
    void getDocumentById_shouldReturnOptional() {
        when(documentRepository.findById(10L))
                .thenReturn(Optional.of(document));

        Optional<TechnicianDocument> result = service.getDocumentById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().getType()).isEqualTo(DocumentType.DIPLOMA);
        verify(documentRepository).findById(10L);
    }

    // ===================== VERIFY DOCUMENT =====================

    @Test
    void verifyDocument_shouldVerifyDocument_whenUserIsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        service.verifyDocument(10L, "Document valide");

        assertThat(document.isVerified()).isTrue();
        assertThat(document.getVerificationNote()).isEqualTo("Document valide");

        verify(documentRepository).save(document);
    }

    @Test
    void verifyDocument_shouldThrowException_whenUserIsNotAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(technician));

        assertThatThrownBy(() -> service.verifyDocument(10L, "Note"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Only admins can verify documents");
    }

    // ===================== REJECT DOCUMENT =====================

    @Test
    void rejectDocument_shouldRejectDocument_whenUserIsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        service.rejectDocument(10L, "Document invalide");

        assertThat(document.isVerified()).isFalse();
        assertThat(document.getVerificationNote()).isEqualTo("Document invalide");

        verify(documentRepository).save(document);
    }

    // ===================== DELETE DOCUMENT =====================

    @Test
    void deleteDocument_shouldDelete_whenUserIsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        service.deleteDocument(10L);

        verify(documentRepository).delete(document);
    }

    @Test
    void deleteDocument_shouldDelete_whenUserIsOwnerTechnician() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(technician));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        service.deleteDocument(10L);

        verify(documentRepository).delete(document);
    }

    @Test
    void deleteDocument_shouldThrowException_whenUserNotAllowed() {
        User otherTechnician = new User();
        otherTechnician.setIdUser(99L);
        otherTechnician.setRole(Role.TECHNICIAN);

        when(currentUserProvider.getCurrentUserId()).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.of(otherTechnician));
        when(documentRepository.findById(10L)).thenReturn(Optional.of(document));

        assertThatThrownBy(() -> service.deleteDocument(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You are not allowed to delete this document");

        verify(documentRepository, never()).delete(any());
    }
}
