package com.eadl.connect_backend.domain.model.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    @DisplayName("Créer un paiement avec statut PENDING par défaut")
    void shouldCreatePaymentWithPendingStatus() {
        Long reservationId = 1L;
        BigDecimal amount = new BigDecimal("20000");

        Payment payment = Payment.create(reservationId, amount, PaymentMode.MOBILE_MONEY_MTN);

        assertThat(payment).isNotNull();
        assertThat(payment.getIdReservation()).isEqualTo(reservationId);
        assertThat(payment.getAmount()).isEqualByComparingTo(amount);
        assertThat(payment.getMode()).isEqualTo(PaymentMode.MOBILE_MONEY_MTN);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payment.getCreatedAt()).isNotNull();
        assertThat(payment.getTransactionRef())
                .isNotNull()
                .startsWith("TXN-");
    }

    @Test
    @DisplayName("Passer un paiement de PENDING à PROCESSING")
    void shouldStartProcessing() {
        Payment payment = Payment.create(1L, new BigDecimal("15000"), PaymentMode.CREDIT_CARD);

        payment.startProcessing();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PROCESSING);
    }

    @Test
    @DisplayName("Impossible de traiter un paiement non PENDING")
    void shouldFailWhenProcessingNonPendingPayment() {
        Payment payment = Payment.create(1L, new BigDecimal("15000"), PaymentMode.CASH);
        payment.startProcessing();

        assertThatThrownBy(payment::startProcessing)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("paiements en attente");
    }

    @Test
    @DisplayName("Marquer un paiement comme COMPLETED")
    void shouldMarkPaymentAsCompleted() {
        Payment payment = Payment.create(2L, new BigDecimal("30000"), PaymentMode.MOBILE_MONEY_ORANGE);
        payment.startProcessing();

        payment.markAsCompleted("SUCCESS");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getProcessedAt()).isNotNull();
        assertThat(payment.getProviderResponse()).isEqualTo("SUCCESS");
        assertThat(payment.isSuccessful()).isTrue();
    }

    @Test
    @DisplayName("Impossible de compléter un paiement non PROCESSING")
    void shouldFailWhenCompletingNonProcessingPayment() {
        Payment payment = Payment.create(3L, new BigDecimal("10000"), PaymentMode.BANK_TRANSFER);

        assertThatThrownBy(() -> payment.markAsCompleted("OK"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("en cours de traitement");
    }

    @Test
    @DisplayName("Marquer un paiement comme FAILED")
    void shouldMarkPaymentAsFailed() {
        Payment payment = Payment.create(4L, new BigDecimal("12000"), PaymentMode.CREDIT_CARD);
        payment.startProcessing();

        payment.markAsFailed("Solde insuffisant");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(payment.getProcessedAt()).isNotNull();
        assertThat(payment.getErrorMessage()).isEqualTo("Solde insuffisant");
    }

    @Test
    @DisplayName("Rembourser un paiement COMPLETED")
    void shouldRefundCompletedPayment() {
        Payment payment = Payment.create(5L, new BigDecimal("40000"), PaymentMode.MOBILE_MONEY_MTN);
        payment.startProcessing();
        payment.markAsCompleted("OK");

        payment.refund("Client insatisfait");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(payment.canBeRefunded()).isFalse();
        assertThat(payment.getErrorMessage()).isEqualTo("Client insatisfait");
    }

    @Test
    @DisplayName("Impossible de rembourser un paiement non COMPLETED")
    void shouldFailWhenRefundingNonCompletedPayment() {
        Payment payment = Payment.create(6L, new BigDecimal("5000"), PaymentMode.CASH);

        assertThatThrownBy(() -> payment.refund("Erreur"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("remboursés");
    }

    @Test
    @DisplayName("Annuler un paiement PENDING")
    void shouldCancelPendingPayment() {
        Payment payment = Payment.create(7L, new BigDecimal("8000"), PaymentMode.BANK_TRANSFER);

        payment.cancel();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    @DisplayName("Impossible d'annuler un paiement non PENDING")
    void shouldFailWhenCancellingNonPendingPayment() {
        Payment payment = Payment.create(8L, new BigDecimal("9000"), PaymentMode.CREDIT_CARD);
        payment.startProcessing();

        assertThatThrownBy(payment::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("en attente");
    }

    // ==================== Tests equals() / not equals ====================

    @Test
    @DisplayName("Deux paiements avec le même id sont égaux")
    void shouldBeEqualWhenSameId() {
        Payment p1 = new Payment();
        p1.setIdPayment(1L);
        p1.setStatus(PaymentStatus.COMPLETED);

        Payment p2 = new Payment();
        p2.setIdPayment(1L);
        p2.setStatus(PaymentStatus.COMPLETED);

        assertThat(p1).isEqualTo(p2);
    }

    @Test
    @DisplayName("Deux paiements avec des id différents ne sont pas égaux")
    void shouldNotBeEqualWhenDifferentId() {
        Payment p1 = new Payment();
        p1.setIdPayment(1L);

        Payment p2 = new Payment();
        p2.setIdPayment(2L);

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    @DisplayName("Deux paiements avec id null ne sont pas égaux")
    void shouldNotBeEqualIfIdIsNull() {
        Payment p1 = new Payment();
        Payment p2 = new Payment();

        assertThat(p1).isNotEqualTo(p2);
    }
}
