package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;

public interface TechnicianProfileJpaRepository extends JpaRepository<TechnicianProfileEntity, Long> {

    /**
     * Récupère le profil d’un technicien via son User ID
     */
    Optional<TechnicianProfileEntity> findByTechnician_IdUser(Long idTechnician);

    /**
     * Récupère tous les profils ayant au moins une compétence dans une catégorie
     */
    List<TechnicianProfileEntity> findDistinctBySkills_Category_IdCategory(Long categoryId);

    /**
     * Récupère les profils vérifiés
     */
    List<TechnicianProfileEntity> findByVerified(boolean verified);

    /**
     * Récupère les profils non vérifiés
     */
    List<TechnicianProfileEntity> findByVerifiedFalse();

    /**
     * Récupère les profils par statut de disponibilité
     */
    List<TechnicianProfileEntity> findByAvailabilityStatus(AvailabilityStatus status);

    /**
     * Récupère les profils par disponibilité et vérification
     */
    List<TechnicianProfileEntity> findByAvailabilityStatusAndVerified(
            AvailabilityStatus status,
            boolean verified
    );

    /**
     * Recherche multi-critères avec skills et catégorie
     */
    @Query("""
        SELECT DISTINCT tp
        FROM TechnicianProfileEntity tp
        JOIN tp.technician t
        LEFT JOIN tp.skills s
        WHERE (:city IS NULL OR t.city = :city)
          AND (:neighborhood IS NULL OR t.neighborhood = :neighborhood)
          AND (:categoryId IS NULL OR s.category.idCategory = :categoryId)
          AND (:verifiedOnly IS NULL OR tp.verified = :verifiedOnly)
          AND (:activeOnly IS NULL OR t.active = :activeOnly)
          AND (:availabilityStatus IS NULL OR tp.availabilityStatus = :availabilityStatus)
          AND (:minHourlyRate IS NULL OR tp.hourlyRate >= :minHourlyRate)
          AND (:maxHourlyRate IS NULL OR tp.hourlyRate <= :maxHourlyRate)
    """)
    List<TechnicianProfileEntity> search(
        @Param("city") String city,
        @Param("neighborhood") String neighborhood,
        @Param("categoryId") Long categoryId,
        @Param("verifiedOnly") Boolean verifiedOnly,
        @Param("activeOnly") Boolean activeOnly,
        @Param("availabilityStatus") AvailabilityStatus availabilityStatus,
        @Param("minHourlyRate") BigDecimal minHourlyRate,
        @Param("maxHourlyRate") BigDecimal maxHourlyRate
    );

    /**
     * Récupère les techniciens les mieux notés par ville
     */
    @Query("""
        SELECT tp
        FROM TechnicianProfileEntity tp
        JOIN tp.technician t
        WHERE (:city IS NULL OR t.city = :city)
          AND (:verifiedOnly = false OR tp.verified = true)
        ORDER BY tp.averageRating DESC
    """)
    List<TechnicianProfileEntity> findTopRatedByCity(
        @Param("city") String city,
        @Param("verifiedOnly") boolean verifiedOnly,
        Pageable pageable
    );

    // /**
    //  * Récupère les profils disponibles dans un rayon géographique
    //  */
    // @Query("""
    //     SELECT tp
    //     FROM TechnicianProfileEntity tp
    //     JOIN tp.technician t
    //     WHERE tp.availabilityStatus = :availabilityStatus
    //     AND (6371 * 2 * ASIN(
    //             SQRT(
    //                 POWER(SIN((:latitude - t.latitude) * PI()/180 / 2), 2) +
    //                 COS(:latitude * PI()/180) * COS(t.latitude * PI()/180) *
    //                 POWER(SIN((:longitude - t.longitude) * PI()/180 / 2), 2)
    //             )
    //         )) <= :radiusKm
    // """)
    // List<TechnicianProfileEntity> findNearbyAvailable(
    //     @Param("latitude") BigDecimal latitude,
    //     @Param("longitude") BigDecimal longitude,
    //     @Param("radiusKm") BigDecimal radiusKm,
    //     @Param("availabilityStatus") AvailabilityStatus availabilityStatus
    // );
}