package com.eadl.connect_backend.domain.model.admin;

public enum ActionType {
    // Actions sur les utilisateurs
    USER_SUSPENDED,             // Suspension d'utilisateur
    USER_REACTIVATED,           // Réactivation d'utilisateur
    USER_DELETED,               // Suppression d'utilisateur
    USER_ROLE_CHANGED,          // Changement de rôle
    
    // Actions KYC
    KYC_APPROVED,               // KYC approuvé
    KYC_REJECTED,               // KYC rejeté
    DOCUMENT_VERIFIED,          // Document vérifié
    DOCUMENT_REJECTED,          // Document rejeté
    
    // Actions sur les réservations
    RESERVATION_CANCELLED,      // Annulation de réservation
    RESERVATION_MODIFIED,       // Modification de réservation
    
    // Actions sur les avis
    REVIEW_DELETED,             // Suppression d'avis
    REVIEW_FLAGGED,             // Avis signalé
    REVIEW_APPROVED,            // Avis approuvé après modération
    
    // Actions sur les paiements
    PAYMENT_REFUNDED,           // Remboursement effectué
    PAYMENT_DISPUTED,           // Litige de paiement
    
    // Actions sur les catégories
    CATEGORY_CREATED,           // Catégorie créée
    CATEGORY_UPDATED,           // Catégorie modifiée
    CATEGORY_DELETED,           // Catégorie supprimée
    
    // Actions système
    SYSTEM_CONFIG_CHANGED,      // Configuration système modifiée
    BULK_OPERATION              // Opération en masse
}
