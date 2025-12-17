package com.eadl.connect_backend.application.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour l'envoi d'un message
 */
public class SendMessageRequest {
    
    @NotNull(message = "L'ID de la conversation est obligatoire")
    private Long idConversation;
    
    @NotBlank(message = "Le contenu du message est obligatoire")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    private String content;
    
    private String fileUrl;
    
    private String messageType; // TEXT, IMAGE, FILE
    
    // Getters & Setters
    public Long getIdConversation() {
        return idConversation;
    }
    
    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
