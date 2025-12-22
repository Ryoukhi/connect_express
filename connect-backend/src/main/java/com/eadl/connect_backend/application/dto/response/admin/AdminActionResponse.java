package com.eadl.connect_backend.application.dto.response.admin;

import java.time.LocalDateTime;

import com.eadl.connect_backend.domain.model.admin.ActionType;

/**
 * DTO pour la réponse d'une action administrateur
 */
public class AdminActionResponse {

    private Long idAction;
    private Long idAdmin;
    private Long idTargetUser; // peut être null
    private ActionType actionType;
    private String reason;
    private String details; // JSON ou texte détaillé
    private LocalDateTime createdAt;
    private String ipAddress;
    private String userAgent;

    // Getters & Setters
    public Long getIdAction() {
        return idAction;
    }

    public void setIdAction(Long idAction) {
        this.idAction = idAction;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public Long getIdTargetUser() {
        return idTargetUser;
    }

    public void setIdTargetUser(Long idTargetUser) {
        this.idTargetUser = idTargetUser;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
