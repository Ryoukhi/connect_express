package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    /**
     * Calcule la note moyenne pour une liste d'IDs de review
     */
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.idReview IN :reviewIds")
    BigDecimal calculateAverageRating(@Param("reviewIds") List<Long> reviewIds);

    /**
     * Récupère tous les avis d'un client
     */
    List<ReviewEntity> findByClient_IdUser(Long idClient);

    /**
     * Récupère toutes les notes (int) d'une liste d'IDs de review
     */
    @Query("SELECT r.rating FROM ReviewEntity r WHERE r.idReview IN :reviewIds")
    List<Integer> findRatingsByReviewIds(@Param("reviewIds") List<Long> reviewIds);

    Optional<ReviewEntity> findByClient_IdUserAndReservation_IdReservation(
        Long clientId,
        Long reservationId
    );
}