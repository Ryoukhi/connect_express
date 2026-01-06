package com.eadl.connect_backend.domain.model.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payment - Entité représentant un paiement
 */
public class Payment {
    private Long idPayment;
    private Long idReservation;
    private BigDecimal amount;
    private PaymentMode mode;
    private PaymentStatus status;
    private String transactionRef;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String errorMessage;
    private String providerResponse;

    Payment() {}

    // ========== Factory Method ==========
    public static Payment create(Long idReservation, BigDecimal amount, PaymentMode mode) {
        Payment payment = new Payment();
        payment.idReservation = idReservation;
        payment.amount = amount;
        payment.mode = mode;
        payment.status = PaymentStatus.PENDING;
        payment.createdAt = LocalDateTime.now();
        payment.transactionRef = generateTransactionRef();
        return payment;
    }

    // ========== Business Logic Methods ==========
    public void startProcessing() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Seuls les paiements en attente peuvent être traités");
        }
        this.status = PaymentStatus.PROCESSING;
    }

    public void markAsCompleted(String providerResponse) {
        if (status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Le paiement doit être en cours de traitement");
        }
        this.status = PaymentStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
        this.providerResponse = providerResponse;
    }

    public void markAsFailed(String errorMessage) {
        if (status == PaymentStatus.COMPLETED || status == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Impossible de marquer ce paiement comme échoué");
        }
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    public void refund(String reason) {
        if (status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Seuls les paiements complétés peuvent être remboursés");
        }
        this.status = PaymentStatus.REFUNDED;
        this.errorMessage = reason;
    }

    public void cancel() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Seuls les paiements en attente peuvent être annulés");
        }
        this.status = PaymentStatus.CANCELLED;
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.COMPLETED;
    }

    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED;
    }

    private static String generateTransactionRef() {
        return "TXN-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 10000);
    }

    // ========== Getters ==========
    public Long getIdPayment() {
        return idPayment;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMode getMode() {
        return mode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getProviderResponse() {
        return providerResponse;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdPayment(Long idPayment) {
        this.idPayment = idPayment;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment that = (Payment) o;

        // Si l'ID est null, les objets ne sont pas égaux
        if (this.idPayment == null || that.idPayment == null) return false;

        return this.idPayment.equals(that.idPayment);
    }

    @Override
    public int hashCode() {
        return idPayment != null ? idPayment.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "idPayment=" + idPayment +
                ", transactionRef='" + transactionRef + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", mode=" + mode +
                '}';
    }
}