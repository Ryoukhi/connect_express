package com.eadl.connect_backend.domain.model.reservation;

public enum ReservationStatus {
    PENDING,        // En attente d'acceptation du technicien
    ACCEPTED,       // Acceptée par le technicien
    EN_ROUTE,       // Technicien en route
    IN_PROGRESS,    // Intervention en cours
    COMPLETED,      // Terminée
    CANCELLED,      // Annulée
    REJECTED        // Refusée par le technicien
}