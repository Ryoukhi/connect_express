package com.eadl.connect_backend.application.dto.response.profile;

import java.time.LocalDateTime;

public class TechnicianDocumentResponse {
    
    private Long idDocument;
    private String type;
    private String url;
    private LocalDateTime uploadedAt;
    private boolean verified;
    private String verificationNote;
    
    // Getters & Setters
    public Long getIdDocument() {
        return idDocument;
    }
    
    public void setIdDocument(Long idDocument) {
        this.idDocument = idDocument;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public String getVerificationNote() {
        return verificationNote;
    }
    
    public void setVerificationNote(String verificationNote) {
        this.verificationNote = verificationNote;
    }
}
