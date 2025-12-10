package com.eadl.connect_backend.domain.port.in.technician;

import com.eadl.connect_backend.domain.model.reservation.TimeSlot;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Port IN - Service de disponibilité
 * Use cases pour la gestion des disponibilités des techniciens
 */
public interface AvailabilityService {
    
    /**
     * Ajoute une plage horaire de disponibilité
     */
    void addTimeSlot(Long idTechnician, TimeSlot timeSlot);
    
    /**
     * Supprime une plage horaire
     */
    void removeTimeSlot(Long idTechnician, TimeSlot timeSlot);
    
    /**
     * Récupère toutes les plages horaires d'un technicien
     */
    List<TimeSlot> getTimeSlots(Long idTechnician);
    
    /**
     * Récupère les plages horaires pour un jour spécifique
     */
    List<TimeSlot> getTimeSlotsForDay(Long idTechnician, DayOfWeek day);
    
    /**
     * Vérifie si un technicien est disponible à une date/heure
     */
    boolean isAvailable(Long idTechnician, LocalDateTime dateTime);
    
    /**
     * Récupère les créneaux disponibles pour une période
     */
    List<LocalDateTime> getAvailableSlots(Long idTechnician, 
                                         LocalDateTime startDate, 
                                         LocalDateTime endDate);
}