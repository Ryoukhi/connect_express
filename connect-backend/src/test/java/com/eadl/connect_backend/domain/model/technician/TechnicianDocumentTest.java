package com.eadl.connect_backend.domain.model.technician;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TechnicianDocumentTest {

    @Test
    @DisplayName("create() doit créer un document KYC non vérifié")
    void create_shouldInitializeDocumentCorrectly() {
        // given
        Long profileId = 1L;
        DocumentType type = DocumentType.IDENTITY_CARD;
        String url = "https://docs/cni.pdf";

        // when
        TechnicianDocument document = TechnicianDocument.create(profileId, type, url);

        // then
        assertNotNull(document);
        assertEquals(profileId, document.getIdProfile());
        assertEquals(type, document.getType());
        assertEquals(url, document.getUrl());
        assertFalse(document.isVerified());
        assertNotNull(document.getUploadedAt());
        assertNull(document.getVerificationNote());
    }

    @Test
    @DisplayName("approve() doit valider le document avec une note")
    void approve_shouldMarkDocumentAsVerified() {
        // given
        TechnicianDocument document =
                TechnicianDocument.create(1L, DocumentType.DIPLOMA, "diploma.pdf");

        String note = "Document valide";

        // when
        document.approve(note);

        // then
        assertTrue(document.isVerified());
        assertEquals(note, document.getVerificationNote());
    }

    @Test
    @DisplayName("reject() doit invalider le document avec une note")
    void reject_shouldMarkDocumentAsRejected() {
        // given
        TechnicianDocument document =
                TechnicianDocument.create(1L, DocumentType.CERTIFICATE, "cert.pdf");

        String note = "Document flou";

        // when
        document.reject(note);

        // then
        assertFalse(document.isVerified());
        assertEquals(note, document.getVerificationNote());
    }

    @Test
    @DisplayName("Deux documents avec le même id doivent être égaux")
    void equals_shouldBeBasedOnId() {
        // given
        TechnicianDocument doc1 = new TechnicianDocument();
        doc1.setIdDocument(10L);

        TechnicianDocument doc2 = new TechnicianDocument();
        doc2.setIdDocument(10L);

        // then
        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
    }

    @Test
    @DisplayName("Deux documents avec des ids différents ne sont pas égaux")
    void equals_shouldReturnFalseForDifferentIds() {
        // given
        TechnicianDocument doc1 = new TechnicianDocument();
        doc1.setIdDocument(1L);

        TechnicianDocument doc2 = new TechnicianDocument();
        doc2.setIdDocument(2L);

        // then
        assertNotEquals(doc1, doc2);
    }
}
