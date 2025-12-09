package com.eadl.connect_backend.domain.model.admin;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AdminAction - Action effectuée par un administrateur
 * Pour l'audit et la traçabilité des opérations admin
 */
public class AdminAction {
    private Long idAction;
    private Long idAdmin;
    private Long idTargetUser; // Utilisateur ciblé (peut être null pour actions système)
    private ActionType actionType;
    private String reason;
    private String details; // JSON avec détails supplémentaires
    private LocalDateTime createdAt;
    private String ipAddress;
    private String userAgent;

    private AdminAction() {}

    // ========== Factory Methods ==========
    public static AdminAction create(Long idAdmin, ActionType actionType, String reason) {
        AdminAction action = new AdminAction();
        action.idAdmin = idAdmin;
        action.actionType = actionType;
        action.reason = reason;
        action.createdAt = LocalDateTime.now();
        return action;
    }

    public static AdminAction createWithTarget(Long idAdmin, Long idTargetUser, 
                                              ActionType actionType, String reason) {
        AdminAction action = create(idAdmin, actionType, reason);
        action.idTargetUser = idTargetUser;
        return action;
    }

    // ========== Business Logic Methods ==========
    public void addDetails(String details) {
        this.details = details;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean targetsUser() {
        return idTargetUser != null;
    }

    public boolean isKycAction() {
        return actionType == ActionType.KYC_APPROVED ||
               actionType == ActionType.KYC_REJECTED ||
               actionType == ActionType.DOCUMENT_VERIFIED ||
               actionType == ActionType.DOCUMENT_REJECTED;
    }

    public boolean isUserManagementAction() {
        return actionType == ActionType.USER_SUSPENDED ||
               actionType == ActionType.USER_REACTIVATED ||
               actionType == ActionType.USER_DELETED ||
               actionType == ActionType.USER_ROLE_CHANGED;
    }

    public boolean isModerationAction() {
        return actionType == ActionType.REVIEW_DELETED ||
               actionType == ActionType.REVIEW_FLAGGED ||
               actionType == ActionType.REVIEW_APPROVED;
    }

    public boolean isFinancialAction() {
        return actionType == ActionType.PAYMENT_REFUNDED ||
               actionType == ActionType.PAYMENT_DISPUTED;
    }

    public boolean isCriticalAction() {
        return actionType == ActionType.USER_DELETED ||
               actionType == ActionType.PAYMENT_REFUNDED ||
               actionType == ActionType.SYSTEM_CONFIG_CHANGED;
    }

    // ========== Getters ==========
    public Long getIdAction() {
        return idAction;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public Long getIdTargetUser() {
        return idTargetUser;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getReason() {
        return reason;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdAction(Long idAction) {
        this.idAction = idAction;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAction that = (AdminAction) o;
        return Objects.equals(idAction, that.idAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAction);
    }

    @Override
    public String toString() {
        return "AdminAction{" +
                "idAction=" + idAction +
                ", idAdmin=" + idAdmin +
                ", idTargetUser=" + idTargetUser +
                ", actionType=" + actionType +
                ", createdAt=" + createdAt +
                '}';
    }
}

