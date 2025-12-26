package com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity;

import lombok.*;

import java.time.LocalDateTime;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDateTime dateRequested;

    private LocalDateTime scheduledTime;

    private ReservationStatus status;

    private BigDecimal price;

    private String address;

    private String description;

    private String cancellationReason;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    

    // Client who requested the reservation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private UserEntity client;

    // Technician assigned to the reservation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_technician")
    private UserEntity technician;

    @OneToOne(mappedBy = "reservation", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private FactureEntity facture;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
