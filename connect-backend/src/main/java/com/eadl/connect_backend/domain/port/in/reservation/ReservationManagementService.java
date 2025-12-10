package com.eadl.connect_backend.domain.port.in.reservation;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import java.time.LocalDate;
import java.util.List;

/**
 * Port IN - Service de gestion des réservations (Admin)
 * Use cases pour la gestion administrative des réservations
 */
public interface ReservationManagementService {
    
    /**
     * Récupère toutes les réservations avec pagination
     */
    List<Reservation> getAllReservations(int page, int size);
    
    /**
     * Recherche des réservations par critères
     */
    List<Reservation> searchReservations(String searchTerm, String status, 
                                        LocalDate startDate, LocalDate endDate);
    
    /**
     * Annule une réservation (admin)
     */
    Reservation adminCancelReservation(Long idAdmin, Long idReservation, String reason);
    
    /**
     * Modifie une réservation (admin)
     */
    Reservation adminModifyReservation(Long idAdmin, Long idReservation, 
                                      String field, String value);
    
    /**
     * Récupère les réservations problématiques (annulées, en conflit, etc.)
     */
    List<Reservation> getProblematicReservations();
    
    /**
     * Récupère les réservations d'aujourd'hui
     */
    List<Reservation> getTodayReservations();
    
    /**
     * Récupère les réservations à venir
     */
    List<Reservation> getUpcomingReservations(int days);
}