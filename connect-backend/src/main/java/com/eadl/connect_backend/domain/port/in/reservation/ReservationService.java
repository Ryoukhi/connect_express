package com.eadl.connect_backend.domain.port.in.reservation;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port IN - Service de réservation
 * Use cases pour la gestion des réservations
 */
public interface ReservationService {
    
    /**
     * Crée une nouvelle réservation
     */
    Reservation createReservation(Long idClient, Long idTechnician, 
                                 LocalDateTime scheduledTime, BigDecimal price,
                                 String address, String description);
    
    /**
     * Récupère une réservation par son ID
     */
    Optional<Reservation> getReservationById(Long idReservation);
    
    /**
     * Récupère toutes les réservations d'un client
     */
    List<Reservation> getClientReservations(Long idClient);
    
    /**
     * Récupère toutes les réservations d'un technicien
     */
    List<Reservation> getTechnicianReservations(Long idTechnician);
    
    /**
     * Récupère les réservations actives d'un technicien
     */
    List<Reservation> getTechnicianActiveReservations(Long idTechnician);
    
    /**
     * Récupère les réservations en attente d'un technicien
     */
    List<Reservation> getTechnicianPendingReservations(Long idTechnician);
    
    /**
     * Récupère les réservations par statut
     */
    List<Reservation> getReservationsByStatus(ReservationStatus status);
    
    /**
     * Accepte une réservation (par le technicien)
     */
    Reservation acceptReservation(Long idReservation, Long idTechnician);
    
    /**
     * Rejette une réservation (par le technicien)
     */
    Reservation rejectReservation(Long idReservation, Long idTechnician);
    
    /**
     * Marque le technicien en route
     */
    Reservation startRoute(Long idReservation, Long idTechnician);
    
    /**
     * Démarre l'intervention
     */
    Reservation startWork(Long idReservation, Long idTechnician);
    
    /**
     * Termine l'intervention
     */
    Reservation completeReservation(Long idReservation, Long idTechnician);
    
    /**
     * Annule une réservation
     */
    Reservation cancelReservation(Long idReservation, Long userId, String reason);
    
    
    /**
     * Compte les réservations complétées d'un technicien
     */
    Long countCompletedReservations(Long idTechnician);

    /**
     * Supprime une réservation
     */
    void deleteReservation(Long idReservation);
}