package com.eadl.connect_backend.application.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.payment.PaymentStatus;
import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.port.in.admin.StatisticsService;
import com.eadl.connect_backend.domain.port.out.persistence.PaymentRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReservationRepository;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {
    
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final TechnicianProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    
    public StatisticsServiceImpl(ReservationRepository reservationRepository,
                                PaymentRepository paymentRepository,
                                TechnicianProfileRepository profileRepository,
                                UserRepository userRepository,
                                ReviewRepository reviewRepository) {
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }
    
    @Override
    public Map<String, Object> getGeneralStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalReservations", getTotalReservations());
        stats.put("totalRevenue", getTotalRevenue());
        stats.put("activeTechnicians", getActiveTechniciansCount());
        stats.put("activeClients", getActiveClientsCount());
        stats.put("satisfactionRate", getAverageSatisfactionRate());
        stats.put("completedReservations", 
            reservationRepository.countByStatus(ReservationStatus.COMPLETED));
        stats.put("pendingReservations", 
            reservationRepository.countByStatus(ReservationStatus.PENDING));
        
        return stats;
    }
    
    @Override
    public Long getTotalReservations() {
        return reservationRepository.count();
    }
    
    @Override
    public Map<String, Long> getReservationsByStatus() {
        Map<String, Long> stats = new HashMap<>();
        
        for (ReservationStatus status : ReservationStatus.values()) {
            Long count = reservationRepository.countByStatus(status);
            stats.put(status.name(), count);
        }
        
        return stats;
    }
    
    @Override
    public BigDecimal getTotalRevenue() {
        BigDecimal total = paymentRepository.sumAllCompletedPayments();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public Map<LocalDate, BigDecimal> getRevenueByPeriod(LocalDate startDate, LocalDate endDate) {
        // Récupérer tous les paiements de la période
        var payments = paymentRepository.findByCreatedAtBetween(
            startDate.atStartOfDay(),
            endDate.atTime(23, 59)
        );
        
        // Grouper par date
        return payments.stream()
            .collect(Collectors.groupingBy(
                p -> p.getCreatedAt().toLocalDate(),
                Collectors.reducing(
                    BigDecimal.ZERO,
                    p -> p.getAmount(),
                    BigDecimal::add
                )
            ));
    }
    
    @Override
    public Long getActiveTechniciansCount() {
        return profileRepository.countByVerified(true);
    }
    
    @Override
    public Long getActiveClientsCount() {
        return userRepository.countByRole(Role.CLIENT);
    }
    
    @Override
    public Double getAverageSatisfactionRate() {
        // Calculer la moyenne de toutes les notes
        List<Review> reviews = 
            reviewRepository.findRecent(Integer.MAX_VALUE);
        
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = reviews.stream()
            .mapToInt(r -> r.getRating().getValue())
            .sum();
        
        return sum / reviews.size();
    }
    
    @Override
    public Map<String, Long> getReservationsByCategory() {
        // Cette méthode nécessiterait une jointure avec TechnicianSkill
        // Pour simplifier, on retourne une map vide
        // Dans une vraie implémentation, il faudrait une requête personnalisée
        return new HashMap<>();
    }
    
    @Override
    public List<Map<String, Object>> getTopTechnicians(int limit) {
        List<TechnicianProfile> profiles = 
            profileRepository.findTopRated(limit);
        
        return profiles.stream()
            .map(p -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idTechnician", p.getIdTechnician());
                map.put("averageRating", p.getAverageRating());
                map.put("completedJobs", p.getCompletedJobs());
                return map;
            })
            .toList();
    }
    
    @Override
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPayments", paymentRepository.count());
        stats.put("totalRevenue", getTotalRevenue());
        stats.put("pendingPayments", 
            paymentRepository.countByStatus(
                PaymentStatus.PENDING));
        stats.put("completedPayments", 
            paymentRepository.countByStatus(
                PaymentStatus.COMPLETED));
        stats.put("failedPayments", 
            paymentRepository.countByStatus(
                PaymentStatus.FAILED));
        
        return stats;
    }
    
    @Override
    public Double getConversionRate() {
        Long totalUsers = userRepository.count();
        Long usersWithReservations = reservationRepository.count(); // Simplifié
        
        if (totalUsers == 0) {
            return 0.0;
        }
        
        return (usersWithReservations.doubleValue() / totalUsers.doubleValue()) * 100;
    }
}
