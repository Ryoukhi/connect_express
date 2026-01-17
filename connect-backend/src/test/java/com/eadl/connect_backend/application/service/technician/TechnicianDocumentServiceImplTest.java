package com.eadl.connect_backend.application.service.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianDocument;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianDocumentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianDocumentServiceImplTest {

    @Mock
    private TechnicianDocumentRepository documentRepository;

    @Mock
    private TechnicianSkillRepository skillRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private TechnicianDocumentServiceImpl documentService;

    private TechnicianDocument document;
    private TechnicianSkill skill;
    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        skill = new TechnicianSkill();
        skill.setIdSkill(10L);
        skill.setIdUser(100L); // Owner

        document = new TechnicianDocument();
        document.setIdDocument(1L);
        document.setIdProfile(10L); // Link to skill

        adminUser = mock(User.class);
        normalUser = mock(User.class);
    }

    // ================= ADD =================

    @Test
    void shouldAddDocumentSuccessfully() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));
        when(documentRepository.save(any(TechnicianDocument.class))).thenReturn(document);

        TechnicianDocument result = documentService.addDocument(document);

        assertThat(result).isNotNull();
        verify(documentRepository).save(any(TechnicianDocument.class));
    }

    @Test
    void shouldThrowIfAddDocumentNotOwner() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(999L); // Not owner
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));

        assertThatThrownBy(() -> documentService.addDocument(document))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldThrowIfSkillNotFound() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.addDocument(document))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ================= VERIFY / REJECT =================

    @Test
    void shouldVerifyDocumentAsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(50L);
        when(currentUserProvider.getCurrentUser()).thenReturn(adminUser);
        when(adminUser.isAdmin()).thenReturn(true);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        documentService.verifyDocument(1L, "OK");

        verify(documentRepository).save(document);
    }

    @Test
    void shouldRejectDocumentAsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(50L);
        when(currentUserProvider.getCurrentUser()).thenReturn(adminUser);
        when(adminUser.isAdmin()).thenReturn(true);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        documentService.rejectDocument(1L, "Not clear enough");

        verify(documentRepository).save(document);
    }

    @Test
    void shouldThrowVerifyIfNotAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(currentUserProvider.getCurrentUser()).thenReturn(normalUser);
        when(normalUser.isAdmin()).thenReturn(false);

        assertThatThrownBy(() -> documentService.verifyDocument(1L, "OK"))
                .isInstanceOf(SecurityException.class);
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteDocumentAsOwner() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(currentUserProvider.getCurrentUser()).thenReturn(normalUser);
        when(normalUser.isAdmin()).thenReturn(false);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));

        documentService.deleteDocument(1L);

        verify(documentRepository).delete(document);
    }

    @Test
    void shouldDeleteDocumentAsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(50L);
        when(currentUserProvider.getCurrentUser()).thenReturn(adminUser);
        when(adminUser.isAdmin()).thenReturn(true);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill)); // Still needs to look up skill for logic

        documentService.deleteDocument(1L);

        verify(documentRepository).delete(document);
    }

    @Test
    void shouldThrowDeleteIfForbidden() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(999L); // Stranger
        when(currentUserProvider.getCurrentUser()).thenReturn(normalUser);
        when(normalUser.isAdmin()).thenReturn(false);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));

        assertThatThrownBy(() -> documentService.deleteDocument(1L))
                .isInstanceOf(SecurityException.class);
    }

    // ================= READ =================

    @Test
    void shouldGetDocumentsBySkillId() {
        when(documentRepository.findByProfileId(10L)).thenReturn(List.of(document));

        List<TechnicianDocument> result = documentService.getDocumentsByTechnicianSkillId(10L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldGetDocumentById() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        Optional<TechnicianDocument> result = documentService.getDocumentById(1L);

        assertThat(result).isPresent();
    }
}
