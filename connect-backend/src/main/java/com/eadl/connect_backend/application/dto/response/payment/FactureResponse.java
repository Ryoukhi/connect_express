package com.eadl.connect_backend.application.dto.response.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour la réponse facture
 */
public class FactureResponse {
    
    private Long idFacture;
    private Long idReservation;
    private String invoiceNumber;
    private String pdfUrl;
    private BigDecimal amount;
    private LocalDateTime generatedAt;
    
    // Getters & Setters
    public Long getIdFacture() {
        return idFacture;
    }
    
    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }
    
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public String getPdfUrl() {
        return pdfUrl;
    }
    
    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}