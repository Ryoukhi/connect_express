package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "factures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    private String pdfUrl;

    private BigDecimal amount;

    private LocalDateTime generatedAt;

    private String invoiceNumber;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", insertable = false, updatable = false)
    private ReservationEntity reservation;

}
