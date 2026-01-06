package com.eadl.connect_backend.domain.model.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AdminActionTest {

    @Test
    void shouldCreateAdminAction() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.SYSTEM_CONFIG_CHANGED,
                "Maintenance système"
        );

        assertThat(action.getIdAdmin()).isEqualTo(1L);
        assertThat(action.getActionType()).isEqualTo(ActionType.SYSTEM_CONFIG_CHANGED);
        assertThat(action.getReason()).isEqualTo("Maintenance système");
        assertThat(action.getCreatedAt()).isNotNull();
        assertThat(action.getIdTargetUser()).isNull();
    }

    @Test
    void shouldCreateAdminActionWithTargetUser() {
        AdminAction action = AdminAction.createWithTarget(
                1L,
                42L,
                ActionType.USER_SUSPENDED,
                "Violation des règles"
        );

        assertThat(action.getIdAdmin()).isEqualTo(1L);
        assertThat(action.getIdTargetUser()).isEqualTo(42L);
        assertThat(action.targetsUser()).isTrue();
    }

    @Test
    void shouldAddDetails() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.REVIEW_DELETED,
                "Contenu inapproprié"
        );

        action.addDetails("{\"commentId\":123}");

        assertThat(action.getDetails()).contains("commentId");
    }

    @Test
    void shouldSetIpAddressAndUserAgent() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.SYSTEM_CONFIG_CHANGED,
                "Audit"
        );

        action.setIpAddress("192.168.1.10");
        action.setUserAgent("Mozilla/5.0");

        assertThat(action.getIpAddress()).isEqualTo("192.168.1.10");
        assertThat(action.getUserAgent()).isEqualTo("Mozilla/5.0");
    }

    @Test
    void shouldDetectKycActions() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.KYC_APPROVED,
                "KYC validé"
        );

        assertThat(action.isKycAction()).isTrue();
    }

    @Test
    void shouldDetectUserManagementActions() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.USER_REACTIVATED,
                "Compte réactivé"
        );

        assertThat(action.isUserManagementAction()).isTrue();
    }

    @Test
    void shouldDetectModerationActions() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.REVIEW_FLAGGED,
                "Signalement"
        );

        assertThat(action.isModerationAction()).isTrue();
    }

    @Test
    void shouldDetectFinancialActions() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.PAYMENT_REFUNDED,
                "Remboursement"
        );

        assertThat(action.isFinancialAction()).isTrue();
    }

    @Test
    void shouldDetectCriticalActions() {
        AdminAction action = AdminAction.create(
                1L,
                ActionType.USER_DELETED,
                "Suppression définitive"
        );

        assertThat(action.isCriticalAction()).isTrue();
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        AdminAction action1 = AdminAction.create(
                1L,
                ActionType.USER_DELETED,
                "Suppression"
        );
        AdminAction action2 = AdminAction.create(
                1L,
                ActionType.USER_DELETED,
                "Suppression"
        );

        action1.setIdAction(10L);
        action2.setIdAction(10L);

        assertThat(action1).isEqualTo(action2);
        assertThat(action1.hashCode()).isEqualTo(action2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdDifferent() {
        AdminAction action1 = AdminAction.create(
                1L,
                ActionType.USER_DELETED,
                "Suppression"
        );
        AdminAction action2 = AdminAction.create(
                1L,
                ActionType.USER_DELETED,
                "Suppression"
        );

        action1.setIdAction(10L);
        action2.setIdAction(11L);

        assertThat(action1).isNotEqualTo(action2);
    }
}
