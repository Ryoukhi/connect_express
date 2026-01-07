package com.eadl.connect_backend.domain.model.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FactureTest {

    @Test
    @DisplayName("Créer une facture avec les valeurs par défaut")
    void shouldCreateFactureSuccessfully() {
        // GIVEN
        Long reservationId = 10L;
        BigDecimal amount = new BigDecimal("25000");
        String pdfUrl = "https://cdn.example.com/facture.pdf";

        // WHEN
        Facture facture = Facture.create(reservationId, amount, pdfUrl);

        // THEN
        assertThat(facture).isNotNull();
        assertThat(facture.getIdReservation()).isEqualTo(reservationId);
        assertThat(facture.getAmount()).isEqualByComparingTo(amount);
        assertThat(facture.getPdfUrl()).isEqualTo(pdfUrl);
        assertThat(facture.getGeneratedAt()).isNotNull();
        assertThat(facture.getInvoiceNumber())
                .isNotNull()
                .startsWith("INV-");
    }

    @Test
    @DisplayName("Mettre à jour l'URL du PDF de la facture")
    void shouldUpdatePdfUrl() {
        // GIVEN
        Facture facture = Facture.create(
                1L,
                new BigDecimal("15000"),
                "old-url.pdf"
        );

        // WHEN
        facture.updatePdfUrl("new-url.pdf");

        // THEN
        assertThat(facture.getPdfUrl()).isEqualTo("new-url.pdf");
    }

    @Test
    @DisplayName("Deux factures avec le même id et numéro sont égales")
    void shouldBeEqualWhenSameIdAndInvoiceNumber() {
        // GIVEN
        Facture facture1 = new Facture();
        facture1.setIdFacture(1L);
        facture1.setInvoiceNumber("INV-2026-123");

        Facture facture2 = new Facture();
        facture2.setIdFacture(1L);
        facture2.setInvoiceNumber("INV-2026-123");

        // THEN
        assertThat(facture1).isEqualTo(facture2);
        assertThat(facture1.hashCode()).isEqualTo(facture2.hashCode());
    }

    @Test
    @DisplayName("Deux factures différentes ne sont pas égales")
    void shouldNotBeEqualWhenDifferent() {
        // GIVEN
        Facture facture1 = new Facture();
        facture1.setIdFacture(1L);
        facture1.setInvoiceNumber("INV-1");

        Facture facture2 = new Facture();
        facture2.setIdFacture(2L);
        facture2.setInvoiceNumber("INV-2");

        // THEN
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    @DisplayName("Reconstruction depuis la base de données")
    void shouldReconstructFactureFromDatabase() {
        // GIVEN
        Facture facture = new Facture();
        LocalDateTime now = LocalDateTime.now();

        // WHEN
        facture.setIdFacture(5L);
        facture.setIdReservation(20L);
        facture.setAmount(new BigDecimal("50000"));
        facture.setPdfUrl("db-url.pdf");
        facture.setGeneratedAt(now);
        facture.setInvoiceNumber("INV-DB-001");

        // THEN
        assertThat(facture.getIdFacture()).isEqualTo(5L);
        assertThat(facture.getIdReservation()).isEqualTo(20L);
        assertThat(facture.getAmount()).isEqualByComparingTo("50000");
        assertThat(facture.getPdfUrl()).isEqualTo("db-url.pdf");
        assertThat(facture.getGeneratedAt()).isEqualTo(now);
        assertThat(facture.getInvoiceNumber()).isEqualTo("INV-DB-001");
    }
}
