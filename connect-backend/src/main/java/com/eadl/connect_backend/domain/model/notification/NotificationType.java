package com.eadl.connect_backend.domain.model.notification;

public enum NotificationType {
    // Notifications liées aux réservations
    RESERVATION_CREATED,        // Nouvelle demande de réservation
    RESERVATION_ACCEPTED,       // Réservation acceptée par le technicien
    RESERVATION_REJECTED,       // Réservation refusée par le technicien
    RESERVATION_CANCELLED,      // Réservation annulée
    RESERVATION_RESCHEDULED,    // Réservation replanifiée
    
    // Notifications de statut
    TECHNICIAN_EN_ROUTE,        // Technicien en route
    TECHNICIAN_ARRIVED,         // Technicien arrivé
    JOB_STARTED,                // Intervention commencée
    JOB_COMPLETED,              // Intervention terminée
    
    // Notifications de paiement
    PAYMENT_PENDING,            // Paiement en attente
    PAYMENT_COMPLETED,          // Paiement réussi
    PAYMENT_FAILED,             // Paiement échoué
    PAYMENT_REFUNDED,           // Paiement remboursé
    
    // Notifications de documents
    INVOICE_GENERATED,          // Facture générée
    DOCUMENT_UPLOADED,          // Document téléchargé (KYC)
    DOCUMENT_VERIFIED,          // Document vérifié
    DOCUMENT_REJECTED,          // Document rejeté
    
    // Notifications de compte
    ACCOUNT_CREATED,            // Compte créé
    ACCOUNT_VERIFIED,           // Compte vérifié (KYC approuvé)
    ACCOUNT_SUSPENDED,          // Compte suspendu
    ACCOUNT_REACTIVATED,        // Compte réactivé
    EMAIL_VERIFIED,             // Email vérifié
    PHONE_VERIFIED,             // Téléphone vérifié
    
    // Notifications de chat
    NEW_MESSAGE,                // Nouveau message reçu
    
    // Notifications d'avis
    REVIEW_RECEIVED,            // Nouvel avis reçu
    REVIEW_REPORTED,            // Avis signalé
    
    // Notifications système
    SYSTEM_MAINTENANCE,         // Maintenance système
    SYSTEM_UPDATE              // Mise à jour système
}