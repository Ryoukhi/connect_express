package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.idReview IN :reviewIds")
    BigDecimal calculateAverageRating(@Param("reviewIds") List<Long> reviewIds);

    /**
     * Récupère tous les avis d'un client
     */
    List<ReviewEntity> findByClientId(Long idClient);

    List<Integer> findRatingsByReviewIds(@Param("reviewIds") List<Long> reviewIds);

}
