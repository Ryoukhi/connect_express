package com.eadl.connect_backend.domain.port.in.payment;

import com.eadl.connect_backend.domain.model.payment.Payment;
import com.eadl.connect_backend.domain.model.payment.PaymentMode;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Service de paiement
 * Use cases pour la gestion des paiements
 */
public interface PaymentService {
    
    /**
     * Crée un nouveau paiement
     */
    Payment createPayment(Long idReservation, BigDecimal amount, PaymentMode mode);
    
    /**
     * Traite un paiement (appel au gateway de paiement)
     */
    Payment processPayment(Long idPayment);
    
    /**
     * Marque un paiement comme complété
     */
    Payment completePayment(Long idPayment, String providerResponse);
    
    /**
     * Marque un paiement comme échoué
     */
    Payment failPayment(Long idPayment, String errorMessage);
    
    /**
     * Rembourse un paiement
     */
    Payment refundPayment(Long idPayment, String reason);
    
    /**
     * Annule un paiement en attente
     */
    Payment cancelPayment(Long idPayment);
    
    /**
     * Récupère un paiement par son ID
     */
    Optional<Payment> getPaymentById(Long idPayment);
    
    /**
     * Récupère le paiement d'une réservation
     */
    Optional<Payment> getPaymentByReservation(Long idReservation);
    
    /**
     * Récupère l'historique des paiements d'un client
     */
    List<Payment> getClientPaymentHistory(Long idClient);
    
    /**
     * Récupère les paiements reçus par un technicien
     */
    List<Payment> getTechnicianPayments(Long idTechnician);
    
    /**
     * Récupère les paiements en attente
     */
    List<Payment> getPendingPayments();
    
    /**
     * Vérifie si une réservation est payée
     */
    boolean isReservationPaid(Long idReservation);
    
    /**
     * Calcule le montant total des paiements d'un technicien
     */
    BigDecimal getTotalEarnings(Long idTechnician);
}