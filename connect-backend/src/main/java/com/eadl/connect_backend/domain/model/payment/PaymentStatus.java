package com.eadl.connect_backend.domain.model.payment;

public enum PaymentStatus {
    PENDING,        // En attente de traitement
    PROCESSING,     // En cours de traitement
    COMPLETED,      // Paiement réussi
    FAILED,         // Paiement échoué
    REFUNDED,       // Remboursé
    CANCELLED       // Annulé
}