package com.eadl.connect_backend.domain.port.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.payment.Payment;
import com.eadl.connect_backend.domain.model.payment.PaymentStatus;

/**
 * Port OUT - Repository Payment
 */
public interface PaymentRepository {
    
    /**
     * Sauvegarde un paiement
     */
    Payment save(Payment payment);
    
    /**
     * Récupère un paiement par son ID
     */
    Optional<Payment> findById(Long idPayment);
    
    /**
     * Récupère un paiement par référence de transaction
     */
    Optional<Payment> findByTransactionRef(String transactionRef);
    
    /**
     * Récupère le paiement d'une réservation
     */
    Optional<Payment> findByReservationId(Long idReservation);
    
    /**
     * Récupère tous les paiements
     */
    List<Payment> findAll();
    
    /**
     * Récupère les paiements par statut
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * Récupère les paiements en attente
     */
    List<Payment> findPendingPayments();
    
    /**
     * Récupère l'historique des paiements d'un client
     */
    List<Payment> findByClientId(Long idClient);
    
    /**
     * Récupère les paiements reçus par un technicien
     */
    List<Payment> findByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les paiements dans une période
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Calcule le total des paiements complétés d'un technicien
     */
    BigDecimal sumCompletedPaymentsByTechnicianId(Long idTechnician);
    
    /**
     * Calcule le total des revenus de la plateforme
     */
    BigDecimal sumAllCompletedPayments();
    
    /**
     * Compte les paiements
     */
    Long count();
    
    /**
     * Compte les paiements par statut
     */
    Long countByStatus(PaymentStatus status);
    
    /**
     * Supprime un paiement
     */
    void delete(Payment payment);
}

