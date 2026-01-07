package com.eadl.connect_backend.domain.model.technician;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Document KYC du technicien
 */
public class TechnicianDocument {
    private Long idDocument;
    private Long idProfile;
    private DocumentType type;
    private String url;
    private LocalDateTime uploadedAt;
    private boolean verified;
    private String verificationNote;

    public TechnicianDocument() {}

    // ========== Factory Method ==========
    public static TechnicianDocument create(Long idProfile, DocumentType type, String url) {
        TechnicianDocument document = new TechnicianDocument();
        document.idProfile = idProfile;
        document.type = type;
        document.url = url;
        document.uploadedAt = LocalDateTime.now();
        document.verified = false;
        return document;
    }

    // ========== Business Logic Methods ==========
    public void approve(String note) {
        this.verified = true;
        this.verificationNote = note;
    }

    public void reject(String note) {
        this.verified = false;
        this.verificationNote = note;
    }

    // ========== Getters ==========
    public Long getIdDocument() {
        return idDocument;
    }

    public Long getIdProfile() {
        return idProfile;
    }

    public DocumentType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getVerificationNote() {
        return verificationNote;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdDocument(Long idDocument) {
        this.idDocument = idDocument;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVerificationNote(String verificationNote) {
        this.verificationNote = verificationNote;
    }

    

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicianDocument that = (TechnicianDocument) o;
        return Objects.equals(idDocument, that.idDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocument);
    }

    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }


}