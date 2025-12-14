package com.eadl.connect_backend.domain.port.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;

/**
 * Port OUT - Repository Reservation
 */
public interface ReservationRepository {
    
    /**
     * Sauvegarde une réservation
     */
    Reservation save(Reservation reservation);
    
    /**
     * Récupère une réservation par son ID
     */
    Optional<Reservation> findById(Long idReservation);
    
    /**
     * Récupère toutes les réservations
     */
    List<Reservation> findAll();
    
    /**
     * Récupère les réservations d'un client
     */
    List<Reservation> findByClientId(Long idClient);
    
    /**
     * Récupère les réservations d'un technicien
     */
    List<Reservation> findByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les réservations par statut
     */
    List<Reservation> findByStatus(ReservationStatus status);
    
    /**
     * Récupère les réservations d'un technicien par statut
     */
    List<Reservation> findByTechnicianIdAndStatus(Long idTechnician, ReservationStatus status);
    
    /**
     * Récupère les réservations actives d'un technicien
     */
    List<Reservation> findActiveTechnicianReservations(Long idTechnician);
    
    /**
     * Récupère les réservations dans une période
     */
    List<Reservation> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Récupère les réservations d'aujourd'hui
     */
    List<Reservation> findTodayReservations();
    
    /**
     * Récupère les réservations à venir
     */
    List<Reservation> findUpcomingReservations(int days);
    
    /**
     * Récupère les réservations complétées d'un technicien
     */
    List<Reservation> findCompletedByTechnicianId(Long idTechnician);
    
    /**
     * Récupère les réservations avec pagination
     */
    List<Reservation> findAll(int page, int size);
    
    /**
     * Recherche des réservations par critères
     */
    List<Reservation> search(String searchTerm, ReservationStatus status, 
                            LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Compte les réservations
     */
    Long count();
    
    /**
     * Compte les réservations par statut
     */
    Long countByStatus(ReservationStatus status);
    
    /**
     * Compte les réservations complétées d'un technicien
     */
    Long countCompletedByTechnicianId(Long idTechnician);
    
    /**
     * Supprime une réservation
     */
    void delete(Reservation reservation);
}