package com.eadl.connect_backend.domain.model.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Réservation - Entité racine Aggregate
 */
public class Reservation {
    private Long idReservation;
    private Long idClient;
    private Long idTechnician;
    private Long idReview;
    private LocalDateTime dateRequested;
    private LocalDateTime scheduledTime;
    private ReservationStatus status;
    private BigDecimal price;
    private String address;
    private String description;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    public Reservation() {}

    // ========== Business Logic Methods ==========
    public void accept() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Seules les réservations en attente peuvent être acceptées");
        }
        this.status = ReservationStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Seules les réservations en attente peuvent être rejetées");
        }
        this.status = ReservationStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startRoute() {
        if (status != ReservationStatus.ACCEPTED) {
            throw new IllegalStateException("Le technicien doit d'abord accepter la réservation");
        }
        this.status = ReservationStatus.EN_ROUTE;
        this.updatedAt = LocalDateTime.now();
    }

    public void startWork() {
        if (status != ReservationStatus.EN_ROUTE && status != ReservationStatus.ACCEPTED) {
            throw new IllegalStateException("Statut invalide pour démarrer l'intervention");
        }
        this.status = ReservationStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (status != ReservationStatus.IN_PROGRESS) {
            throw new IllegalStateException("L'intervention doit être en cours pour être terminée");
        }
        this.status = ReservationStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cette réservation ne peut pas être annulée");
        }
        this.status = ReservationStatus.CANCELLED;
        this.cancellationReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void reschedule(LocalDateTime newScheduledTime) {
        if (status == ReservationStatus.COMPLETED || 
            status == ReservationStatus.CANCELLED ||
            status == ReservationStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cette réservation ne peut pas être replanifiée");
        }
        this.scheduledTime = newScheduledTime;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (status != ReservationStatus.PENDING && status != ReservationStatus.ACCEPTED) {
            throw new IllegalStateException("Le prix ne peut être modifié qu'avant le début de l'intervention");
        }
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeCancelled() {
        return status != ReservationStatus.COMPLETED && 
               status != ReservationStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == ReservationStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }

    public boolean isActive() {
        return status == ReservationStatus.ACCEPTED || 
               status == ReservationStatus.EN_ROUTE || 
               status == ReservationStatus.IN_PROGRESS;
    }

    // ========== Getters ==========
    public Long getIdReservation() {
        return idReservation;
    }

    public Long getIdClient() {
        return idClient;
    }

    public Long getIdTechnician() {
        return idTechnician;
    }

    public LocalDateTime getDateRequested() {
        return dateRequested;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    
    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(idReservation, that.idReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", idClient=" + idClient +
                ", idTechnician=" + idTechnician +
                ", scheduledTime=" + scheduledTime +
                ", status=" + status +
                ", price=" + price +
                '}';
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void setIdTechnician(Long idTechnician) {
        this.idTechnician = idTechnician;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public void setDateRequested(LocalDateTime dateRequested) {
        this.dateRequested = dateRequested;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Object getId() {
        return null;
    }
}
