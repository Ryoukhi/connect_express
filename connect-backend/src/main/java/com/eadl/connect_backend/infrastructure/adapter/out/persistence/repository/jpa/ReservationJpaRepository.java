package com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eadl.connect_backend.domain.model.reservation.ReservationStatus;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReservationEntity;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByClient_IdUser(Long idClient);

    List<ReservationEntity> findByTechnician_IdUser(Long idTechnician);

    List<ReservationEntity> findByStatus(ReservationStatus status);

    boolean existsByTechnician_IdUserAndScheduledTimeBetween(
            Long idTechnician,
            LocalDateTime end,
            LocalDateTime start
    );

    long countByTechnician_IdUserAndStatus(Long idTechnician, ReservationStatus status);

    long countByStatusAndScheduledTimeBetween(ReservationStatus status, LocalDateTime start, LocalDateTime end);

    long countByStatus(ReservationStatus status);

        @Query("""
                select coalesce(sum(r.price), 0)
                from ReservationEntity r
                where r.technician.idUser = :technicianId
                    and r.status = :status
        """)
        BigDecimal sumPriceByTechnicianAndStatus(Long technicianId, ReservationStatus status);

    @Query("""
        SELECT COALESCE(AVG(r.review.rating), 0.0)
        FROM ReservationEntity r
        WHERE r.technician.idUser = :technicianId
        AND r.status = :status
        AND r.review IS NOT NULL
    """)
    Double averageRatingByTechnicianAndStatus(
            @Param("technicianId") Long technicianId,
            @Param("status") ReservationStatus status
    );

    @Query("""
        select r.review.idReview
        from ReservationEntity r
        where r.technician.idUser = :technicianId
          and r.status = :status
          and r.review is not null
    """)
    List<Long> findReviewIdsByTechnicianAndStatus(Long technicianId, ReservationStatus status);
}
