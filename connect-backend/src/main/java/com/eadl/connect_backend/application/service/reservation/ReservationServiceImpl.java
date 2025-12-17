package com.eadl.connect_backend.application.service.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.notification.NotificationType;
import com.eadl.connect_backend.domain.model.reservation.Reservation;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.port.exception.ReservationNotFoundException;
import com.eadl.connect_backend.domain.port.in.notification.NotificationService;
import com.eadl.connect_backend.domain.port.in.reservation.ReservationService;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;
    
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                 NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    public Reservation createReservation(Long idClient, Long idTechnician, 
                                        LocalDateTime scheduledTime, BigDecimal price,
                                        String address, String description) {
        // Créer la réservation
        Reservation reservation = Reservation.create(
            idClient, idTechnician, scheduledTime, price, address, description
        );
        
        reservation = reservationRepository.save(reservation);
        
        // Notifier le technicien
        notificationService.sendNotification(
            idTechnician,
            NotificationType.RESERVATION_CREATED,
            "Nouvelle demande de réservation",
            "Vous avez reçu une nouvelle demande de réservation"
        );
        
        return reservation;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long idReservation) {
        return reservationRepository.findById(idReservation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getClientReservations(Long idClient) {
        return reservationRepository.findByClientId(idClient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getTechnicianReservations(Long idTechnician) {
        return reservationRepository.findByTechnicianId(idTechnician);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getTechnicianActiveReservations(Long idTechnician) {
        return reservationRepository.findActiveTechnicianReservations(idTechnician);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getTechnicianPendingReservations(Long idTechnician) {
        return reservationRepository.findByTechnicianIdAndStatus(
            idTechnician, ReservationStatus.PENDING
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
    
    @Override
    public Reservation acceptReservation(Long idReservation, Long idTechnician) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        // Vérifier que c'est bien le technicien de la réservation
        if (!reservation.getIdTechnician().equals(idTechnician)) {
            throw new IllegalStateException("Vous n'êtes pas le technicien de cette réservation");
        }
        
        // Accepter
        reservation.accept();
        reservation = reservationRepository.save(reservation);
        
        // Notifier le client
        notificationService.sendNotification(
            reservation.getIdClient(),
            NotificationType.RESERVATION_ACCEPTED,
            "Réservation acceptée",
            "Le technicien a accepté votre réservation"
        );
        
        return reservation;
    }
    
    @Override
    public Reservation rejectReservation(Long idReservation, Long idTechnician) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        if (!reservation.getIdTechnician().equals(idTechnician)) {
            throw new IllegalStateException("Vous n'êtes pas le technicien de cette réservation");
        }
        
        reservation.reject();
        reservation = reservationRepository.save(reservation);
        
        // Notifier le client
        notificationService.sendNotification(
            reservation.getIdClient(),
            NotificationType.RESERVATION_REJECTED,
            "Réservation refusée",
            "Le technicien a refusé votre réservation"
        );
        
        return reservation;
    }
    
    @Override
    public Reservation startRoute(Long idReservation, Long idTechnician) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        reservation.startRoute();
        reservation = reservationRepository.save(reservation);
        
        // Notifier le client
        notificationService.sendNotification(
            reservation.getIdClient(),
            NotificationType.TECHNICIAN_EN_ROUTE,
            "Technicien en route",
            "Le technicien est en route vers votre domicile"
        );
        
        return reservation;
    }
    
    @Override
    public Reservation startWork(Long idReservation, Long idTechnician) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        reservation.startWork();
        reservation = reservationRepository.save(reservation);

        // Notifier le client
        notificationService.sendNotification(
            reservation.getIdClient(),
            NotificationType.JOB_STARTED,
            "Intervention commencée",
            "Le technicien a commencé l'intervention"
        );
        return reservation;
    }

    @Override
    public Reservation completeReservation(Long idReservation, Long idTechnician) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        reservation.complete();
        reservation = reservationRepository.save(reservation);
        
        // Notifier le client
        notificationService.sendNotification(
            reservation.getIdClient(),
            NotificationType.JOB_COMPLETED,
            "Réservation terminée",
            "Le technicien a terminé l'intervention"
        );
        
        return reservation;
    }

    @Override
    public Reservation cancelReservation(Long idReservation, Long userId, String reason) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        reservation.cancel(reason);
        reservation = reservationRepository.save(reservation);
        // Notifier le technicien
        notificationService.sendNotification(
            reservation.getIdTechnician(),
            NotificationType.RESERVATION_CANCELLED,
            "Réservation annulée",
            "La réservation a été annulée. Raison: " + reason
        );

        return reservation;
    }   

    @Override
    public Long countCompletedReservations(Long idTechnician) {
        return reservationRepository.countCompletedByTechnicianId(
            idTechnician
        );
    }

    @Override
    public void deleteReservation(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
            .orElseThrow(() -> new ReservationNotFoundException("Réservation non trouvée"));
        
        reservationRepository.delete(reservation);
    }
}