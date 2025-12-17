package com.eadl.connect_backend.application.service.technician;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.TimeSlot;
import com.eadl.connect_backend.domain.port.in.technician.AvailabilityService;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service Availability
 * Gestion des disponibilités des techniciens
 */
@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {
    
    private final ReservationRepository reservationRepository;
    
    // Stockage en mémoire des disponibilités (à remplacer par une table en prod)
    // Map<idTechnician, List<TimeSlot>>
    private final Map<Long, List<TimeSlot>> availabilityMap = new HashMap<>();
    
    public AvailabilityServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    @Override
    public void addTimeSlot(Long idTechnician, TimeSlot timeSlot) {
        // Récupérer les plages existantes
        List<TimeSlot> slots = availabilityMap.getOrDefault(idTechnician, new ArrayList<>());
        
        // Vérifier les chevauchements
        for (TimeSlot existingSlot : slots) {
            if (existingSlot.overlaps(timeSlot)) {
                throw new IllegalArgumentException(
                    "Cette plage horaire chevauche une plage existante");
            }
        }
        
        // Ajouter la nouvelle plage
        slots.add(timeSlot);
        availabilityMap.put(idTechnician, slots);
    }
    
    @Override
    public void removeTimeSlot(Long idTechnician, TimeSlot timeSlot) {
        List<TimeSlot> slots = availabilityMap.get(idTechnician);
        
        if (slots != null) {
            slots.remove(timeSlot);
            availabilityMap.put(idTechnician, slots);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlots(Long idTechnician) {
        return availabilityMap.getOrDefault(idTechnician, new ArrayList<>());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlotsForDay(Long idTechnician, DayOfWeek day) {
        List<TimeSlot> allSlots = getTimeSlots(idTechnician);
        
        return allSlots.stream()
            .filter(slot -> slot.getDayOfWeek().equals(day))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long idTechnician, LocalDateTime dateTime) {
        // 1. Vérifier si le créneau est dans les disponibilités générales
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();
        
        List<TimeSlot> daySlots = getTimeSlotsForDay(idTechnician, dayOfWeek);
        
        boolean inGeneralAvailability = daySlots.stream()
            .anyMatch(slot -> slot.contains(time));
        
        if (!inGeneralAvailability) {
            return false;
        }
        
        // 2. Vérifier qu'il n'y a pas de réservation à ce moment
        List<Reservation> activeReservations = 
            reservationRepository.findActiveTechnicianReservations(idTechnician);
        
        for (Reservation reservation : activeReservations) {
            LocalDateTime scheduledTime = reservation.getScheduledTime();
            
            // Estimer une durée de 2h pour chaque intervention
            LocalDateTime endTime = scheduledTime.plusHours(2);
            
            if (!dateTime.isBefore(scheduledTime) && dateTime.isBefore(endTime)) {
                return false; // Conflit avec une réservation existante
            }
        }
        
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAvailableSlots(Long idTechnician, 
                                                 LocalDateTime startDate, 
                                                 LocalDateTime endDate) {
        // Parcourir chaque jour de la période
        LocalDateTime current = startDate;
        List<LocalDateTime> availableSlots = new ArrayList<>();

        while (!current.isAfter(endDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            List<TimeSlot> daySlots = getTimeSlotsForDay(idTechnician, dayOfWeek);
            
            // Pour chaque plage horaire du jour
            for (TimeSlot slot : daySlots) {
                // Générer des créneaux de 30 minutes
                LocalTime slotStart = slot.getStartTime();
                LocalTime slotEnd = slot.getEndTime();
                
                LocalTime currentTime = slotStart;
                while (currentTime.isBefore(slotEnd)) {
                    LocalDateTime slotDateTime = LocalDateTime.of(
                        current.toLocalDate(), currentTime
                    );
                    
                    // Vérifier si le créneau est disponible
                    if (isAvailable(idTechnician, slotDateTime)) {
                        availableSlots.add(slotDateTime);
                    }
                    
                    // Passer au créneau suivant (30 min plus tard)
                    currentTime = currentTime.plusMinutes(30);
                }
            }
            
            // Passer au jour suivant
            current = current.plusDays(1);
        }
        
        return availableSlots;
    }

    /**
     * Initialise les disponibilités par défaut d'un technicien
     * Exemple: Lundi-Vendredi 8h-17h
     */
    public void initializeDefaultAvailability(Long idTechnician) {
        List<TimeSlot> defaultSlots = new ArrayList<>();
        
        // Lundi au Vendredi: 8h-12h et 14h-18h
        for (int day = 1; day <= 5; day++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(day);
            
            // Matin
            defaultSlots.add(TimeSlot.of(
                dayOfWeek, 
                LocalTime.of(8, 0), 
                LocalTime.of(12, 0)
            ));
            
            // Après-midi
            defaultSlots.add(TimeSlot.of(
                dayOfWeek, 
                LocalTime.of(14, 0), 
                LocalTime.of(18, 0)
            ));
        }
        
        // Samedi: 9h-13h
        defaultSlots.add(TimeSlot.of(
            DayOfWeek.SATURDAY,
            LocalTime.of(9, 0),
            LocalTime.of(13, 0)
        ));
        
        availabilityMap.put(idTechnician, defaultSlots);
    }

    /**
     * Supprime toutes les disponibilités d'un technicien
     */
    public void clearAvailability(Long idTechnician) {
        availabilityMap.remove(idTechnician);
    }

    /**
     * Vérifie si un technicien peut accepter une nouvelle réservation
     * (pas plus de 3 réservations actives en même temps)
     */
    public boolean canAcceptNewReservation(Long idTechnician) {
        List<Reservation> activeReservations = 
            reservationRepository.findActiveTechnicianReservations(idTechnician);
        
        return activeReservations.size() < 3;
    }
}