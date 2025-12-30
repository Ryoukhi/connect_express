package com.eadl.connect_backend.domain.model.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Facture - Document de facturation généré après paiement
 */
public class Facture {
    private Long idFacture;
    private Long idReservation;
    private String pdfUrl;
    private BigDecimal amount;
    private LocalDateTime generatedAt;
    private String invoiceNumber;

    public Facture() {}

    // ========== Factory Method ==========
    public static Facture create(Long idReservation, BigDecimal amount, String pdfUrl) {
        Facture facture = new Facture();
        facture.idReservation = idReservation;
        facture.amount = amount;
        facture.pdfUrl = pdfUrl;
        facture.generatedAt = LocalDateTime.now();
        facture.invoiceNumber = generateInvoiceNumber();
        return facture;
    }

    // ========== Business Logic Methods ==========
    public void updatePdfUrl(String newPdfUrl) {
        this.pdfUrl = newPdfUrl;
    }

    private static String generateInvoiceNumber() {
        return "INV-" + LocalDateTime.now().getYear() + "-" + 
               System.currentTimeMillis();
    }

    // ========== Getters ==========
    public Long getIdFacture() {
        return idFacture;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facture facture = (Facture) o;
        return Objects.equals(idFacture, facture.idFacture) &&
               Objects.equals(invoiceNumber, facture.invoiceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFacture, invoiceNumber);
    }

    @Override
    public String toString() {
        return "Facture{" +
                "idFacture=" + idFacture +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", amount=" + amount +
                ", generatedAt=" + generatedAt +
                '}';
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}