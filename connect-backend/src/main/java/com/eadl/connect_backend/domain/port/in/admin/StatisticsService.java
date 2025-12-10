package com.eadl.connect_backend.domain.port.in.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Port IN - Service de statistiques
 * Use cases pour les statistiques et rapports admin
 */
public interface StatisticsService {
    
    /**
     * Récupère les statistiques générales de la plateforme
     */
    Map<String, Object> getGeneralStatistics();
    
    /**
     * Nombre total de missions
     */
    Long getTotalReservations();
    
    /**
     * Nombre de missions par statut
     */
    Map<String, Long> getReservationsByStatus();
    
    /**
     * Revenus généraux de la plateforme
     */
    BigDecimal getTotalRevenue();
    
    /**
     * Revenus par période
     */
    Map<LocalDate, BigDecimal> getRevenueByPeriod(LocalDate startDate, LocalDate endDate);
    
    /**
     * Nombre de techniciens actifs
     */
    Long getActiveTechniciansCount();
    
    /**
     * Nombre de clients actifs
     */
    Long getActiveClientsCount();
    
    /**
     * Taux de satisfaction moyen des clients
     */
    Double getAverageSatisfactionRate();
    
    /**
     * Statistiques par catégorie
     */
    Map<String, Long> getReservationsByCategory();
    
    /**
     * Top techniciens (par nombre de missions)
     */
    List<Map<String, Object>> getTopTechnicians(int limit);
    
    /**
     * Statistiques de paiement
     */
    Map<String, Object> getPaymentStatistics();
    
    /**
     * Taux de conversion (inscriptions -> réservations)
     */
    Double getConversionRate();
}